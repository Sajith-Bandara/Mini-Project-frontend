package com.example.assisment.ui.activities;

import android.annotation.SuppressLint;
import android.content.Intent;
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


public class SignupActivity extends AppCompatActivity {

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

        Intent intent =new Intent(this,RecoverFormActivity.class);
        intent.putExtra("email",email.getText().toString());
        intent.putExtra("password",password.getText().toString());

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if( password.getText().toString().isEmpty() || confirmPassword.getText().toString().isEmpty() || email.getText().toString().isEmpty()){
                    Toast.makeText(getApplicationContext(),getString(R.string.empty_field_toast),Toast.LENGTH_LONG).show();
                }else if(!password.getText().toString().equals(confirmPassword.getText().toString())){
                    Toast.makeText(getApplicationContext(),getString(R.string.signup_toast),Toast.LENGTH_LONG).show();
                }else{
                    Log.i(tag,email.getText().toString());
                    Log.i(tag,password.getText().toString());
                    startActivity(intent);
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
}