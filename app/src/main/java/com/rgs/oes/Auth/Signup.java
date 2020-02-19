package com.rgs.oes.Auth;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.rgs.oes.Home;
import com.rgs.oes.R;

import java.util.Date;

public class Signup extends AppCompatActivity {

    private static final String[] gender = new String[]{
            "Male", "Female"
    };

    private static final String[] year = new String[]{
            "1st", "2nd" , "3rd" , "4th"
    };

    private static final String[] branch = new String[]{
            "CSE", "IT" , "EEE" , "ME" , "CE" , "EC"
    };

    String Gender , Year , Branch, name ,fullname , phoneno , emailID, rollno;
    Button gender_button, Year_button , branch_button, buttom_signup;

     AutoCompleteTextView fullnameSignup;
     AutoCompleteTextView usernameSignup;
     AutoCompleteTextView rollnoSignup;
     AutoCompleteTextView phonenoSignup;
     AutoCompleteTextView emailSignup;
     EditText passwordSignup;
    FirebaseAuth firebaseAuth;
    CharSequence s;
    View parent_view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        setTitle("Sign Up");

        firebaseAuth = FirebaseAuth.getInstance();


        {
            parent_view = findViewById(R.id.signup_layout);
            gender_button = findViewById(R.id.gender);
            Year_button = findViewById(R.id.year);
            branch_button = findViewById(R.id.branch);
            fullnameSignup =  findViewById(R.id.fullname_signup);
            usernameSignup =  findViewById(R.id.username_signup);
            rollnoSignup =  findViewById(R.id.rollno_signup);
            phonenoSignup =  findViewById(R.id.phoneno_signup);
            emailSignup =  findViewById(R.id.email_signup);
            passwordSignup =  findViewById(R.id.password_signup);
            buttom_signup = findViewById(R.id.button_signup);

        }

        buttom_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                emailID = emailSignup.getText().toString();
                fullname = fullnameSignup.getText().toString();
                phoneno = phonenoSignup.getText().toString();
                rollno = rollnoSignup.getText().toString();


                String paswd = passwordSignup.getText().toString();
                name = usernameSignup.getText().toString();

                if (emailID.isEmpty()) {
                    usernameSignup.setError("Set your Username");
                    usernameSignup.requestFocus();
                } else if (name.isEmpty()) {
                    emailSignup.setError("Provide your Email first!");
                    emailSignup.requestFocus();
                } else if (paswd.isEmpty()) {
                    passwordSignup.setError("Set your password");
                    passwordSignup.requestFocus();
                } else if (!(emailID.isEmpty() && paswd.isEmpty() && name.isEmpty())) {
                    firebaseAuth.createUserWithEmailAndPassword(emailID, paswd).addOnCompleteListener(Signup.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (!task.isSuccessful()) {
                                Toast.makeText(Signup.this.getApplicationContext(),
                                        "SignUp unsuccessful: " + task.getException().getMessage(),
                                        Toast.LENGTH_SHORT).show();
                            } else {

                                //Date
                                Date d = new Date();
                                s  = DateFormat.format("MMMM d, yyyy HH:mm:ss", d.getTime());

                                //Storing data to display in the Nav bar and in the app
                                SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("sp",0);
                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                editor.putString("uid" , firebaseAuth.getUid());
                                editor.putString("name" , name);
                                editor.putString("email" , emailID);
                                editor.putString("rollno" , rollno);
                                editor.putString("phoneno" , phoneno);
                                editor.putString("fullname" , fullname);
                                editor.putString("gender" , Gender);
                                editor.putString("year" , Year);
                                editor.putString("branch" , Branch);
                                editor.apply();

                                //To save data in Firebase Database
                                DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Users/" + firebaseAuth.getUid());
                                databaseReference.child("Name").setValue(name);
                                databaseReference.child("RollNO").setValue(rollno);
                                databaseReference.child("PhoneNO").setValue(phoneno);
                                databaseReference.child("Fullname").setValue(fullname);
                                databaseReference.child("Email").setValue(emailID);
                                databaseReference.child("Gender").setValue(Gender);
                                databaseReference.child("Year").setValue(Year);
                                databaseReference.child("Branch").setValue(Branch);
                                databaseReference.child("UID").setValue(firebaseAuth.getUid());
                                databaseReference.child("Date").setValue(s);
                                databaseReference.child("Role").setValue(0);
                                databaseReference.child("Admin").setValue(0);
                                showaccountcreatedDialog();

                            }
                        }
                    });
                } else {
                    Toast.makeText(Signup.this, "Error", Toast.LENGTH_SHORT).show();
                }
            }
        });



    }

    public void clickAction(View view) {
        int id = view.getId();
        switch (id) {
            case R.id.gender:
                GenderDialog();
                break;
            case R.id.year:
                YearDialog();
                break;
            case R.id.branch:
                BranchDialog();
                break;
        }
    }

    private void GenderDialog() {
        Gender = gender[0];
        AlertDialog.Builder builder = new AlertDialog.Builder(this , R.style.ALertdialogmee);
        builder.setTitle("Gender");
        builder.setSingleChoiceItems(gender, 0, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Gender = gender[i];
            }
        });
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                gender_button.setText(Gender);
                Snackbar.make(parent_view, "selected : " + Gender, Snackbar.LENGTH_SHORT).show();
            }
        });
        builder.setNegativeButton("Cancel", null);
        builder.show();
    }

    private void YearDialog() {
        Year = year[0];
        AlertDialog.Builder builder = new AlertDialog.Builder(this , R.style.ALertdialogmee);
        builder.setTitle("Year");
        builder.setSingleChoiceItems(year, 0, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Year = year[i];
            }
        });
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Year_button.setText(Year);
                Snackbar.make(parent_view, "selected : " + Year, Snackbar.LENGTH_SHORT).show();
            }
        });
        builder.setNegativeButton("Cancel", null);
        builder.show();
    }

    private void BranchDialog() {
        Branch = branch[0];
        AlertDialog.Builder builder = new AlertDialog.Builder(this , R.style.ALertdialogmee);
        builder.setTitle("Branch");
        builder.setSingleChoiceItems(branch, 0, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Branch = branch[i];
            }
        });
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                branch_button.setText(Branch);
                Snackbar.make(parent_view, "selected : " + Branch, Snackbar.LENGTH_SHORT).show();
            }
        });
        builder.setNegativeButton("Cancel", null);
        builder.show();
    }

    public void showaccountcreatedDialog() {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.acc_confirmed);
        dialog.setCancelable(true);

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;

        ((AppCompatButton) dialog.findViewById(R.id.bt_close)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               startActivity(new Intent(Signup.this , Home.class));
                finish();
                dialog.dismiss();
            }
        });

        dialog.show();
        dialog.getWindow().setAttributes(lp);
    }


}
