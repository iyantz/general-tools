package cn.yantz.tools.httpclienttools;

import org.apache.http.config.SocketConfig;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;

public class HttpClientFactory {

    private final ConnectionManagerSupport connectionManagerSupport;
    private final HttpConfig httpConfig;

    public HttpClientFactory(ConnectionManagerSupport connectionManagerSupport, HttpConfig httpConfig) {
        this.connectionManagerSupport = connectionManagerSupport;
        this.httpConfig = httpConfig;
    }

    public CloseableHttpClient createHttpClient() {
        PoolingHttpClientConnectionManager connectionManager = connectionManagerSupport.getConnectionManager();

        // 配置Socket参数
        SocketConfig socketConfig = SocketConfig.custom()
                .setSoTimeout(httpConfig.getSocketTimeout())
                .build();
        connectionManager.setDefaultSocketConfig(socketConfig);

        // 构建HttpClient
        return HttpClients.custom()
                .setConnectionManager(connectionManager)
                .setConnectionManagerShared(true)
                .setKeepAliveStrategy((response, context) ->
                        httpConfig.isKeepAlive() ? httpConfig.getIdleTimeout() : 0)
                .setDefaultRequestConfig(org.apache.http.client.config.RequestConfig.custom()
                        .setConnectTimeout(httpConfig.getConnectTimeout())
                        .setSocketTimeout(httpConfig.getSocketTimeout())
                        .setConnectionRequestTimeout(httpConfig.getConnectionRequestTimeout())
                        .build())
                .build();
    }
}
