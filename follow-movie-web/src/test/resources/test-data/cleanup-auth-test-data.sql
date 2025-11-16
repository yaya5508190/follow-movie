-- 清理测试用户数据
DELETE FROM sys_user WHERE username = 'test';
DELETE FROM sys_user WHERE username like 'newuser_%';

