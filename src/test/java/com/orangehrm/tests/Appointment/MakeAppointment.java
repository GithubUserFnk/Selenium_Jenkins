package com.orangehrm.tests.Appointment;

import com.orangehrm.base.BaseTest;
import com.orangehrm.pages.MakeAppointmentPage;
import com.orangehrm.utils.Screenshoot;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.MediaEntityBuilder;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.openqa.selenium.WebDriver;
import org.testng.ITestContext;

import java.io.FileInputStream;
import java.util.LinkedHashMap;
import java.util.Map;

public class MakeAppointment extends BaseTest {

    /**
     * Method reusable untuk membuat appointment
     * @param username - dipakai untuk ExtentReport / logging
     * @param context - untuk ambil Excel file (Tanggal Visit + Komen)
     */
    public void createAppointmentPage(String username, ITestContext context) throws Exception {

        MakeAppointmentPage ma = new MakeAppointmentPage(driver);

        // set default facility & program
        ma.selectFacility();          // default: Hongkong CURA Healthcare Center
        ma.selectHealtcareProgram();  // default: Medicaid

        // ===== Ambil Excel data (Tanggal Visit + Komen) =====
        String formPath = context.getCurrentXmlTest().getParameter("formExcelPath");
        String sheetName = context.getCurrentXmlTest().getParameter("formSheet");

        FileInputStream fis = new FileInputStream(formPath);
        Workbook workbook = new XSSFWorkbook(fis);
        Sheet sheet = workbook.getSheet(sheetName);

        // ambil header Excel
        Row headerRow = sheet.getRow(0);
        int colCount = headerRow.getPhysicalNumberOfCells();
        String[] headers = new String[colCount];
        for (int i = 0; i < colCount; i++) headers[i] = headerRow.getCell(i).toString();

        int rowCount = sheet.getPhysicalNumberOfRows();
        for (int i = 1; i < rowCount; i++) {
            Row row = sheet.getRow(i);

            // buat map <header, value>
            Map<String, String> rowData = new LinkedHashMap<>();
            for (int j = 0; j < colCount; j++) {
                rowData.put(headers[j], row.getCell(j).toString());
            }

            // ===== Buat test node untuk ExtentReport =====
            ExtentTest testNode = extent.createTest("Appointment - " + username + " - Row " + i);

            // loop field Excel (Tanggal Visit + Komen)
            for (Map.Entry<String, String> entry : rowData.entrySet()) {
                ma.setField(entry.getKey(), entry.getValue());
            }

            // ===== Screenshot input =====
            String ssInput = Screenshoot.capture(driver, "Input_Appointment_" + username + "_Row" + i);
            testNode.info("Input Appointment screenshot",
                    MediaEntityBuilder.createScreenCaptureFromPath(ssInput).build());

            // ===== Submit =====
            ma.submitAppointment();

            // ===== Cek status =====
            if (ma.isBooked()) {
                String ssBooked = Screenshoot.capture(driver, "BookingConfirmation_" + username + "_Row" + i);
                testNode.info("Booking Confirmation screenshot",
                        MediaEntityBuilder.createScreenCaptureFromPath(ssBooked).build());
            } else {
                testNode.warning("Booking gagal untuk row " + i);
            }

            // ===== Kembali ke homepage & siap buat row berikutnya =====
            ma.gotoHomepage();
            ma.createAppointment(); // method di page object untuk reset form
            Thread.sleep(1000);
        }

        workbook.close();
        fis.close();
    }

    public void setDriver(WebDriver driver) {
        this.driver = driver;
    }
}
