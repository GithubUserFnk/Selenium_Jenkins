package com.orangehrm.base;

import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import io.github.bonigarcia.wdm.WebDriverManager;

import java.io.File;
import java.io.IOException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;

import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import com.aventstack.extentreports.MediaEntityBuilder;
import org.apache.commons.io.FileUtils;

public class BaseTest {
    protected WebDriver driver;
    protected Logger logger;
    protected ExtentReports extent;
    protected ExtentTest test;

    @BeforeMethod
    public void setUp() {
        // Setup WebDriver otomatis
        WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver();

        // Setup Log4j
        logger = LogManager.getLogger(this.getClass());
        logger.info("Browser dibuka");

        // Setup ExtentReports 5.x
        ExtentSparkReporter spark = new ExtentSparkReporter("test-report.html");
        extent = new ExtentReports();
        extent.attachReporter(spark);

        test = extent.createTest(this.getClass().getSimpleName());
    }

    @AfterMethod
    public void tearDown() {
        if(driver != null) driver.quit();
        logger.info("Browser ditutup");

        if(extent != null) extent.flush(); // generate report
    }

    public void captureScreenshot(String name) {
        try {
            // Ambil screenshot
            File srcFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
            String path = "screenshots/" + name + "_" + System.currentTimeMillis() + ".png";
            File destFile = new File(path);
            FileUtils.copyFile(srcFile, destFile);

            // Attach ke ExtentReport
            test.pass("Screenshot: " + name,
                    MediaEntityBuilder.createScreenCaptureFromPath(path).build());

        } catch (IOException e) {
            test.fail("Gagal ambil screenshot: " + e.getMessage());
        }
    }
}
