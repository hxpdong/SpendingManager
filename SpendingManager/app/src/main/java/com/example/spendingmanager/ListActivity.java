package com.example.spendingmanager;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

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

public class ListActivity extends AppCompatActivity {
    FloatingActionButton toenter;
    FirebaseAuth mAuth;
    FirebaseUser user;

    RecyclerView recyclerView;
    DatabaseReference databaseReference;
    MyAdapter myAdapter;
    ArrayList<Activities> list;

    int totalnet = 0, income = 0, spending = 0;
    TextView ttnet, ttincome, ttspend;
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
        else {
            actionBar.setTitle("Hello, " + user.getEmail());
        }
        ttincome = findViewById(R.id.ttincome);
        ttspend = findViewById(R.id.ttspending);
        ttnet = findViewById(R.id.nettotal);

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
                list.clear();
                income = 0;
                spending = 0;
                totalnet = 0;
                for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                    Activities act = dataSnapshot.getValue(Activities.class);
                    act.setId(dataSnapshot.getKey());
                    list.add(act);
                }
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
                finish();
            }
        });

    }
}