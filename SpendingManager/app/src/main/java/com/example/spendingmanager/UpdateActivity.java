package com.example.spendingmanager;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class UpdateActivity extends AppCompatActivity {
    FirebaseAuth mAuth;
    FirebaseUser user;
    EditText txtname, txtamount;
    DatePicker datePicker;
    TimePicker timePicker;
    RadioButton rdincome, rdspending;
    Button btnsave, btnclear;
    String name, date, time, id;
    int type, amount;
    DatabaseReference databaseReference;
    FirebaseDatabase firebaseDatabase;
    Activity activityInfo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update);
        ActionBar actionBar;
        actionBar = getSupportActionBar();
        ColorDrawable colorDrawable = new ColorDrawable(Color.parseColor("#0099FF"));
        // Set BackgroundDrawable
        actionBar.setBackgroundDrawable(colorDrawable);
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        if (user == null){
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);
            finish();
        }
        txtname = findViewById(R.id.txtUptName);
        txtamount = findViewById(R.id.txtUptAmount);
        datePicker = findViewById(R.id.txtUptDate);
        timePicker = findViewById(R.id.txtUptTime);
        rdincome = findViewById(R.id.rdUptIncome);
        rdspending = findViewById(R.id.rdUptSpending);
        btnsave = findViewById(R.id.btnUptSave);
        btnclear = findViewById(R.id.btnUptClear);
        timePicker.setIs24HourView(true);

        name = getIntent().getStringExtra("name");
        type = getIntent().getIntExtra("type", 1);
        date = getIntent().getStringExtra("date");
        time = getIntent().getStringExtra("time");
        amount = getIntent().getIntExtra("amount", 0);
        id = getIntent().getStringExtra("id");

        txtname.setText(name);
        txtamount.setText(String.valueOf(amount));
        if(type == 1) rdincome.setChecked(true);
        else if (type ==2) rdspending.setChecked(true);

        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
        try {
            Date dt = format.parse(date);
            datePicker.init(dt.getYear()+1900,dt.getMonth(), dt.getDate(),null);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        SimpleDateFormat format2 = new SimpleDateFormat("HH:mm");
        try{
            Date date = format2.parse(time);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                timePicker.setHour(date.getHours());
                timePicker.setMinute(date.getMinutes());
            }

        }catch ( ParseException e){
            e.printStackTrace();
        }

        btnsave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(TextUtils.isEmpty(txtname.getText())){
                    Toast.makeText(getApplicationContext(), "Enter activity name", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(TextUtils.isEmpty(txtamount.getText())){
                    Toast.makeText(getApplicationContext(), "Enter amount", Toast.LENGTH_SHORT).show();
                    return;
                }
                updateData();
            }
        });
        btnclear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), ListActivity.class);
                startActivity(intent);
            }
        });
    }private void updateData(){
        String name = txtname.getText().toString();
        int type = 0;
        if(rdincome.isChecked()) type = 1;
        else if(rdspending.isChecked()) type = 2;
        else type = 1;
        int date = datePicker.getDayOfMonth();
        int month = datePicker.getMonth()+1;
        int year = datePicker.getYear();
        String stdate, stmonth, styear;
        if (date < 10)
            stdate = "0"+String.valueOf(date);
        else stdate = String.valueOf(date);
        if (month < 10)
            stmonth = "0"+String.valueOf(month);
        else stmonth = String.valueOf(month);
        styear = String.valueOf(year);
        String noteDate = stdate+"/" +stmonth + "/"+ styear;
        int hour = 0, minute = 0;
        String sthour, stminute;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            hour = timePicker.getHour();
            minute = timePicker.getMinute();
        }
        if (hour < 10)
            sthour = "0"+String.valueOf(hour);
        else sthour = String.valueOf(hour);
        if (minute < 10)
            stminute = "0" +String.valueOf(minute);
        else stminute = String.valueOf(minute);
        String noteTime = sthour + ":" + stminute;

        int amount = Integer.parseInt(txtamount.getText().toString());
        Activities act = new Activities(name,type,noteDate, noteTime,amount, user.getEmail());
        databaseReference = FirebaseDatabase.getInstance().getReference().child("Activities").child(id);
        databaseReference.setValue(act);
        Toast.makeText(getApplicationContext(), "Activity updated", Toast.LENGTH_SHORT).show();
        finish();
        Intent intent = new Intent(getApplicationContext(), ListActivity.class);
        startActivity(intent);
    }
}