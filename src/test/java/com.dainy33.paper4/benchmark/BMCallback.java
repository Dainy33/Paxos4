package com.dainy33.paper4.benchmark;

import com.dainy33.paper4.core.PaxosCallback;

public class BMCallback implements PaxosCallback {
	
	private int count = 1;
	private int fail = 0;

	@Override
	public void callback(byte[] msg) {
		String mString = new String(msg);
		if (mString.equals("end")) {
			System.out.println("fail count : " + fail + " count : " + count);
			System.out.println("[end] : " + System.currentTimeMillis());
		} else {
			if (count == Integer.valueOf(mString)) {
				count++;
			} else {
				System.out.println("[FAIL] : " + count + " " + mString);
				fail++;
				count = Integer.valueOf(mString);
				count++;
			}
		}
	}

}
