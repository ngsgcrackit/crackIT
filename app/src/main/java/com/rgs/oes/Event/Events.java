package com.rgs.oes.Event;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rgs.oes.R;

import java.util.ArrayList;
import java.util.List;

public class Events extends AppCompatActivity {

    DatabaseReference databaseReference;
    private List<Model> listData;
    private RecyclerView rv;
    private MyAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_events);
        setTitle("Events");
        databaseReference = FirebaseDatabase.getInstance().getReference();
        adapter = new MyAdapter(Events.this);

        rv=(RecyclerView)findViewById(R.id.rec_view);
        rv.setHasFixedSize(true);
        rv.setLayoutManager(new LinearLayoutManager(this));
        listData=new ArrayList<>();

        databaseReference.child("Events").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                listData.clear();

                for (DataSnapshot childDataSnapshot : dataSnapshot.getChildren()) {
                    String key = childDataSnapshot.getKey();
                    listData.add(new Model(key));
                }
                adapter.setlist(listData);
                rv.setAdapter(adapter);

                // get total available quest
                int size = (int) dataSnapshot.getChildrenCount();
//                TextView usercount = findViewById(R.id.admin_usercount);
//                usercount.setText(size+"");
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
