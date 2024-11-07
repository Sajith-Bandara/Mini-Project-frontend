package com.example.assisment;

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


public class LoginActivity extends AppCompatActivity {

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

        Intent logIntent = new Intent(this,HomeActivity.class);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i(tag,email.getText().toString());
                Log.i(tag,password.getText().toString());
                startActivity(logIntent);
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
}