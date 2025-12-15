package com.broadridge;


import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.broadridge.utils.BrowserUtils;
import com.broadridge.utils.Driver;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.testng.annotations.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static com.broadridge.utils.BrowserUtils.v2ContactUsFiller;
import static com.broadridge.utils.BrowserUtils.v2DownloadFiller;
import static com.broadridge.utils.Driver.extent;

@Listeners(com.broadridge.FailureLogger.class)
public class Tests {



    @BeforeTest
    public void setUp() throws IOException {
        System.out.println("-------------------------------TEST STARTED--------------------------------");

    }

    @AfterClass
    public void tearDown() {

        Driver.closeDriver();
        Driver.test.info("ChromeDriver closed");

        extent.flush(); // Write the report to HTML
    }


    @Test(dataProvider = "broadridgePages", dataProviderClass = TestData.class, enabled = false, priority = 2)
    public void FormTester(String url, String pageComparisonURL) throws IOException, InterruptedException {

        // setting the all form flags
        int hasHeaderContact = 0;
        int hasV1ContactUsForm = 0;
        int hasV2ContactUsForm = 0;
        int hasV2downloadForm = 0;

        System.out.println("page URL = " + url);

        // navigating to the page
        Driver.getDriver().get(url);
        Thread.sleep(3000);
        String currentUrl = Driver.getDriver().getCurrentUrl();

        //setting the report for each test
        Driver.test = extent.createTest(url);

        System.out.println("current URL = " + currentUrl);
        Driver.test.info("current page URL " + currentUrl);

        try {
            WebElement headerContact = Driver.getDriver().findElement(By.xpath("//button[@data-modal='contact-modal']"));
            System.out.println("header ContactUs button found = " + headerContact.isDisplayed());
            Driver.test.info("Header Contact Us Button found");
            hasHeaderContact = 1;
        } catch (Exception e) {
            System.out.println("Header ContactUs button not found");
            Driver.test.info("Header Contact Us Button not found");
        }


        try {
            WebElement v1ContactUsForm = Driver.getDriver().findElement(By.xpath("//section[@id='form']"));
            System.out.println("v1 ContactUs form found = " + v1ContactUsForm.isDisplayed());
            Driver.test.info("v1 Contact Us Form found");
            hasV1ContactUsForm = 1;
        } catch (Exception e) {
            System.out.println("v1 ContactUs form not found");
            Driver.test.info("v1 Contact Us Form not found");
        }

        try {
            WebElement v2ContactUsForm = Driver.getDriver().findElement(By.xpath("(//section[@class='section contact-us'])[2]"));
            System.out.println("v2 ContactUs form found = " + v2ContactUsForm.isDisplayed());
            Driver.test.info("v2 Contact Us Form found");
            hasV2ContactUsForm = 1;
        } catch (Exception e) {
            System.out.println("v2 ContactUs form not found");
            Driver.test.info("v2 Contact Us Form not found");
        }

        try {
            WebElement v2downloadForm = Driver.getDriver().findElement(By.xpath("//section[@class='section download-form']"));
            System.out.println("v2 download form found = " + v2downloadForm.isDisplayed());
            Driver.test.info("v2 Download Form found");
            hasV2downloadForm = 1;
        } catch (Exception e) {
            System.out.println("v2 download form not found");
            Driver.test.info("v2 Download Form not found");
        }

        //filling the forms according to page

        if (hasHeaderContact == 1 && hasV2ContactUsForm == 1){
            BrowserUtils.headerContactUsFiller(pageComparisonURL, 1);
            v2ContactUsFiller(pageComparisonURL, 1);
        } else if (hasHeaderContact == 1 && hasV1ContactUsForm == 1){
            BrowserUtils.headerContactUsFiller(pageComparisonURL, 1);
            BrowserUtils.v1ContactUsFiller(pageComparisonURL);
        } else if (hasHeaderContact == 1 && hasV2downloadForm == 1){
            BrowserUtils.headerContactUsFiller(pageComparisonURL, 1);
            BrowserUtils.v2DownloadFiller(pageComparisonURL, 1);
        } else if (hasHeaderContact == 1 && hasV2ContactUsForm == 0 && hasV2downloadForm == 0 && hasV1ContactUsForm == 0){
            BrowserUtils.headerContactUsFiller(pageComparisonURL, 1);
        }else {
            System.out.println("--------!!! NO FORM IS FOUND ON THE PAGE !!!--------");
        }


    }

    @Test()
    public void formTest() throws IOException, InterruptedException {
        int lastrow = BrowserUtils.lastrow();
        int counter = 0;
        String pageURL = "";
        //creating a unique test number
        int uniqueNumber = (int) (System.currentTimeMillis() % 100_000)+1;
        System.out.println(uniqueNumber);


        while (counter <= lastrow) {
            pageURL = BrowserUtils.readExcel(counter, 0);
            Driver.getDriver().get(pageURL);
            Thread.sleep(1000);
            counter++;

            //clicking the cookie button
            try{
                WebElement cookieButton = Driver.getDriver().findElement(By.xpath("//button[.='Accept all cookies']"));
                cookieButton.click();
                Thread.sleep(1000);
            }catch (Exception e){
                System.out.println("no cookie button");
            }

            //checking the header contact us button and if it is existed filling the form
            try{
                WebElement headerContactUsButton = Driver.getDriver().findElement(By.xpath("//button[@data-modal='contact-modal']"));
                Driver.test.info("Test Number = " + uniqueNumber);
                System.out.println("header ContactUs button found = " + headerContactUsButton.isDisplayed());
                Driver.test.info("Header Contact Us Button found");
                headerContactUsButton.click();
                Thread.sleep(1000);
                BrowserUtils.headerContactUsFiller(pageURL, uniqueNumber);

            }catch (Exception e){
                System.out.println("header contact us form can not be filled");
                Driver.test.info("Header Contact Us Form can not be filled");
            }

            //checking the bottom contact us form and if it is existed filling the form
            try{
                WebElement v2ContactUsForm = Driver.getDriver().findElement(By.xpath("(//section[@class='section contact-us'])[2]"));
                System.out.println("v2 ContactUs form found on the bottom = " + v2ContactUsForm.isDisplayed());
                v2ContactUsFiller(pageURL, uniqueNumber);

            }catch (Exception e){
                System.out.println("V2 ContactUs form NOT found on the bottom");
                Driver.test.info("V2 Contact Us Form can not be filled");

            }

            //checking the download form and if it is existed filling the form
            try{
                WebElement v2downloadForm = Driver.getDriver().findElement(By.xpath("//section[@class='section download-form']"));
                System.out.println("v2 download form found = " + v2downloadForm.isDisplayed());
                v2DownloadFiller(pageURL, uniqueNumber);

            }catch (Exception e){
                System.out.println("V2 Download form NOT found on the bottom");
                Driver.test.info("Download Form can not be filled");

            }


        }
    }












}
