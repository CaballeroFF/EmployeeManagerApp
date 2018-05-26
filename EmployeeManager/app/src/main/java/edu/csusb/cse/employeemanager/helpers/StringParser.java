package edu.csusb.cse.employeemanager.helpers;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class StringParser {

    private static final String TAG = "DEBUG";

    public StringParser(){

    }

    public String formattedEmployee(String employee){
        StringBuilder stringBuilder = new StringBuilder();
        String[] list = employee.split("\n");

        for (String s: list) {
            String[] lines = s.split(": ");
            stringBuilder.append(lines[1]).append(",");
        }
        Log.d(TAG, "formattedEmployee: " + stringBuilder);
        stringBuilder.deleteCharAt(stringBuilder.length() - 1);
        return stringBuilder.toString();
    }

    public List<String> getEmployeesFromJSON(String json){
        List<String> items = new ArrayList<>();
        try {
            JSONObject jsonObject = new JSONObject(json);
            Iterator<String> iterator = jsonObject.keys();
            while (iterator.hasNext()){
                String key = iterator.next();
                items.add(jsonObject.get(key).toString());
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return items;
    }

    public String employeeToJSON(String employee){

        String[] employeeArray = employee.split(",");

        JSONObject jsonEmployee = new JSONObject();
        try {
            jsonEmployee.put("name",employeeArray[0]);
            jsonEmployee.put("id",employeeArray[1]);
            jsonEmployee.put("department",employeeArray[2]);
            jsonEmployee.put("title",employeeArray[3]);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonEmployee.toString();
    }
}
