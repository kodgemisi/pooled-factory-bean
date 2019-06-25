package com.kodgemisi.spring.pooledfactory;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.pool2.impl.GenericObjectPool;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.springframework.beans.factory.SmartFactoryBean;
import org.springframework.beans.factory.config.AbstractFactoryBean;

/**
 * Created on June, 2019
 *
 * @author destans
 */
@Slf4j
public abstract class AbstractPooledFactoryBean<P extends Poolable> extends AbstractFactoryBean<P> implements SmartFactoryBean<P> {

	private final GenericObjectPool<P> objectPool;

	public AbstractPooledFactoryBean(AbstractPooledObjectFactory<P> abstractPooledObjectFactory) {
		this(abstractPooledObjectFactory, new DefaultPoolConfig<>());
	}

	public AbstractPooledFactoryBean(AbstractPooledObjectFactory<P> abstractPooledObjectFactory, GenericObjectPoolConfig<P> poolConfig) {
		this.objectPool = new GenericObjectPool<>(abstractPooledObjectFactory, poolConfig);
		abstractPooledObjectFactory.setObjectPool(this.objectPool);
		this.setSingleton(false);
	}

	@Override
	public Class<?> getObjectType() {
		return Poolable.class;
	}

	@Override
	protected P createInstance() {
		try {

			if (log.isTraceEnabled()) {
				log.trace("Borrowing object from the pool");
			}

			final P p = objectPool.borrowObject();

			if (log.isTraceEnabled()) {
				log.trace("Borrowed object from the pool {}", p);
			}

			return p;
		}
		catch (Exception e) {
			throw new IllegalStateException("Couldn't borrow an instance from the pool.", e);
		}
	}

	public P getPoolable() {
		return createInstance();
	}

}
