package com.example.assisment.ui.activities;

import static android.hardware.biometrics.BiometricManager.Authenticators.BIOMETRIC_STRONG;
import static android.hardware.biometrics.BiometricManager.Authenticators.DEVICE_CREDENTIAL;

import static com.example.assisment.ui.adapters.Config.BASE_URL;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;

import androidx.appcompat.app.AppCompatActivity;
import androidx.biometric.BiometricManager;
import androidx.biometric.BiometricPrompt;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.assisment.R;
import com.example.assisment.data.api.ApiService;
import com.example.assisment.data.models.ChangePassword;
import com.example.assisment.data.models.RecoveryData;

import java.util.Map;
import java.util.concurrent.Executor;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ProfileActivity extends AppCompatActivity {

    TextView email,fullname,mothersmedianname,friendname,birthday,petname,ownquestion,ownanswer;
    Button changepwd;
    private Executor executor;
    private BiometricPrompt biometricPrompt;
    private BiometricPrompt.PromptInfo promptInfo;
    private static final String SHARED_PREFS = "app_prefs";
    private static final String TOKEN_KEY = "token";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_profile);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);
        String token = sharedPreferences.getString(TOKEN_KEY, "");


        email = findViewById(R.id.email);
        fullname = findViewById(R.id.fullname);
        mothersmedianname = findViewById(R.id.mothername);
        friendname = findViewById(R.id.friendname);
        birthday = findViewById(R.id.bithday);
        petname = findViewById(R.id.pname);
        ownquestion = findViewById(R.id.ownquestion);
        ownanswer = findViewById(R.id.ownans);
        changepwd = findViewById(R.id.changepwd);


        changepwd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showChangePasswordDialog(token);
            }
        });


        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        // Create the service
        ApiService service = retrofit.create(ApiService.class);

        // Make the request
        Call<Map<String,String>> call = service.getEmail(token);
        call.enqueue(new Callback<Map<String,String>>() {
            @Override
            public void onResponse(Call<Map<String,String>> call, Response<Map<String,String>> response) {
                if (response.isSuccessful()) {
                    System.out.println("Response: " + response.body());
                    Map<String,String> details = response.body();
                    if (details != null) {
                        email.setText(details.get("email"));
                    }
                } else {
                    System.out.println("Failed to fetch details: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<Map<String,String>> call, Throwable t) {
                t.printStackTrace();
            }
        });


        BiometricManager biometricManager = BiometricManager.from(this);
        switch (biometricManager.canAuthenticate(BiometricManager.Authenticators.BIOMETRIC_STRONG | BiometricManager.Authenticators.DEVICE_CREDENTIAL)) {
            case BiometricManager.BIOMETRIC_SUCCESS:
                // Device supports biometric authentication
                showBiometricPrompt(token);
                break;
            case BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE:
                // No biometric features available on this device
                break;
            case BiometricManager.BIOMETRIC_ERROR_HW_UNAVAILABLE:
                // Biometric features are currently unavailable
                break;
            case BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED:
                final Intent enrollIntent = new Intent(Settings.ACTION_BIOMETRIC_ENROLL);
                enrollIntent.putExtra(Settings.EXTRA_BIOMETRIC_AUTHENTICATORS_ALLOWED,
                        BIOMETRIC_STRONG | DEVICE_CREDENTIAL);
                startActivityForResult(enrollIntent, 10001);
                break;
        }

    }
    private void showBiometricPrompt(String token) {
        Executor executor = ContextCompat.getMainExecutor(this);
        BiometricPrompt biometricPrompt = new BiometricPrompt(this, executor, new BiometricPrompt.AuthenticationCallback() {
            @Override
            public void onAuthenticationError(int errorCode,
                                              @NonNull CharSequence errString) {
                super.onAuthenticationError(errorCode, errString);
                finish();
                Toast.makeText(getApplicationContext(),
                                "Authentication error: " + errString, Toast.LENGTH_SHORT)
                        .show();
            }

            @Override
            public void onAuthenticationSucceeded(@NonNull BiometricPrompt.AuthenticationResult result) {
                super.onAuthenticationSucceeded(result);
                Filldata(token);
            }

            @Override
            public void onAuthenticationFailed() {
                super.onAuthenticationFailed();
                finish();
                Toast.makeText(getApplicationContext(), "Authentication failed",Toast.LENGTH_SHORT).show();
            }
        });
        promptInfo = new BiometricPrompt.PromptInfo.Builder()
                .setTitle("Biometric Authentication Needed")
                .setSubtitle("After the authentication you can see your profile")
                .setAllowedAuthenticators(BiometricManager.Authenticators.BIOMETRIC_STRONG | BiometricManager.Authenticators.DEVICE_CREDENTIAL)
                .build();

        biometricPrompt.authenticate(promptInfo);
    }

    private void Filldata(String token){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        // Create the service
        ApiService service = retrofit.create(ApiService.class);

        // Make the request
        Call<RecoveryData> call = service.getRecoveryData(token);
        call.enqueue(new Callback<RecoveryData>() {
            @Override
            public void onResponse(Call<RecoveryData> call, Response<RecoveryData> response) {
                if (response.isSuccessful()) {
                    RecoveryData details = response.body();
                    if (details != null) {
                        fullname.setText(details.getFullName());
                        mothersmedianname.setText(details.getMothersMaidenName());
                        friendname.setText(details.getChildhoodBestFriendName());
                        birthday.setText(details.getDateOfBirth());
                        petname.setText(details.getChildhoodPetName());
                        ownquestion.setText(details.getOwnQuestion());
                        ownanswer.setText(details.getOwnAnswer());
                    }
                } else {
                    System.out.println("Failed to fetch details: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<RecoveryData> call, Throwable t) {
                t.printStackTrace();
            }
        });

    }
    public void showChangePasswordDialog(String token) {
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_change_password, null);

        EditText oldPassword = dialogView.findViewById(R.id.oldPassword);
        EditText newPassword = dialogView.findViewById(R.id.newPassword);
        EditText confirmNewPassword = dialogView.findViewById(R.id.confirmNewPassword);
        Button changePasswordButton = dialogView.findViewById(R.id.changePasswordButton);

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        dialogBuilder.setView(dialogView);
        AlertDialog dialog = dialogBuilder.create();

        changePasswordButton.setOnClickListener(v -> {
            String oldPw = oldPassword.getText().toString().trim();
            String newPw = newPassword.getText().toString().trim();
            String confirmPw = confirmNewPassword.getText().toString().trim();

            if (oldPw.isEmpty() || newPw.isEmpty() || confirmPw.isEmpty()) {
                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
            } else if (!newPw.equals(confirmPw)) {
                Toast.makeText(this, "New passwords do not match", Toast.LENGTH_SHORT).show();
            } else {
                changePassword(oldPw, newPw,token);
                dialog.dismiss();
            }
        });

        // Show the dialog
        dialog.show();
    }
    private void changePassword(String oldPassword, String newPassword,String token) {

        ChangePassword req = new ChangePassword();
        req.setOldPassword(oldPassword);
        req.setNewPassword(newPassword);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        // Create the service
        ApiService service = retrofit.create(ApiService.class);

        // Make the request
        Call<Map<String,String>> call = service.changePassword(req,token);
        call.enqueue(new Callback<Map<String,String>> () {
            @Override
            public void onResponse(Call<Map<String,String>>  call, Response<Map<String,String>>  response) {
                if (response.isSuccessful()) {
                    Map<String,String> details = response.body();
                    if (details != null) {
                        Toast.makeText(getApplicationContext(), "Password changed successfully", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    System.out.println("Failed to fetch details: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<Map<String,String>>  call, Throwable t) {
                t.printStackTrace();
            }
        });

    }

}