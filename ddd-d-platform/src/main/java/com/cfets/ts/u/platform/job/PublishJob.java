package com.cfets.ts.u.platform.job;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import org.apache.commons.io.IOUtils;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;

import com.cfets.cwap.s.stp.SimpleMessage;
import com.cfets.ts.u.platform.bean.ServiceTS;
import com.google.gson.Gson;
import com.google.gson.internal.LinkedTreeMap;

public abstract class PublishJob {
	public static DateFormat dayFormat = new SimpleDateFormat("yyyy-MM-dd");
	public static DateFormat dateFormat = new SimpleDateFormat(
			"yyyy-MM-dd HH:mm:ss");
	public abstract void dayOfDelay();

	public boolean getServiceStatus() {

		String url = "http://200.31.156.30:8034/ts/monitor/rest/ts-s-monitor/status?env=MON";
		HttpGet request = new HttpGet(url);//
		RequestConfig config = RequestConfig.custom().setConnectTimeout(3000)
				.setSocketTimeout(3000).build();
		request.setConfig(config);
		// 获取当前客户端对象
		CloseableHttpClient httpClient = HttpClientBuilder.create().build();
		// 通过请求对象获取响应对象
		try {
			CloseableHttpResponse response = httpClient.execute(request);
			// System.out.println(EntityUtils.toString(response.getEntity(),"utf-8"));
			String json = EntityUtils.toString(response.getEntity(), "utf-8");
			SimpleMessage sim = SimpleMessage.blank();
			sim = new Gson().fromJson(json, SimpleMessage.class);
			ArrayList str = (ArrayList) sim.getData().get("services");
			for (Object tt : str) {
				ServiceTS service = new ServiceTS();
				LinkedTreeMap map = new LinkedTreeMap();
				map = (LinkedTreeMap) tt;
				System.out.println(map);
				service.setName(String.valueOf(map.get("name")));
				service.setContext(String.valueOf(map.get("context")));
				service.setIp(String.valueOf(map.get("ip")));
				service.setPort(String.valueOf(map.get("port")));
				service.setStatus(String.valueOf(map.get("status")));
				System.out.println(service);
				if ("FAILED".equals(service.getStatus())
						&& (!"fcs".equals(service.getName()) && !"mc"
								.equals(service.getName()))) {
					return false;
				}
			}

			IOUtils.closeQuietly(response);
		} catch (Exception e) {
			System.out.println(e);
			return false;
		} finally {
			IOUtils.closeQuietly(httpClient);
		}
		return true;

	}
}
