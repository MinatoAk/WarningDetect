package com.xuanxuan.warningdetect;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.xuanxuan.warningdetect.constant.APIKeys;
import com.xuanxuan.warningdetect.constant.FilePathConstant;
import com.xuanxuan.warningdetect.entity.WarningData;
import com.xuanxuan.warningdetect.entity.cppWarningDataDTO;
import com.xuanxuan.warningdetect.exception.BusinessException;
import com.xuanxuan.warningdetect.exception.ErrorCode;
import com.xuanxuan.warningdetect.manager.ZhiPuAIManager;
import com.xuanxuan.warningdetect.promptbuilder.PromptBuilder;
import com.xuanxuan.warningdetect.promptbuilder.JavaPromptTemplate;
import com.xuanxuan.warningdetect.promptbuilder.cppPromptTemplate;
import com.zhipu.oapi.ClientV4;
import com.zhipu.oapi.service.v4.model.ChatMessage;
import lombok.extern.slf4j.Slf4j;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

@Slf4j
public class WarningDetectMain {

    /**
     * 存放警告数据集
     */
    private static final List<WarningData> warningDataList = new ArrayList<>();

    /**
     * 存放 cpp 数据从而转化成 JSON 格式
     */
    private static final List<cppWarningDataDTO> cppWarningDataDTOList = new ArrayList<>();

    // todo: 修改系统预设为希望运行的提示词模板
    private static final String systemPrompt = cppPromptTemplate.UAF_COT_PROMPT;

    // todo: 修改日志位置为期望的日志路径
    private static final String logFilePath = FilePathConstant.CPP_CHATGLM3TURBO_UAF_COT_LOG_PATH;

    /**
     * 智谱大模型客户端
     */
    private final ClientV4 clientV4 = new ClientV4.Builder(APIKeys.ZHIPU_AI_KEY).build();

    /**
     * 智谱大模型封装方法
     */
    private static final ZhiPuAIManager zhiPuAIManager = new ZhiPuAIManager();

    /**
     * 用户 Prompt 构造器
     */
    private static final PromptBuilder promptBuilder = new PromptBuilder();

    /**
     * 记录大模型判断的结果
     */
    private static Integer TPNum = 0;
    private static Integer TNNum = 0;
    private static Integer FPNum = 0;
    private static Integer FNNum = 0;
    private static Integer UKNum = 0;

