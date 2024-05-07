package com.example.task_manager;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Calendar;

public class AddEditTaskActivity extends AppCompatActivity {
    private static final int REQUEST_CONTACT = 1;

    private EditText editTextTaskName, editTextTaskDescription;
    private Button buttonChooseDate, buttonChooseTime, buttonSaveTask, buttonChooseContact;
    private Spinner spinnerReminderOptions, spinnerCategory;
    private TaskDatabaseHelper dbHelper;
    private String selectedContactNumber = "";  // Almacena el número del contacto seleccionado

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit_task);

        editTextTaskName = findViewById(R.id.edit_text_task_name);
        editTextTaskDescription = findViewById(R.id.edit_text_task_description);
        buttonChooseDate = findViewById(R.id.button_choose_date);
        buttonChooseTime = findViewById(R.id.button_choose_time);
        buttonSaveTask = findViewById(R.id.button_save_task);
        buttonChooseContact = findViewById(R.id.button_choose_contact);
        spinnerReminderOptions = findViewById(R.id.spinner_reminder_options);
        spinnerCategory = findViewById(R.id.spinner_category);

        dbHelper = new TaskDatabaseHelper(this);

        setUpSpinners();
        setUpDateAndTimePickers();
        setupContactPicker();

        buttonSaveTask.setOnClickListener(v -> saveTask());
    }

    private void setUpSpinners() {
        ArrayAdapter<CharSequence> reminderAdapter = ArrayAdapter.createFromResource(this,
                R.array.reminder_options, android.R.layout.simple_spinner_item);
        reminderAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerReminderOptions.setAdapter(reminderAdapter);

        ArrayAdapter<CharSequence> categoryAdapter = ArrayAdapter.createFromResource(this,
                R.array.task_categories, android.R.layout.simple_spinner_item);
        categoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCategory.setAdapter(categoryAdapter);
    }

    private void setUpDateAndTimePickers() {
        buttonChooseDate.setOnClickListener(v -> {
            Calendar calendar = Calendar.getInstance();
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int day = calendar.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog datePickerDialog = new DatePickerDialog(this, (view, year1, monthOfYear, dayOfMonth) -> {
                buttonChooseDate.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year1);
            }, year, month, day);
            datePickerDialog.show();
        });

        buttonChooseTime.setOnClickListener(v -> {
            Calendar calendar = Calendar.getInstance();
            int hour = calendar.get(Calendar.HOUR_OF_DAY);
            int minute = calendar.get(Calendar.MINUTE);

            TimePickerDialog timePickerDialog = new TimePickerDialog(this, (view, hourOfDay, minute1) -> {
                buttonChooseTime.setText(hourOfDay + ":" + minute1);
            }, hour, minute, false);
            timePickerDialog.show();
        });
    }

    private void setupContactPicker() {
        buttonChooseContact.setOnClickListener(v -> {
            Intent contactPickerIntent = new Intent(Intent.ACTION_PICK);
            contactPickerIntent.setType(ContactsContract.CommonDataKinds.Phone.CONTENT_TYPE);
            startActivityForResult(contactPickerIntent, REQUEST_CONTACT);
        });
    }

    @SuppressLint("Range")
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CONTACT && resultCode == RESULT_OK) {
            Uri contactUri = data.getData();
            Cursor cursor = getContentResolver().query(contactUri, new String[]{ContactsContract.CommonDataKinds.Phone.NUMBER}, null, null, null);
            if (cursor != null && cursor.moveToFirst()) {
                selectedContactNumber = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                buttonChooseContact.setText(selectedContactNumber);
                cursor.close();
            }
        }
    }

    private void saveTask() {
        String name = editTextTaskName.getText().toString().trim();
        String description = editTextTaskDescription.getText().toString().trim();
        String date = buttonChooseDate.getText().toString();
        String time = buttonChooseTime.getText().toString();
        String reminder = spinnerReminderOptions.getSelectedItem().toString();
        String category = spinnerCategory.getSelectedItem().toString();

        if (name.isEmpty() || description.isEmpty() || date.equals("Seleccionar Fecha") || time.equals("Seleccionar Hora")) {
            Toast.makeText(this, "Por favor complete todos los campos", Toast.LENGTH_SHORT).show();
            return;
        }

        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("name", name);
        values.put("description", description);
        values.put("date", date);
        values.put("time", time);
        values.put("reminder", reminder);
        values.put("category", category);
        values.put("contact", selectedContactNumber);

        long newRowId = db.insert("tasks", null, values);
        if (newRowId == -1) {
            Toast.makeText(this, "Error al guardar la tarea", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Tarea guardada exitosamente", Toast.LENGTH_SHORT).show();
            finish();
        }
    }
}
