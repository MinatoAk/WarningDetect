package com.xuanxuan.warningdetect.entity;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class cppWarningDataDTO {

    @ExcelProperty(value = "vtype")
    private String warningType;

    @ExcelProperty(value = "line")
    private String line;

    @ExcelProperty(value = "file")
    private String file;

    @ExcelProperty(value = "qualifier")
    private String qualifier;

    @ExcelProperty(value = "trace_filename")
    private String traceFileName;

    @ExcelProperty(value = "trace_line_number")
    private String traceLineNumber;

    @ExcelProperty(value = "trace_column_number")
    private String traceColumnNumber;

    @ExcelProperty(value = "trace_description")
    private String traceDescription;

    @ExcelProperty(value = "warning_method")
    private String warningMethod;
}
