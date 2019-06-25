package com.kodgemisi.spring.pooledfactory;

import org.apache.commons.pool2.ObjectPool;

/**
 * Created on June, 2019
 *
 * @author destan
 */
public class DefaultWrapperPoolable<S> extends AbstractPoolable implements WrapperPoolable<S> {

	protected DefaultWrapperPoolable(ObjectPool<Poolable> objectPool) {
		super(objectPool);
	}

	@Override
	public S getWrappedObject() {
		throw new UnsupportedOperationException();
	}
}
