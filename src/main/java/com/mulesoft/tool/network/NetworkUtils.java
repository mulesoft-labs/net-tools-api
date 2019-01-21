package com.mulesoft.tool.network;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.io.SequenceInputStream;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.UnknownHostException;

import javax.net.ssl.*;
import java.security.cert.*;

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

	public static String curl(String url) throws IOException {
		//-i include protocol headers
		//-L follow redirects
		//-k insecure
		//-E cert status
		return execute(new ProcessBuilder("curl","-k", "-E", "-i","-L", url));
	}

	public static String testConnect(String host, String port) {
		long startTime = System.nanoTime();
		long totalTime = System.nanoTime();
		String result = "";
		for (int x = 1; x <= 3; x++) {
			try {
				Socket socket = new Socket();
				startTime = System.nanoTime();
				socket.connect(new InetSocketAddress(host, Integer.parseInt(port)), 10000);
				socket.setSoTimeout(10000);
				if (socket.isConnected()) {
					totalTime = System.nanoTime() - startTime;
					socket.getInputStream();
				}
				socket.close();
			} 
			catch (java.net.UnknownHostException e) {
				return "Could not resolve host " + host;
			}
			catch (java.net.SocketTimeoutException e) {
				return "Timeout while trying to connect to " + host;
			}
			catch (java.lang.IllegalArgumentException e) {
				return e.getMessage();
			}
			catch (Exception e) {
				ByteArrayOutputStream b = new ByteArrayOutputStream();
				e.printStackTrace(new PrintStream(b));
				return b.toString();
			}
			result = result + "Probe " + x + ": Connection successful, RTT=" + Long.toString(totalTime/1000000) + "ms\n";
		}
		return result + "socket test completed";
	}

	public static String traceRoute(String host) throws Exception {
		return execute(new ProcessBuilder("traceroute", "-w", "3", "-q", "1", "-m", "18", "-n", host));
	}

	public static String ssltest(String host, String port) throws Exception {
/* 		Got stuck trying to use OpenSSL to fetch the certs. The first way it works, 
		but the connection is kept opened and except to send to data to the remote server; 
		so I tried to add "| echo" to emulate a return keypress, but I'm not able to run openssl that way.
		Leavig it all here in case someone can fix it.

		return execute(new ProcessBuilder("openssl", "s_client", "-showcerts", "-servername", host, "-connect", host+":"+port, "| echo"));
		return execute(new ProcessBuilder("/bin/sh", "openssl s_client -showcerts -servername "+host+" -connect "+host+":"+port+ " | echo")); */

		Certificate[] certs;
		String result;
		SSLSession mySSLSession;

		try {
			SSLSocketFactory factory =
			(SSLSocketFactory)SSLSocketFactory.getDefault();
		
			SSLSocket socket =
				(SSLSocket)factory.createSocket(host, Integer.parseInt(port));
			socket.startHandshake();
			certs = socket.getSession().getPeerCertificates();
			mySSLSession = socket.getSession();
			socket.close();
		} catch (Exception e) {
			return e.getMessage();
		}

		result = "SSL connection results\n";
		result = result + "======================\n";

		result = result + "Negotiated protocol: " + mySSLSession.getProtocol() +"\n";
		result = result + "Negotiated cipher suite: " + mySSLSession.getCipherSuite() + "\n\n";
		
		result = result + "Certificates retrieved: " + certs.length + "\n";
		for (Certificate cert : certs) {
			result = result + "\nCertificate\n===========\n";
			result = result + cert.toString();
			result = result + "";
            if(cert instanceof X509Certificate) {
                try {
                    ( (X509Certificate) cert).checkValidity();
                    result = result + "\nCertificate is active for current date\n\n";
                } catch(CertificateExpiredException cee) {
                    result = result + "\nCertificate is expired\n\n";
                }
            }
        }

		return "SSL connection successful!\n\n" + result;
	}

	private static String execute(ProcessBuilder pb) throws IOException {
		Process p = pb.start();
		SequenceInputStream sis = new SequenceInputStream(p.getInputStream(), p.getErrorStream());
		java.util.Scanner s = new java.util.Scanner(sis).useDelimiter("\\A");
		return s.hasNext() ? s.next() : "";
	}
}