package com.example.labubas;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.view.MotionEvent;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    RecyclerView rvRepair, rvPainting;
    DatabaseHelper db;
    ArrayList<RequestModel> repairList, paintingList;
    RequestAdapter repairAdapter, paintingAdapter;
    Button btnRepair, btnPaint, btnStats;
    TextView tvClock;

    Handler handler = new Handler(Looper.getMainLooper());
    Runnable clockRunnable;
    Runnable resetRunnable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        db = new DatabaseHelper(this);

        btnRepair = findViewById(R.id.btnCreateRepair);
        btnPaint = findViewById(R.id.btnCreatePainting);
        btnStats = findViewById(R.id.btnStatistics);
        tvClock = findViewById(R.id.tvRealTimeClock);

        btnRepair.setOnClickListener(v -> startActivity(new Intent(MainActivity.this, CreateRequestActivity.class)));
        btnPaint.setOnClickListener(v -> startActivity(new Intent(MainActivity.this, PaintingActivity.class)));
        btnStats.setOnClickListener(v -> startActivity(new Intent(MainActivity.this, StatisticsActivity.class)));

        rvRepair = findViewById(R.id.rvRepairRequests);
        rvPainting = findViewById(R.id.rvPaintingRequests);

        resetRunnable = () -> {
            vibratePhone();

            showResetDialog();
        };

        tvClock.setOnTouchListener((v, event) -> {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    handler.postDelayed(resetRunnable, 5000);
                    return true;
                case MotionEvent.ACTION_UP:
                case MotionEvent.ACTION_CANCEL:
                    handler.removeCallbacks(resetRunnable);
                    return true;
            }
            return false;
        });

        loadData();
        startClock();
    }

    private void vibratePhone() {
        Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        if (v != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                v.vibrate(VibrationEffect.createOneShot(200, VibrationEffect.DEFAULT_AMPLITUDE));
            } else {
                v.vibrate(200);
            }
        }
    }

    private void showResetDialog() {
        new AlertDialog.Builder(this)
                .setTitle("⛔ ОПАСНАЯ ЗОНА ⛔")
                .setMessage("Вы активировали секретное меню.\n\nВы действительно хотите удалить ВСЕ данные из приложения? Это действие нельзя отменить.")
                .setPositiveButton("УДАЛИТЬ ВСЁ", (dialog, which) -> {
                    db.deleteAllData();
                    loadData();
                    Toast.makeText(this, "База данных полностью очищена", Toast.LENGTH_LONG).show();
                })
                .setNegativeButton("Отмена", null)
                .show();
    }

    private void startClock() {
        clockRunnable = new Runnable() {
            @Override
            public void run() {
                SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss\ndd.MM.yyyy", Locale.getDefault());
                tvClock.setText(sdf.format(new Date()));
                handler.postDelayed(this, 1000);
            }
        };
        handler.post(clockRunnable);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler.removeCallbacks(clockRunnable);
        handler.removeCallbacks(resetRunnable);
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadData();
    }

    private void loadData() {
        repairList = new ArrayList<>();
        paintingList = new ArrayList<>();

        // 1. РЕМОНТ
        List<Map<String, String>> repairsFromDb = db.getRepairRequests();
        int repairCount = 0;
        for (int i = repairsFromDb.size() - 1; i >= 0; i--) {
            if (repairCount >= 2) break;
            Map<String, String> map = repairsFromDb.get(i);
            try {
                int id = Integer.parseInt(map.get("id"));
                String owner = map.get("ownerName");
                String phone = map.get("phoneNumber");
                String car = map.get("carModel");
                String issue = map.get("issueDescription");
                String date = map.get("date");
                String time = map.get("time");
                String status = map.get("status");
                repairList.add(new RequestModel(id, owner, phone, car, issue, date, time, status, "repair"));
                repairCount++;
            } catch (Exception e) { e.printStackTrace(); }
        }

        List<Map<String, String>> paintingFromDb = db.getPaintingRequests();
        int paintCount = 0;
        for (int i = paintingFromDb.size() - 1; i >= 0; i--) {
            if (paintCount >= 2) break;
            Map<String, String> map = paintingFromDb.get(i);
            try {
                int id = Integer.parseInt(map.get("id"));
                String owner = map.get("ownerName");
                String phone = map.get("phoneNumber");
                String car = map.get("carModel");
                String color = map.get("color");
                String date = map.get("date");
                String time = map.get("time");
                String status = map.get("status");
                paintingList.add(new RequestModel(id, owner, phone, car, color, date, time, status, "painting"));
                paintCount++;
            } catch (Exception e) { e.printStackTrace(); }
        }

        repairAdapter = new RequestAdapter(this, repairList, null);
        rvRepair.setLayoutManager(new LinearLayoutManager(this));
        rvRepair.setAdapter(repairAdapter);

        paintingAdapter = new RequestAdapter(this, paintingList, null);
        rvPainting.setLayoutManager(new LinearLayoutManager(this));
        rvPainting.setAdapter(paintingAdapter);
    }
}