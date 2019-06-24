package com.kodgemisi.spring.pooledfactory;

import org.apache.commons.pool2.ObjectPool;

import java.io.Closeable;
import java.io.IOException;

/**
 *
 * <p>This class is designed to automatically return the pooled object to the {@link org.apache.commons.pool2.ObjectPool}.</p>
 * <p>Objects of this class are always put into a {@link org.apache.commons.pool2.PooledObject}.</p>
 * <p>This class can (roughly) be considered as providing <em>auto-returning to the pool</em> capability to {@code PooledObject}s.</p>
 *
 * Created on June, 2019
 *
 * @author destans
 */
public abstract class Poolable implements Closeable {

	private final ObjectPool<Poolable> objectPool;

	protected Poolable(ObjectPool<Poolable> objectPool) {
		this.objectPool = objectPool;
	}

	@Override
	public void close() throws IOException {
		try {
			this.objectPool.returnObject(this);
		}
		catch (Exception e) {
			throw new IOException(e);
		}
	}

	public void activateObject() throws Exception {

	}

	public void passivateObject() throws Exception {

	}

}
