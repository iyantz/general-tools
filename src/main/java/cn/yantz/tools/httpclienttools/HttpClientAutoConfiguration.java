package cn.yantz.tools.httpclienttools;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(HttpConfig.class)
public class HttpClientAutoConfiguration {

    @Bean
    public ConnectionManagerSupport connectionManagerSupport(HttpConfig httpConfig) {
        return new ConnectionManagerSupport(httpConfig.getMaxTotal(), httpConfig.getMaxPerRoute());
    }

    @Bean
    public HttpClientFactory httpClientFactory(ConnectionManagerSupport connectionManagerSupport, HttpConfig httpConfig) {
        return new HttpClientFactory(connectionManagerSupport, httpConfig);
    }

    @Bean
    public HttpClientTemplate httpClientTemplate(HttpClientFactory httpClientFactory) {
        return new HttpClientTemplate(httpClientFactory);
    }
}
