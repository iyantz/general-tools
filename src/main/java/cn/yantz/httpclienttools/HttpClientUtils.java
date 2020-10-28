package cn.yantz.httpclienttools;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.StatusLine;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLContextBuilder;
import org.apache.http.conn.ssl.TrustStrategy;
import org.apache.http.conn.ssl.X509HostnameVerifier;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;

import javax.net.ssl.*;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Base64;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

/**
 * http 方式调用接口
 * Created by zuokp on 2017/9/16.
 */
public class HttpClientUtils {

    public static final String dataTypeJson = "application/json";
    public static final String dataTypeXml = "application/xml";
    private final static String CONTENT_TYPE_TEXT_JSON = "text/json";

    private static final String SYMBOL_QUESTION_MARK = "?";
    private static final String SYMBOL_AND_MARK = "&";
    private static final String SYMBOL_EQUAL_MARK = "=";


    /**
     * HTTP POST请求
     *
     * @param url         请求地址 String
     * @param data        请求Body数据   Map<String, Object>
     * @param params      请求URL参数  Map<String, Object>
     * @param headerMap   请求头 Map<String,String>
     * @param withEncrypt 请求报文是否Base64加密，true：加密；false，不加密
     * @return 返回结果 String, 正常返回响应报文，异常返回状态码
     * @throws Exception HTTPClient异常，加密转换异常
     */
    public static String httpPost(String url, Map<String, Object> data, Map<String, Object> params, Map<String, String> headerMap, Boolean withEncrypt) throws Exception {
        url = setUrlParams(url, params);
        // create HttpPost
        HttpPost httpPost = new HttpPost(url);
        // set header
        setHttpHeader(httpPost, headerMap);
        // set body
        setHttpBody(httpPost, data, withEncrypt);
        // create HttpClient
        CloseableHttpClient httpClient = HttpClientBuilder.create().build();
        return httpPostExecute(httpPost, httpClient);
    }

    /**
     * HTTPS POST请求
     *
     * @param url         请求地址
     * @param data        请求Body数据 Map集合
     * @param params      请求URL参数数据 Map集合
     * @param headerMap   请求Header数据 Map集合
     * @param withEncrypt 请求报文是否Base64加密，true:加密；false：不加密
     * @return String 结果
     * @throws Exception 异常
     */
    public static String doHttpsPost(String url, Map<String, Object> data, Map<String, Object> params, Map<String, String> headerMap, Boolean withEncrypt)
            throws Exception {
        url = setUrlParams(url, params);
        // create HttpPost
        HttpPost httpPost = new HttpPost(url);
        // set header
        setHttpHeader(httpPost, headerMap);
        // set body
        setHttpBody(httpPost, data, withEncrypt);
        // create SSL connect
        SSLContext sslContext = getSSLContext();
        X509HostnameVerifier hostnameVerifier = createHostnameVerifier();
        SSLConnectionSocketFactory socketFactory = new SSLConnectionSocketFactory(sslContext, hostnameVerifier);
        HttpClientBuilder httpClientBuilder = HttpClientBuilder.create();
        httpClientBuilder.setSSLSocketFactory(socketFactory);
        CloseableHttpClient httpClient = httpClientBuilder.build();
        return httpPostExecute(httpPost, httpClient);
    }

    /**
     * 执行HTTP POST请求
     *
     * @param httpPost   HttpPost
     * @param httpClient CloseableHttpClient
     * @return String
     * @throws IOException 异常
     */
    private static String httpPostExecute(HttpPost httpPost, CloseableHttpClient httpClient) throws IOException {
        CloseableHttpResponse execute = httpClient.execute(httpPost);
        StatusLine statusLine = execute.getStatusLine();
        HttpEntity resultEntity = execute.getEntity();
        if (Objects.isNull(resultEntity)) {
            throw new RuntimeException("httpPostExecute resultEntity is null");
        }
        String resultString = EntityUtils.toString(resultEntity);
        if (StringUtils.isEmpty(resultString)) {
            throw new RuntimeException("httpPostExecute" + statusLine);
        }
        httpClient.close();
        return resultString;
    }

