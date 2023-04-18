package com.example.spendingmanager;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RegisterActivity extends AppCompatActivity {
    LinearLayout turnmain;
    EditText username, passwd, name, phone;
    Button btnregister;
    private FirebaseAuth mAuth;

    DatabaseReference databaseReference;
    FirebaseDatabase firebaseDatabase;
    ProgressDialog noti;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        noti = new ProgressDialog(RegisterActivity.this);
        noti.setTitle("Assistant");
        noti.setMessage("The app will use email and password to create account");
        noti.setButton(DialogInterface.BUTTON_NEGATIVE, "OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                noti.dismiss();//dismiss dialog
            }
        });
        noti.setIcon(getResources().getDrawable(R.drawable.logo));
        noti.setCancelable(true);

        mAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference().child("Users");
        turnmain = findViewById(R.id.btnTurnMain);
        turnmain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

        username = findViewById(R.id.txtRegUsername);
        passwd = findViewById(R.id.txtRegPassword);
        name = findViewById(R.id.txtRegName);
        phone = findViewById(R.id.txtRegPhone);
        btnregister = findViewById(R.id.btnRegister);

        btnregister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String usname, uspasswd, uname, uphone;
                usname = String.valueOf(username.getText());
                uspasswd = String.valueOf(passwd.getText());
                uname = String.valueOf(name.getText());
                uphone = String.valueOf(phone.getText());
                if(TextUtils.isEmpty(uname)){
                    Toast.makeText(RegisterActivity.this, "Please enter full name", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(TextUtils.isEmpty(uphone)){
                    Toast.makeText(RegisterActivity.this, "Please enter phone number", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(TextUtils.isEmpty(usname)){
                    Toast.makeText(RegisterActivity.this, "Please enter email", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(TextUtils.isEmpty(uspasswd)){
                    Toast.makeText(RegisterActivity.this, "Please enter password", Toast.LENGTH_SHORT).show();
                    return;
                }

                Users us = new Users(usname, uname, uphone);
                databaseReference.push().setValue(us);

                mAuth.createUserWithEmailAndPassword(usname, uspasswd)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    // Sign in success, update UI with the signed-in user's information
                                    Toast.makeText(RegisterActivity.this, "Account created", Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                    startActivity(intent);
                                } else {
                                    // If sign in fails, display a message to the user.
                                    Toast.makeText(RegisterActivity.this, "Authentcation Failed", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
        });
        findViewById(R.id.btnInfoReg).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                noti.show();
                //Toast.makeText(getApplicationContext(), "The app will use email and password to create account", Toast.LENGTH_SHORT).show();
            }
        });
    }
}