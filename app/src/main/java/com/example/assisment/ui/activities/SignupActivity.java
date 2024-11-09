package com.example.assisment.ui.activities;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.assisment.R;
import com.example.assisment.data.api.ApiService;
import com.example.assisment.data.models.LoginRequest;
import com.example.assisment.data.models.LoginResponse;
import com.example.assisment.data.models.SignupRequest;
import com.example.assisment.ui.adapters.Config;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class SignupActivity extends AppCompatActivity {

    private static final String SHARED_PREFS = "app_prefs";
    private static final String TOKEN_KEY = "token";
    private static final String PRIVATE_KEY = "private_key";
    private static final String ROLE_KEY = "role";

    private TextView switchToLogin;
    private EditText email;
    private EditText password;
    private EditText confirmPassword;
    private Button button;
    private final String tag = "filter";

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_signup);

        email=findViewById(R.id.emailSingup);
        password=findViewById(R.id.passwordSignup);
        confirmPassword=findViewById(R.id.confirmPasswod);
        button=findViewById(R.id.signupBtn);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if( password.getText().toString().isEmpty() || confirmPassword.getText().toString().isEmpty() || email.getText().toString().isEmpty()){
                    Toast.makeText(getApplicationContext(),getString(R.string.empty_field_toast),Toast.LENGTH_LONG).show();
                }else if(!password.getText().toString().equals(confirmPassword.getText().toString())){
                    Toast.makeText(getApplicationContext(),getString(R.string.signup_toast),Toast.LENGTH_LONG).show();
                }else{
                    signupUser(email.getText().toString(),password.getText().toString());
                }
            }
        });

        switchToLogin = findViewById(R.id.toLogin);
        Intent intent1 = new Intent(this,LoginActivity.class);

        switchToLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(intent1);
            }
        });


        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    private void signupUser(String email, String password) {

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Config.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ApiService apiService = retrofit.create(ApiService.class);
        SignupRequest signupRequest = new SignupRequest(email, password);

        Intent signupIntent = new Intent(this,RecoverFormActivity.class);

        apiService.signup(signupRequest).enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {

                if (response.isSuccessful() && response.body() != null) {
                    saveLoginData(response.body());
                    startActivity(signupIntent);

                } else {
                    Toast.makeText(SignupActivity.this, "Signup failed", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                Log.e(tag,t.toString());
                Toast.makeText(SignupActivity.this, "Network error", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void saveLoginData(LoginResponse loginResponse) {
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(TOKEN_KEY, loginResponse.getToken());
        editor.putString(PRIVATE_KEY, loginResponse.getPrivateKey());
        editor.putString(ROLE_KEY, loginResponse.getRole());
        editor.apply();
    }
}