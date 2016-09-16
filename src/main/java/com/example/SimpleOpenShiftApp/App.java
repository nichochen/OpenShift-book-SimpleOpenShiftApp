package com.example.SimpleOpenShiftApp;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;

import org.json.JSONObject;

/**
 * 
 * @author Chen Geng
 *
 */
public class App {

	private final String MASTER_URL = "https://master.example.com:8443";
	private String authKey = "Authorization";
	private String authValue = "Bearer GplQ2zXsM9RH6h3Xtv2njG1mcDV4-kwZkQLm92hhGHk";

	public static void main(String[] args) throws Exception {
		App app = new App();
		app.run();
	}

	public void run() throws Exception {
		
		Client c = Util.getJerseyClient();
		WebTarget target = c.target(MASTER_URL);

		System.out.println("----- 创建项目");
		String projectDef = "{\"kind\":\"ProjectRequest\",\"apiVersion\":\"v1\",\"metadata\":{\"name\":\"my-project\",\"creationTimestamp\":null},\"displayName\":\"My Project\"}";
		Entity<?> proData = Entity.entity(projectDef, MediaType.APPLICATION_JSON);
		target.path("/oapi/v1/projectrequests").request().header(authKey, authValue).post(proData);

		System.out.println("----- 创建Pod `hello-openshift`");
		String podDef = "{\"kind\":\"Pod\",\"apiVersion\":\"v1\",\"metadata\":{\"name\":\"hello-openshift\"},\"spec\":{\"containers\":[{\"name\":\"hello-openshift\",\"image\":\"openshift/hello-openshift:latest\",\"ports\":[{\"containerPort\":8080,\"protocol\":\"TCP\"}]}]}}";
		Entity<?> podData = Entity.entity(podDef, MediaType.APPLICATION_JSON);
		target.path("/api/v1/namespaces/my-project/pods").request().header(authKey, authValue).post(podData);

		System.out.println("----- 获取Pod列表");
		String responseMsg = target.path("/api/v1/namespaces/my-project/pods").request().header(authKey, authValue)
				.get(String.class);
		JSONObject json = new JSONObject(responseMsg);
		System.out.println(json.toString(4)); 

	}

}
