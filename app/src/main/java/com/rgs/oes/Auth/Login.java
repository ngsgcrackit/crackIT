package com.rgs.oes.Auth;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rgs.oes.Home;
import com.rgs.oes.R;

public class Login extends AppCompatActivity {


    public EditText login_username, login_password;
    Button button_login;
    TextView signup;
    FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener authStateListener;
    DatabaseReference databaseReference;
    static public String Name, Rollno, Phoneno, Fullname, Email, Gender, Year, Branch, UID, Role, Admin;
    CountDownTimer mCountDownTimer;
    String userEmail;
    LinearLayout linearLayout,lyt_progress;
    RelativeLayout relativeLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        firebaseAuth = FirebaseAuth.getInstance();


        lyt_progress = (LinearLayout) findViewById(R.id.login_loading);
        lyt_progress.setVisibility(View.GONE);
        login_username = findViewById(R.id.username);
        login_password = findViewById(R.id.password);
        relativeLayout = findViewById(R.id.login_layout);
        signup = (TextView) findViewById(R.id.sign_up);
        button_login = findViewById(R.id.login);


        authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    Toast.makeText(Login.this, "Please wait...", Toast.LENGTH_SHORT).show();
                    lyt_progress.setVisibility(View.VISIBLE);
                    lyt_progress.setAlpha(1.0f);
                    relativeLayout.setVisibility(View.GONE);
                    new Firebaseretrive().execute();
                } else {
                    firebaseAuth.removeAuthStateListener(authStateListener);
                    Toast.makeText(Login.this, "Login to continue", Toast.LENGTH_SHORT).show();
                }
            }
        };
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent I = new Intent(Login.this, Signup.class);
                startActivity(I);
                finish();
            }
        });
        button_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                userEmail = login_username.getText().toString();
                String userPaswd = login_password.getText().toString();

                if (userEmail.isEmpty()) {
                    login_username.setError("Provide your Email first!");
                    login_username.requestFocus();
                } else if (userPaswd.isEmpty()) {
                    login_password.setError("Enter Password!");
                    login_password.requestFocus();
                } else if (userEmail.isEmpty() && userPaswd.isEmpty()) {
                    Toast.makeText(Login.this, "Fields Empty!", Toast.LENGTH_SHORT).show();
                } else if (!(userEmail.isEmpty() && userPaswd.isEmpty())) {
                    firebaseAuth.signInWithEmailAndPassword(userEmail, userPaswd).addOnCompleteListener(Login.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (!task.isSuccessful()) {
                                Toast.makeText(Login.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            } else {
                                lyt_progress.setVisibility(View.VISIBLE);
                                lyt_progress.setAlpha(1.0f);
                                relativeLayout.setVisibility(View.GONE);
                                new Firebaseretrive().execute();
                            }
                        }
                    });
                } else {
                    Toast.makeText(Login.this, "Error", Toast.LENGTH_SHORT).show();
                }
            }
        });
        login_password.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if ((event != null && (event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) || (actionId == EditorInfo.IME_ACTION_DONE)) {
                    //do what you want on the press of 'done'
                    button_login.performClick();
                }
                return false;
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        firebaseAuth.addAuthStateListener(authStateListener);
    }

    private class Firebaseretrive extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... params) {

            //Getting data from Firebase Database
            databaseReference = FirebaseDatabase.getInstance().getReference().child("Users").child(firebaseAuth.getUid());
            databaseReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    //To make sure the data exist
                    if (dataSnapshot.hasChild("Name") && dataSnapshot.hasChild("Email") && dataSnapshot.hasChild("UID")) {
                        Name = dataSnapshot.child("Name").getValue().toString();
                        Email = dataSnapshot.child("Email").getValue().toString();
                        UID = dataSnapshot.child("UID").getValue().toString();
                        Rollno = dataSnapshot.child("RollNO").getValue().toString();
                        Phoneno = dataSnapshot.child("PhoneNO").getValue().toString();
                        Fullname = dataSnapshot.child("Fullname").getValue().toString();
                        Gender = dataSnapshot.child("Gender").getValue().toString();
                        Year = dataSnapshot.child("Year").getValue().toString();
                        Branch = dataSnapshot.child("Branch").getValue().toString();
                        if (dataSnapshot.hasChild("Role")){
                            Role = dataSnapshot.child("Role").getValue().toString();} else {Role = "0";}
                        if (dataSnapshot.hasChild("Admin")){
                            Admin= dataSnapshot.child("Admin").getValue().toString();} else {Admin = "0";}

                    } else {
                        Name = "NO data found";
                        Email = "NO data found";
                        UID = "NO data found";
                        Rollno = "NO data found";
                        Phoneno = "NO data found";
                        Fullname = "NO data found";
                        Gender = "NO data found";
                        Year = "NO data found";
                        Branch = "NO data found";
                        Role = "0";
                        Admin = "0";
                    }

                    //Storing data to display in the Nav bar and in the app
                    SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("sp",0);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("uid" , firebaseAuth.getUid());
                    editor.putString("name" , Name);
                    editor.putString("email" , Email);
                    editor.putString("rollno" , Rollno);
                    editor.putString("phoneno" , Phoneno);
                    editor.putString("fullname" , Fullname);
                    editor.putString("gender" , Gender);
                    editor.putString("year" , Year);
                    editor.putString("branch" , Branch);
                    editor.putString("UID" , UID);
                    editor.putString("Role" , Role);
                    editor.putString("Admin" , Admin);
                    editor.apply();


                    mCountDownTimer = new CountDownTimer(4000, 1000) {
                        public void onTick(long millisUntilFinished) {

                        }

                        public void onFinish() {
                            startActivity(new Intent(Login.this, Home.class));
                            finish();
                        }
                    }.start();

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    databaseError.toException();
                }
            });

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {

        }
    }

}
