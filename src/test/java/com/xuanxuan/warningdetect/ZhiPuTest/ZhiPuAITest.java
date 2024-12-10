package com.xuanxuan.warningdetect.ZhiPuTest;

import com.xuanxuan.warningdetect.manager.ZhiPuAIManager;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

/**
 * 测试封装的智谱大模型方法类
 */

@SpringBootTest
public class ZhiPuAITest {

    @Resource
    private ZhiPuAIManager zhiPuAIManager;

    @Test
    public void test() {
         String systemMessage = "你是一个 AI 助手，请根据用户输入回答问题";
         String userMessage = "请你在 100 字以内介绍一下什么是原神";
         String result = zhiPuAIManager.doStableSyncRequest(systemMessage, userMessage);
         System.out.println(result);
    }
}
