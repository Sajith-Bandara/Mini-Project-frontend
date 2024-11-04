package com.example.assisment;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;


public class SignupActivity extends AppCompatActivity {

    private EditText email;
    private EditText password;
    private EditText confirmPassword;
    private Button button;
    private final String tag = "filter";

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
                String passwordString = password.getText().toString();
                String confirmPasswordString = confirmPassword.getText().toString();

                if(passwordString.equals(confirmPasswordString)){
                    Log.i(tag,email.getText().toString());
                    Log.i(tag,passwordString);
                }else{
                    Toast.makeText(getApplicationContext(),getString(R.string.signup_toast),Toast.LENGTH_LONG).show();
                }
            }
        });

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
}