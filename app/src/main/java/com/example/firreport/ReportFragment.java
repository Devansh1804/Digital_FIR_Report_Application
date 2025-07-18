package com.example.firreport;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class ReportFragment extends Fragment {

    private EditText reportTypeEditText;
    private EditText descriptionEditText;
    private Button submitButton;

    public ReportFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_report, container, false);

        reportTypeEditText = view.findViewById(R.id.report_type_input);
        descriptionEditText = view.findViewById(R.id.description_input);
        submitButton = view.findViewById(R.id.submit_report_button);

        submitButton.setOnClickListener(v -> submitReport());

        return view;
    }

    private void submitReport() {
        String reportType = reportTypeEditText.getText().toString().trim();
        String description = descriptionEditText.getText().toString().trim();

        if (TextUtils.isEmpty(reportType) || TextUtils.isEmpty(description)) {
            Toast.makeText(getContext(), "Please fill in all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        // Save report to ReportStorage
        Report report = new Report(reportType, description);
        ReportStorage.saveReport(requireContext(), report);

        // Save report count and date to SharedPreferences for AccountFragment
        SharedPreferences sharedPref = requireActivity().getSharedPreferences("ReportPrefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();

        int currentCount = sharedPref.getInt("report_count", 0);
        editor.putInt("report_count", currentCount + 1);

        String currentDate = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
        editor.putString("last_report_date", currentDate);

        editor.apply();

        Toast.makeText(getContext(), "Report submitted successfully", Toast.LENGTH_SHORT).show();

        // Clear fields
        reportTypeEditText.setText("");
        descriptionEditText.setText("");
    }
}
