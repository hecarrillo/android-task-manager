package com.example.task_manager;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.TaskViewHolder> {

    private List<Task> tasks;
    private TaskDatabaseHelper dbHelper;

    public TaskAdapter(List<Task> tasks, TaskDatabaseHelper dbHelper) {
        this.tasks = tasks;
        this.dbHelper = dbHelper;
    }

    @NonNull
    @Override
    public TaskViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.task_item, parent, false);
        return new TaskViewHolder(itemView);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull TaskViewHolder holder, int position) {
        Task currentTask = tasks.get(position);
        holder.textViewName.setText(currentTask.getName());
        holder.textViewDescription.setText(currentTask.getDescription());
        holder.textViewDate.setText(currentTask.getDate() + " at " + currentTask.getTime());

        // Setup buttons
        holder.buttonDelete.setOnClickListener(v -> {
            dbHelper.deleteTask(currentTask.getId());
            tasks.remove(position);
            notifyItemRemoved(position);
            notifyItemRangeChanged(position, tasks.size());
        });

        holder.checkboxDone.setOnCheckedChangeListener(null); // Primero desactiva el listener para evitar disparos innecesarios
        holder.checkboxDone.setChecked(currentTask.isDone());

        // Ahora establece el nuevo listener
        holder.checkboxDone.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (buttonView.isShown()) { // Esto verifica que el cambio fue debido a la interacción del usuario y no a la recarga de datos
                currentTask.setDone(isChecked);
                dbHelper.updateTask(currentTask);
                // Actualiza la UI en el hilo principal si es necesario
                ((Activity) holder.itemView.getContext()).runOnUiThread(() -> {
                    notifyItemChanged(position);
                });
            }
        });


    }

    @Override
    public int getItemCount() {
        return tasks.size();
    }

    public void setTasks(List<Task> tasks) {
        this.tasks = tasks;
        notifyDataSetChanged();
    }

    static class TaskViewHolder extends RecyclerView.ViewHolder {
        private final TextView textViewName;
        private final TextView textViewDescription;
        private final TextView textViewDate;
        private final Button buttonDelete;
        private final CheckBox checkboxDone;

        public TaskViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewName = itemView.findViewById(R.id.text_view_task_name);
            textViewDescription = itemView.findViewById(R.id.text_view_task_description);
            textViewDate = itemView.findViewById(R.id.text_view_task_date);
            buttonDelete = itemView.findViewById(R.id.button_delete_task);
            checkboxDone = itemView.findViewById(R.id.checkbox_done);
        }
    }
}
