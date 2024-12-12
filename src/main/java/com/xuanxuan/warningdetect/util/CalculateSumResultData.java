package com.xuanxuan.warningdetect.util;

import com.xuanxuan.warningdetect.constant.FilePathConstant;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;

public class CalculateSumResultData {

    public static void main(String[] args) {
        int TP = 30 + 24 + 31 + 39;
        int TN = 75 + 95 + 64 + 111;
        int FP = 31 + 21 + 31 + 49;
        int FN = 59 + 66 + 57 + 57;
        int UK = 50 + 40 + 64 + 80;

        int sum = TP + TN + FP + FN + UK;

        double accuracy = 1.0 * (TP + TN) / (TP + TN + FP + FN);
        double precision = 1.0 * TP / (TP + FP);
        double recall = 1.0 * TP / (TP + FN);
        double f1 = 2 * precision * recall / (precision + recall);

        try (FileOutputStream fos = new FileOutputStream(FilePathConstant.RESULT_LOG_PATH, true);
             OutputStreamWriter osw = new OutputStreamWriter(fos, StandardCharsets.UTF_8)) {

            osw.write("Total Test Cases Num: " + sum + "\n");
            osw.write("TP: " + TP + " TN: " + TN + " FP: " + FP + " FN: " + FN + " UK: " + UK + "\n");
            osw.write("Accuracy: " + accuracy + " Precision: " + precision + "\nRecall: " + recall + " F1: " + f1 +"\n\n");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
