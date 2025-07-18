package com.example.firreport;

import org.json.JSONException;
import org.json.JSONObject;

public class Report {
    private String type;
    private String description;

    public Report(String type, String description) {
        this.type = type;
        this.description = description;
    }

    public String getType() {
        return type;
    }

    public String getDescription() {
        return description;
    }

    // Convert Report object to JSON String
    public JSONObject toJson() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("type", type);
            jsonObject.put("description", description);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject;
    }

    // Convert JSON String back to Report object
    public static Report fromJson(JSONObject jsonObject) {
        try {
            return new Report(jsonObject.getString("type"), jsonObject.getString("description"));
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }
}
