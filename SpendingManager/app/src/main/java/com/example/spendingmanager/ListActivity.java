package com.example.spendingmanager;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public class ListActivity extends AppCompatActivity {
    FloatingActionButton toenter;
    FirebaseAuth mAuth;
    FirebaseUser user;

    RecyclerView recyclerView;
    DatabaseReference databaseReference;
    MyAdapter myAdapter;
    ArrayList<Activities> list;
    ProgressDialog noti;
    int totalnet = 0, income = 0, spending = 0;
    TextView ttnet, ttincome, ttspend, loaddt;
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_actionbar, menu);
        return super.onCreateOptionsMenu(menu);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Xử lý sự kiện khi người dùng nhấp vào các mục menu trên ActionBar
        int itemId = item.getItemId();
        switch (itemId) {
            case R.id.userinfo:
                DatabaseReference dbr = FirebaseDatabase.getInstance().getReference("Users");
                Query query = dbr.orderByChild("email").equalTo(user.getEmail());
                query.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                            Users us = dataSnapshot.getValue(Users.class);
                            us.setUid(dataSnapshot.getKey());
                            Intent intent = new Intent(getApplicationContext(),UserInfoActivity.class);
                            intent.putExtra("uid", us.getUid());
                            intent.putExtra("name", us.getName());
                            intent.putExtra("phone", us.getPhone());
                            startActivity(intent);
                        }
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
                return true;
            case R.id.logout:
                FirebaseAuth.getInstance().signOut();
                Intent intent2 = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent2);
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        ActionBar actionBar;
        actionBar = getSupportActionBar();
        ColorDrawable colorDrawable = new ColorDrawable(getResources().getColor(R.color.main_color));
        // Set BackgroundDrawable
        actionBar.setBackgroundDrawable(colorDrawable);
        noti = new ProgressDialog(ListActivity.this);
        noti.setTitle("Assistant");
        noti.setMessage("To update/delete an activity, press and hold this activity.\n\nTo see detail an activity, click on this activity.");
        noti.setButton(DialogInterface.BUTTON_NEGATIVE, "OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                noti.dismiss();//dismiss dialog
            }
        });
        noti.setIcon(getResources().getDrawable(R.drawable.logo));
        noti.setCancelable(true);

        if(!isOnline()){
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

        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        if (user == null){
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);
            finish();
        }
        else {
            actionBar.setTitle("Hello, " + user.getEmail());
        }
        ttincome = findViewById(R.id.ttincome);
        ttspend = findViewById(R.id.ttspending);
        ttnet = findViewById(R.id.nettotal);
        loaddt = findViewById(R.id.txtloaddata);
        loaddt.setVisibility(View.VISIBLE);
        recyclerView = findViewById(R.id.rcListItem);
        databaseReference = FirebaseDatabase.getInstance().getReference("Activities");
        Query query = databaseReference.orderByChild("ofUser").equalTo(user.getEmail());
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        list = new ArrayList<>();
        myAdapter = new MyAdapter(this, list);
        recyclerView.setAdapter(myAdapter);

        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                loaddt.setVisibility(View.VISIBLE);
                list.clear();
                income = 0;
                spending = 0;
                totalnet = 0;
                for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                    Activities act = dataSnapshot.getValue(Activities.class);
                    act.setId(dataSnapshot.getKey());
                    list.add(act);
                }
                Collections.reverse(list);
                for (int i = 0; i< list.size(); i++){
                    if(list.get(i).getType() == 1)
                        income = income + list.get(i).getAmount();
                    else if(list.get(i).getType() == 2)
                        spending = spending + list.get(i).getAmount();
                }
                totalnet = income - spending;
                ttincome.setText(String.valueOf(income) + "VND");
                ttspend.setText(String.valueOf(spending) + "VND");
                if(totalnet < 0) ttnet.setTextColor(Color.parseColor("#FF0000"));
                else ttnet.setTextColor(Color.parseColor("#339900"));
                ttnet.setText(String.valueOf(totalnet) + "VND");
                myAdapter.notifyDataSetChanged();
                loaddt.setVisibility(View.GONE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        toenter = findViewById(R.id.btnToEnter);
        toenter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ListActivity.this, EnterActivity.class);
                startActivity(intent);
                
            }
        });

        findViewById(R.id.btnInfoList).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                noti.show();
                //Toast.makeText(getApplicationContext(), "To update/delete an activity, press and hold this activity", Toast.LENGTH_SHORT).show();
            }
        });
    }
    private Boolean isOnline() {
        ConnectivityManager cm = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo ni = cm.getActiveNetworkInfo();
        if(ni != null && ni.isConnected()) {
            return true;
        }
        return false;
    }
}