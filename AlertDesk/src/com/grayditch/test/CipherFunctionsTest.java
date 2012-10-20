package com.grayditch.test;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import com.grayditch.functions.ARC4;

public class CipherFunctionsTest {

	/**
	 * @param args
	 * @throws BadPaddingException
	 * @throws IllegalBlockSizeException
	 * @throws NoSuchPaddingException
	 * @throws NoSuchAlgorithmException
	 * @throws UnsupportedEncodingException
	 * @throws InvalidKeyException
	 */
	public static void main(String[] args) throws InvalidKeyException,
			UnsupportedEncodingException, NoSuchAlgorithmException,
			NoSuchPaddingException, IllegalBlockSizeException,
			BadPaddingException {
		String message = "Hola que tal";
		String pass = "12346";

		String cryptString = ARC4.stringToHex(ARC4.RunRC4(message, pass));
		System.out.println("Crypted: " + cryptString);
		System.out.println("Decrypted: "
				+ ARC4.RunRC4(ARC4.convertHexToString(cryptString), pass));

	}

}
