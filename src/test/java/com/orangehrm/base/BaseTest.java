package com.orangehrm.base;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import io.github.bonigarcia.wdm.WebDriverManager;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;

import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.AfterMethod;

import java.util.Map;
import java.io.File;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class BaseTest {

    protected WebDriver driver;
    protected Logger logger;
    protected static ExtentReports extent; // 🔥 STATIC

    @BeforeSuite
    public void setupReport() {
        try {
            // Buat folder reports kalau belum ada
            File reportDir = new File("reports");
            if (!reportDir.exists()) {
                reportDir.mkdirs();
            }

            // Timestamp untuk report unik per run
            String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
            String reportPath = "reports/test-report_" + timestamp + ".html";

            ExtentSparkReporter spark = new ExtentSparkReporter(reportPath);
            extent = new ExtentReports();
            extent.attachReporter(spark);

            System.out.println("ExtentReport dibuat: " + reportPath);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @BeforeMethod
    public void setUp() {
        try {
            ChromeOptions options = new ChromeOptions();

            options.addArguments(
                    "--disable-save-password-bubble",
                    "--disable-notifications",
                    "--disable-infobars",
                    "--disable-features=PasswordLeakDetection,PasswordManagerOnboarding"
            );

            options.setExperimentalOption("prefs", Map.of(
                    "credentials_enable_service", false,
                    "profile.password_manager_enabled", false,
                    "profile.password_manager_leak_detection", false
            ));

            // Setup ChromeDriver
            WebDriverManager.chromedriver().setup();
            driver = new ChromeDriver(options);

            // Logger
            logger = LogManager.getLogger(this.getClass());
            logger.info("Browser dibuka (Chrome, password breach OFF)");

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Gagal buka browser: " + e.getMessage());
        }
    }

    @AfterMethod
    public void tearDown() {
        if (driver != null) {
            driver.quit();
            logger.info("Browser ditutup");
        }
    }

    @AfterSuite
    public void flushReport() {
        if (extent != null) {
            extent.flush(); // 🔥 cuma sekali per suite
            System.out.println("ExtentReport flush selesai");
        }
    }
}
