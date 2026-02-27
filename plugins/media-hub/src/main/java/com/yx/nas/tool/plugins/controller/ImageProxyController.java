package com.yx.nas.tool.plugins.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;

@RestController
@RequestMapping("/api/proxy")
public class ImageProxyController {

    private final WebClient webClient;

    @Autowired
    public ImageProxyController(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.build();
    }

    @GetMapping("/image")
    public Mono<ResponseEntity<Resource>> proxyImage(@RequestParam("url") String imageUrl) {
        if (!StringUtils.hasText(imageUrl)) {
            return Mono.just(ResponseEntity.badRequest().build());
        }

        // URL 解码
        String decodedUrl = URLDecoder.decode(imageUrl, StandardCharsets.UTF_8);

        return webClient.get()
                .uri(decodedUrl)
                .header("Referer", "https://movie.douban.com/")
                .retrieve()
                .bodyToMono(byte[].class)
                .map(bytes -> {
                    ByteArrayResource resource = new ByteArrayResource(bytes) {
                        @Override
                        public String getFilename() {
                            return "image";
                        }
                    };

                    // 根据 URL 猜测图片类型
                    String contentType = guessContentType(decodedUrl);

                    HttpHeaders headers = new HttpHeaders();
                    headers.setContentType(MediaType.parseMediaType(contentType));
                    headers.setCacheControl("public, max-age=3600");

                    return ResponseEntity.ok()
                            .headers(headers)
                            .body((Resource) resource);
                })
                .onErrorResume(e -> Mono.just(ResponseEntity.status(HttpStatus.BAD_GATEWAY).build()));
    }

    private String guessContentType(String url) {
        String lowerUrl = url.toLowerCase();
        if (lowerUrl.endsWith(".png")) {
            return "image/png";
        } else if (lowerUrl.endsWith(".gif")) {
            return "image/gif";
        } else if (lowerUrl.endsWith(".webp")) {
            return "image/webp";
        }
        // 默认返回 jpeg
        return "image/jpeg";
    }
}
