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
import org.springframework.test.context.jdbc.Sql;
import org.springframework.util.LinkedMultiValueMap;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Sql(scripts = "/test-data/auth-test-data.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_CLASS)
@Sql(scripts = "/test-data/cleanup-auth-test-data.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_CLASS)
public class UserTest {

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
    @DisplayName("创建用户成功 - 需要先登录")
    void testCreateUserSuccess() {
        // 先登录
        var loginHeaders = new HttpHeaders();
        loginHeaders.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        var loginBody = new LinkedMultiValueMap<String, String>();
        loginBody.add("username", TEST_USERNAME);
        loginBody.add("password", TEST_PASSWORD);

        var loginResp = rest.postForEntity("/api/auth/login", new HttpEntity<>(loginBody, loginHeaders), String.class);
        assertThat(loginResp.getStatusCode()).isEqualTo(HttpStatus.OK);

        // 获取 Cookie
        var cookies = loginResp.getHeaders().get("Set-Cookie");
        assertThat(cookies).isNotNull();
        var sessionCookie = cookies.stream()
                .filter(c -> c.startsWith("JSESSIONID="))
                .findFirst()
                .orElseThrow();

        // 创建新用户
        String newUsername = "newuser_" + System.currentTimeMillis();
        var headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.add("Cookie", sessionCookie);
        String requestBody = """
                {
                    "userName": "%s",
                    "password": "newpass123",
                    "nickname": "新用户",
                    "email": "newuser@example.com",
                    "enabled": true
                }
                """.formatted(newUsername);

        var resp = rest.postForEntity("/api/user/register", new HttpEntity<>(requestBody, headers), String.class);
        assertThat(resp.getBody()).contains("\"code\":10000");
        assertThat(resp.getBody()).contains("\"message\":\"操作成功\"");
        assertThat(resp.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    @DisplayName("创建用户失败 - 未登录")
    void testCreateUserFailureUnauthorized() {
        String newUsername = "newuser_" + System.currentTimeMillis();
        var headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        String requestBody = """
                {
                    "username": "%s",
                    "password": "newpass123",
                    "nickname": "新用户",
                    "email": "newuser@example.com",
                    "enabled": true
                }
                """.formatted(newUsername);

        var resp = rest.postForEntity("/api/user/register", new HttpEntity<>(requestBody, headers), String.class);
        assertThat(resp.getBody()).contains("\"code\":401");
        assertThat(resp.getBody()).contains("\"message\":\"未认证，请先登录\"");
        assertThat(resp.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
    }

    @Test
    @DisplayName("创建用户失败 - 用户名已存在")
    void testCreateUserFailureUserExists() {
        // 先登录
        var loginHeaders = new HttpHeaders();
        loginHeaders.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        var loginBody = new LinkedMultiValueMap<String, String>();
        loginBody.add("username", TEST_USERNAME);
        loginBody.add("password", TEST_PASSWORD);

        var loginResp = rest.postForEntity("/api/auth/login", new HttpEntity<>(loginBody, loginHeaders), String.class);
        assertThat(loginResp.getStatusCode()).isEqualTo(HttpStatus.OK);

        // 获取 Cookie
        var cookies = loginResp.getHeaders().get("Set-Cookie");
        assertThat(cookies).isNotNull();
        var sessionCookie = cookies.stream()
                .filter(c -> c.startsWith("JSESSIONID="))
                .findFirst()
                .orElseThrow();

        // 尝试创建已存在的用户
        var headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.add("Cookie", sessionCookie);
        String requestBody = """
                {
                    "userName": "%s",
                    "password": "anypassword",
                    "nickname": "测试",
                    "email": "test2@example.com",
                    "enabled": true
                }
                """.formatted(TEST_USERNAME);

        var resp = rest.postForEntity("/api/user/register", new HttpEntity<>(requestBody, headers), String.class);
        assertThat(resp.getBody()).contains("\"code\":40001");
        assertThat(resp.getBody()).contains("\"message\":\"用户已存在");
        assertThat(resp.getStatusCode()).isEqualTo(HttpStatus.OK);
    }
}
