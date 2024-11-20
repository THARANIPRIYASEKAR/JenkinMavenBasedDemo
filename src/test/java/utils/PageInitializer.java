package utils;

import pages.AddEmployeePage;
import pages.DashboardPage;
import pages.EmployeeListPage;
import pages.LoginPage;

//this class initialize the page objects and their web elements
public class PageInitializer {

    public static LoginPage loginPage;
    public static DashboardPage dashboardPage;
    public static AddEmployeePage addEmployeePage;
    public static EmployeeListPage employeeListPage;

    public static void initializePageObjects(){

        loginPage = new LoginPage();
        dashboardPage = new DashboardPage();
        addEmployeePage = new AddEmployeePage();
        employeeListPage= new EmployeeListPage();
    }
}
