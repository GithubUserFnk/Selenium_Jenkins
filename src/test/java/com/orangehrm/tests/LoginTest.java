package com.orangehrm.tests;

import com.orangehrm.base.*;
import com.orangehrm.pages.*;

import org.testng.Assert;
import org.testng.annotations.*;
import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;

public class LoginTest extends BaseTest {

    @Test
    public void loginPage() {
        driver.get("https://katalon-demo-cura.herokuapp.com/");
        driver.manage().window().maximize();
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(20));
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//a[@id='btn-make-appointment']")));

        LoginPage login = new LoginPage(driver);
        login.loginAs("John Doe", "ThisIsNotAPassword");
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[@class='col-sm-12 text-center']/h2")));
        captureScreenshot("Dashboard");
        Assert.assertTrue(login.isBannerPresent());
        test.pass("Login Sukses");
    }

}
