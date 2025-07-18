package com.example.firreport;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import java.util.ArrayList;
import java.util.List;

public class SharedViewModel extends ViewModel {
    private final MutableLiveData<List<Report>> reports = new MutableLiveData<>(new ArrayList<>());

    public void addReport(String type, String description) {
        List<Report> currentReports = reports.getValue();
        if (currentReports != null) {
            currentReports.add(new Report(type, description));
            reports.setValue(currentReports);
        }
    }

    public LiveData<List<Report>> getReports() {
        return reports;
    }
}
