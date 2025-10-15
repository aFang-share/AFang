package com.example.afanguserbackend;

import com.example.afanguserbackend.utils.EmailUtil;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class AfangUserBackendApplicationTests {
    @Resource
    EmailUtil emailUtil;

    @Test
    void contextLoads() {
        try {
            emailUtil.sendEmailByCode("3111659946@qq.com");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }
}
