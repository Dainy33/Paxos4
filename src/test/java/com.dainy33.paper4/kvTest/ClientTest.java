package com.dainy33.paper4.kvTest;

import com.dainy33.paper4.exception.PaxosClientNullAddressException;
import com.dainy33.paper4.main.MyPaxosClient;
import com.google.gson.Gson;

import java.io.IOException;

public class ClientTest {

	public static void main(String[] args) {
		try {
			MyPaxosClient client = new MyPaxosClient();
			client.setSendBufferSize(20);
			client.setRemoteAddress("localhost", 33333);
			Gson gson = new Gson();
			client.submit(gson.toJson(new MsgBean("put", "name", "Mike")).getBytes(), 1);
			client.submit(gson.toJson(new MsgBean("put", "name", "Neo")).getBytes(), 1);
			client.submit(gson.toJson(new MsgBean("get", "name", "")).getBytes(), 1);
			client.flush(1);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (PaxosClientNullAddressException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
