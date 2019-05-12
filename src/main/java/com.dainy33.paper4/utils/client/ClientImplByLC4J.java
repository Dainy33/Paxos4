package com.dainy33.paper4.utils.client;

import com.github.luohaha.client.LightCommClient;
import com.github.luohaha.connection.Conn;
import com.github.luohaha.exception.ConnectionCloseException;
import com.github.luohaha.param.ClientParam;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;

public class ClientImplByLC4J implements CommClient {

	private LightCommClient client;
	private Map<String, Conn> addressToConn = new HashMap<>();

	public ClientImplByLC4J(int ioThreadPoolSize) throws IOException {
		// TODO Auto-generated constructor stub
		this.client = new LightCommClient(ioThreadPoolSize);
	}

	@Override
	public void sendTo(String ip, int port, byte[] msg) throws IOException {
		// TODO Auto-generated method stub
		ClientParam param = new ClientParam();
		param.setLogLevel(Level.WARNING);
		param.setOnConnect(conn -> {
			String key = ip + ":" + port;
			this.addressToConn.put(key, conn);
		});
		String key = ip + ":" + port;
		if (!addressToConn.containsKey(key)) {
			client.connect(ip, port, param);
		}
		int count = 0;
		do {
			try {
				if (count >= 3)
					break;
				while (addressToConn.get(key) == null);
				addressToConn.get(key).write(msg);
				break;
			} catch (ConnectionCloseException e) {
				e.printStackTrace();
				addressToConn.remove(key);
				client.connect(ip, port, param);
				count++;
			}
		} while (true);
	}

}
