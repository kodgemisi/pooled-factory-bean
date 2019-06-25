package com.kodgemisi.spring.pooledfactory;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.pool2.ObjectPool;

import java.io.Closeable;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Arrays;

/**
 * Created on June, 2019
 *
 * @author destan
 */
@Slf4j
public abstract class AbstractSoapObjectFactory<S> extends AbstractPooledObjectFactory<WrapperPoolable<S>> {

	@Override
	public WrapperPoolable<S> createObject() {
		return createObject(getObjectPool());
	}

	protected WrapperPoolable<S> createObject(ObjectPool<Poolable> pool) {
		final S s = createSoapObject();
		final WrapperPoolable<S> poolable = new SoapProxyWrapperPoolable<>(pool);

		log.info("Creating new Poolable instance");

		// get interfaces of S and add WrapperPoolable interface
		final Class<?>[] sInterfaces = s.getClass().getInterfaces();
		final Class<?>[] interfaces = Arrays.copyOf(sInterfaces, sInterfaces.length + 1);
		interfaces[sInterfaces.length] = WrapperPoolable.class;

		return (WrapperPoolable<S>) Proxy.newProxyInstance(WrapperPoolable.class.getClassLoader(),
																		interfaces,
																		new DynamicInvocationHandler(poolable, s));
	}

	protected abstract S createSoapObject();
}

@RequiredArgsConstructor
class DynamicInvocationHandler implements InvocationHandler {

	private final Poolable poolable;

	private final Object object;

	@Override
	public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {

		// CAVEAT: most "super" types should be first, otherwise methods would be caught early by the if blocks

		// for toString, equals etc.
		if (method.getDeclaringClass().isAssignableFrom(Object.class)) {
			return method.invoke(poolable, args);
		}

		// If the method is Closeable#close then it means we're returning the object to the pool.
		// Remember we put the proxy into the pool not the wrapped SoapPoolable!
		// If we return the object in the poolable field the we get following exception:
		// java.lang.IllegalStateException: Returned object not currently part of this pool
		if (method.getDeclaringClass().isAssignableFrom(Closeable.class)) {

			this.poolable.getObjectPool().returnObject((Poolable) proxy);
			return null;// Closeable has only one method and it returns void
		}

		if (method.getDeclaringClass().isAssignableFrom(Poolable.class)) {
			return method.invoke(poolable, args);
		}

		// WrapperPoolable#getWrappedObject method should return the wrapped object directly
		// See implementation of com.kodgemisi.spring.pooledfactory.SoapProxyWrapperPoolable#getWrappedObject
		if (method.getDeclaringClass().isAssignableFrom(WrapperPoolable.class)) {
			return object;
		}

		return method.invoke(object, args);
	}

}
