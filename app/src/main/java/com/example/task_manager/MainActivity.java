// src/main/java/com/example/task_manager/MainActivity.java
package com.example.task_manager;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Configura el botón flotante para agregar nuevas tareas
        FloatingActionButton fabAddTask = findViewById(R.id.fab_add_task);
        fabAddTask.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, AddEditTaskActivity.class);
            intent.putExtra("isEditMode", false); // Modo de agregar
            startActivity(intent);
        });

        // Carga el fragmento de lista de tareas inicialmente
        loadFragment(new TaskListFragment());

        // Configura la barra de navegación inferior
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnItemSelectedListener(item -> {
            Fragment selectedFragment = null;
            int itemId = item.getItemId();

            if (itemId == R.id.nav_all_tasks) {
                selectedFragment = new TaskListFragment();
            } else if (itemId == R.id.nav_categories) {
                selectedFragment = new CategoriesFragment();
            } else if (itemId == R.id.nav_calendar) {
                selectedFragment = new TaskListFragment();
            }

            if (selectedFragment != null) {
                loadFragment(selectedFragment);
            }
            if (selectedFragment != null) {
                loadFragment(selectedFragment);
            }
            return true;
        });
    }

    // Método para reemplazar el contenido principal con un fragmento dado
    private void loadFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.content_frame, fragment)
                .commit();
    }
}
