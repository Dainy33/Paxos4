package com.dainy33.paper4.exception;

public class PaxosClientNullAddressException extends Exception {
	public PaxosClientNullAddressException() {
		super("paxos client remote side address is null");
	}
}
