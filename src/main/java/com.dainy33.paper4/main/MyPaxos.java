package com.dainy33.paper4.main;

import com.dainy33.paper4.core.Accepter;
import com.dainy33.paper4.core.ConfObject;
import com.dainy33.paper4.core.InfoObject;
import com.dainy33.paper4.core.Learner;
import com.dainy33.paper4.core.PaxosCallback;
import com.dainy33.paper4.core.Proposer;
import com.dainy33.paper4.packet.Packet;
import com.dainy33.paper4.utils.FileUtils;
import com.dainy33.paper4.utils.client.ClientImplByLC4J;
import com.dainy33.paper4.utils.client.CommClient;
import com.dainy33.paper4.utils.serializable.ObjectSerialize;
import com.dainy33.paper4.utils.serializable.ObjectSerializeImpl;
import com.dainy33.paper4.utils.server.CommServer;
import com.dainy33.paper4.utils.server.ServerImplByLC4J;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MyPaxos {

	/**
	 * 全局配置文件信息
	 */
	private ConfObject confObject;

	/**
	 * 本节点的信息
	 */
	private InfoObject infoObject;

	/**
	 * 配置文件所在的位置
	 */
	private String confFile;

	private Map<Integer, PaxosCallback> groupidToCallback = new HashMap<>();

	private Map<Integer, Proposer> groupidToProposer = new HashMap<>();

	private Map<Integer, Accepter> groupidToAccepter = new HashMap<>();

	private Map<Integer, Learner> groupidToLearner = new HashMap<>();

	private Gson gson = new Gson();
	
	private ObjectSerialize objectSerialize = new ObjectSerializeImpl();
	
	private Logger logger = Logger.getLogger("MyPaxos");

	/*
	 * 客户端
	 */
	private CommClient client;

	public MyPaxos(String confFile) throws IOException {
		super();
		this.confFile = confFile;
		this.confObject = gson.fromJson(FileUtils.readFromFile(this.confFile), ConfObject.class);
		this.infoObject = getMy(this.confObject.getNodes());
		// 启动客户端
		this.client = new ClientImplByLC4J(4);
		//this.logger.setLevel(Level.WARNING);
	}
	
	/**
	 * 设置log级别
	 * @param level
	 * 级别
	 */
	public void setLogLevel(Level level) {
		this.logger.setLevel(level);
	}
	
	/**
	 * add handler
	 * @param handler
	 * handler
	 */
	public void addLogHandler(Handler handler) {
		this.logger.addHandler(handler);
	}

	/**
	 * 
	 * @param id
	 * @param executor
	 */
	public void setGroupId(int groupId, PaxosCallback executor) {
		Accepter accepter = new Accepter(infoObject.getId(), confObject.getNodes(), infoObject, confObject, groupId,
				this.client);
		Proposer proposer = new Proposer(infoObject.getId(), confObject.getNodes(), infoObject, confObject.getTimeout(),
				accepter, groupId, this.client);
		Learner learner = new Learner(infoObject.getId(), confObject.getNodes(), infoObject, confObject, accepter,
				executor, groupId, this.client);
		this.groupidToCallback.put(groupId, executor);
		this.groupidToAccepter.put(groupId, accepter);
		this.groupidToProposer.put(groupId, proposer);
		this.groupidToLearner.put(groupId, learner);
	}

	/**
	 * 获得我的accepter或者proposer信息
	 * 
	 * @param accepters
	 * @return
	 */
	private InfoObject getMy(List<InfoObject> infoObjects) {
		for (InfoObject each : infoObjects) {
			if (each.getId() == confObject.getMyid()) {
				return each;
			}
		}
		return null;
	}

	/**
	 * 启动paxos服务器
	 * 
	 * @throws IOException
	 * @throws InterruptedException
	 * @throws ClassNotFoundException 
	 */
	public void start() throws IOException, InterruptedException, ClassNotFoundException {
		// 启动paxos服务器
		CommServer server = new ServerImplByLC4J(this.infoObject.getPort(), 4);
		System.out.println("paxos server-" + confObject.getMyid() + " start...");
		while (true) {
			byte[] data = server.recvFrom();
			//Packet packet = gson.fromJson(new String(data), Packet.class);
			Packet packet = objectSerialize.byteArrayToObject(data, Packet.class);
			int groupId = packet.getGroupId();
			Accepter accepter = this.groupidToAccepter.get(groupId);
			Proposer proposer = this.groupidToProposer.get(groupId);
			Learner learner = this.groupidToLearner.get(groupId);
			if (accepter == null || proposer == null || learner == null) {
				return;
			}
			switch (packet.getWorkerType()) {
			case ACCEPTER:
				accepter.sendPacket(packet.getPacketBean());
				break;
			case PROPOSER:
				proposer.sendPacket(packet.getPacketBean());
				break;
			case LEARNER:
				learner.sendPacket(packet.getPacketBean());
				break;
			case SERVER:
				proposer.sendPacket(packet.getPacketBean());
				break;
			default:
				break;
			}
		}
	}
}
