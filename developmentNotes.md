# Notes for annotation processor

* The class that extends `PoolableWrapper` should be annotated. (`@WrappedPoolable`)
* `AbstractPooledFactoryBean` and `AbstractPooledObjectFactory` implementations should be generated (in the same package as package-private).
    * Before generating followings should be checked:
        * Does annotated class really extend`PoolableWrapper`
        * Does annotated have non-private 1-argument (objectPool) contructor
        * Does the constructor passes the arguments correctly, especially the 2nd one is not null.

# Why not using dynamic proxies

```java
import com.kodgemisi.blog.demo.lib.AbstractPoolable;
import com.kodgemisi.blog.demo.lib.Poolable;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.pool2.ObjectPool;
import tr.gov.nvi.tckimlik.ws.KPSPublic;
import tr.gov.nvi.tckimlik.ws.KPSPublicSoap;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * Created on June, 2019
 *
 * @author destan
 */
@Slf4j
public class PoolableKPSPublicSoap extends AbstractPoolable implements KPSPublicSoap {

	 private PoolableKPSPublicSoap(ObjectPool<Poolable> objectPool) {
		super(objectPool);
	}

	@Override
	public void activateObject()  {
		log.info("Setting http headers...");
	}

	@Override
	public void passivateObject()  {
		log.info("Removing http headers...");
	}

	static <T extends Poolable & KPSPublicSoap> T createObject(ObjectPool<Poolable> pool) {
		final KPSPublicSoap kpsPublicSoap = new KPSPublic().getKPSPublicSoap();
		final PoolableKPSPublicSoap poolableKPSPublicSoap = new PoolableKPSPublicSoap(pool);
		return (T) Proxy.newProxyInstance(KPSPublicSoap.class.getClassLoader(),
					  new Class[]{KPSPublicSoap.class, Poolable.class}, new DynamicInvocationHandler(poolableKPSPublicSoap, kpsPublicSoap));
	}

	@Override
	public boolean tcKimlikNoDogrula(long l, String s, String s1, int i) {
		return false;
	}
}

@RequiredArgsConstructor
class DynamicInvocationHandler implements InvocationHandler {

	private final Poolable poolable;
	private final Object object;

	@Override
	public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {

		if (method.getDeclaringClass().isAssignableFrom(Poolable.class)) {
			return method.invoke(poolable, args);
		}

		return method.invoke(object, args);
	}
}
```

```java
class KPSPublicSoapObjectFactory<T extends Poolable & KPSPublicSoap> extends AbstractPooledObjectFactory<T> {

	@Override
	public T createObject() {
		return PoolableKPSPublicSoap.createObject(getObjectPool());
	}
}
```

```java
		try (Poolable poolable = factoryBean.getPoolable()) {
			final boolean result = ((KPSPublicSoap) poolable).tcKimlikNoDogrula(request.getTckn(), request.getFirstName(), request.getLastName(), request.getBirthYear());
			return ResponseEntity.ok(result);
		}
```

* You have to cast it when using
* We put the proxy in pool then borrow the proxy. But due to the implementation of DynamicProxy handler and Pool#close, we return the actual Poolable
object (instead of the proxy) to the pool which gives an exception.