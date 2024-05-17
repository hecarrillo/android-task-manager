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
    private static final int DATABASE_VERSION = 3;  // Asegúrate de que la versión está actualizada para forzar el onUpgrade si es necesario
    private static final String TABLE_TASKS = "tasks";
    private static final String TABLE_CATEGORIES = "categories";

    public TaskDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_CATEGORIES_TABLE = "CREATE TABLE " + TABLE_CATEGORIES + "("
                + "id INTEGER PRIMARY KEY AUTOINCREMENT,"
                + "name TEXT UNIQUE)";
        db.execSQL(CREATE_CATEGORIES_TABLE);

        String CREATE_TASKS_TABLE = "CREATE TABLE " + TABLE_TASKS + "("
                + "id INTEGER PRIMARY KEY AUTOINCREMENT,"
                + "name TEXT,"
                + "description TEXT,"
                + "date TEXT,"
                + "time TEXT,"
                + "reminder TEXT,"
                + "category_id INTEGER,"
                + "contact TEXT,"
                + "done INTEGER DEFAULT 0,"
                + "FOREIGN KEY (category_id) REFERENCES " + TABLE_CATEGORIES + "(id))";
        db.execSQL(CREATE_TASKS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion < 3) {
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_TASKS);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_CATEGORIES);
            onCreate(db);
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
        values.put("category_id", task.getCategoryId());
        values.put("contact", task.getContact());
        values.put("done", task.isDone());

        db.insert(TABLE_TASKS, null, values);
        db.close();
    }

    public List<Task> getAllTasks() {
        List<Task> taskList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT t.*, c.name AS category_name FROM " + TABLE_TASKS + " t "
                + "JOIN " + TABLE_CATEGORIES + " c ON t.category_id = c.id";
        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            do {
                int idIndex = cursor.getColumnIndex("id");
                int nameIndex = cursor.getColumnIndex("name");
                int descriptionIndex = cursor.getColumnIndex("description");
                int dateIndex = cursor.getColumnIndex("date");
                int timeIndex = cursor.getColumnIndex("time");
                int reminderIndex = cursor.getColumnIndex("reminder");
                int categoryIdIndex = cursor.getColumnIndex("category_id");
                int categoryIndex = cursor.getColumnIndex("category");
                int contactIndex = cursor.getColumnIndex("contact");
                int doneIndex = cursor.getColumnIndex("done");

                if (idIndex == -1 || nameIndex == -1 || descriptionIndex == -1 || dateIndex == -1 || timeIndex == -1 || reminderIndex == -1 || categoryIndex == -1 || contactIndex == -1 || doneIndex == -1) {
                    cursor.close();
                    return taskList; // Return an empty list if any column index is invalid
                }

                Task task = new Task(
                        cursor.getInt(idIndex),
                        cursor.getString(nameIndex),
                        cursor.getString(descriptionIndex),
                        cursor.getString(dateIndex),
                        cursor.getString(timeIndex),
                        cursor.getString(reminderIndex),
                        cursor.getInt(categoryIdIndex),
                        cursor.getString(categoryIndex),
                        cursor.getString(contactIndex),
                        cursor.getInt(doneIndex) == 1
                );
                taskList.add(task);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
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
        values.put("category_id", task.getCategoryId());
        values.put("contact", task.getContact());
        values.put("done", task.isDone());

        db.update(TABLE_TASKS, values, "id = ?", new String[]{String.valueOf(task.getId())});
        db.close();
    }

    public void addCategory(String categoryName) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("name", categoryName);
        db.insert(TABLE_CATEGORIES, null, values);
        db.close();
    }

    public List<String> getAllCategories() {
        List<String> categories = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_CATEGORIES, new String[]{"name"}, null, null, null, null, null);
        if (cursor.moveToFirst()) {
            do {
                categories.add(cursor.getString(0));
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return categories;
    }

    public void deleteCategory(String categoryName) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_CATEGORIES, "name = ?", new String[]{categoryName});
        db.close();
    }

    public void updateCategory(String oldName, String newName) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("name", newName);
        db.update(TABLE_CATEGORIES, values, "name = ?", new String[]{oldName});
        db.close();
    }

    public long ensureCategory(String categoryName) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.query(TABLE_CATEGORIES, new String[]{"id"}, "name = ?", new String[]{categoryName}, null, null, null);

        if (cursor.moveToFirst()) {
            int colIdx = cursor.getColumnIndex("id");

            if (cursor.getColumnIndex("id") == -1) {
                cursor.close();
                return -1; // Return -1 if the column index is invalid (should never happen)
            }

            long id = cursor.getLong(colIdx);

            cursor.close();
            return id;
        }
        cursor.close();

        // Category does not exist, so insert it
        ContentValues values = new ContentValues();
        values.put("name", categoryName);
        return db.insert(TABLE_CATEGORIES, null, values);
    }
}
