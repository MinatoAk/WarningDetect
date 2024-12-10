package com.xuanxuan.warningdetect.ExcelTest;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.xuanxuan.warningdetect.entity.WarningData;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;

@SpringBootTest
public class ExcelTest {

    @Test
    public void testReadExcel() {
        String filename = "src/main/java/com/xuanxuan/warningdetect/dataset/java.xlsx";
        List<WarningData> warnings = new ArrayList<>();

        EasyExcel.read(filename, WarningData.class, new AnalysisEventListener<WarningData>() {
            // 1) 每解析一行数据,该方法会被调用一次
            @Override
            public void invoke(WarningData warningData, AnalysisContext analysisContext) {
                warnings.add(warningData);
            }

            // 2) 全部解析完成被调用
            @Override
            public void doAfterAllAnalysed(AnalysisContext analysisContext) {
                System.out.println("解析完成...");
                System.out.println("共有数据: " + warnings.size());
            }
        }).sheet().doRead();
    }


}
