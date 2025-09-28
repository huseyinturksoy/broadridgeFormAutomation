package com.broadridge.utils;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.broadridge.ExtentManager;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.devtools.DevTools;
import org.openqa.selenium.devtools.v134.network.Network;
import org.openqa.selenium.devtools.v134.network.model.Request;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.safari.SafariDriver;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

public class Driver {


    private static WebDriver driver;
    public static DevTools devTools;

    public static Request req;

    public static String formurl ;
    public static String formmethod;
    public static String formpayload;

    public static  ExtentReports extent;
    public static ExtentTest test;

    //sets the driver object
    public static WebDriver getDriver() throws IOException {
        if (driver == null){
            String browserType = ConfigurationReader.getProperty("browser");

            switch (browserType){

                case "chrome":
                    WebDriverManager.chromedriver().setup();
                    ChromeOptions options = new ChromeOptions();
                    options.addArguments("--headless");   // or just "--headless"
                    options.addArguments("--no-sandbox");
                    options.addArguments("--disable-dev-shm-usage");
                    options.addArguments("--disable-gpu");
                    options.addArguments("--remote-allow-origins=*");

                    // extend report configuration
                    Path tempDir = Files.createTempDirectory("chrome-user-data");
                    options.addArguments("user-data-dir=" + tempDir.toAbsolutePath().toString());
                    extent = ExtentManager.getInstance();
                    test = extent.createTest("SetUp Test");

                    ChromeDriver chromeDriver = new ChromeDriver(options);
                    driver = chromeDriver;


// DevTools başlat
                    devTools = chromeDriver.getDevTools();
                    devTools.createSession();
                    devTools.send(Network.enable(Optional.empty(), Optional.empty(), Optional.empty()));

                    // Listener ekle
                    devTools.addListener(Network.requestWillBeSent(), request -> {
                        req = request.getRequest();
                        //https://www.broadridge.com/api/form-processor
                        //https://www-dev.broadridge.com/api/form-processor
                        if (req.getUrl().equals("https://www-dev.broadridge.com/api/form-processor")) {
                            formurl = req.getUrl();
                            formpayload = req.getPostData().orElse("No Payload");
                            //System.out.println("form payload = " + formpayload);
                        }
                    });

                    //driver.manage().window().maximize();
                    driver.manage().window().setSize(new Dimension(1285,790));
                    driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
                    test.info("ChromeDriver started successfully");
                    break;
                case "safari":
                    WebDriverManager.safaridriver().setup();
                    driver = new SafariDriver();
                    driver.manage().window().maximize();
                    driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
                    break;
                case "firefox":
                    WebDriverManager.firefoxdriver().setup();
                    driver = new FirefoxDriver();
                    driver.manage().window().maximize();
                    driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
                    break;
                case "chrome-headless":
                    WebDriverManager.chromedriver().setup();
                    //driver = new ChromeDriver(new ChromeOptions().setHeadless(true));
                    driver.manage().window().maximize();
                    driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
                    break;
                case "edge":
                    WebDriverManager.edgedriver().setup();
                    /*EdgeOptions option = new EdgeOptions();
                    option.addArguments("--remote-allow-origins=*");
                    option.addArguments("--deny-permission-prompts");*/
                    driver = new EdgeDriver();
                    driver.manage().window().maximize();
                    driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);

            }
        }

        return driver;
    }

    //used for closing browser
    public static void closeDriver(){
        if (driver != null){
            driver.quit();
            driver = null;
        }
    }
}
