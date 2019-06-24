package com.kodgemisi.spring.pooledfactory;

import org.apache.commons.pool2.impl.GenericObjectPool;
import org.springframework.beans.factory.SmartFactoryBean;
import org.springframework.beans.factory.config.AbstractFactoryBean;

/**
 * Created on June, 2019
 *
 * @author destans
 */
public abstract class AbstractPooledFactoryBean<P extends Poolable> extends AbstractFactoryBean<P> implements SmartFactoryBean<P> {

	private final GenericObjectPool<P> objectPool;

	public AbstractPooledFactoryBean(AbstractPooledObjectFactory<P> abstractPooledObjectFactory) {
		this(abstractPooledObjectFactory, new PoolConfig<>());
	}

	public AbstractPooledFactoryBean(AbstractPooledObjectFactory<P> abstractPooledObjectFactory, PoolConfig<P> poolConfig) {
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
			return objectPool.borrowObject();
		}
		catch (Exception e) {
			throw new IllegalStateException("Couldn't borrow an instance from the pool.", e);
		}
	}

	public P getPoolable() {
		return createInstance();
	}

}
