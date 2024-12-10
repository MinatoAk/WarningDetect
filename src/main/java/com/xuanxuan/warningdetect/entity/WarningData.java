package com.xuanxuan.warningdetect.entity;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class WarningData {
    @ExcelProperty(value = "index")
    private Integer id;

    @ExcelProperty(value = "vtype")
    private String warningType;

    @ExcelProperty(value = "final_label")
    private String label;

    @ExcelProperty(value = "warning_method")
    private String warningMethod;

    @ExcelProperty(value = "warning_line")
    private String warningLine;
}
