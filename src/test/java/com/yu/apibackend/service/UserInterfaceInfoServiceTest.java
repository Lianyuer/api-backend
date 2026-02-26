package com.yu.apibackend.service;

import com.yu.apicommon.service.InnerUserInterfaceInfoService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

@SpringBootTest
class UserInterfaceInfoServiceTest {

    @Resource
    private InnerUserInterfaceInfoService innerUserInterfaceInfoService;

    @Test
    void invokeCount() {
        // 调用了userInterfaceInfoService的invokeCount方法，并传入两个参数(1L, 1L)
        boolean b = innerUserInterfaceInfoService.invokeCount(2016891065461731330L, 2016331719721934850L);
        // 表示断言b的值为true，即测试用例期望invokeCount方法返回true
        Assertions.assertTrue(b);
    }
}