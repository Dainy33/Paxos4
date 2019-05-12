package com.dainy33.paper4.core;

public interface PaxosCallback {
	/**
	 * 执行器，用于执行确定的状态
	 * @param msg
	 */
	public void callback(byte[] msg);
}
