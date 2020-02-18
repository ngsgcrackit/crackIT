package com.rgs.oes;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.TextView;

public class Profile extends AppCompatActivity {

    SharedPreferences sharedPreferences;
    private TextView profileName;
    private TextView profileEmail;
    private TextView profileRollno;
    private TextView profilePhoneno;
    private TextView profileGender;
    private TextView profileBrandhandyear;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        profileName = (TextView) findViewById(R.id.profile_name);
        profileEmail = (TextView) findViewById(R.id.profile_email);
        profileRollno = (TextView) findViewById(R.id.profile_rollno);
        profilePhoneno = (TextView) findViewById(R.id.profile_phoneno);
        profileGender = (TextView) findViewById(R.id.profile_gender);
        profileBrandhandyear = (TextView) findViewById(R.id.profile_brandhandyear);

        sharedPreferences = getApplicationContext().getSharedPreferences("sp", 0);
        profileName.setText(sharedPreferences.getString("name", "NO data found"));
        profileEmail.setText(sharedPreferences.getString("email", "NO data found"));
        profileRollno.setText(sharedPreferences.getString("rollno", "NO data found"));
        profilePhoneno.setText(sharedPreferences.getString("phoneno", "NO data found"));
        profileBrandhandyear.setText(sharedPreferences.getString("year", "NO data found") + " " + sharedPreferences.getString("branch", "NO data found"));
        profileGender.setText(sharedPreferences.getString("gender", "NO data found"));

    }
}
