package cn.itcast.test;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.junit.Test;

public class HttpClientTest {

	@Test
	public void demo() throws Exception {

		HttpClient httpClient = HttpClients.createDefault();
		HttpPost httpPost = new HttpPost("http://api.map.baidu.com/geodata/v3/geotable/create");

		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();

		nameValuePairs.add(new BasicNameValuePair("name", "mytable1"));
		nameValuePairs.add(new BasicNameValuePair("geotype", "1"));
		nameValuePairs.add(new BasicNameValuePair("is_published", "1"));
		nameValuePairs.add(new BasicNameValuePair("ak", "F4GVEAdFGKOCcWS9MmoTqOW2GvGidWA6"));

		httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
		HttpResponse httpResponse = httpClient.execute(httpPost);
		HttpEntity httpEntity = httpResponse.getEntity();
		System.out.println(EntityUtils.toString(httpEntity));
	}

}
