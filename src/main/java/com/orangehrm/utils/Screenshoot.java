package com.orangehrm.utils;

import java.io.File;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

public class Screenshoot {

    public static String capture(WebDriver driver, String name) {
        try {
            File srcFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);

            String safeName = name.replaceAll("[^a-zA-Z0-9_-]", "_");
            File destFile = new File(
                    "screenshots/" + safeName + "_" + System.currentTimeMillis() + ".png"
            );

            destFile.getParentFile().mkdirs();
            FileUtils.copyFile(srcFile, destFile);

            System.out.println("Screenshot saved: " + destFile.getAbsolutePath());
            return destFile.getAbsolutePath(); // 🔥 KUNCI

        } catch (Exception e) {
            System.out.println("Screenshot gagal: " + e.getMessage());
            return null;
        }
    }
}
