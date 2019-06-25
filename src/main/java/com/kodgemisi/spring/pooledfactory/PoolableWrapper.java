package com.kodgemisi.spring.pooledfactory;

import lombok.NonNull;
import org.apache.commons.pool2.ObjectPool;

/**
 * This class provided as a wrapper for 3rd-party classes in order to make them {@link com.kodgemisi.spring.pooledfactory.Poolable}
 * otherwise they cannot extend {@link com.kodgemisi.spring.pooledfactory.Poolable} as developers has no access to the code.
 *
 * Created on June, 2019
 *
 * @author destans
 */
public class PoolableWrapper<T> extends AbstractPoolable {

	private final T object;

	public PoolableWrapper(ObjectPool<Poolable> objectPool, @NonNull T object) {
		super(objectPool);
		this.object = object;
	}

	public T getObject() {
		assert object != null : "object can never be null!";
		return object;
	}
}
