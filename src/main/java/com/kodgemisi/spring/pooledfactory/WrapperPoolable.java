package com.kodgemisi.spring.pooledfactory;

public interface WrapperPoolable<S> extends Poolable {

	S getWrappedObject();

}