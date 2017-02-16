package com.cgi.poc.dw.api.service;

import javax.ws.rs.client.Client;

import com.cgi.poc.dw.dao.FireEventDAO;

public interface APIServiceFactory {
	
	APICallerService create(Client client, String eventUrl, FireEventDAO fireEventDAO);
}
