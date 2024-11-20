package steps;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.junit.Assert;
import utils.CommonMethods;
import utils.ConfigReader;

public class LoginSteps extends CommonMethods {

    @Given("user is able to access HRMS portal")
    public void user_is_able_to_access_hrms_portal() {
        openBrowserAndLaunchApplication();
    }

    @When("user attempts to login with an incorrect or invalid {string} or {string}")
    public void user_attempts_to_login_with_an_incorrect_or_invalid_or(String userName, String password) {
        sendText(userName, loginPage.userNameField);
        sendText(password, loginPage.passwordField);
    }

    @When("user clicks on login button")
    public void user_clicks_on_login_button() {
        click(loginPage.loginButton);
    }

    @Then("user sees clearly visible error message {string}")
    public void user_sees_clearly_visible_error_message(String errorMessage) {
        Assert.assertTrue(loginPage.errorMessage.isDisplayed());
        Assert.assertEquals(errorMessage,loginPage.errorMessage.getText());
    }

    @When("user enters valid credentials")
    public void user_enters_valid_credentials() {
        sendText(ConfigReader.read("userName"), loginPage.userNameField);
        sendText(ConfigReader.read("password"), loginPage.passwordField);
    }

    @Then("user is allowed to enter valid credentials")
    public void user_is_allowed_to_enter_valid_credentials() {
        Assert.assertTrue(loginPage.userNameField.isEnabled());
        Assert.assertTrue(loginPage.passwordField.isEnabled());
        Assert.assertTrue(loginPage.loginButton.isEnabled());
    }

    @Then("user is navigated to dashboard page")
    public void user_is_navigated_to_dashboard_page() {
        Assert.assertEquals("Welcome Admin",dashboardPage.welcomeText.getText());
    }

}
