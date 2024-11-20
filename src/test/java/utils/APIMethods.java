package utils;

import org.json.JSONObject;

public class APIMethods {

    public static String createEmployeePayload(String emp_firstname,
                                               String emp_lastname,
                                               String emp_middle_name,
                                               String emp_gender,
                                               String emp_birthday,
                                               String emp_status,
                                               String emp_job_title) {
        JSONObject obj = new JSONObject();
        obj.put("emp_firstname", emp_firstname);
        obj.put("emp_lastname", emp_lastname);
        obj.put("emp_middle_name", emp_middle_name);
        obj.put("emp_gender", emp_gender);
        obj.put("emp_birthday", emp_birthday);
        obj.put("emp_status", emp_status);
        obj.put("emp_job_title", emp_job_title);
        return obj.toString();
    }

    public static String updateEmployeePayload(String employee_id, String emp_firstname,
                                        String emp_lastname,
                                        String emp_middle_name,
                                        String emp_gender,
                                        String emp_birthday,
                                        String emp_status,
                                        String emp_job_title) {
        JSONObject obj = new JSONObject();
        obj.put("employee_id", employee_id);
        obj.put("emp_firstname", emp_firstname);
        obj.put("emp_lastname", emp_lastname);
        obj.put("emp_middle_name", emp_middle_name);
        obj.put("emp_gender", emp_gender);
        obj.put("emp_birthday", emp_birthday);
        obj.put("emp_status", emp_status);
        obj.put("emp_job_title", emp_job_title);
        return obj.toString();
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
