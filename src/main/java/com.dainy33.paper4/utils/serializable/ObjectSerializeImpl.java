package com.dainy33.paper4.utils.serializable;

import java.io.*;

public class ObjectSerializeImpl implements ObjectSerialize {

	@Override
	public byte[] objectToObjectArray(Object object) throws IOException {
		byte[] bytes = null;
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		ObjectOutputStream oos = new ObjectOutputStream(bos);
		oos.writeObject(object);
		oos.flush();
		bytes = bos.toByteArray();
		oos.close();
		bos.close();
		return bytes;
	}

	@Override
	public <T> T byteArrayToObject(byte[] byteArray, Class<T> type) throws ClassNotFoundException, IOException {
		T obj = null;
		ByteArrayInputStream bis = new ByteArrayInputStream(byteArray);
		ObjectInputStream ois = new ObjectInputStream(bis);
		obj = (T) ois.readObject();
		ois.close();
		bis.close();
		return obj;
	}

}
