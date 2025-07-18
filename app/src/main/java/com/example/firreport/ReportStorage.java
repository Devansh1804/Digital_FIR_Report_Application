package com.example.firreport;

import android.content.Context;
import android.content.SharedPreferences;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;

public class ReportStorage {
    private static final String PREF_NAME = "ReportsData";
    private static final String KEY_REPORTS = "reports";

    public static void saveReport(Context context, Report report) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        List<Report> reports = getReports(context);
        reports.add(report);

        JSONArray jsonArray = new JSONArray();
        for (Report r : reports) {
            jsonArray.put(r.toJson());
        }

        editor.putString(KEY_REPORTS, jsonArray.toString());
        editor.apply();
    }

    public static List<Report> getReports(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        String reportsJson = sharedPreferences.getString(KEY_REPORTS, "[]");

        List<Report> reports = new ArrayList<>();
        try {
            JSONArray jsonArray = new JSONArray(reportsJson);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                reports.add(Report.fromJson(jsonObject));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return reports;
    }
}
