package com.example.spendingmanager;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class DetailActivity extends AppCompatActivity {
    TextView name, type, timedet, datedet, amount, note;
    Button btnMoveUpt, btnCancel;
    String getname, getdate,gettime,getnote, getid;
    int gettype, getamount;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        name = findViewById(R.id.txtDetailName);
        type = findViewById(R.id.txtDetailType);
        timedet = findViewById(R.id.txtDetailTime);
        datedet = findViewById(R.id.txtDetailDate);
        amount = findViewById(R.id.txtDetailAmount);
        note = findViewById(R.id.txtDetailNote);
        btnMoveUpt = findViewById(R.id.btnDetailUpdate);
        btnCancel = findViewById(R.id.btnDetailCancel);

        getname = getIntent().getStringExtra("name");
        gettype = getIntent().getIntExtra("type", 1);
        getdate = getIntent().getStringExtra("date");
        gettime = getIntent().getStringExtra("time");
        getamount = getIntent().getIntExtra("amount", 0);
        getid = getIntent().getStringExtra("id");
        getnote = getIntent().getStringExtra("note");

        name.setText(getname);
        if(gettype == 1) type.setText("Income");
        else if(gettype ==2) type.setText("Spending");
        datedet.setText(getdate);
        timedet.setText(gettime);
        amount.setText(String.valueOf(getamount) + "VND");
        note.setText(getnote);

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        btnMoveUpt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), UpdateActivity.class);
                i.putExtra("name", getname);
                i.putExtra("type", gettype);
                i.putExtra("date", getdate);
                i.putExtra("time", gettime);
                i.putExtra("amount", getamount);
                i.putExtra("id", getid);
                i.putExtra("note", getnote);
                startActivity(i);
                finish();
            }
        });
    }
}