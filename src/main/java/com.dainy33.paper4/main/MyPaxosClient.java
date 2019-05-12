package com.dainy33.paper4.main;

import com.dainy33.paper4.core.WorkerType;
import com.dainy33.paper4.exception.PaxosClientNullAddressException;
import com.dainy33.paper4.packet.Packet;
import com.dainy33.paper4.packet.PacketBean;
import com.dainy33.paper4.packet.Value;
import com.dainy33.paper4.utils.client.ClientImplByLC4J;
import com.dainy33.paper4.utils.client.CommClient;
import com.dainy33.paper4.utils.serializable.ObjectSerialize;
import com.dainy33.paper4.utils.serializable.ObjectSerializeImpl;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.ArrayDeque;
import java.util.Queue;
import java.util.UUID;

public class MyPaxosClient {
	// 要发往的proposer的host地址
	private String host;
	// proposer的port
	private int port;
	// comm client
	private CommClient commClient;
	// buffer's size
	private int bufferSize = 8;
	// buffer
	private Queue<byte[]> buffer; 
	
	private int tmp = 0;
	
	private ObjectSerialize objectSerialize = new ObjectSerializeImpl();
	
	public MyPaxosClient() throws IOException {
		super();
		this.commClient = new ClientImplByLC4J(1);
		this.buffer = new ArrayDeque<>();
	}
	
	/**
	 * 设置发送缓冲区大小
	 * @param bufferSize
	 */
	public void setSendBufferSize(int bufferSize) {
		this.bufferSize = bufferSize;
	}
	
	/**
	 * 设置对端地址
	 * @param host
	 * @param port
	 */
	public void setRemoteAddress(String host, int port) {
		this.host = host;
		this.port = port;
	}
	
	/**
	 * 刷新buffer
	 * @param groupId
	 * @throws UnknownHostException
	 * @throws IOException
	 */
	public void flush(int groupId) throws UnknownHostException, IOException {
		if (this.buffer.isEmpty())
			return;
		UUID uuid = UUID.randomUUID();
		Packet packet = new Packet(new PacketBean("SubmitPacket", new Value(uuid, this.objectSerialize.objectToObjectArray(this.buffer))), groupId, WorkerType.SERVER);
		this.commClient.sendTo(this.host, this.port, this.objectSerialize.objectToObjectArray(packet));
		this.buffer.clear();
	}
	
	/**
	 *  提交提案
	 * @param value
	 * @param groupId
	 * @throws PaxosClientNullAddressException
	 * @throws UnknownHostException
	 * @throws IOException
	 */
	public void submit(byte[] value, int groupId) throws PaxosClientNullAddressException, UnknownHostException, IOException {
		if (this.host == null)
			throw new PaxosClientNullAddressException();
		this.buffer.add(value);
		if (this.buffer.size() >= this.bufferSize) {
			flush(groupId);
		}
	}
}
