package com.example.sara.projeto02;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class ListActivity extends AppCompatActivity {

    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference tagsReference;

    private EditText tagEditText;
    private EditText conteudoEditText;

    private void configuraFirebase(){
        firebaseDatabase = FirebaseDatabase.getInstance();
        tagsReference = firebaseDatabase.getReference("tags");
    }

    @Override
    protected void onStart() {
        super.onStart();
        tagsReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                tags.clear();
                for (DataSnapshot filho : dataSnapshot.getChildren()){
                    Write write = filho.getValue(Write.class);
                    write.setTag(filho.getKey());
                    tags.add(write);
                }
                tagsAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(ListActivity.this, getString(R.string.erroFirebase), Toast.LENGTH_SHORT).show();
            }
        });

    }

            ListView tagsListView;
    private ArrayAdapter<Write> tagsAdapter;
    private List <Write> tags;
    private ArrayList<Write> tagsArrayList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        tagEditText      = findViewById(R.id.tagEditText);
        conteudoEditText = findViewById(R.id.conteudoText);

        tagsListView = findViewById(R.id.tagsListView);
        configuraFirebase();

        tags = new Stack<Write>( );

        tagsAdapter = new ArrayAdapter<Write>(this, android.R.layout.simple_list_item_1, tags);
        tagsListView.setAdapter(tagsAdapter);

        tagsListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

                    @Override
                    public boolean onItemLongClick(AdapterView<?> adapterView, View view,final  int position, long l) {

                        AlertDialog.Builder dBuilder = new AlertDialog.Builder(ListActivity.this);

                        dBuilder.setPositiveButton(getString(R.string.deletarTag),
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        Write tagApagar = tags.get(position);
                                        tagsReference.child(tagApagar.getTag()).removeValue();

                                        Toast.makeText(ListActivity.this, getString(R.string.tagRemovida), Toast.LENGTH_SHORT).show();

                                    }
                                }).setNegativeButton(getString(R.string.voltarPagInicial),
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {

                                        Intent k = new Intent(ListActivity.this, MainActivity.class);
                                    }
                                }).create().show();
                        return true;
                    }
                });

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent j = new Intent(ListActivity.this, MainActivity.class);
                startActivity(j);
            }
        });

    }

    private void configuraDatabase() {
        firebaseDatabase = FirebaseDatabase.getInstance();
        tagsReference    = firebaseDatabase.getReference("tags");
    }

}
