package com.orangehrm.tests.Login;

import com.orangehrm.base.BaseTest;
import com.orangehrm.pages.LoginPage;
import com.orangehrm.utils.ExcelUtils;
import com.orangehrm.utils.Screenshoot;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.MediaEntityBuilder;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.ITestContext;
import org.testng.annotations.Test;
import org.testng.annotations.DataProvider;

import java.time.Duration;

public class LoginTest extends BaseTest {

    @DataProvider(name = "loginData")
    public Object[][] getLoginData(ITestContext context) throws Exception {

        // ambil parameter dari testng.xml
        String excelPath = context
                .getCurrentXmlTest()
                .getParameter("excelPath");

        String sheetName = context
                .getCurrentXmlTest()
                .getParameter("sheetName");

        return ExcelUtils.getExcelData(excelPath, sheetName);
    }

    @Test(enabled = false, dataProvider = "loginData")
    public void loginTest(String username, String password) throws InterruptedException {
        doLogin(username, password);
    }

    public void setDriver(WebDriver driver) {
        this.driver = driver;
    }

    public void doLogin(String username, String password) throws InterruptedException {

        driver.get("https://katalon-demo-cura.herokuapp.com/");
        driver.manage().window().maximize();

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("btn-make-appointment")));

        LoginPage login = new LoginPage(driver);
        ExtentTest testNode = extent.createTest("Login Test - " + username);

        // ===== OPEN & INPUT =====
        login.openLoginForm();
        login.fillCredential(username, password);

        // ===== SCREENSHOT INPUTAN =====
        String inputSS = Screenshoot.capture(driver, "Input_" + username);
        testNode.info("Input credentials screenshot",
                MediaEntityBuilder.createScreenCaptureFromPath(inputSS).build());

        // ===== SUBMIT LOGIN =====
        login.submitLogin();

        Thread.sleep(2000);

        // ===== CEK STATUS LOGIN =====
        if (login.isLoginFail()) {
            String failSS = Screenshoot.capture(driver, "Failed_" + username);
            testNode.fail("Login failed for " + username,
                    MediaEntityBuilder.createScreenCaptureFromPath(failSS).build());

        } else if (login.isLoginSuccess()) {
            String successSS = Screenshoot.capture(driver, "Success_" + username);
            testNode.pass("Login successful for " + username,
                    MediaEntityBuilder.createScreenCaptureFromPath(successSS).build());

        } else {
            testNode.warning("Login status unclear for " + username);
        }
    }
}
