package com.broadridge.utils;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.testng.Assert;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class BrowserUtils {

    public static String generateEmailTimeStamp() {
        LocalDateTime now = LocalDateTime.now();
        String timestamp = now.format(DateTimeFormatter.ofPattern("yyyy-MM-dd_HH_mm_ss"));
        String emailTimeStamp = "automated_TESTTEST_"+timestamp+"@broadridge.com";
        return emailTimeStamp;
    }

    public static void click(WebElement element) throws IOException {
        JavascriptExecutor js = (JavascriptExecutor)Driver.getDriver();
        Actions action = new Actions(Driver.getDriver());

        int numAttempts = 5;
        int attempts = 0;

        while (attempts < numAttempts) {
            try {
                element.click();
                //System.out.println("Element clicked successfully");
                break; // If the click is successful, exit the loop
            } catch (WebDriverException e) {
                js.executeScript("arguments[0].click();", element);
                break;

            }catch (Exception e){
                System.out.println("Attempt #" + (attempts + 1) + ": Click failed");
            }
            attempts++;
        }

        if (attempts == numAttempts) {
            throw new RuntimeException("Element could not be clicked after " + attempts + " attempts");
        }
    }

    public static String getJsonField(String payload, String fieldName) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode root = mapper.readTree(payload);

        String jsonData = root.path(fieldName).asText();

        System.out.println(fieldName+" = " + jsonData);


        return jsonData;
    }

    public static void headerContactUsFiller(String pageComparisonURL) throws IOException, InterruptedException {
        WebElement headerContactUsButton = Driver.getDriver().findElement(By.xpath("//button[@data-modal='contact-modal']"));
        click(headerContactUsButton);
        Thread.sleep(2000);

        WebElement headerFirstName = Driver.getDriver().findElement(By.xpath("(//input[@placeholder='First name'])[1]"));
        headerFirstName.sendKeys("First_TESTTEST");

        WebElement headerLastName = Driver.getDriver().findElement(By.xpath("(//input[@placeholder='Last name'])[1]"));
        headerLastName.sendKeys("Last_TESTTEST");

        WebElement headerEmail = Driver.getDriver().findElement(By.xpath("(//input[@placeholder='john@email.com'])[1]"));
        headerEmail.sendKeys(generateEmailTimeStamp());

        WebElement headerPhone = Driver.getDriver().findElement(By.xpath("(//input[@placeholder='+1 _ _ _ - _ _ _ - _ _ _ _'])[1]"));
        headerPhone.sendKeys("999-999-9999");

        WebElement headerJobTitle = Driver.getDriver().findElement(By.xpath("(//input[@id='job_title'])[1]"));
        headerJobTitle.sendKeys("Job_TESTTEST");

        WebElement headerCompanyName = Driver.getDriver().findElement(By.xpath("(//input[@placeholder='Your company name'])[1]"));
        headerCompanyName.sendKeys("Company_TESTTEST");

        WebElement headerCountrySelectorButton = Driver.getDriver().findElement(By.xpath("(//button[@class='dropdown-trigger'])[1]"));
        click(headerCountrySelectorButton);
        Thread.sleep(1000);

        WebElement headerUnitedStatesOption = Driver.getDriver().findElement(By.xpath("(//li[@data-value='United States'])[1]"));
        click(headerUnitedStatesOption);

        WebElement headerMessage = Driver.getDriver().findElement(By.xpath("(//textarea[@id='comment'])[1]"));
        headerMessage.sendKeys("This is an automated test submission.  Please ignore.");

        WebElement headerSubmitButton = Driver.getDriver().findElement(By.xpath("(//button[@type='submit'])[1]"));
        click(headerSubmitButton);
        Thread.sleep(3000);

        Driver.test.info("............Header Contact Us Form Filled...................");
        System.out.println("................Header Contact Us Form Filled...................");

        String formMessage = Driver.getDriver().findElement(By.xpath("//div[@class='contact-us__form-grid']")).getText();
        System.out.println("form message = " + formMessage);
        Driver.test.info("formMessage = " + formMessage);

        String formpayload = Driver.formpayload;
        System.out.println("formpayload for validation = " + formpayload);
        Driver.test.info("formpayload for validation = " + formpayload);

        String formSubmissionId = getJsonField(formpayload, "formSubmissionId");
        String fullURL = getJsonField(formpayload, "fullURL");
        Driver.test.info("form submission id = " + formSubmissionId );
        Driver.test.info("fullURL = " + fullURL );

        System.out.println("formSubmissionId = " + formSubmissionId);
        System.out.println("fullURL = " + fullURL);

        Assert.assertTrue(formSubmissionId != null);
        Assert.assertEquals(fullURL, pageComparisonURL);


    }

    public static void v1ContactUsFiller(String pageComparisonURL) throws IOException, InterruptedException {

    }


    public static void v2ContactUsFiller(String pageComparisonURL) throws IOException, InterruptedException {
        Driver.getDriver().navigate().refresh();
        Thread.sleep(3000);

        WebElement firstName = Driver.getDriver().findElement(By.xpath("(//input[@placeholder='First name'])[2]"));
        firstName.sendKeys("First_TESTTEST");

        WebElement lastName = Driver.getDriver().findElement(By.xpath("(//input[@placeholder='Last name'])[2]"));
        lastName.sendKeys("Last_TESTTEST");

        WebElement email = Driver.getDriver().findElement(By.xpath("(//input[@placeholder='john@email.com'])[2]"));
        email.sendKeys(generateEmailTimeStamp());

        WebElement phone = Driver.getDriver().findElement(By.xpath("(//input[@placeholder='+1 _ _ _ - _ _ _ - _ _ _ _'])[2]"));
        phone.sendKeys("999-999-9999");

        WebElement jobTitle = Driver.getDriver().findElement(By.xpath("(//input[@id='job_title'])[2]"));
        jobTitle.sendKeys("Job_TESTTEST");

        WebElement companyName = Driver.getDriver().findElement(By.xpath("(//input[@placeholder='Your company name'])[2]"));
        companyName.sendKeys("Company_TESTTEST");

        WebElement countrySelectorButton = Driver.getDriver().findElement(By.xpath("(//button[@class='dropdown-trigger'])[2]"));
        click(countrySelectorButton);
        Thread.sleep(1000);

        WebElement unitedStatesOption = Driver.getDriver().findElement(By.xpath("(//li[@data-value='United States'])[2]"));
        click(unitedStatesOption);

        WebElement message = Driver.getDriver().findElement(By.xpath("(//textarea[@id='comment'])[2]"));
        message.sendKeys("This is an automated test submission.  Please ignore.");

        WebElement submitButton = Driver.getDriver().findElement(By.xpath("(//button[@type='submit'])[2]"));
        click(submitButton);
        Thread.sleep(3000);

        Driver.test.info("............V2 Contact Us Form Filled...................");
        System.out.println("................V2 Contact Us Form Filled...................");

        String formMessage = Driver.getDriver().findElement(By.xpath("//div[@class='contact-us__form-grid']")).getText();
        System.out.println("form message = " + formMessage);
        Driver.test.info("form Message = " + formMessage);

        String formpayload = Driver.formpayload;
        System.out.println("formpayload for validation = " + formpayload);
        Driver.test.info("formpayload for validation = " + formpayload);

        String formSubmissionId = getJsonField(formpayload, "formSubmissionId");
        String fullURL = getJsonField(formpayload, "fullURL");
        Driver.test.info("formSubmissionId = " + formSubmissionId);
        Driver.test.info("fullURL = " + fullURL);


        System.out.println("formSubmissionId = " + formSubmissionId);
        System.out.println("fullURL = " + fullURL);

        Assert.assertTrue(formSubmissionId != null);
        Assert.assertEquals(fullURL, pageComparisonURL);



    }


    public static void  v2DownloadFiller(String pageComparisonURL) throws IOException, InterruptedException {

    }





}
