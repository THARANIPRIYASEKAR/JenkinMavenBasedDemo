package APISteps;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.junit.Assert;
import utils.APIConstants;
import utils.APIMethods;
import utils.CommonMethods;
import utils.ConfigReader;

import java.util.List;
import java.util.Map;
import java.util.Set;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasKey;

public class APITestSteps {

    String baseURI = RestAssured.baseURI = ConfigReader.read("apiBaseURI");
    RequestSpecification request;
    Response response;
    public static String employee_id;
    public static String token;
    public static String apiCall;


    @Given("a JWT bearer token is generated")

    public void a_jwt_bearer_token_is_generated() {

        request = given().
                header(APIConstants.HEADER_CONTENT_TYPE_KEY, APIConstants.HEADER_CONTENT_TYPE_VALUE).
                body("{\n" +
                        "     \"email\": \"tharani4@gmail.com\",\n" +
                        "     \"password\": \"tharani4\"\n" +
                        "}");

        response = request.when().post(APIConstants.GENERATE_TOKEN);
        token = "Bearer " + response.jsonPath().getString("token");

    }

    @Given("a request is prepared for creating the employee")
    public void a_request_is_prepared_for_creating_the_employee() {
        request = given().
                header("Content-Type", "application/json").
                header("Authorization", token).
                body("{\n" +
                        "  \"emp_firstname\": \"Asanth\",\n" +
                        "  \"emp_lastname\": \"Bilal\",\n" +
                        "  \"emp_middle_name\": \"ms\",\n" +
                        "  \"emp_gender\": \"M\",\n" +
                        "  \"emp_birthday\": \"1993-11-07\",\n" +
                        "  \"emp_status\": \"Permanent\",\n" +
                        "  \"emp_job_title\": \"Trainee\"\n" +
                        "}");
    }

    @Given("a create request is prepared using data {string} , {string} , {string} , {string} , {string} , {string} , {string}")
    public void a_create_request_is_prepared_using_data(String firstname, String lastname, String middlename,
                                                        String gender, String birthday, String status,
                                                        String jobtitle) {
        request = given().
                header(APIConstants.HEADER_CONTENT_TYPE_KEY, APIConstants.HEADER_CONTENT_TYPE_VALUE).
                header(APIConstants.HEADER_AUTHORIZATION_KEY, token).
                body(APIMethods.createEmployeePayload(firstname, lastname, middlename, gender, birthday, status, jobtitle));
    }

    @When("a {string} call is made to create the employee")
    public void a_call_is_made_to_create_the_employee(String requestType) {
        response = request.when().post(APIConstants.CREATE_EMPLOYEE);
        apiCall = requestType;
    }

    @Then("the status code for this request is validated")
    public void the_status_code_for_this_request_is_validated() {
        response.then().assertThat().statusCode(CommonMethods.apiStatusCode(apiCall));
    }

    @Then("the employee id {string} is stored and values are validated")
    public void the_employee_id_is_stored_and_values_are_validated(String empPath) {
        if (response.jsonPath().getString(empPath) != null) {
            employee_id = response.jsonPath().getString(empPath);
        }
        response.then().assertThat().body("Message", equalTo("Employee Created"));
        response.then().assertThat().body("Employee.emp_firstname", equalTo("Asanth"));
        response.then().assertThat().body("Employee.emp_middle_name", equalTo("ms"));
        response.then().assertThat().body("Employee.emp_lastname", equalTo("Bilal"));
        response.then().assertThat().body("Employee.emp_birthday", equalTo("1993-11-07"));
        response.then().assertThat().body("Employee.emp_job_title", equalTo("Trainee"));
        response.then().assertThat().body("Employee.emp_status", equalTo("probation"));
    }

    @Given("a request is prepared to retrieve the employee")
    public void a_request_is_prepared_to_retrieve_the_employee() {
        request = given().
                header(APIConstants.HEADER_CONTENT_TYPE_KEY, APIConstants.HEADER_CONTENT_TYPE_VALUE).
                header(APIConstants.HEADER_AUTHORIZATION_KEY, token).
                queryParam("employee_id", employee_id);
    }

    @When("a {string} call is made to retrieve the employee")
    public void a_call_is_made_to_retrieve_the_employee(String requestType) {
        response = request.when().get(APIConstants.GET_ONE_EMPLOYEE);
        this.apiCall = requestType;
    }

    @Then("the {string} retrieved should be same as the requested empid")
    public void the_retrieved_should_be_same_as_the_requested_empid(String empId) {
        String tempEmpId = response.jsonPath().getString(empId);
        Assert.assertEquals(employee_id, tempEmpId);
    }

    @Then("the retrieved employee details are validated")
    public void the_retrieved_employee_details_are_validated() {
        response.then().assertThat().body("employee.employee_id", equalTo(employee_id));
        response.then().assertThat().body("employee.emp_firstname", equalTo("Asanth"));
        response.then().assertThat().body("employee.emp_middle_name", equalTo("ms"));
        response.then().assertThat().body("employee.emp_lastname", equalTo("Bilal"));
        response.then().assertThat().body("employee.emp_birthday", equalTo("1993-11-07"));
        response.then().assertThat().body("employee.emp_gender", equalTo("Male"));
        response.then().assertThat().body("employee.emp_job_title", equalTo("Trainee"));
        response.then().assertThat().body("employee.emp_status", equalTo("permanent"));
    }