    public static void main(String[] args) {
        WarningDetectMain warningDetectMain = new WarningDetectMain();

        // todo: 1) 读取数据集，参数为 java 或 cpp
        String type = "cpp";
        warningDetectMain.getWarningData(type);

        // todo: 2) 如果使用 FewShots 模板需要读 JSON 格式完整数据
        // warningDetectMain.getCPPJsonData();

        // 3) 调用大模型进行警告检测，并且记录日志
        try (FileOutputStream fos = new FileOutputStream(logFilePath, true);
             OutputStreamWriter osw = new OutputStreamWriter(fos, StandardCharsets.UTF_8)) {

            for (int i = 0; i < warningDataList.size(); i ++ ) {
                WarningData warningData = warningDataList.get(i);
                log.info("[x] Now Running Test Case {}: Index {}", i, warningData.getId());

                // todo: 如果是 FewShots 则需要取消注释
                // cppWarningDataDTO cppWarningDataDTO = cppWarningDataDTOList.get(i);

                try {
                    // 3.1) 请求大模型回答
                    // todo: 选择单轮或 FewShots 调用模式
                    /**
                     * 单轮对话调用方式
                     */
                     String userPrompt = promptBuilder.buildUserPrompt(warningData);
                     String res = zhiPuAIManager.doStableSyncRequest(systemPrompt, userPrompt, warningDetectMain.clientV4);

                    /**
                     * 多轮对话调用方式 FewShots
                     */
                    // todo: 选择 FewShots 的提示词模板类型: promptbuilder.MultipleChatMessage 文件夹下
//                    List<ChatMessage> messages = promptBuilder.buildUAFMutipleChatMessages(systemPrompt, cppWarningDataDTO);
//                    String res = zhiPuAIManager.doMutipleChatRequest(messages, warningDetectMain.clientV4);

                    // 3.2) 解析出大模型给的结论
                    String finalLabel = warningDetectMain.analyzeLabel(res);
                    String trueLabel = warningData.getLabel();

                    // 3.3) 统计大模型结果
                    if ("TP".equals(finalLabel) && "TP".equals(trueLabel)) TPNum ++;
                    else if ("TP".equals(finalLabel) && "FP".equals(trueLabel)) FPNum ++;
                    else if ("FP".equals(finalLabel) && "TP".equals(trueLabel)) FNNum ++;
                    else if ("FP".equals(finalLabel) && "FP".equals(trueLabel)) TNNum ++;
                    else UKNum ++;

                    // 3.4) 日志记录大模型回答信息
                    String formattedRes = String.format("%d - Java Test Case %d:\n%s\n", i, warningData.getId(), res);
                    osw.write(formattedRes);
                    osw.write("Final Label: " + finalLabel + " True Label:" + trueLabel + "\n");
                    osw.write("------------------------------------------------------------\n\n");

                } catch (Exception e) {
                    log.error("[x ERROR x] Error Test Case {}: Index {}", i, warningData.getId());
                    // 3.5) 调用超时的记录，最后可以单独请求重试
                    warningDetectMain.recordErrorData(i, warningData.getId());
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        // 4) 评估结果计算，并且记录日志
        warningDetectMain.getResultData();
    }

    /**
     * 读取数据集
     *
     * @param type: 取值为 java 或 cpp 读取对应数据集
     */
    private void getWarningData(String type) {
        String dataSetPath = "";

        if ("cpp".equals(type)) dataSetPath = FilePathConstant.CPP_DATA_PATH;
        else if ("java".equals(type)) dataSetPath = FilePathConstant.JAVA_DATA_PATH;
        else throw new BusinessException(ErrorCode.PARAMS_ERROR, "请求参数需要为 cpp 或 java，读取对应数据集");
        EasyExcel.read(dataSetPath, WarningData.class, new AnalysisEventListener<WarningData>() {
            // 1) 每解析一行数据,该方法会被调用一次
            @Override
            public void invoke(WarningData warningData, AnalysisContext analysisContext) {
                warningDataList.add(warningData);
            }

            // 2) 全部解析完成被调用
            @Override
            public void doAfterAllAnalysed(AnalysisContext analysisContext) {
                log.info("[x] 读取警告数据集完成，包含数据 {} 条", warningDataList.size());
            }
        }).sheet().doRead();
    }

    /**
     * 读取 cpp 序列化数据集
     */
    private void getCPPJsonData() {
        EasyExcel.read(FilePathConstant.CPP_DATA_PATH, cppWarningDataDTO.class, new AnalysisEventListener<cppWarningDataDTO>() {
            // 1) 每解析一行数据,该方法会被调用一次
            @Override
            public void invoke(cppWarningDataDTO cppWarningDataDTO, AnalysisContext analysisContext) {
                cppWarningDataDTOList.add(cppWarningDataDTO);
            }

            // 2) 全部解析完成被调用
            @Override
            public void doAfterAllAnalysed(AnalysisContext analysisContext) {
                log.info("[x] 序列化 cpp 数据集，包含数据 {} 条", cppWarningDataDTOList.size());
            }
        }).sheet().doRead();
    }

    /**
     * 解析大模型最终结论
     */
    private String analyzeLabel(String res) {
        int start = res.indexOf("@@@");
        String labelStr = res.substring(start + 4, start + 5);

        if ("f".equals(labelStr) || "F".equals(labelStr)) return "FP";
        else if ("r".equals(labelStr) || "R".equals(labelStr)) return "TP";

        return "UK";
    }

    /**
     * 计算 F1, Recall, Precision, Accuracy
     */
    private void getResultData() {
        try (FileOutputStream fos = new FileOutputStream(FilePathConstant.RESULT_LOG_PATH, true);
             OutputStreamWriter osw = new OutputStreamWriter(fos, StandardCharsets.UTF_8)) {

            int sum = TPNum + TNNum + FPNum + FNNum + UKNum;

            osw.write("Total Test Cases Num: " + sum + "\n");
            osw.write("TP: " + TPNum + " TN: " + TNNum + " FP: " + FPNum + " FN: " + FNNum + " UK: " + UKNum + "\n");

            double accuracy = 1.0 * (TPNum + TNNum) / (TPNum + TNNum + FPNum + FNNum);
            double precision = 1.0 * TPNum / (TPNum + FPNum);
            double recall = 1.0 * TPNum / (TPNum + FNNum);
            double f1 = 2 * precision * recall / (precision + recall);

            osw.write("Accuracy: " + accuracy + " Precision: " + precision + "\nRecall: " + recall + " F1: " + f1 +"\n\n");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 记录一下超时的任务，后面重做
     */
    private void recordErrorData(int i, int index) {
        try (FileOutputStream fos = new FileOutputStream(FilePathConstant.ERROR_DATA_LOG_PATH, true);
             OutputStreamWriter osw = new OutputStreamWriter(fos, StandardCharsets.UTF_8)) {

            osw.write("i: " + i + " index: " + index + "\n");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}