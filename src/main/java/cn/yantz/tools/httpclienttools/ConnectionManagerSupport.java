package cn.yantz.tools.httpclienttools;

import lombok.extern.slf4j.Slf4j;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.pool.PoolStats;

@Slf4j
public class ConnectionManagerSupport {
    private final PoolingHttpClientConnectionManager connectionManager;

    public ConnectionManagerSupport(int maxTotal, int maxPerRoute) {
        this.connectionManager = new PoolingHttpClientConnectionManager();
        this.connectionManager.setMaxTotal(maxTotal);
        this.connectionManager.setDefaultMaxPerRoute(maxPerRoute);
    }

    public PoolingHttpClientConnectionManager getConnectionManager() {
        return this.connectionManager;
    }

    public void closeIdleConnections() {
        PoolStats stats = this.connectionManager.getTotalStats();
        log.info("连接池状态 - 可用: {}, 租用: {}, 等待: {}, 最大: {}",
                stats.getAvailable(), stats.getLeased(), stats.getPending(), stats.getMax());
        this.connectionManager.closeExpiredConnections();
    }
}
