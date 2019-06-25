package com.kodgemisi.spring.pooledfactory;

import org.apache.commons.pool2.ObjectPool;

/**
 * Created on June, 2019
 *
 * @author destan
 */
class SoapProxyWrapperPoolable<S> extends AbstractPoolable implements WrapperPoolable<S> {

	SoapProxyWrapperPoolable(ObjectPool<Poolable> objectPool) {
		super(objectPool);
	}

	@Override
	public S getWrappedObject() {
		throw new UnsupportedOperationException("This method is never meant to be invoked directly. It should be invoked via the proxy object.");
	}
}
