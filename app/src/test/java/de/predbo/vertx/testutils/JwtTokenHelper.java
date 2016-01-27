package de.predbo.vertx.testutils;

import io.vertx.core.json.JsonObject;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

public class JwtTokenHelper {

	private static final Charset UTF8 = StandardCharsets.UTF_8;

	public static JsonObject decodeTokenToJsonObj(String token) {
		String[] segments = token.split("\\.");
		if (segments.length != 3) {
			throw new RuntimeException("Not enough or too many segments");
		}

		// All segment should be base64
		String headerSeg = segments[0];
		String payloadSeg = segments[1];
//		String signatureSeg = segments[2];

		// base64 decode and parse JSON
		JsonObject header = new JsonObject(new String(base64urlDecode(headerSeg), UTF8));
		JsonObject payload = new JsonObject(new String(base64urlDecode(payloadSeg), UTF8));
		System.out.println("JWT Token:\n" + header.encodePrettily() + "\n" +  payload.encodePrettily());
		
		return payload;
	}

	public static String decodeTokenToString(String encodedToken) {
		return decodeTokenToJsonObj(encodedToken).encode();
	}

	public static String decodeTokenToStringFromCookieHeader(String cookieHeader) {
		if (cookieHeader != null) {
			String[] parts = cookieHeader.split("; ");
			for (String string : parts) {
				if (string.startsWith("auth_token=")) {
					return decodeTokenToString(string.substring(11));
				}
			}
		}
		return null;
	}

	private static byte[] base64urlDecode(String str) {
		return Base64.getUrlDecoder().decode(str.getBytes(UTF8));
	}

}
