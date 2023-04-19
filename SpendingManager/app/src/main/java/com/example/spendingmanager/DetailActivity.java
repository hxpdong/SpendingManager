package com.example.spendingmanager;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class DetailActivity extends AppCompatActivity {
    TextView name, type, timedet, datedet, amount, note;
    Button btnMoveUpt, btnCancel;
    String getname, getdate,gettime,getnote, getid;
    int gettype, getamount;
    ProgressDialog noti;
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

        noti = new ProgressDialog(getApplicationContext());
        View currentView = this.getWindow().getDecorView().findViewById(android.R.id.content);
        currentView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
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
                return false;
            }
        });

        if(!Internet.isOnline(getApplicationContext())){
            noti.setTitle("Internet connection");
            noti.setMessage("You are not connecting to the Internet.\n\nPlease check Internet connection and try again.");
            noti.setCancelable(false);
            noti.setIcon(getResources().getDrawable(R.drawable.nointernet));
            noti.setButton(DialogInterface.BUTTON_NEGATIVE,"Retry",  new DialogInterface.OnClickListener(){
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    noti.dismiss();//dismiss dialog

                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(intent);
                    finish();

                }
            });
            noti.show();
            return;
        }

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