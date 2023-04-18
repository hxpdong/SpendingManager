package com.example.spendingmanager;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.text.Layout;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {
    FirebaseAuth mAuth;
    FirebaseUser user;

    Context context;
    ArrayList<Activities> list;

    public MyAdapter(Context context, ArrayList<Activities> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.item_view, parent, false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Activities act = list.get(position);
        int typeid = act.getType();
        holder.id.setText(act.getId());
        holder.name.setText(act.getName());
        if(typeid == 1){
            holder.type.setText("Income");
            holder.typemode.setText("+");
            holder.amount.setTextColor(Color.parseColor("#339900"));
        }
        else if(typeid == 2) {
            holder.type.setText("Spending");
            holder.typemode.setText("-");
            holder.amount.setTextColor(Color.parseColor("#FF0000"));
        }
        holder.date.setText(act.getDate());
        holder.time.setText(act.getTime());
        holder.amount.setText(String.valueOf(act.getAmount()));

        holder.itemView.setOnCreateContextMenuListener(new View.OnCreateContextMenuListener() {
            @Override
            public void onCreateContextMenu(ContextMenu contextMenu, View view, ContextMenu.ContextMenuInfo contextMenuInfo) {
                contextMenu.setHeaderTitle("Select action");
                contextMenu.add(0, R.id.update, 0, "Update").setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(@NonNull MenuItem menuItem) {
                        Intent i = new Intent(context, UpdateActivity.class);
                        i.putExtra("name", act.getName());
                        i.putExtra("type", act.getType());
                        i.putExtra("date", act.getDate());
                        i.putExtra("time", act.getTime());
                        i.putExtra("amount", act.getAmount());
                        i.putExtra("ofuser", act.getOfUser());
                        i.putExtra("id", act.getId());
                        i.putExtra("note", act.getNote());
                        context.startActivity(i);
                        return false;
                    }
                });
                contextMenu.add(0, R.id.delete, 0, "Delete").setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {

                    @Override
                    public boolean onMenuItemClick(@NonNull MenuItem menuItem) {
                        DatabaseReference databaseReference =
                                FirebaseDatabase.getInstance().getReference().child("Activities").child(act.getId());
                        databaseReference.removeValue();
                        list.remove(holder.getAdapterPosition());
                        notifyDataSetChanged();
                        return false;
                    }
                });
            }
        });

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(context, DetailActivity.class);
                i.putExtra("name", act.getName());
                i.putExtra("type", act.getType());
                i.putExtra("date", act.getDate());
                i.putExtra("time", act.getTime());
                i.putExtra("amount", act.getAmount());
                i.putExtra("ofuser", act.getOfUser());
                i.putExtra("id", act.getId());
                i.putExtra("note", act.getNote());
                context.startActivity(i);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{

        TextView id, name, type, date, time, amount, typemode;


        public MyViewHolder(@NonNull View itemView){
            super(itemView);

            id = itemView.findViewById(R.id.idItemID);
            name = itemView.findViewById(R.id.idName);
            type = itemView.findViewById(R.id.idType);
            date = itemView.findViewById(R.id.idDate);
            time = itemView.findViewById(R.id.idTime);
            amount = itemView.findViewById(R.id.idAmount);
            typemode = itemView.findViewById(R.id.idTypeNote);
        }
    }
}
