package com.dainy33.paper4.benchmark;

import com.dainy33.paper4.main.MyPaxos;

import java.io.IOException;

public class BMServer3 {
	public static void main(String[] args) {
		try {
			MyPaxos server = new MyPaxos("./conf/conf3.json");
			server.setGroupId(1, new BMCallback());
			server.start();
		} catch (IOException | InterruptedException | ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
