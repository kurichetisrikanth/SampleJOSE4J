package com.sree.jose4j.controller;

import org.jose4j.lang.JoseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.sree.jose4j.model.OCSReqResDTO;
import com.sree.jose4j.serviceImpl.EncDecService;


@RestController
public class EncDecController {
	
	@Autowired
	private EncDecService service;
	
	//OCS PROCESS RECIEVED REQUEST FROM IB
	@PostMapping(value="/ocs-process-req")
	//public String processIBRequest(@RequestBody OCSReqResDTO req) throws JoseException {
	public String processIBRequest(@RequestBody String req) throws JoseException {	
		//return service.processIBRequest(req.toString());
		return service.processIBRequest(req);
	}
	
	//SENDING OCS RESPONSE TO IB
	@PostMapping("/ocs-send-res")
	public String sendOCSResponse(@RequestBody String res) throws JoseException {
		return service.sendOCSResponse(res);
	}
	
	//SENDING IB REQUEST TO OCS
	@PostMapping(value="/ib-send-req")
	//public OCSReqResDTO sendIBRequest(@RequestBody String req) throws JoseException {
	public String sendIBRequest(@RequestBody String req) throws JoseException {
		return service.sendIBRequest(req);
	}
	
	//IB PROCESS RECIEVED RESPONSE FROM OCS
	@PostMapping(value="/ib-process-res")
	public String processOCSResponse(@RequestBody String res) throws JoseException {
		return service.processOCSResponse(res);
	}
	
}
