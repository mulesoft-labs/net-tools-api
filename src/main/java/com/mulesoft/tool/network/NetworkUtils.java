package com.mulesoft.tool.network;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.io.SequenceInputStream;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.UnknownHostException;

public class NetworkUtils {

	public static String resolveIPs(String host) throws UnknownHostException {
		InetAddress[] addresses = InetAddress.getAllByName(host);
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < addresses.length; i++) {
			if (i != 0) {
				sb.append(", ");
			}
			sb.append(addresses[i].getHostAddress());
		}
		return sb.toString();
	}

	public static String ping(String host) throws Exception {
		return execute(new ProcessBuilder("ping", "-c", "4", host));
	}

	public static String curl(String host, String port, String path) throws IOException {
		String url = "";
		url = url.concat(host.concat(":").concat(port).concat("/").concat(path));
		//-i include protocol headers
		//-L follow redirects
		//-k insecure
		//-E cert status
		return execute(new ProcessBuilder("curl","-k", "-E", "-i","-L", url));
	}

	public static String testConnect(String host, String port) {
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

	public static String traceRoute(String host) throws Exception {
		return execute(new ProcessBuilder("traceroute", "-w", "3", "-q", "1", "-m", "18", "-n", host));
	}

	private static String execute(ProcessBuilder pb) throws IOException {
		Process p = pb.start();
		SequenceInputStream sis = new SequenceInputStream(p.getInputStream(), p.getErrorStream());
		java.util.Scanner s = new java.util.Scanner(sis).useDelimiter("\\A");
		return s.hasNext() ? s.next() : "";
	}
}
