package com.kodgemisi.spring.pooledfactory;

import org.apache.commons.pool2.ObjectPool;

import java.io.Closeable;

/**
 * <p>This class is designed to automatically return the pooled object to the {@link org.apache.commons.pool2.ObjectPool}.</p>
 * <p>Objects of this class are always put into a {@link org.apache.commons.pool2.PooledObject}.</p>
 * <p>This class can (roughly) be considered as providing <em>auto-returning to the pool</em> capability to {@code PooledObject}s.</p>
 * <p>
 * Created on June, 2019
 *
 * @author destans
 */
public interface Poolable extends Closeable {

	default void activateObject() {

	}

	default void passivateObject() {

	}

	ObjectPool<Poolable> getObjectPool();

	@Override
	default void close() {
		try {
			getObjectPool().returnObject(this);
		}
		catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

}
