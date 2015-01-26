package com.mulesoft.tool.vpc;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.SequenceInputStream;
import java.util.Map;

import org.apache.commons.io.IOUtils;
import org.apache.commons.net.telnet.TelnetClient;
import org.mule.api.MuleEventContext;
import org.mule.api.lifecycle.Callable;
import org.mule.api.transport.PropertyScope;

public class Telnet implements Callable {

	@Override
	public Object onCall(MuleEventContext eventContext) throws Exception {
		Map<String, String> inbound = (Map<String, String>) eventContext.getMessage().getProperty("http.query.params",
				PropertyScope.INBOUND);
		return call(inbound.get("host"), inbound.get("port"));
	}

	public InputStream call(String host, String port) throws Exception {

		TelnetClient telnet = new TelnetClient();
		telnet.connect(host, Integer.parseInt(port.trim()));
		byte[] out = new byte[10000];
		InputStream outS = telnet.getInputStream();
		IOUtils.write("GET /\n".getBytes(), telnet.getOutputStream());
		int length = readInputStreamWithTimeout(outS, out, 5000);
		telnet.disconnect();
		return new ByteArrayInputStream(out, 0, length);
	}

	public InputStream call2(String host, String port) throws Exception {
		ProcessBuilder pb = new ProcessBuilder("telnet", host, port);
		Process p = pb.start();
		SequenceInputStream s = new SequenceInputStream(p.getInputStream(), p.getErrorStream());
		byte[] out = new byte[10000];
		int length = readInputStreamWithTimeout(s, out, 5000);
		p.destroy();
		return new ByteArrayInputStream(out, 0, length);
	}

	public static int readInputStreamWithTimeout(InputStream is, byte[] b, int timeoutMillis) throws Exception {
		int bufferOffset = 0;
		long maxTimeMillis = System.currentTimeMillis() + timeoutMillis;
		while (System.currentTimeMillis() < maxTimeMillis && bufferOffset < b.length) {
			int readLength = java.lang.Math.min(is.available(), b.length - bufferOffset);
			// can alternatively use bufferedReader, guarded by isReady():
			int readResult = is.read(b, bufferOffset, readLength);
			if (readResult == -1)
				break;
			bufferOffset += readResult;
		}
		return bufferOffset;
	}

}
