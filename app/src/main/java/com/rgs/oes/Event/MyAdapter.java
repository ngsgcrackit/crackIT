package com.rgs.oes.Event;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.rgs.oes.R;
import com.rgs.oes.Test;

import java.util.List;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder>{
private List<Model>listData;
Context context;

public void setlist(List<Model> listData) {
        this.listData = listData;
        }

    MyAdapter(Context context) {
        this.context = context;
    }

@NonNull
@Override
public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.lidt_data,parent,false);
        return new ViewHolder(view);
        }

@Override
public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final Model ld=listData.get(position);
        final String hello = ld.getKey();
        holder.txtid.setText(hello);
//        holder.txtname.setText(ld.getTest());
        holder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, Test.class);
                intent.putExtra("testid" , ld.getKey());
                v.getContext().startActivity(intent);
            }
        });
}

@Override
public int getItemCount() {
        return listData.size();
        }

public class ViewHolder extends RecyclerView.ViewHolder{
    private TextView txtid,txtname,txtmovie;
    CardView cardView;
    LinearLayout linearLayout;
    public ViewHolder(View itemView) {
        super(itemView);
        linearLayout = itemView.findViewById(R.id.linearlayout_events);
        txtid=(TextView)itemView.findViewById(R.id.one);
        txtname=(TextView)itemView.findViewById(R.id.two);
        cardView = itemView.findViewById(R.id.card_view);


    }
}
}