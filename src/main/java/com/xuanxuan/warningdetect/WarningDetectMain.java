package com.xuanxuan.warningdetect;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.xuanxuan.warningdetect.constant.APIKeys;
import com.xuanxuan.warningdetect.constant.FilePathConstant;
import com.xuanxuan.warningdetect.entity.WarningData;
import com.xuanxuan.warningdetect.manager.ZhiPuAIManager;
import com.xuanxuan.warningdetect.promptbuilder.PromptBuilder;
import com.xuanxuan.warningdetect.promptbuilder.PromptTemplate;
import com.zhipu.oapi.ClientV4;
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
     * 存放 Java 警告数据集
     */
    private List<WarningData> javaWarningData = new ArrayList<>();

    /**
     * 系统预设
     */
    private static final String systemPrompt = PromptTemplate.BASIC_SYSTEM_PROMPT;

    /**
     * 智谱大模型客户端
     */
    private ClientV4 clientV4 = new ClientV4.Builder(APIKeys.ZHIPU_AI_KEY).build();

    /**
     * 智谱大模型封装方法
     */
    private static ZhiPuAIManager zhiPuAIManager = new ZhiPuAIManager();

    /**
     * 用户 Prompt 构造器
     */
    private static PromptBuilder promptBuilder = new PromptBuilder();

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

        // 1) 读取 Java 数据集
        warningDetectMain.getJavaData();

        // 2) 调用大模型进行警告检测，并且记录日志
        try (FileOutputStream fos = new FileOutputStream(FilePathConstant.JAVA_LOG_PATH);
             OutputStreamWriter osw = new OutputStreamWriter(fos, StandardCharsets.UTF_8)) {

            for (int i = 0; i < 10; i ++ ) {
                WarningData warningData = warningDetectMain.javaWarningData.get(i);
                log.info("[x] Now Running Test Case {}: Index {}", i + 1, warningData.getId());

                // 2.1) 请求大模型回答
                String userPrompt = promptBuilder.buildUserPrompt(warningData);
                String res = zhiPuAIManager.doStableSyncRequest(systemPrompt, userPrompt, warningDetectMain.clientV4);

                // 2.2) 解析出大模型给的结论
                String finalLabel = warningDetectMain.analyzeLabel(res);
                String trueLabel = warningData.getLabel();

                // 2.3) 统计大模型结果
                if ("TP".equals(finalLabel) && "TP".equals(trueLabel)) TPNum ++;
                else if ("TP".equals(finalLabel) && "FP".equals(trueLabel)) FPNum ++;
                else if ("FP".equals(finalLabel) && "TP".equals(trueLabel)) FNNum ++;
                else if ("FP".equals(finalLabel) && "FP".equals(trueLabel)) TNNum ++;
                else UKNum ++;

                // 2.4) 日志记录大模型回答信息
                String formattedRes = String.format("%d - Java Test Case %d:\n%s\n", i, warningData.getId(), res);
                osw.write(formattedRes);
                osw.write("Final Label: " + finalLabel + " True Label:" + trueLabel + "\n");
                osw.write("------------------------------------------------------------\n\n");
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        // 3) 评估结果计算，并且记录日志
        try (FileOutputStream fos = new FileOutputStream(FilePathConstant.RESULT_LOG_PATH);
             OutputStreamWriter osw = new OutputStreamWriter(fos, StandardCharsets.UTF_8)) {


            osw.write("Total Test Cases Num: " + warningDetectMain.javaWarningData.size() + "\n");
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
     * 读取 Java 警告数据
     */
    private void getJavaData() {
        EasyExcel.read(FilePathConstant.JAVA_DATA_PATH, WarningData.class, new AnalysisEventListener<WarningData>() {
            // 1) 每解析一行数据,该方法会被调用一次
            @Override
            public void invoke(WarningData warningData, AnalysisContext analysisContext) {
                javaWarningData.add(warningData);
            }

            // 2) 全部解析完成被调用
            @Override
            public void doAfterAllAnalysed(AnalysisContext analysisContext) {
                log.info("[x] 读取 Java 警告数据集完成，包含数据 {} 条", javaWarningData.size());
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
}
