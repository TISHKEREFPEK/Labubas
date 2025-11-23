package com.example.labubas;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import java.util.List;
import java.util.Map;

public class StatisticsActivity extends AppCompatActivity {

    TextView tvRepair, tvPaint;
    Button btnOpenRepair, btnOpenPaint;
    DatabaseHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistics);

        db = new DatabaseHelper(this);
        tvRepair = findViewById(R.id.tvRepairStats);
        tvPaint = findViewById(R.id.tvPaintingStats);
        btnOpenRepair = findViewById(R.id.btnOpenRepairList);
        btnOpenPaint = findViewById(R.id.btnOpenPaintingList);

        btnOpenRepair.setOnClickListener(v -> {
            Intent intent = new Intent(StatisticsActivity.this, DetailedListActivity.class);
            intent.putExtra("TYPE", "repair");
            startActivity(intent);
        });

        btnOpenPaint.setOnClickListener(v -> {
            Intent intent = new Intent(StatisticsActivity.this, DetailedListActivity.class);
            intent.putExtra("TYPE", "painting");
            startActivity(intent);
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        calculateStats();
    }

    private void calculateStats() {
        List<Map<String, String>> repairs = db.getRepairRequests();
        int repairActive = 0, repairDone = 0;
        for (Map<String, String> m : repairs) {
            if ("выполнено".equals(m.get("status"))) repairDone++;
            else repairActive++;
        }
        tvRepair.setText("В работе: " + repairActive + "\nВыполнено: " + repairDone);

        List<Map<String, String>> paints = db.getPaintingRequests();
        int paintActive = 0, paintDone = 0;
        for (Map<String, String> m : paints) {
            if ("выполнено".equals(m.get("status"))) paintDone++;
            else paintActive++;
        }
        tvPaint.setText("В работе: " + paintActive + "\nВыполнено: " + paintDone);
    }
}