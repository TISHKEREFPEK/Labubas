package com.example.labubas;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class DetailedListActivity extends AppCompatActivity {

    RecyclerView rvActive, rvDone;
    TextView tvTitle;
    DatabaseHelper db;
    String type;

    ArrayList<RequestModel> listActive, listDone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detailed_list);

        db = new DatabaseHelper(this);
        rvActive = findViewById(R.id.rvActive);
        rvDone = findViewById(R.id.rvDone);
        tvTitle = findViewById(R.id.tvPageTitle);

        type = getIntent().getStringExtra("TYPE");

        if ("repair".equals(type)) {
            tvTitle.setText("Все заявки на РЕМОНТ");
        } else {
            tvTitle.setText("Все заявки на ПОКРАСКУ");
        }

        loadSplitLists();
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadSplitLists();
    }

    private void loadSplitLists() {
        listActive = new ArrayList<>();
        listDone = new ArrayList<>();

        List<Map<String, String>> rawData;

        if ("repair".equals(type)) {
            rawData = db.getRepairRequests();
        } else {
            rawData = db.getPaintingRequests();
        }

        for (Map<String, String> map : rawData) {
            try {
                int id = Integer.parseInt(map.get("id"));
                String status = map.get("status");
                String owner = map.get("ownerName");
                String phone = map.get("phoneNumber");
                String car = map.get("carModel");
                String date = map.get("date");

                String issueOrColor;
                String time = "";

                if (map.get("time") != null) {
                    time = map.get("time");
                }

                if ("repair".equals(type)) {
                    issueOrColor = map.get("issueDescription");
                } else {
                    issueOrColor = map.get("color");
                }

                RequestModel model = new RequestModel(id, owner, phone, car, issueOrColor, date, time, status, type);
                if ("выполнено".equalsIgnoreCase(status)) {
                    listDone.add(model);
                } else {
                    listActive.add(model);
                }
            } catch (Exception e) { e.printStackTrace(); }
        }

        setupRecycler(rvActive, listActive);
        setupRecycler(rvDone, listDone);
    }

    private void setupRecycler(RecyclerView rv, ArrayList<RequestModel> list) {
        RequestAdapter adapter = new RequestAdapter(this, list, request -> {

            Intent intent;
            if ("repair".equals(type)) {
                intent = new Intent(this, EditRequestActivity.class);
                intent.putExtra("ISSUE", request.getIssueOrColor());
                intent.putExtra("TIME", request.getTime());
            } else {
                intent = new Intent(this, EditPaintingActivity.class);
                intent.putExtra("COLOR", request.getIssueOrColor());
                intent.putExtra("TIME", request.getTime());
            }

            intent.putExtra("ID", request.getId());
            intent.putExtra("OWNER", request.getOwnerName());
            intent.putExtra("PHONE", request.getPhone());
            intent.putExtra("CAR", request.getCarModel());
            intent.putExtra("DATE", request.getDate());
            intent.putExtra("STATUS", request.getStatus());

            startActivity(intent);
        });

        rv.setLayoutManager(new LinearLayoutManager(this));
        rv.setAdapter(adapter);
    }
}