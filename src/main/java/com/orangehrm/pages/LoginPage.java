package com.orangehrm.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class LoginPage {
    private WebDriver driver;

    // Locators
    private By makeAppointmentButton = By.xpath("//a[@id='btn-make-appointment']");
    private By usernameField = By.xpath("//input[@id='txt-username']");
    private By passwordField = By.xpath("//input[@id='txt-password']");
    private By loginButton = By.xpath("//button[@id='btn-login']");
    private By banner = By.xpath("//div[@class='col-sm-12 text-center']/h2");

    // Constructor
    public LoginPage(WebDriver driver) {
        this.driver = driver;
    }

    // Actions / Methods
    public void clickAppointment() {
        driver.findElement(makeAppointmentButton).click();
    }

    public void enterUsername(String username) {
        driver.findElement(usernameField).sendKeys(username);
    }

    public void enterPassword(String password) {
        driver.findElement(passwordField).sendKeys(password);
    }

    public void clickLogin() {
        driver.findElement(loginButton).click();
    }

    public boolean isBannerPresent() {
        return driver.findElement(banner).isDisplayed();
    }

    // Optional: method gabungan
    public void loginAs(String username, String password) {
        clickAppointment();
        enterUsername(username);
        enterPassword(password);
        clickLogin();
    }
}
