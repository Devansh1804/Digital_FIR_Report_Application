package com.example.firreport;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

public class AccountFragment extends Fragment {

    private TextView totalReportsText, inReviewText, resolvedText, lastReportDateText;
    private boolean isMenuOpen = false;
    private MediaPlayer sirenPlayer;

    public AccountFragment() {}

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_account, container, false);

        totalReportsText = view.findViewById(R.id.total_reports_count);
        inReviewText = view.findViewById(R.id.in_review_count);
        resolvedText = view.findViewById(R.id.approved_count);
        lastReportDateText = view.findViewById(R.id.last_report_date);

        loadReportData();

        Button sosButton = view.findViewById(R.id.sos_button);
        sosButton.setOnClickListener(v -> new AlertDialog.Builder(requireContext())
                .setTitle("Alert")
                .setMessage("Message has been sent to higher authority")
                .setPositiveButton("Call", (dialog, which) -> dialog.dismiss())
                .show());

        Button logoutButton = view.findViewById(R.id.logout_button);
        logoutButton.setOnClickListener(v -> {
            SharedPreferences.Editor editor = requireActivity().getSharedPreferences("LoginPrefs", Context.MODE_PRIVATE).edit();
            editor.putBoolean("is_logged_in", false);
            editor.apply();

            Intent intent = new Intent(requireActivity(), LoginActivity.class);
            startActivity(intent);
            requireActivity().finish();
        });

        ImageButton menuButton = view.findViewById(R.id.menu_button);
        Button buttonLocation = view.findViewById(R.id.button_northeast);
        Button buttonSiren = view.findViewById(R.id.button_west);

        Animation showAnim = AnimationUtils.loadAnimation(getContext(), R.anim.show);
        Animation hideAnim = AnimationUtils.loadAnimation(getContext(), R.anim.hide);

        menuButton.setOnClickListener(v -> {
            if (isMenuOpen) {
                buttonLocation.startAnimation(hideAnim);
                buttonSiren.startAnimation(hideAnim);
                buttonLocation.setVisibility(View.GONE);
                buttonSiren.setVisibility(View.GONE);
            } else {
                buttonLocation.setVisibility(View.VISIBLE);
                buttonSiren.setVisibility(View.VISIBLE);
                buttonLocation.startAnimation(showAnim);
                buttonSiren.startAnimation(showAnim);
            }
            isMenuOpen = !isMenuOpen;
        });

        // Siren Button
        buttonSiren.setOnClickListener(v -> {
            sirenPlayer = MediaPlayer.create(requireContext(), R.raw.siren);
            sirenPlayer.setLooping(true);
            sirenPlayer.start();

            new AlertDialog.Builder(requireContext())
                    .setTitle("Siren Started")
                    .setMessage("Press stop to stop the siren.")
                    .setPositiveButton("Stop Siren", (dialog, which) -> {
                        if (sirenPlayer != null && sirenPlayer.isPlaying()) {
                            sirenPlayer.stop();
                            sirenPlayer.release();
                            sirenPlayer = null;
                        }
                        dialog.dismiss();
                    })
                    .setCancelable(false)
                    .show();
        });

        // Location Button
        buttonLocation.setOnClickListener(v -> {
            Uri gmmIntentUri = Uri.parse("geo:0,0?q=Current+Location");
            Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
            mapIntent.setPackage("com.google.android.apps.maps");
            if (mapIntent.resolveActivity(requireActivity().getPackageManager()) != null) {
                startActivity(mapIntent);
            } else {
                new AlertDialog.Builder(requireContext())
                        .setTitle("Error")
                        .setMessage("Google Maps not found.")
                        .setPositiveButton("OK", (dialog, which) -> dialog.dismiss())
                        .show();
            }
        });

        return view;
    }

    private void loadReportData() {
        SharedPreferences sharedPref = requireActivity().getSharedPreferences("ReportPrefs", Context.MODE_PRIVATE);
        int totalReports = sharedPref.getInt("report_count", 0);
        String lastDate = sharedPref.getString("last_report_date", "N/A");

        totalReportsText.setText(String.valueOf(totalReports));
        inReviewText.setText(String.valueOf(totalReports));
        resolvedText.setText("2");
        lastReportDateText.setText(lastDate);
    }
}
