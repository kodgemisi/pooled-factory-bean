package com.kodgemisi.spring.pooledfactory;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.pool2.ObjectPool;

/**
 * Created on June, 2019
 *
 * @author destan
 */
@Slf4j
public class AbstractPoolable implements Poolable {

	protected final ObjectPool<Poolable> objectPool;

	protected AbstractPoolable(ObjectPool<Poolable> objectPool) {
		this.objectPool = objectPool;
	}

	@Override
	public ObjectPool<Poolable> getObjectPool() {
		return objectPool;
	}
}
