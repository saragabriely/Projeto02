package com.example.sara.projeto02;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    // FIREBASE
    FirebaseDatabase firebaseDatabase;
    private DatabaseReference tagsReference, uDatabase;

    // TAG
    private EditText tagEditText;

    // LISTA
    ListView  tagsListView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tagEditText      = findViewById(R.id.tagEditText);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        configuraDatabase();

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final Write write;
                final String tag      = tagEditText.getEditableText().toString();

                if(tag.isEmpty()){      // Caso o campo esteja vazio
                    Toast.makeText(MainActivity.this, getString(R.string.digitarTag), Toast.LENGTH_SHORT).show();
                }
                else{

                    final Intent p = new Intent(MainActivity.this, TagActivity.class);

                    final Bundle bundleFB = new Bundle();

                    uDatabase = firebaseDatabase.getReference().child("tags");

                    uDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {

                            for (DataSnapshot filho : dataSnapshot.getChildren()){

                                Write write = filho.getValue(Write.class);

                                String ss, scont;

                                ss    = filho.child("tag").getValue().toString();
                                scont = filho.child("conteudo").getValue().toString();

                                if(tag.toString() != null){

                                    assert ss != null;
                                    if(!ss.equals(tag)) {

                                        // Caso a tag não exista, a string é passada para a outra actibity,
                                        // e lá será adicionada ao firebase

                                        // Atribui um 'id'
                                        bundleFB.putString("tagET", tag);

                                        // Passa a tag para a outra activity
                                         p.putExtras(bundleFB);

                                         // Direciona para a outra actibity
                                        startActivity(p);

                                        Toast.makeText(MainActivity.this, getString(R.string.tagAdicionada),
                                                Toast.LENGTH_SHORT).show();
                                    }
                                    else{ // Caso a tag exista, Captura a tag e o seu respectivo conteudo

                                        bundleFB.putString("tagFB", tag);
                                        bundleFB.putString("tagFBConteudo", scont);

                                        // E passa os valores para a outra tela
                                        p.putExtras(bundleFB);

                                        // Direciona para a outra activity
                                        startActivity(p);

                                        Toast.makeText(MainActivity.this, getString(R.string.tagExistente), Toast.LENGTH_SHORT).show();
                                    }
                                }
                                else{
                                    Toast.makeText(MainActivity.this, getString(R.string.campoVazio), Toast.LENGTH_SHORT).show();
                                }
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                        }
                    });
                }
            }
        });

        // ----------------------------- LIST ACTIVITY ------------------------------------------
        FloatingActionButton fab1 = (FloatingActionButton) findViewById(R.id.fab1);
        fab1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i = new Intent(MainActivity.this, ListActivity.class);
                startActivity(i);
            }
        });
    }

    private void configuraDatabase() {
        firebaseDatabase = FirebaseDatabase.getInstance();
        tagsReference    = firebaseDatabase.getReference("tags");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
