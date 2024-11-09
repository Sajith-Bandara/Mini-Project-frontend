package com.example.assisment.ui.activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.assisment.R;
import com.example.assisment.data.api.ApiService;
import com.example.assisment.data.models.LoginRequest;
import com.example.assisment.data.models.LoginResponse;
import com.example.assisment.ui.adapters.Config;

import android.content.Context;
import android.content.SharedPreferences;
import android.widget.Toast;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class LoginActivity extends AppCompatActivity {

    private static final String SHARED_PREFS = "app_prefs";
    private static final String TOKEN_KEY = "token";
    private static final String PRIVATE_KEY = "private_key";
    private static final String ROLE_KEY = "role";

    private TextView switchToSignup;
    private TextView recoverAccount;
    private Button button;
    private EditText email;
    private EditText password;

    private final String tag="filter";

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);

        button = findViewById(R.id.signinBtn);
        email = findViewById(R.id.emailLogin);
        password = findViewById(R.id.passwordLogin);



        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loginUser(email.getText().toString(),password.getText().toString());
            }
        });

        switchToSignup = findViewById(R.id.toSignup);
        Intent intent = new Intent(this, SignupActivity.class);

        switchToSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(intent);
            }
        });

        recoverAccount = findViewById(R.id.recoverAccount);
        Intent intent1 = new Intent(this,RecoverAccountActivity.class);

        recoverAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {startActivity(intent1);            }
        });



        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    private void loginUser(String email, String password) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Config.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ApiService apiService = retrofit.create(ApiService.class);
        LoginRequest loginRequest = new LoginRequest(email, password);


        Log.i(tag,loginRequest.toString());
        Intent logIntent = new Intent(this,HomeActivity.class);
        Intent logIntentAdmin = new Intent(this,AdminHome.class);

        apiService.login(loginRequest).enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {

                Log.i(tag,response.body().getRole().toString());

                if (response.isSuccessful() && response.body() != null) {
                    saveLoginData(response.body());

                    SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);

                    if(sharedPreferences.getString(ROLE_KEY,"").equals("USER")){
                        startActivity(logIntent);
                        finish();
                    }else if (sharedPreferences.getString(ROLE_KEY,"").equals("ADMIN")){
                        startActivity(logIntentAdmin);
                        finish();
                    }

                } else {
                    Toast.makeText(LoginActivity.this, "Login failed", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                Log.e(tag,t.toString());
                Toast.makeText(LoginActivity.this, "Network error", Toast.LENGTH_SHORT).show();
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