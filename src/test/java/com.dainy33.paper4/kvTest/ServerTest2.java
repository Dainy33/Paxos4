package com.dainy33.paper4.kvTest;

import com.dainy33.paper4.main.MyPaxos;

import java.io.IOException;

public class ServerTest2 {
	public static void main(String[] args) {
		try {
			MyPaxos server = new MyPaxos("./conf/conf2.json");
			server.setGroupId(1, new KvCallback());
			server.setGroupId(2, new KvCallback());
			server.start();
		} catch (IOException | InterruptedException | ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
