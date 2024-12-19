package com.xuanxuan.warningdetect.constant;

public interface FilePathConstant {
    /**
     * Java 数据集路径
     */
    String JAVA_DATA_PATH = "dataset/java.xlsx";

    /**
     * cpp 数据集路径
     */
    String CPP_DATA_PATH = "dataset/cpp.xlsx";

    /**
     * 大模型结果日志
     */
    String RESULT_LOG_PATH = "logs/LLMResult.log";

    /**
     * 运行超时的任务合集
     */
    String ERROR_DATA_LOG_PATH = "logs/error_data_index.log";

    /**
     * 测试日志
     */
    String TEST_LOG_PATH = "logs/test.log";

    /**
     * 日志路径: <cpp / Java><model><promptTemplate>
     */
    String JAVA_CHATGLM3TURBO_BASIC_LOG_PATH = "logs/Java/ChatGLM3Turbo/Basic.log";
    String JAVA_CHATGLM3TURBO_COR_COT_LOG_PATH = "logs/Java/ChatGLM3Turbo/COR_COT.log";
    String JAVA_CHATGLM3TURBO_BAP_COT_LOG_PATH = "logs/Java/ChatGLM3Turbo/BAP_COT.log";
    String JAVA_CHATGLM3TURBO_DOD_COT_LOG_PATH = "logs/Java/ChatGLM3Turbo/DOD_COT.log";
    String JAVA_CHATGLM3TURBO_EXP_COT_LOG_PATH = "logs/Java/ChatGLM3Turbo/EXP_COT.log";
    String JAVA_CHATGLM3TURBO_I18N_COT_LOG_PATH = "logs/Java/ChatGLM3Turbo/I18N_COT.log";
    String JAVA_CHATGLM3TURBO_MAL_COT_LOG_PATH = "logs/Java/ChatGLM3Turbo/MAL_COT.log";
    String JAVA_CHATGLM3TURBO_MUL_COT_LOG_PATH = "logs/Java/ChatGLM3Turbo/MUL_COT.log";
    String JAVA_CHATGLM3TURBO_PER_COT_LOG_PATH = "logs/Java/ChatGLM3Turbo/PER_COT.log";

    String CPP_CHATGLM3TURBO_BOF_COT_FEW_SHOTS_LOG_PATH = "logs/cpp/ChatGLM3Turbo/BOF_COT_FEW_SHOTS.log";
    String CPP_CHATGLM3TURBO_NQD_COT_FEW_SHOTS_LOG_PATH = "logs/cpp/ChatGLM3Turbo/NQD_COT_FEW_SHOTS.log";
    String CPP_CHATGLM3TURBO_UAF_COT_FEW_SHOTS_LOG_PATH = "logs/cpp/ChatGLM3Turbo/UAF_COT_FEW_SHOTS.log";
    String CPP_CHATGLM3TURBO_BOF_COT_LOG_PATH = "logs/cpp/ChatGLM3Turbo/BOF_COT.log";
    String CPP_CHATGLM3TURBO_NQD_COT_LOG_PATH = "logs/cpp/ChatGLM3Turbo/NQD_COT.log";
    String CPP_CHATGLM3TURBO_UAF_COT_LOG_PATH = "logs/cpp/ChatGLM3Turbo/UAF_COT.log";
}
