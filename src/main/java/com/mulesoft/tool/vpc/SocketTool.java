package com.mulesoft.tool.vpc;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.Map;

import org.mule.api.MuleEventContext;
import org.mule.api.lifecycle.Callable;
import org.mule.api.transport.PropertyScope;

public class SocketTool implements Callable {

	@Override
	public Object onCall(MuleEventContext eventContext) throws Exception {
		@SuppressWarnings("unchecked")
		Map<String, String> inbound = (Map<String, String>) eventContext.getMessage().getProperty("http.query.params",
				PropertyScope.INBOUND);
		return call(inbound.get("host"), inbound.get("port"));
	}

	public String call(String host, String port) {
		try {
			
			Socket socket = new Socket();
			socket.connect(new InetSocketAddress(host, Integer.parseInt(port)), 10000);
			socket.setSoTimeout(10000);
			if (socket.isConnected()) {
				socket.getInputStream();
			}
			socket.close();
		} catch (Exception e) {
			ByteArrayOutputStream b = new ByteArrayOutputStream();
			e.printStackTrace(new PrintStream(b));
			return b.toString();
		}

		return "Connection successful";
	}
}
