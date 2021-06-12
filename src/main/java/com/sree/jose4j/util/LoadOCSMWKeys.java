package com.sree.jose4j.util;

import java.io.File;
import java.io.FileInputStream;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

public class LoadOCSMWKeys {
	public static PrivateKey ocs_privateKey;
	public static PublicKey mw_publicKey;
	public static PublicKey ocs_publicKey;

	static {
		ClassLoader classLoader = LoadOCSMWKeys.class.getClassLoader();

		File sender_pri_key_file = new File(classLoader.getResource("static/mw/sender_pri_key.pem").getFile());
		File rec_pub_key_file = new File(classLoader.getResource("static/mw/rec_pub_key.cer").getFile());
		File sender_pub_key_file = new File(classLoader.getResource("static/mw/sender_pub.cer").getFile());

		try {
			ocs_privateKey = readPrivateKey(sender_pri_key_file);
			ocs_publicKey = readPublicKeyNew(sender_pub_key_file);
			mw_publicKey = readPublicKeyNew(rec_pub_key_file);
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
	
	
	public static RSAPublicKey readPublicKeyNew(File file) throws Exception {
		FileInputStream fis = new FileInputStream(file);
		CertificateFactory cf1 = CertificateFactory.getInstance("X509");
		X509Certificate crt = (X509Certificate) cf1.generateCertificate(fis);
		RSAPublicKey publicKey = (RSAPublicKey) crt.getPublicKey();
		return publicKey;
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
