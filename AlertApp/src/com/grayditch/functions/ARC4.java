package com.grayditch.functions;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class ARC4 {

	public static String RunRC4(String aInput, String pass)
			throws NoSuchAlgorithmException, UnsupportedEncodingException {

		MessageDigest md = MessageDigest.getInstance("SHA1");

		byte[] digest = md.digest((pass).getBytes());

		String aKey = ARC4.stringToHex(new String(digest));
		
		aKey = pass;

		int[] iS = new int[256];
		byte[] iK = new byte[256];

		for (int i = 0; i < 256; i++)
			iS[i] = i;

		int j = 1;

		for (short i = 0; i < 256; i++) {
			iK[i] = (byte) aKey.charAt((i % aKey.length()));
		}

		j = 0;

		for (int i = 0; i < 255; i++) {
			j = (j + iS[i] + iK[i]) % 256;
			int temp = iS[i];
			iS[i] = iS[j];
			iS[j] = temp;
		}

		int i = 0;
		j = 0;
		String rOutput = "";
		short iMask = 15;
		char[] iInputChar = aInput.toCharArray();
		char[] iOutputChar = new char[iInputChar.length];
		for (short x = 0; x < iInputChar.length; x++) {
			i = (i + 1) % 256;
			j = (j + iS[i]) % 256;
			int temp = iS[i];
			iS[i] = iS[j];
			iS[j] = temp;
			int t = (iS[i] + (iS[j] % 256)) % 256;
			int iY = iS[t];
			char iCY = (char) iY;
			iOutputChar[x] = (char) (iInputChar[x] ^ iCY);
		}

		return new String(iOutputChar);

	}

	public static String convertHexToString(String hex) {

		StringBuilder sb = new StringBuilder();
		StringBuilder temp = new StringBuilder();

		// 49204c6f7665204a617661 split into two characters 49, 20, 4c...
		for (int i = 0; i < hex.length() - 1; i += 2) {

			// grab the hex in pairs
			String output = hex.substring(i, (i + 2));
			// convert hex to decimal
			int decimal = Integer.parseInt(output, 16);
			// convert the decimal to character
			sb.append((char) decimal);

			temp.append(decimal);
		}

		return sb.toString();
	}

	public static String stringToHex(final String in) {
		StringBuilder out = new StringBuilder(in.length() * 2);

		for (int i = 0; i < in.length(); i++) {
			String hex = Integer.toHexString(in.charAt(i));
			if (hex.length() < 2)
				hex = "0" + hex;
			out.append(hex);
		}

		return out.toString();
	}

}