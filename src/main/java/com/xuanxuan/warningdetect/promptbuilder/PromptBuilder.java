package com.xuanxuan.warningdetect.promptbuilder;

import com.xuanxuan.warningdetect.entity.WarningData;
import com.xuanxuan.warningdetect.entity.cppWarningDataDTO;
import com.xuanxuan.warningdetect.promptbuilder.MutipleChatMessage.BOF_COT_ChatMessages;
import com.xuanxuan.warningdetect.promptbuilder.MutipleChatMessage.NQD_COT_ChatMessages;
import com.xuanxuan.warningdetect.promptbuilder.MutipleChatMessage.UAF_COT_ChatMessages;
import com.zhipu.oapi.service.v4.model.ChatMessage;
import com.zhipu.oapi.service.v4.model.ChatMessageRole;

import java.util.ArrayList;
import java.util.List;

public class PromptBuilder {

    /**
     * 构造 Test Case 的 userPrompt
     */
    public String buildUserPrompt(WarningData warningData) {
        StringBuilder userMessage = new StringBuilder();

        userMessage.append("warning type: ").append(warningData.getWarningType()).append("\n");
        userMessage.append("warning line: ").append("\n");
        userMessage.append("```\n");
        userMessage.append(warningData.getWarningLine()).append("\n");
        userMessage.append("```\n");
        userMessage.append("corresponding code snippet: ").append("\n");
        userMessage.append("```\n");
        userMessage.append(warningData.getWarningMethod()).append("\n");
        userMessage.append("```\n");

        return userMessage.toString();
    }

    /**
     *  构造 BOF 的多轮对话
     */
    public List<ChatMessage> buildBOFMutipleChatMessages(String systemMessage, cppWarningDataDTO warningDataDTO) {
        List<ChatMessage> messages = new ArrayList<>();

        // 1) 添加系统预设
        ChatMessage systemChatMessage = new ChatMessage(ChatMessageRole.SYSTEM.value(), systemMessage);
        messages.add(systemChatMessage);

        // 2) 添加多轮对话历史记录
        ChatMessage userChatMessage1 = new ChatMessage(ChatMessageRole.USER.value(), BOF_COT_ChatMessages.BOF_COT_Q1);
        ChatMessage assistantChatMessage1 = new ChatMessage(ChatMessageRole.ASSISTANT.value(), BOF_COT_ChatMessages.BOF_COT_A1);
        ChatMessage userChatMessage2 = new ChatMessage(ChatMessageRole.USER.value(), BOF_COT_ChatMessages.BOF_COT_Q2);
        ChatMessage assistantChatMessage2 = new ChatMessage(ChatMessageRole.ASSISTANT.value(), BOF_COT_ChatMessages.BOF_COT_A2);

        messages.add(userChatMessage1);
        messages.add(assistantChatMessage1);
        messages.add(userChatMessage2);
        messages.add(assistantChatMessage2);

        // 3) 添加用户输入
        String userPrompt = buildMutipleChatUserPrompt(warningDataDTO);
        ChatMessage userChatMessage3 = new ChatMessage(ChatMessageRole.USER.value(), userPrompt);
        messages.add(userChatMessage3);

        return messages;
    }

    /**
     *  构造 NQD 的多轮对话
     */
    public List<ChatMessage> buildNQDMutipleChatMessages(String systemMessage, cppWarningDataDTO warningDataDTO) {
        List<ChatMessage> messages = new ArrayList<>();

        // 1) 添加系统预设
        ChatMessage systemChatMessage = new ChatMessage(ChatMessageRole.SYSTEM.value(), systemMessage);
        messages.add(systemChatMessage);

        // 2) 添加多轮对话历史记录
        ChatMessage userChatMessage1 = new ChatMessage(ChatMessageRole.USER.value(), NQD_COT_ChatMessages.NQD_COT_Q1);
        ChatMessage assistantChatMessage1 = new ChatMessage(ChatMessageRole.ASSISTANT.value(), NQD_COT_ChatMessages.NQD_COT_A1);
        ChatMessage userChatMessage2 = new ChatMessage(ChatMessageRole.USER.value(), NQD_COT_ChatMessages.NQD_COT_Q2);
        ChatMessage assistantChatMessage2 = new ChatMessage(ChatMessageRole.ASSISTANT.value(), NQD_COT_ChatMessages.NQD_COT_A2);

        messages.add(userChatMessage1);
        messages.add(assistantChatMessage1);
        messages.add(userChatMessage2);
        messages.add(assistantChatMessage2);

        // 3) 添加用户输入
        String userPrompt = buildMutipleChatUserPrompt(warningDataDTO);
        ChatMessage userChatMessage3 = new ChatMessage(ChatMessageRole.USER.value(), userPrompt);
        messages.add(userChatMessage3);

        return messages;
    }

