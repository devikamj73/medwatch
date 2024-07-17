package com.example.notesapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.ActionBar;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;


public class forgotpassword extends AppCompatActivity {

    private EditText mforgotpassword;
    private Button mpasswordrecoverbutton;
    private TextView mgobacktologin;

    FirebaseAuth firebaseAuth;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgotpassword);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            // Now you can use the action bar
            actionBar.hide();
        }



        mforgotpassword = findViewById(R.id.forgotpassword);
        mpasswordrecoverbutton = findViewById(R.id.passwordrecoverbutton);
        mgobacktologin = findViewById(R.id.gobacktologin);

        firebaseAuth=FirebaseAuth.getInstance();


        //when go back to login is pressed we should go to main activity page
        mgobacktologin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(forgotpassword.this, MainActivity.class);
                startActivity(intent);
            }
        });
        //when forgot password is pressed we have to send a mail to recover
        mpasswordrecoverbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String mail = mforgotpassword.getText().toString().trim();
                if (mail.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Enter your mail first", Toast.LENGTH_SHORT).show();

                } else {
                    //firebase
                    firebaseAuth.sendPasswordResetEmail(mail).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful())
                            {
                                Toast.makeText(getApplicationContext(),"mail sent, recover your password from mail",Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(forgotpassword.this, MainActivity.class));
                            }
                            else
                            {
                                Toast.makeText(getApplicationContext(),"Wrong mail or account does not exist",Toast.LENGTH_SHORT).show();


                            }

                        }
                    });
                }
            }
        });
    }
}