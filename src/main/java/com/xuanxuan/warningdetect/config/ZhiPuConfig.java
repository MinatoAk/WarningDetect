package com.xuanxuan.warningdetect.config;

import com.zhipu.oapi.ClientV4;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "zhipu")
@Data
@Slf4j
public class ZhiPuConfig {
    /**
     * apiKey 需要从智谱开放平台获取
     * https://open.bigmodel.cn/usercenter/proj-mgmt/apikeys
     */
    private String apiKey;

    @Bean
    public ClientV4 getClientV4() {
        ClientV4 clientV4 = new ClientV4.Builder(apiKey).build();
        log.info("[x] 智谱大模型初始化成功");
        return clientV4;
    }
}
