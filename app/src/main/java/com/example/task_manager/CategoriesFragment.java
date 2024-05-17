package com.example.task_manager;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import androidx.fragment.app.Fragment;
import java.util.ArrayList;

public class CategoriesFragment extends Fragment {

    private ListView listView;
    private CategoryAdapter adapter;
    private ArrayList<Category> categories;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_categories, container, false);

        listView = view.findViewById(R.id.list_categories);
        Button btnAddCategory = view.findViewById(R.id.btn_add_category);
        EditText editTextCategoryName = view.findViewById(R.id.editText_category_name);

        categories = loadCategories(); // Cargar categorías existentes
        adapter = new CategoryAdapter(getActivity(), categories);
        listView.setAdapter(adapter);

        btnAddCategory.setOnClickListener(v -> {
            String categoryName = editTextCategoryName.getText().toString().trim();
            if (!categoryName.isEmpty()) {
                Category newCategory = new Category(categoryName);
                categories.add(newCategory);
                adapter.notifyDataSetChanged();
                saveCategory(newCategory); // Guardar la categoría en la base de datos
                editTextCategoryName.setText(""); // Limpiar el campo de texto
            }
        });

        return view;
    }

    private ArrayList<Category> loadCategories() {
        // Aquí deberías cargar las categorías de la base de datos
        return new ArrayList<>(); // Temporal, implementar carga real
    }

    private void saveCategory(Category category) {
        // Aquí deberías implementar cómo guardar las categorías en la base de datos
    }
}
