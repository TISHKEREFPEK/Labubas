package com.example.labubas;

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

public class PaintingActivity extends AppCompatActivity {

    EditText etOwner, etPhone, etCar, etColor, etDate, etTime;
    Button btnCreate;
    DatabaseHelper db;

    Calendar calendar = Calendar.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_painting);

        db = new DatabaseHelper(this);

        etOwner = findViewById(R.id.etOwnerName);
        etPhone = findViewById(R.id.etPhone);
        etCar = findViewById(R.id.etCarModel);
        etColor = findViewById(R.id.etColor);
        etDate = findViewById(R.id.etDate);
        etTime = findViewById(R.id.etTime);
        btnCreate = findViewById(R.id.btnCreate);

        etPhone.addTextChangedListener(new PhoneTextWatcher(etPhone));

        etDate.setOnClickListener(v -> {
            DatePickerDialog dialog = new DatePickerDialog(PaintingActivity.this,
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
            new TimePickerDialog(PaintingActivity.this,
                    (view, hourOfDay, minute) -> {
                        // Создаем копию для проверки
                        Calendar checkCalendar = (Calendar) calendar.clone();
                        checkCalendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                        checkCalendar.set(Calendar.MINUTE, minute);

                        // Проверяем, не прошло ли это время
                        if (checkCalendar.getTimeInMillis() < System.currentTimeMillis()) {
                            Toast.makeText(PaintingActivity.this, "Нельзя выбрать прошедшее время!", Toast.LENGTH_SHORT).show();
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

        btnCreate.setOnClickListener(v -> saveData());
    }

    private void updateDateLabel() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy", Locale.getDefault());
        etDate.setText(sdf.format(calendar.getTime()));
    }

    private void updateTimeLabel() {
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm", Locale.getDefault());
        etTime.setText(sdf.format(calendar.getTime()));
    }

    private void saveData() {
        String owner = etOwner.getText().toString();
        String phone = etPhone.getText().toString();
        String car = etCar.getText().toString();
        String color = etColor.getText().toString();
        String date = etDate.getText().toString();
        String time = etTime.getText().toString();

        if(owner.isEmpty() || car.isEmpty() || date.isEmpty()) {
            Toast.makeText(this, "Заполните основные поля!", Toast.LENGTH_SHORT).show();
            return;
        }

        Map<String, String> newRequest = new HashMap<>();
        newRequest.put("ownerName", owner);
        newRequest.put("phoneNumber", phone);
        newRequest.put("carModel", car);
        newRequest.put("color", color);
        newRequest.put("date", date);
        newRequest.put("time", time);

        long result = db.insertPaintingRequest(newRequest);

        if (result != -1) {
            Toast.makeText(this, "Заявка создана!", Toast.LENGTH_SHORT).show();
            finish();
        } else {
            Toast.makeText(this, "Ошибка сохранения", Toast.LENGTH_SHORT).show();
        }
    }
}