package com.example.notesapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class toaddnew extends AppCompatActivity {

    EditText mtitleofmed, mcreatecontentofmed;
    private Button mselecttimebtn;
    FloatingActionButton msavemed;
    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;
    FirebaseFirestore firebaseFirestore;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_toaddnew);




        msavemed=findViewById(R.id.savemed);
        mtitleofmed=findViewById(R.id.titleofmed);
        mselecttimebtn = findViewById(R.id.selecttimebtn);
        mcreatecontentofmed = findViewById(R.id.createcontentofnote);


        androidx.appcompat.widget.Toolbar toolbar = findViewById(R.id.toolbarofcreatenote);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

//        Toolbar toolbar=findViewById(R.id.toolbarofcreatenote);
//        setSupportActionBar(toolbar);
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        firebaseAuth=FirebaseAuth.getInstance();
        firebaseFirestore=FirebaseFirestore.getInstance();
        firebaseUser=firebaseAuth.getInstance().getCurrentUser();

        mselecttimebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(toaddnew.this, AlarmClock.class);
                startActivity(intent);

            }
        });

        msavemed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String title=mtitleofmed.getText().toString();
                String content=mcreatecontentofmed.getText().toString();
                if(title.isEmpty() || content.isEmpty())
                {
                    Toast.makeText(getApplicationContext(),"fill the fields",Toast.LENGTH_SHORT).show();

                }
                else{
                    DocumentReference documentReference=firebaseFirestore.collection("meds").document(firebaseUser.getUid()).collection("myMeds").document();
                    Map<String,Object> note=new HashMap<>();
                    note.put("title",title);
                    note.put("content",content);


                    documentReference.set(note).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            Toast.makeText(getApplicationContext(),"Medicine added successfully",Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(toaddnew.this, homescreen.class));





                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(getApplicationContext(),"failed to add reminder",Toast.LENGTH_SHORT).show();

                        }
                    });
                }

            }
        });

    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if(item.getItemId()==android.R.id.home)
        {
            onBackPressed();

        }
        return super.onOptionsItemSelected(item);
    }
}