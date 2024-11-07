package com.example.assisment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.List;

public class RecoverAccountActivity extends AppCompatActivity {

    private Spinner spinner1, spinner2;
    private ArrayAdapter<String> adapter1, adapter2;
    private List<String> originalOptions, options1, options2;
    private Button button;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_recover_account);

        spinner1 = findViewById(R.id.spinner);
        spinner2 = findViewById(R.id.spinner2);

        // Initialize the list of options
        originalOptions = new ArrayList<>();
        originalOptions.add("Select Field");
        originalOptions.add("Mother's Maiden Name");
        originalOptions.add("Childhood Best Friend's Name");
        originalOptions.add("Childhood Pet Name");
        originalOptions.add("Your Own Question");

        // Clone the lists for each spinner
        options1 = new ArrayList<>(originalOptions);
        options2 = new ArrayList<>(originalOptions);

        // Set up the adapters for each spinner
        adapter1 = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, options1);
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner1.setAdapter(adapter1);

        adapter2 = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, options2);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner2.setAdapter(adapter2);

        // Set listeners for both spinners
        setupSpinnerListener(spinner1, adapter2, options2);
        setupSpinnerListener(spinner2, adapter1, options1);

        button = findViewById(R.id.recoverBtn);
        Intent intent = new Intent(this, LoginActivity.class);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(intent);
            }
        });

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    private void setupSpinnerListener(Spinner spinner, ArrayAdapter<String> otherAdapter, List<String> otherOptions) {
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedItem = parent.getItemAtPosition(position).toString();

                if (!selectedItem.equals("Select Field")) {
                    // Update the other spinner to remove the selected item
                    otherOptions.clear();
                    for (String option : originalOptions) {
                        if (!option.equals(selectedItem)) {
                            otherOptions.add(option);
                        }
                    }
                }

                otherAdapter.notifyDataSetChanged();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Optional: Handle no selection case
            }
        });
    }
}