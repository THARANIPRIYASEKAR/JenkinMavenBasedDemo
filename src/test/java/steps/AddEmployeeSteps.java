package steps;

import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.junit.Assert;
import utils.CommonMethods;
import utils.DbReader;
import utils.ExcelReader;

import java.util.List;
import java.util.Map;

public class AddEmployeeSteps extends CommonMethods {

    public String firstName;
    public String middleName;
    public String lastName;
    public String employeeId;

    @When("user clicks on PIM option")
    public void user_clicks_on_pim_option() {
        click(dashboardPage.pimOption);
    }

    @When("user clicks on add employee option")
    public void user_clicks_on_add_employee_option() {
        click(addEmployeePage.addEmployeeOption);
    }

    @Then("user is navigated to add employee page")
    public void user_is_navigated_to_add_employee_page() {
        Assert.assertEquals("Add Employee", addEmployeePage.panelHeader.getText());
    }

    @Then("user sees unique employee ID populated in the employee Id field")
    public void user_sees_unique_employee_id_populated_in_the_employee_id_field() {
        Assert.assertNotNull(addEmployeePage.employeeId.getText());
    }

    @When("user enters firstname middlename and lastname in the name fields")
    public void user_enters_firstname_middlename_and_lastname_in_the_name_fields() {

        List<Map<String, String>> employeeNames = ExcelReader.readExcel();
        int count = 1;

        for (Map<String, String> employee : employeeNames) {
            sendText(employee.get("firstName"), addEmployeePage.firstNameField);
            firstName = employee.get("firstName");
            sendText(employee.get("middleName"), addEmployeePage.middleNameField);
            middleName = employee.get("middleName");
            sendText(employee.get("lastName"), addEmployeePage.lastNameField);
            lastName = employee.get("lastName");
            employeeId = addEmployeePage.employeeId.getAttribute("value");
            if (count < employeeNames.size()) {
                user_clicks_on_save_button();
                employee_added_successfully();
                user_clicks_on_add_employee_option();
                employee_details_are_verified_from_backend();
            }
            count++;
        }

    }

    @When("user clicks on save button")
    public void user_clicks_on_save_button() {
        click(addEmployeePage.saveButton);
    }

    @Then("employee added successfully")
    public void employee_added_successfully() {
        String employeeFullName = firstName + " " + middleName + " " + lastName;
        Assert.assertEquals(employeeFullName, employeeListPage.employeeFullName.getText());
    }

    @Then("employee details are verified from backend")
    public void employee_details_are_verified_from_backend() {
        String query = "Select emp_firstname,emp_middle_name,emp_lastname from hs_hr_employees where employee_id = " + employeeId;
        List<Map<String, String>> dataFromDb = DbReader.fetch(query);
        String actualFN = dataFromDb.get(0).get("emp_firstname");
        String actualMN = dataFromDb.get(0).get("emp_middle_name");
        String actualLN = dataFromDb.get(0).get("emp_lastname");
        Assert.assertEquals(firstName, actualFN);
        Assert.assertEquals(middleName, actualMN);
        Assert.assertEquals(lastName, actualLN);
    }

    @When("user enters unique employee Id")
    public void user_enters_unique_employee_id() {
        sendText(uniqueId(), addEmployeePage.employeeId);
    }

    @When("user enters incomplete {string} {string} {string}")
    public void user_enters_incomplete(String firstName, String middleName, String lastName) {
        sendText(firstName, addEmployeePage.firstNameField);
        sendText(middleName, addEmployeePage.middleNameField);
        sendText(lastName, addEmployeePage.lastNameField);

        this.firstName = firstName;
        this.middleName = middleName;
        this.lastName = lastName;
    }

    @Then("user sees {string} on the incomplete field")
    public void user_sees_on_the_incomplete_field(String errorMessage) {
        String incompleteField="";
        if (firstName.isBlank()) { incompleteField = "firstName"; }
        else if (lastName.isBlank()) { incompleteField = "lastName"; }
        Assert.assertEquals(errorMessage, addEmployeePage.addEmployeeErrorMessage(incompleteField));
    }

    @When("user enters existing employee ID {string}")
    public void user_enters_existing_employee_id(String empId) {
        sendText(empId, addEmployeePage.employeeId);
    }

    @Then("user sees error message {string} on entering invalid employee ID")
    public void user_sees_error_message_on_entering_invalid_employee_ID(String errorMessage) {
        Assert.assertTrue(addEmployeePage.existingEmpIdErrorMsg.getText().contains(errorMessage));
    }
}