    /**
     *  构造 UAF 的多轮对话
     */
    public List<ChatMessage> buildUAFMutipleChatMessages(String systemMessage, cppWarningDataDTO warningDataDTO) {
        List<ChatMessage> messages = new ArrayList<>();

        // 1) 添加系统预设
        ChatMessage systemChatMessage = new ChatMessage(ChatMessageRole.SYSTEM.value(), systemMessage);
        messages.add(systemChatMessage);

        // 2) 添加多轮对话历史记录
        ChatMessage userChatMessage1 = new ChatMessage(ChatMessageRole.USER.value(), UAF_COT_ChatMessages.UAF_COT_Q1);
        ChatMessage assistantChatMessage1 = new ChatMessage(ChatMessageRole.ASSISTANT.value(), UAF_COT_ChatMessages.UAF_COT_A1);
        ChatMessage userChatMessage2 = new ChatMessage(ChatMessageRole.USER.value(), UAF_COT_ChatMessages.UAF_COT_Q2);
        ChatMessage assistantChatMessage2 = new ChatMessage(ChatMessageRole.ASSISTANT.value(), UAF_COT_ChatMessages.UAF_COT_A2);

        messages.add(userChatMessage1);
        messages.add(assistantChatMessage1);
        messages.add(userChatMessage2);
        messages.add(assistantChatMessage2);

        // 3) 添加用户输入
        String userPrompt = buildMutipleChatUserPrompt(warningDataDTO);
        ChatMessage userChatMessage3 = new ChatMessage(ChatMessageRole.USER.value(), userPrompt);
        messages.add(userChatMessage3);

        return messages;
    }

    /**
     * 构造 cpp 多轮对话的用户输入
     */
    public String buildMutipleChatUserPrompt(cppWarningDataDTO warningDataDTO) {
        StringBuilder userMessage = new StringBuilder();

        userMessage.append("# Bug Report\n").append("```json\n");
        userMessage.append("{\n");
        userMessage.append("\"bug_type\": ").append("\"").append(warningDataDTO.getWarningType()).append("\",").append("\n");
        userMessage.append("\"line\": ").append("\"").append(warningDataDTO.getLine()).append("\",").append("\n");
        userMessage.append("\"file\": ").append("\"").append(warningDataDTO.getFile()).append("\",").append("\n");
        userMessage.append("\"qualifier\": ").append("\"").append(warningDataDTO.getQualifier()).append("\",").append("\n");
        userMessage.append("\"Trace\":\n");
        userMessage.append("{\n");
        userMessage.append("\"filename\": ").append("\"").append(warningDataDTO.getTraceFileName()).append("\",").append("\n");
        userMessage.append("\"line_number\": ").append("\"").append(warningDataDTO.getTraceLineNumber()).append("\",").append("\n");
        userMessage.append("\"column_number\": ").append("\"").append(warningDataDTO.getTraceColumnNumber()).append("\",").append("\n");
        userMessage.append("\"description\": ").append("\"").append(warningDataDTO.getTraceDescription()).append("\"").append("\n");
        userMessage.append("}\n");
        userMessage.append("}\n");
        userMessage.append("```\n");
        userMessage.append("\n");
        userMessage.append("# Code Snippet\n").append("```C,C++\n");
        userMessage.append(warningDataDTO.getWarningMethod()).append("\n");
        userMessage.append("```\n");

        return userMessage.toString();
    }
}
