package com.example.notesapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
//import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class homescreen extends AppCompatActivity {



    FloatingActionButton maddnewmed;
    private FirebaseAuth firebaseAuth;

    RecyclerView mrecyclerview;
    LinearLayoutManager linearLayoutManager;
    FirebaseUser firebaseUser;
    FirebaseFirestore firebaseFirestore;
    FirestoreRecyclerAdapter<firebasemodel, NoteViewHolder> noteAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homescreen);

        maddnewmed = findViewById(R.id.addnewmed);





        firebaseAuth = firebaseAuth.getInstance();

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        firebaseFirestore = FirebaseFirestore.getInstance();


        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            ((ActionBar) actionBar).setTitle("MED WATCH");
        }


        maddnewmed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(homescreen.this, toaddnew.class));


            }
        });

        Query query = firebaseFirestore.collection("meds").document(firebaseUser.getUid()).collection("myMeds").orderBy("title", Query.Direction.ASCENDING);
        FirestoreRecyclerOptions<firebasemodel> allusermeds = new FirestoreRecyclerOptions.Builder<firebasemodel>().setQuery(query, firebasemodel.class).build();

        noteAdapter = new FirestoreRecyclerAdapter<firebasemodel, NoteViewHolder>(allusermeds) {
            @Override
            protected void onBindViewHolder(@NonNull NoteViewHolder holder, int position, @NonNull firebasemodel model) {
                ImageView popupbutton=holder.itemView.findViewById(R.id.menupopupbuttom);

                holder.medtitle.setText(model.getTitle());

                String docId=noteAdapter.getSnapshots().getSnapshot(position).getId();

                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent=new Intent(v.getContext(),notedetails.class);

                        intent.putExtra("title",model.getTitle());
                        intent.putExtra("noteId",docId);
                        v.getContext().startActivity(intent);

//                        Toast.makeText(getApplicationContext(),"This is clicked",Toast.LENGTH_SHORT).show();
}
                });

                popupbutton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        PopupMenu popupMenu=new PopupMenu(v.getContext(),v);
                        popupMenu.setGravity(Gravity.END);
                        popupMenu.getMenu().add("Delete").setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                            @Override
                            public boolean onMenuItemClick(@NonNull MenuItem item) {
                                //Toast.makeText(v.getContext(),"This note is deleted",Toast.LENGTH_SHORT).show();
                                DocumentReference documentReference=firebaseFirestore.collection("meds").document(firebaseUser.getUid()).collection("myMeds").document(docId);
                                documentReference.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        Toast.makeText(v.getContext(),"This note is deleted",Toast.LENGTH_SHORT).show();



                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(v.getContext(),"Failed to deleted",Toast.LENGTH_SHORT).show();


                                    }
                                });
                                return false;
                            }
                        });
                        popupMenu.show();
                    }
                });


            }

            @NonNull
            @Override
            public NoteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.notes_layout, parent, false);
                return new NoteViewHolder(view);
            }
        };

        mrecyclerview = findViewById(R.id.recyclerview);
        mrecyclerview.setHasFixedSize(true);
//        staggeredGridLayoutManager = new StaggeredGridLayoutManager(1,StaggeredGridLayoutManager.VERTICAL);
//
//        mrecyclerview.setLayoutManager(staggeredGridLayoutManager);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mrecyclerview.setLayoutManager(linearLayoutManager);

        mrecyclerview.setAdapter(noteAdapter);



    }



    public class NoteViewHolder extends RecyclerView.ViewHolder {
        private TextView medtitle;
        LinearLayout mnotes;


        public NoteViewHolder(@NonNull View itemView) {
            super(itemView);
            medtitle = itemView.findViewById(R.id.medtitle);
            mnotes = itemView.findViewById(R.id.note);

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.logout) {
            firebaseAuth.signOut();
            finish();
            startActivity(new Intent(homescreen.this, MainActivity.class));
        } else {
            System.out.println("Invalid input");

        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onStart() {
        super.onStart();
        noteAdapter.startListening();

    }

    @Override
    protected void onStop() {
        super.onStop();
        if(noteAdapter!=null)
        {
            noteAdapter.startListening();
        }

    }

}


