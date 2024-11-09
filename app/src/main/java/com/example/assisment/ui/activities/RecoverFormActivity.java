package com.example.assisment.ui.activities;

import android.content.Intent;
import android.os.Bundle;
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

        String name = fullName.getText().toString();
        String dob=dateOfBirth.getText().toString();
        String mName=mothersName.getText().toString();
        String fName=friendName.getText().toString();
        String pName=petName.getText().toString();

        Intent intent = new Intent(this,RecoverFormActivity2.class);
        intent.putExtra("name",name);
        intent.putExtra("dob",dob);
        intent.putExtra("mName",mName);
        intent.putExtra("fName",fName);
        intent.putExtra("pName",pName);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(name.isEmpty() || dob.isEmpty() || mName.isEmpty() || fName.isEmpty() || pName.isEmpty()){
                    Toast.makeText(getApplicationContext(),getString(R.string.empty_field_toast),Toast.LENGTH_LONG).show();
                }else{
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