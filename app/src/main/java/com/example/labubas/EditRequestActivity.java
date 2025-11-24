package com.example.labubas;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class EditRequestActivity extends AppCompatActivity {

    EditText etOwner, etPhone, etCar, etIssue, etDate, etTime;
    Button btnSave, btnStatusChange, btnDelete;
    DatabaseHelper db;
    int requestId;
    String currentStatus;

    Calendar calendar = Calendar.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_request);

        db = new DatabaseHelper(this);

        etOwner = findViewById(R.id.etOwnerName);
        etPhone = findViewById(R.id.etPhone);
        etCar = findViewById(R.id.etCarModel);
        etIssue = findViewById(R.id.etIssue);
        etDate = findViewById(R.id.etDate);
        etTime = findViewById(R.id.etTime);
        btnSave = findViewById(R.id.btnSave);
        btnStatusChange = findViewById(R.id.btnStatusChange);
        btnDelete = findViewById(R.id.btnDelete);

        etPhone.addTextChangedListener(new PhoneTextWatcher(etPhone));

        requestId = getIntent().getIntExtra("ID", -1);
        currentStatus = getIntent().getStringExtra("STATUS");

        etOwner.setText(getIntent().getStringExtra("OWNER"));
        etPhone.setText(getIntent().getStringExtra("PHONE"));
        etCar.setText(getIntent().getStringExtra("CAR"));
        etIssue.setText(getIntent().getStringExtra("ISSUE"));

        String dateFromIntent = getIntent().getStringExtra("DATE");
        etDate.setText(dateFromIntent);
        syncCalendarWithDateField(dateFromIntent);

        etTime.setText(getIntent().getStringExtra("TIME"));

        etDate.setOnClickListener(v -> {
            syncCalendarWithDateField(etDate.getText().toString());

            DatePickerDialog dialog = new DatePickerDialog(this,
                    (view, year, month, dayOfMonth) -> {
                        calendar.set(Calendar.YEAR, year);
                        calendar.set(Calendar.MONTH, month);
                        calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                        updateDateLabel();
                    },
                    calendar.get(Calendar.YEAR),
                    calendar.get(Calendar.MONTH),
                    calendar.get(Calendar.DAY_OF_MONTH));

            dialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
            dialog.show();
        });

        etTime.setOnClickListener(v -> {
            syncCalendarWithDateField(etDate.getText().toString());

            new TimePickerDialog(this,
                    (view, hourOfDay, minute) -> {
                        Calendar checkCalendar = (Calendar) calendar.clone();
                        checkCalendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                        checkCalendar.set(Calendar.MINUTE, minute);

                        if (checkCalendar.getTimeInMillis() < System.currentTimeMillis()) {
                            Toast.makeText(EditRequestActivity.this, "Нельзя выбрать прошедшее время!", Toast.LENGTH_SHORT).show();
                        } else {
                            calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                            calendar.set(Calendar.MINUTE, minute);
                            updateTimeLabel();
                        }
                    },
                    calendar.get(Calendar.HOUR_OF_DAY),
                    calendar.get(Calendar.MINUTE),
                    true).show();
        });

        setupStatusButton();

        btnSave.setOnClickListener(v -> updateData());

        btnStatusChange.setOnClickListener(v -> {
            boolean isCompleted = currentStatus != null && currentStatus.equalsIgnoreCase("выполнено");
            if (isCompleted) {
                db.updateRepairStatus(requestId, "в работе");
                Toast.makeText(this, "Ремонт возвращен в работу!", Toast.LENGTH_SHORT).show();
            } else {
                db.updateRepairStatus(requestId, "выполнено");
                Toast.makeText(this, "Ремонт выполнен!", Toast.LENGTH_SHORT).show();
            }
            finish();
        });

        btnDelete.setOnClickListener(v -> {
            new AlertDialog.Builder(this)
                    .setTitle("Удаление")
                    .setMessage("Вы точно хотите удалить эту заявку?")
                    .setPositiveButton("Да, удалить", (dialog, which) -> {
                        db.deleteRepairRequest(requestId);
                        Toast.makeText(this, "Заявка удалена", Toast.LENGTH_SHORT).show();
                        finish();
                    })
                    .setNegativeButton("Отмена", null)
                    .show();
        });
    }

    private void syncCalendarWithDateField(String dateStr) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy", Locale.getDefault());
            calendar.setTime(sdf.parse(dateStr));
        } catch (Exception e) {
            calendar.setTimeInMillis(System.currentTimeMillis());
        }
    }

    private void updateDateLabel() {
        String myFormat = "dd.MM.yyyy";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.getDefault());
        etDate.setText(sdf.format(calendar.getTime()));
    }

    private void updateTimeLabel() {
        String myFormat = "HH:mm";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.getDefault());
        etTime.setText(sdf.format(calendar.getTime()));
    }

    private void setupStatusButton() {
        if (currentStatus == null) currentStatus = "в работе";
        if (currentStatus.equalsIgnoreCase("выполнено")) {
            btnStatusChange.setText("ВОЗОБНОВИТЬ РАБОТУ");
            btnStatusChange.setBackgroundTintList(getResources().getColorStateList(android.R.color.holo_orange_dark));
        } else {
            btnStatusChange.setText("ЗАВЕРШИТЬ ЗАЯВКУ");
            btnStatusChange.setBackgroundTintList(getResources().getColorStateList(android.R.color.holo_green_dark));
        }
    }

    private void updateData() {
        String phone = etPhone.getText().toString();

        if (phone.length() < 18) {
            Toast.makeText(this, "Номер введен не полностью!", Toast.LENGTH_SHORT).show();
            return;
        }

        Map<String, String> updatedRequest = new HashMap<>();
        updatedRequest.put("ownerName", etOwner.getText().toString());
        updatedRequest.put("phoneNumber", etPhone.getText().toString());
        updatedRequest.put("carModel", etCar.getText().toString());
        updatedRequest.put("issueDescription", etIssue.getText().toString());
        updatedRequest.put("date", etDate.getText().toString());
        updatedRequest.put("time", etTime.getText().toString());


        db.updateRepairRequest(requestId, updatedRequest);
        Toast.makeText(this, "Данные обновлены", Toast.LENGTH_SHORT).show();
        finish();
    }
}