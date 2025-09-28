package com.broadridge;

import org.testng.ITestListener;
import org.testng.ITestResult;

import java.io.FileWriter;
import java.io.IOException;

public class FailureLogger implements ITestListener {

    @Override
    public void onTestFailure(ITestResult result) {
        String testName = result.getMethod().getMethodName();
        String errorMessage = result.getThrowable() != null ? result.getThrowable().getMessage() : "No error";
        String failedUrl = "N/A";

        try {
            // If your test stores pageURL in parameters
            Object[] params = result.getParameters();
            if (params.length > 0) {
                failedUrl = params[0].toString();
            }
        } catch (Exception ignored) {}

        try (FileWriter fw = new FileWriter("target/failed-tests.txt", true)) {
            fw.write("❌ <b>Test:</b> " + testName + "<br/>\n");
            fw.write("&nbsp;&nbsp; <b>URL:</b> " + failedUrl + "  <br/>\n");
            fw.write("&nbsp;&nbsp; <b>Error:</b> " + errorMessage + " <br/><br/>\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
