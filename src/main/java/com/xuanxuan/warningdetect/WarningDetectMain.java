package com.xuanxuan.warningdetect;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.xuanxuan.warningdetect.constant.APIKeys;
import com.xuanxuan.warningdetect.constant.FilePathConstant;
import com.xuanxuan.warningdetect.entity.WarningData;
import com.xuanxuan.warningdetect.manager.ZhiPuAIManager;
import com.zhipu.oapi.ClientV4;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

@Slf4j
public class WarningDetectMain {

    /**
     * 存放 Java 警告数据集
     */
    List<WarningData> javaWarningData = new ArrayList<>();

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

    public static void main(String[] args) {
        WarningDetectMain warningDetectMain = new WarningDetectMain();
        ZhiPuAIManager zhiPuAIManager = new ZhiPuAIManager();
        // 1) 创建智谱大模型调用的客户端
        ClientV4 clientV4 = new ClientV4.Builder(APIKeys.ZHIPU_AI_KEY).build();

        // 2) 读取 Java 数据集
        warningDetectMain.getJavaData();

        // 3) 分批调用大模型进行警告检测
    }
}
