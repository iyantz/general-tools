package cn.yantz.tools.httpclienttools;

import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

@Slf4j
public class HttpClientTemplate {

    private final CloseableHttpClient httpClient;

    public HttpClientTemplate(HttpClientFactory factory) {
        this.httpClient = factory.createHttpClient();
    }

    public String doGet(String url) throws IOException {
        HttpGet httpGet = new HttpGet(url);
        return executeRequest(httpGet);
    }

    public String doPost(String url, String body) throws IOException {
        HttpPost httpPost = new HttpPost(url);
        StringEntity entity = new StringEntity(body, StandardCharsets.UTF_8);
        httpPost.setEntity(entity);
        return executeRequest(httpPost);
    }

    private String executeRequest(HttpRequestBase request) throws IOException {
        try (CloseableHttpResponse response = httpClient.execute(request)) {
            HttpEntity entity = response.getEntity();
            return entity != null ? EntityUtils.toString(entity) : null;
        }
    }
}
