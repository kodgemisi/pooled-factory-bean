package com.kodgemisi.spring.pooledfactory;

import org.apache.commons.pool2.impl.GenericObjectPoolConfig;

/**
 * Created on June, 2019
 *
 * @author destans
 */
public class PoolConfig<T> extends GenericObjectPoolConfig<T> {

	@Override
	public boolean getBlockWhenExhausted() {
		return false;
	}

}
