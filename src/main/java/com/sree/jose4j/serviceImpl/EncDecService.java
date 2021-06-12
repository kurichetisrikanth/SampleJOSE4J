package com.sree.jose4j.serviceImpl;

import org.jose4j.jwe.ContentEncryptionAlgorithmIdentifiers;
import org.jose4j.jwe.JsonWebEncryption;
import org.jose4j.jwe.KeyManagementAlgorithmIdentifiers;
import org.jose4j.jwk.JsonWebKey;
import org.jose4j.jws.AlgorithmIdentifiers;
import org.jose4j.jws.JsonWebSignature;
import org.jose4j.jwx.CompactSerializer;
import org.jose4j.lang.JoseException;
import org.springframework.stereotype.Component;

import com.sree.jose4j.model.OCSReqResDTO;
import com.sree.jose4j.util.LoadIBOCSKeys;
import com.sree.jose4j.util.LoadOCSMWKeys;



@Component
public class EncDecService {

	public String processIBRequest(String payload) throws JoseException {
		
		JsonWebEncryption jwe = new JsonWebEncryption();
		JsonWebKey jwk = JsonWebKey.Factory.newJwk(LoadIBOCSKeys.ocs_privateKey);
		//JsonWebKey jwk = JsonWebKey.Factory.newJwk(LoadOCSMWKeys.ocs_privateKey);
        jwe.setCompactSerialization(payload);
        jwe.setKey(jwk.getKey());
        
        String payloadOut = jwe.getPayload();
		
        JsonWebKey jwk1 = JsonWebKey.Factory.newJwk(LoadIBOCSKeys.ib_publicKey);
        JsonWebSignature jws = new JsonWebSignature();
        jws.setDoKeyValidation(false);
        jws.setCompactSerialization(payloadOut);
        //jws.setKey(LoadOCSMWKeys.mw_publicKey);
        jws.setKey(jwk1.getKey());
        
        
        if(jws.verifySignature()) {
        	return jws.getPayload();
        }
        return null;
	}

	public String sendOCSResponse(String payload) throws JoseException {
	//public OCSReqResDTO sendOCSResponse(String payload) throws JoseException {

		//JsonWebKey jwk1 = JsonWebKey.Factory.newJwk(LoadOCSMWKeys.ocs_privateKey);
		JsonWebKey jwk1 = JsonWebKey.Factory.newJwk(LoadIBOCSKeys.ocs_privateKey);
		JsonWebSignature jws = new JsonWebSignature();
		jws.setAlgorithmHeaderValue(AlgorithmIdentifiers.RSA_USING_SHA512);
        jws.setPayload(payload);
        jws.setKey(jwk1.getKey());
        jws.sign();
        
        
        //JsonWebKey jwk = JsonWebKey.Factory.newJwk(LoadOCSMWKeys.mw_publicKey);
        JsonWebKey jwk = JsonWebKey.Factory.newJwk(LoadIBOCSKeys.ib_publicKey);
        JsonWebEncryption jwe = new JsonWebEncryption();
        jwe.setAlgorithmHeaderValue(KeyManagementAlgorithmIdentifiers.RSA_OAEP_256);
        jwe.setEncryptionMethodHeaderParameter(ContentEncryptionAlgorithmIdentifiers.AES_256_CBC_HMAC_SHA_512);
        jwe.setKey(jwk.getKey());
        jwe.setPayload(jws.getCompactSerialization());
        
        System.out.println(jwe.getCompactSerialization());
		//return getOCSReqResFormat(jwe.getCompactSerialization());
        return jwe.getCompactSerialization();
	}

	public String sendIBRequest(String payload) throws JoseException {
	//public OCSReqResDTO sendIBRequest(String payload) throws JoseException {
		
		//JsonWebKey jwk1 = JsonWebKey.Factory.newJwk(LoadOCSMWKeys.ocs_privateKey);
		JsonWebKey jwk1 = JsonWebKey.Factory.newJwk(LoadIBOCSKeys.ib_privateKey);
		JsonWebSignature jws = new JsonWebSignature();
		jws.setAlgorithmHeaderValue(AlgorithmIdentifiers.RSA_USING_SHA512);
        jws.setPayload(payload);
        jws.setKey(jwk1.getKey());
        jws.sign();
        
        
        //JsonWebKey jwk = JsonWebKey.Factory.newJwk(LoadOCSMWKeys.mw_publicKey);
        JsonWebKey jwk = JsonWebKey.Factory.newJwk(LoadIBOCSKeys.ocs_publicKey);
        JsonWebEncryption jwe = new JsonWebEncryption();
        jwe.setAlgorithmHeaderValue(KeyManagementAlgorithmIdentifiers.RSA_OAEP_256);
        jwe.setEncryptionMethodHeaderParameter(ContentEncryptionAlgorithmIdentifiers.AES_256_CBC_HMAC_SHA_512);
        jwe.setKey(jwk.getKey());
        jwe.setPayload(jws.getCompactSerialization());
        
        System.out.println(jwe.getCompactSerialization());
		//return getOCSReqResFormat(jwe.getCompactSerialization());
        return jwe.getCompactSerialization();
	}

	public String processOCSResponse(String payload) throws JoseException {
		JsonWebEncryption jwe = new JsonWebEncryption();
		JsonWebKey jwk = JsonWebKey.Factory.newJwk(LoadIBOCSKeys.ib_privateKey);
		//JsonWebKey jwk = JsonWebKey.Factory.newJwk(LoadOCSMWKeys.ocs_privateKey);
        jwe.setCompactSerialization(payload);
        jwe.setKey(jwk.getKey());
        
        String payloadOut = jwe.getPayload();
		
        JsonWebKey jwk1 = JsonWebKey.Factory.newJwk(LoadIBOCSKeys.ocs_publicKey);
        //JsonWebKey jwk1 = JsonWebKey.Factory.newJwk(LoadOCSMWKeys.mw_publicKey);
        JsonWebSignature jws = new JsonWebSignature();
        jws.setDoKeyValidation(false);
        jws.setCompactSerialization(payloadOut);
        //jws.setKey(LoadOCSMWKeys.mw_publicKey);
        jws.setKey(jwk1.getKey());
        
        
        if(jws.verifySignature()) {
        	return jws.getPayload();
        }
        return null;
	}
	
	public OCSReqResDTO getOCSReqResFormat(String jweStr){
		OCSReqResDTO req_res_dto = new OCSReqResDTO();
		String[] jweArr = CompactSerializer.deserialize(jweStr);
		req_res_dto.setHeaders(jweArr[0]);
		req_res_dto.setEncrypted_key(jweArr[1]);
		req_res_dto.setCiphertext(jweArr[3]);
		req_res_dto.setIv(jweArr[2]);
		req_res_dto.setTag(jweArr[4]);
		return req_res_dto;
		
	}

}