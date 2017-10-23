package com.demo.proxy;

public interface CanalIntercept {
	public void beforeIntercept(InterceptProxyObject target);
	public void afterIntercept(InterceptProxyObject target);
}
