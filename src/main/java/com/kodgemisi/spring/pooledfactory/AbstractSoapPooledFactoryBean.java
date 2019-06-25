package com.kodgemisi.spring.pooledfactory;

/**
 * Created on June, 2019
 *
 * @author destan
 */
public abstract class AbstractSoapPooledFactoryBean<S> extends AbstractPooledFactoryBean<WrapperPoolable<S>> {

	public AbstractSoapPooledFactoryBean(AbstractPooledObjectFactory<WrapperPoolable<S>> abstractPooledObjectFactory) {
		super(abstractPooledObjectFactory);
	}

	public AbstractSoapPooledFactoryBean(AbstractPooledObjectFactory<WrapperPoolable<S>> abstractPooledObjectFactory,
			PoolConfig<WrapperPoolable<S>> poolConfig) {
		super(abstractPooledObjectFactory, poolConfig);
	}

	/**
	 * This method intentionally made private to remind us returning S directly prevents the ability to force developer
	 * to use try-with-resources syntax.
	 */
	@Deprecated
	private S getSoapObject() {
		return this.getPoolable().getWrappedObject();
	}
}
