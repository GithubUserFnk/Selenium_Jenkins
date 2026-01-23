package com.orangehrm.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;
import org.openqa.selenium.support.ui.Select;

public class MakeAppointmentPage {
    private WebDriver driver;

    private By facility = By.xpath("//select[@id='combo_facility']");
    private By checkBox = By.xpath("//input[@id='chk_hospotal_readmission']");
    private By radioMedicaId = By.xpath("//input[@id='radio_program_medicaid']");
    private By tanggalVisit = By.id("txt_visit_date");
    private By comment = By.id("txt_comment");
    private By buttonSubmit = By.id("btn-book-appointment");
    private By confirmAppointment = By.xpath("//div[@class='col-xs-12 text-center']/h2");
    private By buttonHomepage = By.xpath("//a[@class='btn btn-default']");
    private By makeAppointmentButton = By.id("btn-make-appointment");

    public MakeAppointmentPage(WebDriver driver) {
        this.driver = driver;
    }

    public void selectFacility() {
        WebElement dropdownElement = driver.findElement(facility);
        Select select = new Select(dropdownElement);
        select.selectByVisibleText("Hongkong CURA Healthcare Center");
        driver.findElement(checkBox).click();
    }

    public void selectHealtcareProgram() {
        driver.findElement(radioMedicaId).click();
    }

    // universal setter untuk Excel
    public void setField(String fieldName, String value) {
        switch (fieldName.toLowerCase()) {
            case "tanggal visit":
                driver.findElement(tanggalVisit).clear();
                driver.findElement(tanggalVisit).sendKeys(value);
                driver.findElement(comment).click(); // pindah focus
                break;

            case "komen":
                driver.findElement(comment).clear();
                driver.findElement(comment).sendKeys(value);
                break;

            default:
                System.out.println("Field " + fieldName + " tidak ada di Page Object");
        }
    }

    public void submitAppointment() {
        driver.findElement(buttonSubmit).click();
    }

    public boolean isBooked() {
        try {
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
            wait.until(ExpectedConditions.visibilityOfElementLocated(confirmAppointment));
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public void gotoHomepage() {
        driver.findElement(buttonHomepage).click();
    }

    public void createAppointment() {
        driver.findElement(makeAppointmentButton).click();
    }

}
