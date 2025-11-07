package com.yx.nas.tool;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * 密码验证测试
 */
@SpringBootTest
public class PasswordVerificationTest {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Test
    public void generatePasswordHash() {
        String password = "12345678";
        String hash = passwordEncoder.encode(password);

        System.out.println("=".repeat(80));
        System.out.println("测试用户密码哈希生成");
        System.out.println("=".repeat(80));
        System.out.println("原始密码: " + password);
        System.out.println("BCrypt哈希: " + hash);
        System.out.println("哈希长度: " + hash.length());
        System.out.println("验证结果: " + passwordEncoder.matches(password, hash));
        System.out.println("=".repeat(80));
        System.out.println("\n请将上面的哈希值复制到 auth-test-data.sql 中!");
    }

    @Test
    public void verifyExistingHash() {
        String password = "123";
        String existingHash = "$2a$10$rOLJD.ZfrDlp2LB/SB4bX.q3.g6NLaRo7XkBvR7m5dicy.PXXpBLm";

        boolean matches = passwordEncoder.matches(password, existingHash);

        System.out.println("=".repeat(80));
        System.out.println("验证现有哈希");
        System.out.println("=".repeat(80));
        System.out.println("原始密码: " + password);
        System.out.println("现有哈希: " + existingHash);
        System.out.println("验证结果: " + matches);
        System.out.println("=".repeat(80));

        if (!matches) {
            System.out.println("\n❌ 现有哈希不匹配! 请运行 generatePasswordHash() 生成新的哈希值");
        } else {
            System.out.println("\n✅ 哈希值正确! 可以使用此哈希值");
        }
    }
}

