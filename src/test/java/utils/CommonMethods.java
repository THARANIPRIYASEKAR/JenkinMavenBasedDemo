package utils;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.safari.SafariDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.Date;

public class CommonMethods extends PageInitializer {

    public static WebDriver driver;

    public void openBrowserAndLaunchApplication() {

        switch (ConfigReader.read("browser")) {

            case "Chrome":
                ChromeOptions options = new ChromeOptions();
                options.addArguments("--headless=new");
                driver = new ChromeDriver(options);
                break;
            case "FireFox":
                driver = new FirefoxDriver();
                break;
            case "Edge":
                driver = new EdgeDriver();
                break;
            case "Safari":
                driver = new SafariDriver();
                break;
            default:
                throw new RuntimeException("Invalid Browser Name");
        }

        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(Constants.IMPLICIT_WAIT));
        driver.get(ConfigReader.read("url"));

        initializePageObjects();
    }

    public void closeBrowser() {
        if (driver != null) {
            driver.quit();
        }
    }

    public void sendText(String text, WebElement element) {
        element.clear();
        element.sendKeys(text);
    }

    public void click(WebElement element) {
        waitForElementToBeClickable(element);
        element.click();
    }

    public void waitForElementToBeClickable(WebElement element) {
        getWait().until(ExpectedConditions.elementToBeClickable(element));
    }

    public WebDriverWait getWait() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(Constants.EXPLICIT_WAIT));
        return wait;
    }

    public byte[] takeScreenshot(String fileName) {

        TakesScreenshot ts = (TakesScreenshot) driver;
        byte[] picByte = ts.getScreenshotAs(OutputType.BYTES);
        File sourceFile = ts.getScreenshotAs(OutputType.FILE);

        try {
            FileUtils.copyFile(sourceFile, new File(Constants.SCREENSHOT_FILE_PATH + fileName + " " +
                    getTimeStamp("yyyy-MM-dd-HH-mm-ss") + ".png"));
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
        return picByte;
    }

    public String getTimeStamp(String pattern) {
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);
        return sdf.format(date);
    }

    public String uniqueId() {
        int min = 100000;
        int max = 1000000;
        int unique_no = (int) (Math.random() * (max - min) + min);
        return String.valueOf(unique_no);

        //alternative method
        //String generateUUIDNo = String.format("%010d", new BigInteger(UUID.randomUUID().toString().replace("-", ""), 16));
        //String unique_no = generateUUIDNo.substring(generateUUIDNo.length() - 10);
        //return unique_no
    }

    public static int apiStatusCode(String requestType) {

        int statusCode = switch (requestType) {
            case "POST", "PATCH" -> 201;
            case "GET", "PUT", "DELETE" -> 200;
            default -> 0;
        };
        return statusCode;
    }

}

