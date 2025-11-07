package com.yx.nas.tool.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.yx.nas.enums.ResponseCodeEmun;
import com.yx.nas.model.common.CommonResult;
import com.yx.nas.tool.service.CustomUserDetailsService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;

import java.util.HashMap;
import java.util.Map;

/**
 * Spring Security配置
 *
 * @author yx
 * @date 2025-11-05
 */
@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final ObjectMapper objectMapper;
    private final CustomUserDetailsService userDetailsService;

    /**
     * 密码加密器
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * 安全过滤器链配置
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // 指定使用自定义的 UserDetailsService
                .userDetailsService(userDetailsService)
                // 配置授权规则
                .authorizeHttpRequests(authorize -> authorize
                        // 允许登录接口匿名访问
                        .requestMatchers("/api/auth/login", "/api/auth/logout").permitAll()
                        // 允许静态资源访问
                        .requestMatchers("/static/**", "/public/**").permitAll()
                        // 其他所有请求都需要认证
                        .anyRequest().authenticated()
                )
                // 配置表单登录
                .formLogin(form -> form
                        .loginProcessingUrl("/api/auth/login")
                        .successHandler(authenticationSuccessHandler())
                        .failureHandler(authenticationFailureHandler())
                        .permitAll()
                )
                // 配置登出
                .logout(logout -> logout
                        .logoutUrl("/api/auth/logout")
                        .logoutSuccessHandler(logoutSuccessHandler())
                        .invalidateHttpSession(true)
                        .deleteCookies("JSESSIONID")
                        .permitAll()
                )
                // 配置Session管理
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
                        .maximumSessions(1) // 同一用户最多允许1个会话
                        .maxSessionsPreventsLogin(false) // 新会话踢掉旧会话
                )
                // 配置异常处理 - 返回JSON而不是重定向
                .exceptionHandling(exception -> exception
                        .authenticationEntryPoint((request, response, authException) -> {
                            response.setStatus(401);
                            response.setContentType("application/json;charset=UTF-8");
                            CommonResult<Void> result = CommonResult.failure(ResponseCodeEmun.UNAUTHORIZED);
                            response.getWriter().write(objectMapper.writeValueAsString(result));
                        })
                        .accessDeniedHandler((request, response, accessDeniedException) -> {
                            response.setStatus(403);
                            response.setContentType("application/json;charset=UTF-8");
                            CommonResult<Void> result = CommonResult.failure(ResponseCodeEmun.FORBIDDEN);
                            response.getWriter().write(objectMapper.writeValueAsString(result));
                        })
                )
                // CSRF保护 - 对于API可以考虑禁用，或者使用token
                .csrf(AbstractHttpConfigurer::disable);

        return http.build();
    }

    /**
     * 认证成功处理器
     */
    @Bean
    public AuthenticationSuccessHandler authenticationSuccessHandler() {
        return (request, response, authentication) -> {
            response.setContentType("application/json;charset=UTF-8");
            Map<String, String> data = new HashMap<>();
            data.put("userName", authentication.getName());
            CommonResult<Map<String, String>> result = CommonResult.success(data, "登录成功");
            response.getWriter().write(objectMapper.writeValueAsString(result));
        };
    }

    /**
     * 认证失败处理器
     */
    @Bean
    public AuthenticationFailureHandler authenticationFailureHandler() {
        return (request, response, exception) -> {
            response.setStatus(401);
            response.setContentType("application/json;charset=UTF-8");
            CommonResult<Void> result = CommonResult.failure(ResponseCodeEmun.LOGIN_FAILED, "登录失败: " + exception.getMessage());
            response.getWriter().write(objectMapper.writeValueAsString(result));
        };
    }

    /**
     * 登出成功处理器
     */
    @Bean
    public LogoutSuccessHandler logoutSuccessHandler() {
        return (request, response, authentication) -> {
            response.setContentType("application/json;charset=UTF-8");
            CommonResult<Void> result = CommonResult.successWithMessage("登出成功");
            response.getWriter().write(objectMapper.writeValueAsString(result));
        };
    }
}

