package com.xuanxuan.warningdetect.manager;

import com.xuanxuan.warningdetect.exception.BusinessException;
import com.xuanxuan.warningdetect.exception.ErrorCode;
import com.zhipu.oapi.ClientV4;
import com.zhipu.oapi.Constants;
import com.zhipu.oapi.service.v4.model.*;

import java.util.ArrayList;
import java.util.List;

public class ZhiPuAIManager {

    private static final Float stableTemperature = 0.05f;
    private static final Float unStableTemperature = 0.99f;

    /**
     *  非流式方法（答案不稳定）
     *
     * @param systemMessage
     * @param userMessage
     * @return
     */
    public String doUnstableSyncRequest(String systemMessage, String userMessage, ClientV4 clientV4) {
        return doSyncRequest(systemMessage, userMessage, unStableTemperature, clientV4);
    }

    /**
     *  非流式方法（答案稳定）
     *
     * @param systemMessage
     * @param userMessage
     * @return
     */
    public String doStableSyncRequest(String systemMessage, String userMessage, ClientV4 clientV4) {
        return doSyncRequest(systemMessage, userMessage, stableTemperature, clientV4);
    }

    /**
     *  非流式方法
     *
     * @param systemMessage
     * @param userMessage
     * @param temperature
     * @return
     */
    public String doSyncRequest(String systemMessage, String userMessage, Float temperature, ClientV4 clientV4) {
        return doRequest(systemMessage, userMessage, Boolean.FALSE, temperature, clientV4);
    }


    /**
     *  通用方法 (简化消息传递)
     *
     * @param systemMessage
     * @param userMessage
     * @param stream
     * @param temperature
     * @return
     */
    public String doRequest(String systemMessage, String userMessage, Boolean stream, Float temperature, ClientV4 clientV4) {
        List<ChatMessage> messages = new ArrayList<>();
        ChatMessage systemChatMessage = new ChatMessage(ChatMessageRole.SYSTEM.value(), systemMessage);
        ChatMessage userChatMessage = new ChatMessage(ChatMessageRole.USER.value(), userMessage);
        messages.add(systemChatMessage);
        messages.add(userChatMessage);

        return doRequest(messages, stream, temperature, clientV4);
    }


    /**
     * 通用请求
     *
     * @param messages
     * @param stream
     * @param temperature
     * @return
     */
    public String doRequest(List<ChatMessage> messages ,Boolean stream, Float temperature, ClientV4 clientV4) {
        ChatCompletionRequest chatCompletionRequest = ChatCompletionRequest.builder()
                .model(Constants.ModelChatGLM3TURBO)
                .stream(stream)
                .temperature(temperature)
                .invokeMethod(Constants.invokeMethod)
                .messages(messages)
                .build();

        ModelApiResponse invokeModelApiResp = clientV4.invokeModelApi(chatCompletionRequest);
        try {
            return invokeModelApiResp.getData().getChoices().get(0).getMessage().getContent().toString();
        } catch (Exception e) {
            e.printStackTrace();
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, e.getMessage());
        }
    }
}
