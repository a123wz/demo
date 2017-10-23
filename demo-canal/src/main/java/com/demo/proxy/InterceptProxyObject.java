package com.demo.proxy;

import java.lang.reflect.Method;

import net.sf.cglib.proxy.MethodProxy;

public class InterceptProxyObject {
	private Object target;
	private Method method;
	private Object[] args;
	private MethodProxy methodProxy;

	public InterceptProxyObject(Object o, Method method, Object[] args, MethodProxy methodProxy) {
		this.target = o;
		this.method = method;
		this.args = args;
		this.methodProxy = methodProxy;
	}

	public Object getTarget() {
		return target;
	}

	public void setTarget(Object target) {
		this.target = target;
	}

	public Method getMethod() {
		return method;
	}

	public void setMethod(Method method) {
		this.method = method;
	}

	public Object[] getArgs() {
		return args;
	}

	public void setArgs(Object[] args) {
		this.args = args;
	}

	public MethodProxy getMethodProxy() {
		return methodProxy;
	}

	public void setMethodProxy(MethodProxy methodProxy) {
		this.methodProxy = methodProxy;
	}
}
