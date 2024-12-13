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
     * Java 日志路径: </><model><promptTemplate>
     */
    String CHATGLM3TURBO_BASIC_LOG_PATH = "logs/Java/ChatGLM3Turbo/ChatGLM3Turbo_Basic.log";
    String CHATGLM3TURBO_COR_COT_LOG_PATH = "logs/Java/ChatGLM3Turbo/ChatGLM3Turbo_COR_COT.log";
    String CHATGLM3TURBO_BAP_COT_LOG_PATH = "logs/Java/ChatGLM3Turbo/ChatGLM3Turbo_BAP_COT.log";
    String CHATGLM3TURBO_DOD_COT_LOG_PATH = "logs/Java/ChatGLM3Turbo/ChatGLM3Turbo_DOD_COT.log";
    String CHATGLM3TURBO_EXP_COT_LOG_PATH = "logs/Java/ChatGLM3Turbo/ChatGLM3Turbo_EXP_COT.log";
    String CHATGLM3TURBO_I18N_COT_LOG_PATH = "logs/Java/ChatGLM3Turbo/ChatGLM3Turbo_I18N_COT.log";
    String CHATGLM3TURBO_MAL_COT_LOG_PATH = "logs/Java/ChatGLM3Turbo/ChatGLM3Turbo_MAL_COT.log";
    String CHATGLM3TURBO_MUL_COT_LOG_PATH = "logs/Java/ChatGLM3Turbo/ChatGLM3Turbo_MUL_COT.log";
    String CHATGLM3TURBO_PER_COT_LOG_PATH = "logs/Java/ChatGLM3Turbo/ChatGLM3Turbo_PER_COT.log";
}
