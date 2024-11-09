package com.example.assisment.ui.activities;

import android.content.Intent;
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

import com.example.assisment.R;

public class RecoverFormActivity extends AppCompatActivity {

    private EditText fullName;
    private EditText dateOfBirth;
    private EditText mothersName;
    private EditText petName;
    private EditText friendName;
    private Button button;
    private final String tag = "filter";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_recover_form);

        fullName = findViewById(R.id.fname);
        dateOfBirth = findViewById(R.id.dob);
        mothersName = findViewById(R.id.mothersName);
        petName = findViewById(R.id.petName);
        friendName=findViewById(R.id.friendName);
        button = findViewById(R.id.nextBtn1);

        Intent intent = new Intent(this,RecoverFormActivity2.class);


        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(fullName.getText().toString().isEmpty() || dateOfBirth.getText().toString().isEmpty() ||
                        mothersName.getText().toString().isEmpty() || friendName.getText().toString().isEmpty() ||
                        petName.getText().toString().isEmpty()){

                    Toast.makeText(getApplicationContext(),getString(R.string.empty_field_toast),Toast.LENGTH_LONG).show();
                }else{
                    intent.putExtra("name",fullName.getText().toString());
                    intent.putExtra("dob",dateOfBirth.getText().toString());
                    intent.putExtra("mName",mothersName.getText().toString());
                    intent.putExtra("fName",friendName.getText().toString());
                    intent.putExtra("pName",petName.getText().toString());
                    startActivity(intent);
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