package com.xuanxuan.warningdetect.ZhiPuTest;

import com.zhipu.oapi.ClientV4;
import com.zhipu.oapi.Constants;
import com.zhipu.oapi.service.v4.model.ChatCompletionRequest;
import com.zhipu.oapi.service.v4.model.ChatMessage;
import com.zhipu.oapi.service.v4.model.ChatMessageRole;
import com.zhipu.oapi.service.v4.model.ModelApiResponse;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;

/**
 * 智谱 AI 官方示例 demo
 */

@SpringBootTest
public class ZhiPuAIOfficialTest {

    @Test
    public void test() {
        // todo: 1) 设置您的 API_KEY
        String apiKey = "";

        // 2) 创建客户端
        ClientV4 client = new ClientV4.Builder(apiKey).build();

        // 3) 构造请求
        List<ChatMessage> messages = new ArrayList<>();
        ChatMessage chatMessage1 = new ChatMessage(ChatMessageRole.USER.value(), "你好，我是轩轩，很高兴认识你");
        messages.add(chatMessage1);
        ChatMessage chatMessage2 = new ChatMessage(ChatMessageRole.ASSISTANT.value(),
                "你好，轩轩！很高兴认识你，有什么我可以帮助你的吗？无论是学习、娱乐还是其他方面的问题，都可以随时问我哦！");
        messages.add(chatMessage2);
        ChatMessage chatMessage3 = new ChatMessage(ChatMessageRole.USER.value(), "我是谁");
        messages.add(chatMessage3);


        String requestId = String.valueOf(System.currentTimeMillis());
        ChatCompletionRequest chatCompletionRequest = ChatCompletionRequest.builder()
                .model(Constants.ModelChatGLM4)
                .stream(Boolean.FALSE)
                .invokeMethod(Constants.invokeMethod)
                .messages(messages)
                .requestId(requestId)
                .build();

        // 4) 调用
        ModelApiResponse invokeModelApiResp = client.invokeModelApi(chatCompletionRequest);
        System.out.println("model output: " + invokeModelApiResp.getData().getChoices().get(0).getMessage().getContent());
    }
}
