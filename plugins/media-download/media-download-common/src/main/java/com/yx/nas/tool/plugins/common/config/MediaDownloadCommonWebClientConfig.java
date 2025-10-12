package com.yx.nas.tool.plugins.common.config;

import io.netty.channel.ChannelOption;
import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.handler.timeout.WriteTimeoutHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

@Configuration
public class MediaDownloadCommonWebClientConfig {

    @Bean
    public WebClient webClientForTorrentDownload() {
        // 配置 HttpClient 以自动跟随重定向，并设置全面的超时时间为2分钟
        HttpClient httpClient = HttpClient.create()
                .followRedirect(true)
                // 响应超时：等待完整响应的最大时间
                .responseTimeout(Duration.ofMinutes(2))
                // 连接超时：建立TCP连接的最大时间
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 120000)
                // SO_TIMEOUT：socket读取超时
                .option(ChannelOption.SO_TIMEOUT, 120000)
                // 配置读写超时处理器
                .doOnConnected(conn -> conn
                        // 读超时：从连接读取数据的最大空闲时间
                        .addHandlerLast(new ReadTimeoutHandler(120, TimeUnit.SECONDS))
                        // 写超时：向连接写入数据的最大空闲时间
                        .addHandlerLast(new WriteTimeoutHandler(120, TimeUnit.SECONDS))
                );

        // 配置缓冲区大小限制为 16MB（可根据实际种子文件大小调整）
        ExchangeStrategies strategies = ExchangeStrategies.builder()
                .codecs(configurer -> configurer
                        .defaultCodecs()
                        .maxInMemorySize(16 * 1024 * 1024)) // 16MB
                .build();

        WebClient webClient = WebClient.builder()
                .clientConnector(new ReactorClientHttpConnector(httpClient))
                .exchangeStrategies(strategies)
                .build();

        return webClient;
    }
}
