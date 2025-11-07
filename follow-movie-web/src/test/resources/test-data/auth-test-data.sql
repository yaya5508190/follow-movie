-- 清理测试用户数据
DELETE FROM sys_user WHERE username = 'test';

-- 插入测试用户
-- 用户名: test
-- 密码: 123 (BCrypt加密后的值)
-- 注意：这个哈希值是通过 BCryptPasswordEncoder 对 "123" 加密生成的
INSERT INTO sys_user (username, password, nickname, email, enabled, create_time)
VALUES (
    'test',
    '$2a$10$MF9trahyDJNoXQiiema8puUbAE4Ck9j948wEeFkQnqdcERRbCLOTa',
    '测试用户',
    'test@example.com',
    true,
    CURRENT_TIMESTAMP
);

