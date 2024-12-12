package com.xuanxuan.warningdetect.constant;

public interface FilePathConstant {
    /**
     * Java 数据集路径
     */
    String JAVA_DATA_PATH = "dataset/java.xlsx";

    /**
     * 大模型结果日志
     */
    String RESULT_LOG_PATH = "logs/LLMResult.log";

    /**
     * 运行超时的任务合集
     */
    String ERROR_DATA_LOG_PATH = "logs/error_data_index.log";

    /**
     * 日志路径: <model><promptTemplate>
     */
    String CHATGLM3TURBO_BASIC_LOG_PATH = "logs/Java/ChatGLM3Turbo/ChatGLM3Turbo_Basic.log";
    String CHATGLM3TURBO_BAP_COT_LOG_PATH = "logs/Java/ChatGLM3Turbo/ChatGLM3Turbo_BAP_COT.log";
}
