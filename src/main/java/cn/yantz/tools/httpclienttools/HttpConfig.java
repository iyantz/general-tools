package cn.yantz.tools.httpclienttools;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "http.client")
public class HttpConfig {
    /**
     * 连接池最大连接数
     */
    private int maxTotal = 200;

    /**
     * 每个路由的最大连接数
     */
    private int maxPerRoute = 20;

    /**
     * 连接超时时间(毫秒)
     */
    private int connectTimeout = 5000;

    /**
     * 读取超时时间(毫秒)
     */
    private int socketTimeout = 10000;

    /**
     * 连接请求超时时间(毫秒)
     */
    private int connectionRequestTimeout = 3000;

    /**
     * 空闲连接存活时间(毫秒)
     */
    private int idleTimeout = 60000;

    /**
     * 保活策略开关
     */
    private boolean keepAlive = true;
}
