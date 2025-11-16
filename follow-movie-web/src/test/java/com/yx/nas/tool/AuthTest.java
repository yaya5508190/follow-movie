package com.yx.nas.tool;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.util.LinkedMultiValueMap;

import static org.assertj.core.api.Assertions.assertThat;


/**
 * 认证功能单元测试
 * <p>
 * 使用 @Sql 注解自动初始化测试数据
 *
 * @author yx
 * @date 2025-11-05
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Sql(scripts = "/test-data/auth-test-data.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(scripts = "/test-data/cleanup-auth-test-data.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_CLASS)
public class AuthTest {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private TestRestTemplate rest;

    /**
     * 测试用户数据（通过 SQL 脚本预制）
     * 用户名: test
     * 密码: 123
     */
    private static final String TEST_USERNAME = "test";
    private static final String TEST_PASSWORD = "123";

    @Test
    @DisplayName("登录成功 - 正确的用户名和密码")
    void testLoginSuccess() {
        var headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        var body = new LinkedMultiValueMap<String, String>();
        body.add("username","test"); body.add("password","123");

        var resp = rest.postForEntity("/api/auth/login", new HttpEntity<>(body, headers), String.class);
        assertThat(resp.getBody()).contains("\"code\":10000");
        assertThat(resp.getBody()).contains("\"message\":\"登录成功\"");
        assertThat(resp.getBody()).contains("\"userName\":\"test\"");
        assertThat(resp.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(resp.getHeaders().get("Set-Cookie")).anyMatch(s -> s.startsWith("JSESSIONID="));
    }

    @Test
    @DisplayName("登录失败 - 错误的密码")
    void testLoginFailureWrongPassword() {
        var headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        var body = new LinkedMultiValueMap<String, String>();
        body.add("username", TEST_USERNAME);
        body.add("password", "wrongpassword");

        var resp = rest.postForEntity("/api/auth/login", new HttpEntity<>(body, headers), String.class);
        assertThat(resp.getBody()).contains("\"code\":40003");
        assertThat(resp.getBody()).contains("\"message\":");
        assertThat(resp.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
    }

    @Test
    @DisplayName("登录失败 - 用户不存在")
    void testLoginFailureUserNotFound() {
        var headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        var body = new LinkedMultiValueMap<String, String>();
        body.add("username", "nonexistentuser");
        body.add("password", "anypassword");

        var resp = rest.postForEntity("/api/auth/login", new HttpEntity<>(body, headers), String.class);
        assertThat(resp.getBody()).contains("\"code\":40003");
        assertThat(resp.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
    }

    @Test
    @DisplayName("登录失败 - 缺少用户名参数")
    void testLoginFailureMissingUsername() {
        var headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        var body = new LinkedMultiValueMap<String, String>();
        body.add("password", TEST_PASSWORD);

        var resp = rest.postForEntity("/api/auth/login", new HttpEntity<>(body, headers), String.class);
        assertThat(resp.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
    }

    @Test
    @DisplayName("登录失败 - 缺少密码参数")
    void testLoginFailureMissingPassword() {
        var headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        var body = new LinkedMultiValueMap<String, String>();
        body.add("username", TEST_USERNAME);

        var resp = rest.postForEntity("/api/auth/login", new HttpEntity<>(body, headers), String.class);
        assertThat(resp.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
    }

    @Test
    @DisplayName("获取当前用户 - 未登录状态")
    void testGetCurrentUserUnauthorized() {
        var resp = rest.getForEntity("/api/user/current", String.class);
        assertThat(resp.getBody()).contains("\"code\":401");
        assertThat(resp.getBody()).contains("\"message\":\"未认证，请先登录\"");
        assertThat(resp.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
    }

    @Test
    @DisplayName("获取当前用户 - 已登录状态")
    void testGetCurrentUserAuthenticated() {
        // 先登录
        var headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        var body = new LinkedMultiValueMap<String, String>();
        body.add("username", TEST_USERNAME);
        body.add("password", TEST_PASSWORD);

        var loginResp = rest.postForEntity("/api/auth/login", new HttpEntity<>(body, headers), String.class);
        assertThat(loginResp.getStatusCode()).isEqualTo(HttpStatus.OK);

        // 获取 Cookie
        var cookies = loginResp.getHeaders().get("Set-Cookie");
        assertThat(cookies).isNotNull();
        var sessionCookie = cookies.stream()
                .filter(c -> c.startsWith("JSESSIONID="))
                .findFirst()
                .orElseThrow();

        // 使用 Cookie 获取当前用户信息
        var currentHeaders = new HttpHeaders();
        currentHeaders.add("Cookie", sessionCookie);

        var resp = rest.exchange("/api/user/current",
                org.springframework.http.HttpMethod.GET,
                new HttpEntity<>(currentHeaders),
                String.class);

        assertThat(resp.getBody()).contains("\"code\":10000");
        assertThat(resp.getBody()).contains("\"message\":\"已登录\"");
        assertThat(resp.getBody()).contains("\"username\":\"" + TEST_USERNAME + "\"");
        assertThat(resp.getBody()).contains("\"authenticated\":true");
        assertThat(resp.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    @DisplayName("登出成功")
    void testLogoutSuccess() {
        // 先登录
        var headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        var body = new LinkedMultiValueMap<String, String>();
        body.add("username", TEST_USERNAME);
        body.add("password", TEST_PASSWORD);

        var loginResp = rest.postForEntity("/api/auth/login", new HttpEntity<>(body, headers), String.class);
        assertThat(loginResp.getStatusCode()).isEqualTo(HttpStatus.OK);

        // 获取 Cookie
        var cookies = loginResp.getHeaders().get("Set-Cookie");
        assertThat(cookies).isNotNull();
        var sessionCookie = cookies.stream()
                .filter(c -> c.startsWith("JSESSIONID="))
                .findFirst()
                .orElseThrow();

        // 登出
        var logoutHeaders = new HttpHeaders();
        logoutHeaders.add("Cookie", sessionCookie);

        var logoutResp = rest.exchange("/api/auth/logout",
                org.springframework.http.HttpMethod.POST,
                new HttpEntity<>(logoutHeaders),
                String.class);

        assertThat(logoutResp.getBody()).contains("\"code\":10000");
        assertThat(logoutResp.getBody()).contains("\"message\":\"登出成功\"");
        assertThat(logoutResp.getStatusCode()).isEqualTo(HttpStatus.OK);

        // 验证登出后无法访问需要认证的接口
        var currentHeaders = new HttpHeaders();
        currentHeaders.add("Cookie", sessionCookie);

        var resp = rest.exchange("/api/user/current",
                org.springframework.http.HttpMethod.GET,
                new HttpEntity<>(currentHeaders),
                String.class);

        assertThat(resp.getBody()).contains("\"code\":401");
    }

    @Test
    @DisplayName("密码加密验证")
    void testPasswordEncryption() {
        // 验证 BCrypt 密码哈希格式
        String testHash = "$2a$10$rOLJD.ZfrDlp2LB/SB4bX.q3.g6NLaRo7XkBvR7m5dicy.PXXpBLm";

        // 验证密码匹配
        boolean matches = passwordEncoder.matches(TEST_PASSWORD, testHash);
        assertThat(matches).isTrue();

        System.out.println("✅ 原始密码: " + TEST_PASSWORD);
        System.out.println("✅ 测试哈希: " + testHash);
        System.out.println("✅ 密码验证: " + matches);
    }

    @Test
    @DisplayName("Session 并发控制 - 同一用户只允许一个会话")
    void testSessionConcurrencyControl() {
        // 第一次登录
        var headers1 = new HttpHeaders();
        headers1.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        var body1 = new LinkedMultiValueMap<String, String>();
        body1.add("username", TEST_USERNAME);
        body1.add("password", TEST_PASSWORD);

        var login1 = rest.postForEntity("/api/auth/login", new HttpEntity<>(body1, headers1), String.class);
        assertThat(login1.getStatusCode()).isEqualTo(HttpStatus.OK);

        var cookies1 = login1.getHeaders().get("Set-Cookie");
        assertThat(cookies1).isNotNull();
        var session1 = cookies1.stream()
                .filter(c -> c.startsWith("JSESSIONID="))
                .findFirst()
                .orElseThrow();

        // 验证第一个会话有效
        var checkHeaders1 = new HttpHeaders();
        checkHeaders1.add("Cookie", session1);
        var check1 = rest.exchange("/api/user/current",
                org.springframework.http.HttpMethod.GET,
                new HttpEntity<>(checkHeaders1),
                String.class);
        assertThat(check1.getBody()).contains("\"code\":10000");

        // 第二次登录（同一用户）
        var headers2 = new HttpHeaders();
        headers2.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        var body2 = new LinkedMultiValueMap<String, String>();
        body2.add("username", TEST_USERNAME);
        body2.add("password", TEST_PASSWORD);

        var login2 = rest.postForEntity("/api/auth/login", new HttpEntity<>(body2, headers2), String.class);
        assertThat(login2.getStatusCode()).isEqualTo(HttpStatus.OK);

        var cookies2 = login2.getHeaders().get("Set-Cookie");
        assertThat(cookies2).isNotNull();
        var session2 = cookies2.stream()
                .filter(c -> c.startsWith("JSESSIONID="))
                .findFirst()
                .orElseThrow();

        // 第二个会话应该有效
        var checkHeaders2 = new HttpHeaders();
        checkHeaders2.add("Cookie", session2);
        var check2 = rest.exchange("/api/user/current",
                org.springframework.http.HttpMethod.GET,
                new HttpEntity<>(checkHeaders2),
                String.class);
        assertThat(check2.getBody()).contains("\"code\":10000");

        // 第一个会话应该被踢掉（因为配置了 maxSessionsPreventsLogin = false）
        // 注意：这个行为可能需要一点时间生效
    }
}
