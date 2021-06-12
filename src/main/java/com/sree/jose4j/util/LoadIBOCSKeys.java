package com.sree.jose4j.util;

import java.io.File;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.security.KeyFactory;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

public class LoadIBOCSKeys {
	public static RSAPrivateKey ocs_privateKey;
	public static RSAPublicKey ocs_publicKey;
	public static RSAPublicKey ib_publicKey;

	public static RSAPrivateKey ib_privateKey;

	
	static {
		ClassLoader classLoader = LoadIBOCSKeys.class.getClassLoader();

		File ocs_pri_key_file = new File(classLoader.getResource("static/ocs/OCS_Private.pem").getFile());
		File ocs_pub_key_file = new File(classLoader.getResource("static/ocs/OCS_Public.pem").getFile());
		File ib_pub_key_file = new File(classLoader.getResource("static/ocs/IB_Public.pem").getFile());

		File ib_pri_key_file = new File(classLoader.getResource("static/ocs/IB_Private.pem").getFile());
		try {
			ocs_privateKey = readPrivateKey(ocs_pri_key_file);
			ocs_publicKey = readPublicKey(ocs_pub_key_file);
			ib_publicKey = readPublicKey(ib_pub_key_file);
			
			ib_privateKey = readPrivateKey(ib_pri_key_file);
			
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
	
	
	public static RSAPublicKey readPublicKey(File file) throws Exception {
		String key = new String(Files.readAllBytes(file.toPath()), Charset.defaultCharset());

		String publicKeyPEM = key.replace("-----BEGIN PUBLIC KEY-----", "").replaceAll(System.lineSeparator(), "")
				.replace("-----END PUBLIC KEY-----", "");

		byte[] encoded = org.apache.commons.codec.binary.Base64.decodeBase64(publicKeyPEM);

		KeyFactory keyFactory = KeyFactory.getInstance("RSA");
		X509EncodedKeySpec keySpec = new X509EncodedKeySpec(encoded);
		return (RSAPublicKey) keyFactory.generatePublic(keySpec);
	}
	
	
	public static RSAPrivateKey readPrivateKey(File file) throws Exception {
		String key = new String(Files.readAllBytes(file.toPath()), Charset.defaultCharset());

		String privateKeyPEM = key.replace("-----BEGIN PRIVATE KEY-----", "").replaceAll(System.lineSeparator(), "")
				.replace("-----END PRIVATE KEY-----", "");

		byte[] encoded = org.apache.commons.codec.binary.Base64.decodeBase64(privateKeyPEM);

		KeyFactory keyFactory = KeyFactory.getInstance("RSA");
		PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(encoded);
		return (RSAPrivateKey) keyFactory.generatePrivate(keySpec);
	}
}
