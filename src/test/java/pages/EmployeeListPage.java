package pages;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import utils.CommonMethods;

public class EmployeeListPage extends CommonMethods {

    @FindBy(xpath = "//div[@id='profile-pic']/h1")
    public WebElement employeeFullName;

    public EmployeeListPage(){
        PageFactory.initElements(driver,this);
    }
}
