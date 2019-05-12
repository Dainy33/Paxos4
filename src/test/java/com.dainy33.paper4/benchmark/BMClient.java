package com.dainy33.paper4.benchmark;

import com.dainy33.paper4.exception.PaxosClientNullAddressException;
import com.dainy33.paper4.main.MyPaxosClient;

import java.io.IOException;

public class BMClient {
	public static void main(String[] args) {
		MyPaxosClient client;
		try {
			client = new MyPaxosClient();
			client.setSendBufferSize(2000);
			client.setRemoteAddress("localhost", 33333);
			System.out.println("[START] : " + System.currentTimeMillis());
			for (int i = 1; i <= 100000; i++) {
				client.submit(String.valueOf(i).getBytes(), 1);
			}
			client.submit("end".getBytes(), 1);
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
