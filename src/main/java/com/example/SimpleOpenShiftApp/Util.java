package com.example.SimpleOpenShiftApp;

import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import javax.ws.rs.client.Client;

import org.glassfish.jersey.client.ClientConfig;
import org.glassfish.jersey.client.JerseyClientBuilder;
import org.glassfish.jersey.filter.LoggingFilter;

@SuppressWarnings("deprecation")
public class Util {
	//创建不检查SSL证书的客户端
		public static Client getJerseyClient() throws Exception {
			TrustManager[] trustAllCerts = new TrustManager[] { new X509TrustManager() {

				public java.security.cert.X509Certificate[] getAcceptedIssuers() {
					return new X509Certificate[0];
				}

				public void checkServerTrusted(java.security.cert.X509Certificate[] arg0, String arg1)
						throws CertificateException {

				}

				public void checkClientTrusted(java.security.cert.X509Certificate[] arg0, String arg1)
						throws CertificateException {

				}
			} };

			HostnameVerifier hv = new HostnameVerifier() {
				public boolean verify(String hostname, SSLSession session) {
					return true;
				}
			};

			SSLContext sc = SSLContext.getInstance("SSL");
			sc.init(null, trustAllCerts, new SecureRandom());

			ClientConfig c = new ClientConfig();
			//c.register(LoggingFilter.class);

			Client client = new JerseyClientBuilder().sslContext(sc).withConfig(c).hostnameVerifier(hv).build();
			return client;
		}
}
