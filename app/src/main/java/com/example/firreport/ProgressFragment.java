package com.example.firreport;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class ProgressFragment extends Fragment {
    private RecyclerView recyclerView;
    private ReportAdapter reportAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_progress, container, false);
        recyclerView = view.findViewById(R.id.reportsRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        loadReports();
        return view;
    }

    private void loadReports() {
        List<Report> reports = ReportStorage.getReports(getContext());
        reportAdapter = new ReportAdapter(reports);
        recyclerView.setAdapter(reportAdapter);
    }
}
