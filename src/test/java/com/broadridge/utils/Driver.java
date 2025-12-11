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
import org.openqa.selenium.devtools.v140.network.*;
import org.openqa.selenium.devtools.v140.network.model.Request;
import org.openqa.selenium.devtools.v140.network.model.RequestId;
import org.openqa.selenium.devtools.v140.network.model.Response;
import org.openqa.selenium.devtools.v140.network.Network;
import org.openqa.selenium.devtools.v140.network.model.Response;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.safari.SafariDriver;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Duration;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

public class Driver {


    private static WebDriver driver;
    public static DevTools devTools;

    public static Request req;

    public static String formurl ;
    public static String formmethod;

    public static String csvContent;
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
                    //options.addArguments("--headless");   // or just "--headless"
                    options.addArguments("--no-sandbox");
                    options.addArguments("--disable-dev-shm-usage");
                    options.addArguments("--disable-gpu");
                    options.addArguments("--remote-allow-origins=*");
                    options.addArguments("--disable-blink-features=AutomationControlled"); //to disable robot control


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

// Network enable (CDP v140+ imza)
                    devTools.send(Network.enable(
                            Optional.empty(),
                            Optional.empty(),
                            Optional.empty(),
                            Optional.empty()
                    ));

// RequestId saklamak için map
                    Map<RequestId, String> targetRequestIds = new HashMap<>();

// Request listener
                    devTools.addListener(Network.requestWillBeSent(), request -> {
                        req = request.getRequest();

                        if (req.getUrl().contains("/api/form-processor")) {
                            formurl = req.getUrl();
                            formpayload = req.getPostData().orElse("No Payload");
                            System.out.println("📤 Form payload = " + formpayload);
                        }
                    });

// ResponseReceived → sadece requestId sakla
                    devTools.addListener(Network.responseReceived(), responseReceived -> {
                        Response res = responseReceived.getResponse();

                        if (res.getUrl().contains("/api/form-processor")) {
                            RequestId requestId = responseReceived.getRequestId();
                            targetRequestIds.put(requestId, res.getUrl());
                            System.out.println("📌 ResponseReceived → stored RequestId: " + requestId);
                        }
                    });

// LoadingFinished → body artık hazır, güvenli şekilde çek
                    devTools.addListener(Network.loadingFinished(), loadingFinished -> {
                        RequestId requestId = loadingFinished.getRequestId();

                        if (targetRequestIds.containsKey(requestId)) {
                            try {
                                // Body çek
                                Network.GetResponseBodyResponse body = devTools.send(Network.getResponseBody(requestId));

                                String responseBody = body.getBody();

                                // Base64 decode
                                if (body.getBase64Encoded()) {
                                    responseBody = new String(Base64.getDecoder().decode(responseBody), StandardCharsets.UTF_8);
                                }

                                System.out.println("✅ CSV content caught:");
                                System.out.println(responseBody);

                            } catch (Exception e) {
                                System.out.println("❌ Body fetch failed: " + e.getMessage());
                            }
                        }
                    });


                    //driver.manage().window().maximize();
                    driver.manage().window().setSize(new Dimension(1285,790));
                    driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
                    test.info("ChromeDriver started successfully");
                    break;
                case "safari":
                    WebDriverManager.safaridriver().setup();
                    driver = new SafariDriver();
                    driver.manage().window().maximize();
                    driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
                    break;
                case "firefox":
                    WebDriverManager.firefoxdriver().setup();
                    driver = new FirefoxDriver();
                    driver.manage().window().maximize();
                    driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
                    break;
                case "chrome-headless":
                    WebDriverManager.chromedriver().setup();
                    //driver = new ChromeDriver(new ChromeOptions().setHeadless(true));
                    driver.manage().window().maximize();
                    driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
                    break;
                case "edge":
                    WebDriverManager.edgedriver().setup();
                    /*EdgeOptions option = new EdgeOptions();
                    option.addArguments("--remote-allow-origins=*");
                    option.addArguments("--deny-permission-prompts");*/
                    driver = new EdgeDriver();
                    driver.manage().window().maximize();
                    driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));

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
