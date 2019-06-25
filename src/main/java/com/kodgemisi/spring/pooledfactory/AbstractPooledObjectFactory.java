package com.kodgemisi.spring.pooledfactory;

import org.apache.commons.pool2.ObjectPool;
import org.apache.commons.pool2.PooledObject;
import org.apache.commons.pool2.PooledObjectFactory;
import org.apache.commons.pool2.impl.DefaultPooledObject;

/**
 * Creates {@link Poolable} objects to be held by an {@link org.apache.commons.pool2.ObjectPool}
 *
 * Created on June, 2019
 *
 * @author destans
 */
public abstract class AbstractPooledObjectFactory<P extends Poolable> implements PooledObjectFactory<P> {

	/**
	 * Just required to pass to the {@link Poolable} to be used when calling {@link java.io.Closeable#close()} to return the
	 * {@link Poolable} to the pool.
	 */
	protected ObjectPool<P> objectPool;

	public synchronized void setObjectPool(ObjectPool<P> objectPool) {
		if (this.objectPool != null) {
			throw new IllegalStateException("objectPool is already set, cannot be set again!");
		}
		this.objectPool = objectPool;
	}

	protected ObjectPool<Poolable> getObjectPool() {
		return (ObjectPool<Poolable>) this.objectPool;
	}

	public abstract P createObject();

	@Override
	public PooledObject<P> makeObject() throws Exception {
		return new DefaultPooledObject<>(createObject());
	}

	@Override
	public void destroyObject(PooledObject<P> p) throws Exception {

	}

	@Override
	public boolean validateObject(PooledObject<P> p) {
		return false;
	}

	@Override
	public void activateObject(PooledObject<P> p) throws Exception {
		p.getObject().activateObject();
	}

	@Override
	public void passivateObject(PooledObject<P> p) throws Exception {
		p.getObject().passivateObject();
	}
}
