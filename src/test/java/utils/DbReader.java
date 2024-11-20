package utils;

import java.sql.*;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class DbReader {

    public static List<Map<String,String>> fetch(String query){

        String dbURL=ConfigReader.read("dbURL");
        String userName=ConfigReader.read("dbUserName");
        String password=ConfigReader.read("dbPassword");
        List<Map<String, String>> TblData = new ArrayList<>();
        // Establish the connection between java program and the database
        try( Connection connection = DriverManager.getConnection(dbURL, userName, password);
             // takes your queries to the database and bring the results back to Java program
             Statement statement = connection.createStatement();) {

            ResultSet rows = statement.executeQuery(query);
            // Extracting all the info like column names from the statement
            ResultSetMetaData headerInfo = rows.getMetaData();
            // step 1 extract a row
            while (rows.next()) {
                // create a map to store the info from that row into map
                Map<String, String> rowMap = new LinkedHashMap<>();
                for (int i = 1; i <= headerInfo.getColumnCount(); i++) {
                    // extract all the column info and store it inside the map
                    String key = headerInfo.getColumnName(i);
                    String value = rows.getString(i);
                    rowMap.put(key, value);
                }

                TblData.add(rowMap);
            }

        }catch (SQLException e){
            e.printStackTrace();
        }

        return TblData;
    }

}