package com.dainy33.paper4.utils.server;

public interface CommServer {
	public byte[] recvFrom() throws InterruptedException;
}
