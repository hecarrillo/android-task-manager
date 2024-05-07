package com.example.task_manager;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;


public class TaskListFragment extends Fragment {

    private RecyclerView recyclerView;
    private TaskAdapter taskAdapter;
    private TaskDatabaseHelper dbHelper;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_task_list, container, false);
        recyclerView = view.findViewById(R.id.recycler_view_tasks);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        dbHelper = new TaskDatabaseHelper(getContext());
        updateTaskList();

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        updateTaskList();
    }

    private void updateTaskList() {
        new Thread(() -> {
            try {
                List<Task> newTasks = dbHelper.getAllTasks();
                getActivity().runOnUiThread(() -> {
                    if (taskAdapter == null) {
                        taskAdapter = new TaskAdapter(newTasks, dbHelper);
                        recyclerView.setAdapter(taskAdapter);
                    } else {
                        taskAdapter.setTasks(newTasks);
                        taskAdapter.notifyDataSetChanged();
                    }
                });
            } catch (Exception e) {
                Log.e("TaskListFragment", "Error updating task list", e);
            }
        }).start();
    }

}
