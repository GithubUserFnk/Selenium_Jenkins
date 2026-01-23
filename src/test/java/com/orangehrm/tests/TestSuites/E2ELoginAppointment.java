package com.orangehrm.tests.TestSuites;

import com.orangehrm.base.BaseTest;
import com.orangehrm.tests.Login.LoginTest;
import com.orangehrm.tests.Appointment.MakeAppointment;
import com.orangehrm.utils.ExcelUtils;
import org.testng.ITestContext;
import org.testng.annotations.Test;

public class E2ELoginAppointment extends BaseTest {

    @Test
    public void fullFlowLoginAndAppointment(ITestContext context) throws Exception {

        // ===== Inisialisasi helper =====
        LoginTest login = new LoginTest();
        login.setDriver(driver);

        MakeAppointment appointment = new MakeAppointment();
        appointment.setDriver(driver);
        // driver dari BaseTest sudah cukup

        // ===== Ambil data login dari Excel =====
        String loginPath = context.getCurrentXmlTest().getParameter("loginExcelPath");
        String loginSheet = context.getCurrentXmlTest().getParameter("loginSheet");
        Object[][] loginData = ExcelUtils.getExcelData(loginPath, loginSheet);

        // ===== Loop tiap user =====
        for (Object[] loginRow : loginData) {
            String username = (String) loginRow[0];
            String password = (String) loginRow[1];

            // ===== LOGIN =====
            login.doLogin(username, password);

            // ===== CREATE APPOINTMENT =====
            // Excel appointment hanya berisi Tanggal Visit + Komen
            appointment.createAppointmentPage(username, context);

            // ===== Optional: kembali ke homepage sebelum user berikutnya =====
            // driver.navigate().to("https://katalon-demo-cura.herokuapp.com/");
        }
    }
}
