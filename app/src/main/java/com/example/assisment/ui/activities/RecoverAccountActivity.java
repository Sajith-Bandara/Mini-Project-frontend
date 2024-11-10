package com.example.assisment.ui.activities;

import static com.example.assisment.ui.adapters.Config.BASE_URL;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.assisment.R;
import com.example.assisment.data.api.ApiService;
import com.example.assisment.data.models.ChangePassword;
import com.example.assisment.data.models.RecoveryData;
import com.example.assisment.data.models.ResetPW;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RecoverAccountActivity extends AppCompatActivity {

    private Spinner spinner1, spinner2;
    private ArrayAdapter<String> adapter1, adapter2;
    private List<String> originalOptions, options1, options2;
    private Button button;
     private TextView ans1, ans2,fname,dob;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_recover_account);

        spinner1 = findViewById(R.id.spinner);
        spinner2 = findViewById(R.id.spinner2);
        ans1 = findViewById(R.id.answer1);
        ans2 = findViewById(R.id.answer2);
        fname = findViewById(R.id.fnameRecover);
        dob = findViewById(R.id.dobRecover);

        // Initialize the list of options
        originalOptions = new ArrayList<>();
        originalOptions.add("Select Field");
        originalOptions.add("Mother's Maiden Name");
        originalOptions.add("Childhood Best Friend's Name");
        originalOptions.add("Childhood Pet Name");
        originalOptions.add("Your Own Question");
        originalOptions.add("Your Own Answer");

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


        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Validate the fields
                if (spinner1.getSelectedItem().toString().equals("Select Field") || spinner2.getSelectedItem().toString().equals("Select Field")) {
                    Toast.makeText(RecoverAccountActivity.this, "Please select two fields", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (ans1.getText().toString().isEmpty() || ans2.getText().toString().isEmpty()) {
                    Toast.makeText(RecoverAccountActivity.this, "Please fill in both answers", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (fname.getText().toString().isEmpty() || dob.getText().toString().isEmpty()) {
                    Toast.makeText(RecoverAccountActivity.this, "Please fill in both fields", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (dob.getText().toString().length() < 5) {
                    Toast.makeText(RecoverAccountActivity.this, "Please enter a valid date of birth", Toast.LENGTH_SHORT).show();
                    return;
                }


                // Retrieve the selected values from spinners
                String selectedQuestion1 = spinner1.getSelectedItem().toString();
                String selectedQuestion2 = spinner2.getSelectedItem().toString();

                // Retrieve answers from EditText fields
                String answer1 = ans1.getText().toString();
                String answer2 = ans2.getText().toString();


                try {
                    RecoveryData json = new RecoveryData();
                    json.setFullName(fname.getText().toString());
                    json.setDateOfBirth(dob.getText().toString());

                    // Add fields based on spinner selections
                    json.setMothersMaidenName(selectedQuestion1.equals("Mother's Maiden Name") ? answer1 : selectedQuestion2.equals("Mother's Maiden Name") ? answer2 : null);
                    json.setChildhoodBestFriendName( selectedQuestion1.equals("Childhood Best Friend's Name") ? answer1 : selectedQuestion2.equals("Childhood Best Friend's Name") ? answer2 : null);
                    json.setChildhoodPetName( selectedQuestion1.equals("Childhood Pet Name") ? answer1 : selectedQuestion2.equals("Childhood Pet Name") ? answer2 : null);
                    json.setOwnQuestion( selectedQuestion1.equals("Your Own Question") ? answer1 : selectedQuestion2.equals("Your Own Question") ? answer2 : null);
                    json.setOwnAnswer(selectedQuestion1.equals("Your Own Answer") ? answer1 : selectedQuestion2.equals("Your Own Answer") ? answer2 : null);

                    // Log the JSON or use it as needed
                    System.out.println(json.toString());
                    Retrofit retrofit = new Retrofit.Builder()
                            .baseUrl(BASE_URL)
                            .addConverterFactory(GsonConverterFactory.create())
                            .build();

                    // Create the service
                    ApiService service = retrofit.create(ApiService.class);

                    // Make the request
                    Call<Map<String,String>> call = service.recoverAccount(json);
                    call.enqueue(new Callback <Map<String,String>>() {
                        @Override
                        public void onResponse(Call<Map<String,String>> call, Response<Map<String,String>> response) {
                            if (response.isSuccessful()) {
                                Map<String,String> details = response.body();
                                if (details != null) {
                                    String id = details.get("message");
                                    ResetPW(id);

                                }
                            } else {
                                Toast.makeText(RecoverAccountActivity.this, "Error :"+response.code(), Toast.LENGTH_SHORT).show();
                                System.out.println("Failed id: " + response.code());
                            }
                        }

                        @Override
                        public void onFailure(Call<Map<String,String>> call, Throwable t) {
                            t.printStackTrace();
                        }
                    });

                } catch (Exception e) {
                    e.printStackTrace();
                }
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



    private void ResetPW(String id){
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_reset_password, null);

        ResetPW resetPW = new ResetPW();
        resetPW.setId(id);
        EditText newPassword = dialogView.findViewById(R.id.newPassword1);
        EditText confirmNewPassword = dialogView.findViewById(R.id.confirmNewPassword1);
        Button changePasswordButton = dialogView.findViewById(R.id.changePasswordButton1);

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        dialogBuilder.setView(dialogView);
        AlertDialog dialog = dialogBuilder.create();

        changePasswordButton.setOnClickListener(v -> {
            String newPw = newPassword.getText().toString().trim();
            String confirmPw = confirmNewPassword.getText().toString().trim();

            if (newPw.isEmpty() || confirmPw.isEmpty()) {
                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
            } else if (!newPw.equals(confirmPw)) {
                Toast.makeText(this, "New passwords do not match", Toast.LENGTH_SHORT).show();
            } else {
                resetPW.setNewPassword(newPw);
                changePassword(resetPW);
                dialog.dismiss();
            }
        });

        // Show the dialog
        dialog.show();
    }
    private void changePassword(ResetPW resetPW) {



        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        // Create the service
        ApiService service = retrofit.create(ApiService.class);

        // Make the request
        Call<Map<String,String>> call = service.resetPassword(resetPW);
        call.enqueue(new Callback<Map<String,String>> () {
            @Override
            public void onResponse(Call<Map<String,String>>  call, Response<Map<String,String>>  response) {
                if (response.isSuccessful()) {
                    Map<String,String> details = response.body();
                    if (details != null) {
                        Toast.makeText(getApplicationContext(), "Password changed successfully", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(RecoverAccountActivity.this, LoginActivity.class));
                    }
                } else {
                    System.out.println("Failed to fetch details: " + response.code() + response.body());
                }
            }

            @Override
            public void onFailure(Call<Map<String,String>>  call, Throwable t) {
                t.printStackTrace();
            }
        });

    }
}