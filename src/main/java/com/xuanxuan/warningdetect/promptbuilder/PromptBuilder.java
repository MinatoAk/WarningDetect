package com.xuanxuan.warningdetect.promptbuilder;

import com.xuanxuan.warningdetect.entity.WarningData;

public class PromptBuilder {

    /**
     * 构造 Java Test Case 的 userPrompt
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

}
