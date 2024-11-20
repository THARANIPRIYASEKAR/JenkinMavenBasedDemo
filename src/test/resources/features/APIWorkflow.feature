Feature: API Workflow

  Background:
    Given a JWT bearer token is generated

  @api @createEmp
  Scenario: Create Employee
    Given a request is prepared for creating the employee
    When a "POST" call is made to create the employee
    Then the status code for this request is validated
    Then the employee id "Employee.employee_id" is stored and values are validated

  @dynamic
  Scenario: Create employee using more dynamic json payload
    Given a create request is prepared using data "Asanth" , "Bilal" , "ms" , "F" , "1993-11-07" , "probation" , "Trainee"
    When a "POST" call is made to create the employee
    Then the status code for this request is validated
    Then the employee id "Employee.employee_id" is stored and values are validated

  @api @getEmp
  Scenario: Get Created Employee
    Given a request is prepared to retrieve the employee
    When a "GET" call is made to retrieve the employee
    Then the status code for this request is validated
    And the "employee.employee_id" retrieved should be same as the requested empid
    #Then the retrieved employee details are validated
    And the data in the response should be same as the expected values
      | emp_firstname | emp_lastname | emp_middle_name | emp_gender | emp_birthday | emp_status | emp_job_title |
      | Asanth        | Bilal        | ms              | Female     | 1993-11-07   | Probation  | Trainee       |

  @api @updateEmployee
  Scenario: Update Employee
    #Given a request is prepared for updating the employee
    Given a update request is prepared using data "Asanth" , "Bilal" , "ms" , "F" , "1993-11-07" , "Permanent" , "QA Engineer"
    When a "PUT" call is made to update the employee
    Then the status code for this request is validated
    #And the updated employee details are validated
    #And the data in the response should be same as the expected values
     # | emp_firstname | emp_lastname | emp_middle_name | emp_gender | emp_birthday | emp_status | emp_job_title |
     # | Asanth        | Bilal        | ms              | Female     | 1993-11-07   | permanent  | QA Engineer   |

  @api @getTheUpdatedEmployee
  Scenario: Get the updated employee
    Given a request is prepared to retrieve the employee
    When a "GET" call is made to retrieve the employee
    Then the status code for this request is validated
    And the "employee.employee_id" retrieved should be same as the requested empid
    And the data in the response should be same as the expected values
      | emp_firstname | emp_lastname | emp_middle_name | emp_gender | emp_birthday | emp_status | emp_job_title |
      | Asanth        | Bilal        | ms              | Female     | 1993-11-07   | permanent  | QA Engineer   |

  @api @getAllEmp
  Scenario: Get All Employee
    Given a request is prepared for retrieving all the employee
    When a "GET" call is made to retrieve all the employee
    Then the status code for this request is validated
    Then all the employee details are validated

  @api @getJobTitle
  Scenario: Get Job Title
    Given a request is prepared for retrieving job title
    When a "GET" call is made to retrieve job title
    Then the status code for this request is validated
    Then the job titles are validated

  @api @getEmploymentStatus
  Scenario: Get Employment Status
    Given a request is prepared for retrieving employment status
    When a "GET" call is made to retrieve employment status
    Then the status code for this request is validated
    Then the employment status are validated