    /**
     * 设置URL参数
     *
     * @param url    请求URL
     * @param params 请求参数
     * @return String URL
     */
    private static String setUrlParams(String url, Map<String, Object> params) {
        // set params
        if (Objects.nonNull(params) && !params.isEmpty()) {
            String jsonStringParams = JSON.toJSONString(params);
            StringBuilder paramBuilder = new StringBuilder(SYMBOL_QUESTION_MARK);
            Set<Map.Entry<String, Object>> entries = params.entrySet();
            for (Map.Entry<String, Object> param : entries) {
                paramBuilder.append(param.getKey()).append(SYMBOL_EQUAL_MARK).append(param.getValue()).append(SYMBOL_AND_MARK);
            }
            String urlPa = paramBuilder.toString();
            if (urlPa.endsWith(SYMBOL_AND_MARK)) {
                urlPa = urlPa.substring(0, urlPa.length() - 1);
            }
            url = url + urlPa;
        }
        return url;
    }

    /**
     * 设置HttpHeader
     *
     * @param httpPost  HttpPost
     * @param headerMap header数据
     */
    private static void setHttpHeader(HttpPost httpPost, Map<String, String> headerMap) {
        Set<Map.Entry<String, String>> entries = headerMap.entrySet();
        entries.forEach(stringStringEntry -> {
            httpPost.addHeader(stringStringEntry.getKey(), stringStringEntry.getValue());
        });
    }

    /**
     * 设置body数据
     *
     * @param httpPost    HttpPost
     * @param data        body数据
     * @param withEncrypt 是否base64加密
     */
    private static void setHttpBody(HttpPost httpPost, Map<String, Object> data, Boolean withEncrypt) {
        String jsonStringDate = JSON.toJSONString(data, SerializerFeature.WriteMapNullValue, SerializerFeature.WriteNullStringAsEmpty);
        if (withEncrypt) {
            jsonStringDate = Base64.getEncoder().encodeToString(jsonStringDate.getBytes(StandardCharsets.UTF_8));
        }
        httpPost.setEntity(new StringEntity(jsonStringDate, StandardCharsets.UTF_8));
    }

    /**
     * 创建HttpClient信任管理器
     *
     * @return X509TrustManager实例
     */
    private static X509TrustManager createX509TrustManager() {
        //创建TrustManager
        X509TrustManager xtm = new X509TrustManager() {
            @Override
            public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
            }

            @Override
            public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
            }

            @Override
            public X509Certificate[] getAcceptedIssuers() {
                return null;
            }
        };
        return xtm;
    }

    /**
     * 服务器主机校验
     * HOST验证
     *
     * @return HostnameVerifier
     */
    private static X509HostnameVerifier createHostnameVerifier() {
        return new X509HostnameVerifier() {
            @Override
            public void verify(String host, SSLSocket ssl) throws IOException {

            }

            @Override
            public void verify(String host, X509Certificate cert) throws SSLException {

            }

            @Override
            public void verify(String host, String[] cns, String[] subjectAlts) throws SSLException {

            }

            @Override
            public boolean verify(String s, SSLSession sslSession) {
                return true;
            }
        };
    }

    /**
     * 获取SSLContext
     *
     * @return SSLContext
     * @throws KeyStoreException
     * @throws NoSuchAlgorithmException
     * @throws KeyManagementException
     */
    private static SSLContext getSSLContext() throws KeyStoreException, NoSuchAlgorithmException, KeyManagementException {
        //TLS1.0与SSL3.0基本上没有太大的差别，可粗略理解为TLS是SSL的继承者，但它们使用的是相同的SSLContext
        SSLContext ctx = new SSLContextBuilder().loadTrustMaterial(null, new TrustStrategy() {
            //信任所有
            @Override
            public boolean isTrusted(X509Certificate[] chain, String authType) throws CertificateException {
                return true;
            }

        }).build();
        return ctx;
    }


}