    @Then("the data in the response should be same as the expected values")
    public void the_data_in_the_response_should_be_same_as_the_expected_values
            (io.cucumber.datatable.DataTable dataTable) {
        // it is coming from feature file
        List<Map<String, String>> expectedData = dataTable.asMaps();
        //it is coming from the response body which we need whole object
        Map<String, String> actualData = response.jsonPath().get("employee");
        //this is the time to compare  the values
        for (Map<String, String> employeeData : expectedData
        ) {
            //to get all the unique keys
            Set<String> keys = employeeData.keySet();
            //but we need one key at a time to compare with value
            for (String key : keys
            ) {
                //to return the value against the key
                String expectedValue = employeeData.get(key);
                String actualValue = actualData.get(key);
                Assert.assertEquals(expectedValue, actualValue);
            }
        }
    }

    @Given("a update request is prepared using data {string} , {string} , {string} , {string} , {string} , {string} , {string}")
    public void a_update_request_is_prepared_using_data
            (String firstname, String lastname, String middlename,
             String gender, String birthday, String status,
             String jobtitle) {
        request = given().
                header(APIConstants.HEADER_CONTENT_TYPE_KEY,
                        APIConstants.HEADER_CONTENT_TYPE_VALUE).
                header(APIConstants.HEADER_AUTHORIZATION_KEY,token).
                body(APIMethods.updateEmployeePayload
                        (employee_id,firstname,lastname,middlename,
                                gender,birthday,status,jobtitle));

    }

    @Given("a request is prepared for retrieving all the employee")
    public void a_request_is_prepared_for_retrieving_all_the_employee() {
        request = given().
                header("Content-Type", "application/json").
                header("Authorization", token);
    }

    @When("a {string} call is made to retrieve all the employee")
    public void a_call_is_made_to_retrieve_all_the_employee(String requestType) {
        response = request.when().get(ConfigReader.read("apiGetAllEmployee"));
        this.apiCall = requestType;
    }

    @Then("all the employee details are validated")
    public void all_the_employee_details_are_validated() {
        response.then().assertThat().body("$", hasKey("Total Employees"));
    }

    @Given("a request is prepared for retrieving job title")
    public void a_request_is_prepared_for_retrieving_job_title() {
        request = given().
                header("Content-Type", "application/json").
                header("Authorization", token);
    }

    @When("a {string} call is made to retrieve job title")
    public void a_call_is_made_to_retrieve_job_title(String requestType) {
        response = request.when().get(ConfigReader.read("apiGetJobTitle"));
        this.apiCall = requestType;
    }

    @Then("the job titles are validated")
    public void the_job_titles_are_validated() {
        response.then().assertThat().body("$", hasKey("Jobs"));
    }

    @Given("a request is prepared for retrieving employment status")
    public void a_request_is_prepared_for_retrieving_employment_status() {
        request = given().
                header("Content-Type", "application/json").
                header("Authorization", token);
    }

    @When("a {string} call is made to retrieve employment status")
    public void a_call_is_made_to_retrieve_employment_status(String requestType) {
        response = request.when().get(ConfigReader.read("apiGetEmploymentStatus"));
        this.apiCall = requestType;
    }

    @Then("the employment status are validated")
    public void the_employment_status_are_validated() {
        response.then().assertThat().body("$", hasKey("Employeement Status"));
    }

    @Given("a request is prepared for updating the employee")
    public void a_request_is_prepared_for_updating_the_employee() {
        request = given().
                header("Content-Type", "application/json").
                header("Authorization", token).
                body("{\n" +
                        "  \"employee_id\": \"" + employee_id + "\",\n" +
                        "  \"emp_firstname\": \"Asanth\",\n" +
                        "  \"emp_lastname\": \"Bilal\",\n" +
                        "  \"emp_middle_name\": \"ms\",\n" +
                        "  \"emp_gender\": \"M\",\n" +
                        "  \"emp_birthday\": \"1993-11-07\",\n" +
                        "  \"emp_status\": \"Permanent\",\n" +
                        "  \"emp_job_title\": \"QA Engineer\"\n" +
                        "}");
    }

    @When("a {string} call is made to update the employee")
    public void a_call_is_made_to_update_the_employee(String requestType) {
        response = request.when().put(ConfigReader.read("apiUpdateEmployee"));
        this.apiCall = requestType;
    }

    @Then("the updated employee details are validated")
    public void the_updated_employee_details_are_validated() {
        response.then().assertThat().body("Message", equalTo("Employee record Updated"));
        response.then().assertThat().body("Employee.emp_firstname", equalTo("Asanth"));
        response.then().assertThat().body("Employee.emp_middle_name", equalTo("ms"));
        response.then().assertThat().body("Employee.emp_lastname", equalTo("Bilal"));
        response.then().assertThat().body("Employee.emp_birthday", equalTo("1993-11-07"));
        response.then().assertThat().body("Employee.emp_gender", equalTo("Male"));
        response.then().assertThat().body("Employee.emp_job_title", equalTo("QA Engineer"));
        response.then().assertThat().body("Employee.emp_status", equalTo("Permanent"));
    }


}
