package com.example.spendingmanager;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class UserInfoActivity extends AppCompatActivity {
    TextView mail;
    EditText name, phone;
    Button btnsave;
    String uid, namein, phonein;

    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    ProgressDialog noti;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info);
        ActionBar actionBar;
        actionBar = getSupportActionBar();
        ColorDrawable colorDrawable = new ColorDrawable(getResources().getColor(R.color.main_color));
        // Set BackgroundDrawable
        actionBar.setBackgroundDrawable(colorDrawable);
        actionBar.setTitle("User Information");

        noti = new ProgressDialog(getApplicationContext());
        View currentView = this.getWindow().getDecorView().findViewById(android.R.id.content);
        currentView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                InternetCheck();
                return false;
            }
        });
        InternetCheck();

        mail = findViewById(R.id.userMail);
        mail.setText(FirebaseAuth.getInstance().getCurrentUser().getEmail());
        name = findViewById(R.id.userName);
        phone = findViewById(R.id.userPhone);
        btnsave = findViewById(R.id.saveInfo);

        uid = getIntent().getStringExtra("uid");
        namein = getIntent().getStringExtra("name");
        phonein = getIntent().getStringExtra("phone");

        name.setText(namein);
        phone.setText(phonein);

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference().child("Users");

        btnsave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(Internet.isOnline(getApplicationContext())){
                    if(TextUtils.isEmpty(name.getText())){
                        Toast.makeText(getApplicationContext(), "Enter name", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if(TextUtils.isEmpty(phone.getText())){
                        Toast.makeText(getApplicationContext(), "Enter phone", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    String namex = name.getText().toString();
                    String phonex = phone.getText().toString();
                    Users us = new Users(FirebaseAuth.getInstance().getCurrentUser().getEmail(),namex, phonex);
                    databaseReference.child(uid).setValue(us);
                    Toast.makeText(getApplicationContext(), "User information updated", Toast.LENGTH_SHORT).show();
                }else InternetCheck();
            }
        });
    }
    private void InternetCheck(){
        if(!Internet.isOnline(getApplicationContext())) {
            noti.setTitle("Internet connection");
            noti.setMessage("You are not connecting to the Internet.\n\nPlease check Internet connection and try again.");
            noti.setCancelable(false);
            noti.setIcon(getResources().getDrawable(R.drawable.nointernet));
            noti.setButton(DialogInterface.BUTTON_NEGATIVE, "Retry", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    noti.dismiss();//dismiss dialog
                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(intent);
                    finish();
                }
            });
            noti.show();
        }
    }
}