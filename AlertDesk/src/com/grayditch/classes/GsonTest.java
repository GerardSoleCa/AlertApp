package com.grayditch.classes;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;

import com.google.gson.Gson;
import com.grayditch.functions.ARC4;

public class GsonTest {

	public static void main(String[] args) {
		Gson gson = new Gson();

		try {
			CALL call = new CALL("dfdf", "43434");
			String smsString = gson.toJson(call);
			System.out
					.println(ARC4.stringToHex(ARC4.RunRC4(smsString, "1234")));
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	}

}
