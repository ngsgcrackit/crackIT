package com.rgs.oes;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.passchange) {
            forgotpassword();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void update(View view) {

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Users/" + firebaseAuth.getUid());
        databaseReference.child("Name").setValue(editName.getText().toString());
        databaseReference.child("PhoneNO").setValue(editPhPro.getText().toString());
        databaseReference.child("RollNO").setValue(editRollPro.getText().toString());
        databaseReference.child("Gender").setValue(editGenderPro.getText().toString());
    }

    //Forgot password Dialog
    public void forgotpassword() {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); // before
        dialog.setContentView(R.layout.forgotpass);
        dialog.setCancelable(true);

        final EditText email = dialog.findViewById(R.id.passemail);
        email.setText(sharedPreferences.getString("email", "NO data found"));
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        ((AppCompatButton) dialog.findViewById(R.id.bt_close)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                FirebaseAuth.getInstance().sendPasswordResetEmail(email.getText().toString())
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Toast.makeText(Profile.this, "Please Check your mail!", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(Profile.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                dialog.dismiss();
            }
        });

        dialog.show();
        dialog.getWindow().setAttributes(lp);
    }
}
