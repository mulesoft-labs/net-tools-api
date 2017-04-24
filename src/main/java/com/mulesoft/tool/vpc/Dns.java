package com.mulesoft.tool.vpc;

import java.net.InetAddress;
import java.util.Map;

import org.mule.api.MuleEventContext;
import org.mule.api.lifecycle.Callable;
import org.mule.api.transport.PropertyScope;

public class Dns implements Callable {
	@Override
	public Object onCall(MuleEventContext eventContext) throws Exception {
		@SuppressWarnings("unchecked")
		Map<String, String> inbound = (Map<String, String>) eventContext.getMessage().getProperty("http.query.params",
				PropertyScope.INBOUND);
		return call(inbound.get("host"));
	}

	public String call(String host) throws Exception {
		java.net.InetAddress address = InetAddress.getByName(host); 
		return address.getHostAddress();
	}

}