package com.rgs.oes;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Profile extends AppCompatActivity {

    SharedPreferences sharedPreferences;
    private TextView profileEmail;
    private EditText editName;
    private EditText editRollPro;
    private EditText editPhPro;
    private EditText editGenderPro;
    private EditText editBranchPro;
    FirebaseAuth firebaseAuth;






    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        firebaseAuth = FirebaseAuth.getInstance();

        profileEmail = (TextView) findViewById(R.id.profile_email);
        editName = (EditText) findViewById(R.id.edit_name);
        editRollPro = (EditText) findViewById(R.id.edit_roll_pro);
        editPhPro = (EditText) findViewById(R.id.edit_ph_pro);
        editGenderPro = (EditText) findViewById(R.id.edit_gender_pro);
        editBranchPro = (EditText) findViewById(R.id.edit_branch_pro);


        sharedPreferences = getApplicationContext().getSharedPreferences("sp", 0);
        editName.setText(sharedPreferences.getString("name", "NO data found"));
        profileEmail.setText(sharedPreferences.getString("email", "NO data found"));
        editRollPro.setText(sharedPreferences.getString("rollno", "NO data found"));
        editPhPro.setText(sharedPreferences.getString("phoneno", "NO data found"));
        editBranchPro.setText(sharedPreferences.getString("year", "NO data found") + " " + sharedPreferences.getString("branch", "NO data found"));
        editGenderPro.setText(sharedPreferences.getString("gender", "NO data found"));

    }

    public void update(View view) {

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Users/" + firebaseAuth.getUid());
        databaseReference.child("Name").setValue(editName.getText().toString());
        databaseReference.child("PhoneNO").setValue(editPhPro.getText().toString());
        databaseReference.child("RollNO").setValue(editRollPro.getText().toString());
        databaseReference.child("Gender").setValue(editGenderPro.getText().toString());
    }
}
