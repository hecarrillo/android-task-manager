package com.example.task_manager;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class TaskDatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "taskmanager.db";
    private static final int DATABASE_VERSION = 2;  // Asegúrate de que la versión está actualizada para forzar el onUpgrade si es necesario
    private static final String TABLE_TASKS = "tasks";

    public TaskDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_TASKS_TABLE = "CREATE TABLE " + TABLE_TASKS + "("
                + "id INTEGER PRIMARY KEY AUTOINCREMENT,"
                + "name TEXT,"
                + "description TEXT,"
                + "date TEXT,"
                + "time TEXT,"
                + "reminder TEXT,"
                + "category TEXT,"
                + "contact TEXT,"
                + "done INTEGER DEFAULT 0)";
        db.execSQL(CREATE_TASKS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion < 2) {
            db.execSQL("ALTER TABLE " + TABLE_TASKS + " ADD COLUMN done INTEGER DEFAULT 0");
        }
    }

    public void addTask(Task task) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("name", task.getName());
        values.put("description", task.getDescription());
        values.put("date", task.getDate());
        values.put("time", task.getTime());
        values.put("reminder", task.getReminder());
        values.put("category", task.getCategory());
        values.put("contact", task.getContact());
        values.put("done", task.isDone());

        db.insert(TABLE_TASKS, null, values);
        db.close();
    }

    public List<Task> getAllTasks() {
        List<Task> taskList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_TASKS, new String[]{"id", "name", "description", "date", "time", "reminder", "category", "contact", "done"}, null, null, null, null, null);

        if (cursor.moveToFirst()) {
            int idIndex = cursor.getColumnIndex("id");
            int nameIndex = cursor.getColumnIndex("name");
            int descriptionIndex = cursor.getColumnIndex("description");
            int dateIndex = cursor.getColumnIndex("date");
            int timeIndex = cursor.getColumnIndex("time");
            int reminderIndex = cursor.getColumnIndex("reminder");
            int categoryIndex = cursor.getColumnIndex("category");
            int contactIndex = cursor.getColumnIndex("contact");
            int doneIndex = cursor.getColumnIndex("done");

            if (idIndex == -1 || nameIndex == -1 || descriptionIndex == -1 || dateIndex == -1 || timeIndex == -1 || reminderIndex == -1 || categoryIndex == -1 || contactIndex == -1 || doneIndex == -1) {
                cursor.close();
                return taskList; // Return an empty list if any column index is invalid
            }

            do {
                Task task = new Task();
                task.setId(cursor.getInt(idIndex));
                task.setName(cursor.getString(nameIndex));
                task.setDescription(cursor.getString(descriptionIndex));
                task.setDate(cursor.getString(dateIndex));
                task.setTime(cursor.getString(timeIndex));
                task.setReminder(cursor.getString(reminderIndex));
                task.setCategory(cursor.getString(categoryIndex));
                task.setContact(cursor.getString(contactIndex));
                task.setDone(cursor.getInt(doneIndex) == 1);
                taskList.add(task);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return taskList;
    }

    public void deleteTask(int taskId) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_TASKS, "id = ?", new String[]{String.valueOf(taskId)});
        db.close();
    }

    public void updateTask(Task task) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("name", task.getName());
        values.put("description", task.getDescription());
        values.put("date", task.getDate());
        values.put("time", task.getTime());
        values.put("reminder", task.getReminder());
        values.put("category", task.getCategory());
        values.put("contact", task.getContact());
        values.put("done", task.isDone());

        db.update(TABLE_TASKS, values, "id = ?", new String[]{String.valueOf(task.getId())});
        db.close();
    }
}
