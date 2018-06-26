package com.example.sara.projeto02;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.provider.DocumentsContract;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
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

public class TagActivity extends AppCompatActivity {


    FirebaseDatabase firebaseDatabase;
    private DatabaseReference tagsReference;

    TextView tagText;
    EditText conteudoEditText;

    ListView                    tagsListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tag);

        tagText          = findViewById(R.id.tagText);
        conteudoEditText = findViewById(R.id.conteudoText);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        configuraDatabase();

              Write write = new Write();
        final Write atualiza = write;

        final String tagFB, tagET, tagFBConteudo, chave;

        Intent i = getIntent();
        Bundle bundleFB;
        bundleFB = i.getExtras();   // Captura os valores enviados pela outra activity

        String conteudoEdit = conteudoEditText.toString();

        if(bundleFB != null){

            // Tag nova
            if(bundleFB.getString("tagET") != null) {

                tagET = bundleFB.getString("tagET");

                tagText.setText(tagET);

                // Adiciona ao Firebase

                chave = tagET;
                write = new Write(chave, tagET,  conteudoEdit);
                tagsReference.child(write.getChave()).setValue(write);

                // Observador
                conteudoEditText.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {

                        atualiza.setConteudo(s.toString());

                        String conteudo = s.toString();

                        Write seraAtualizado = new Write(conteudo);

                        Toast.makeText(TagActivity.this, conteudo, Toast.LENGTH_SHORT).show();

                        tagsReference.child(tagET).setValue(seraAtualizado);
                    }

                    @Override
                    public void afterTextChanged(Editable s) {
                    }
                });
            }
            else{ // Tag existente - Apenas coloca os valores na TextView e no EditText,
                  // atualiza o conteudo de acordo com as mudanças

                tagFB = bundleFB.getString("tagFB");
                tagFBConteudo = bundleFB.getString("tagFBConteudo");

                tagText.setText(tagFB);
                conteudoEditText.setText(tagFBConteudo);

                conteudoEditText.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {

                        atualiza.setConteudo(s.toString());

                        String conteudo = s.toString();

                        Write seraAtualizado = new Write(conteudo);

                       // Toast.makeText(TagActivity.this, conteudo, Toast.LENGTH_SHORT).show();

                        // Atualiza o texto
                        tagsReference.child(tagFB).setValue(seraAtualizado);
                    }

                    @Override
                    public void afterTextChanged(Editable s) {
                    }
                });
            }
        }
        else{
            Toast.makeText(this, getString(R.string.valor_nulo), Toast.LENGTH_SHORT).show();
        }

        // ----------------------------- LIST ACTIVITY ------------------------------------------
        FloatingActionButton fab1 = (FloatingActionButton) findViewById(R.id.fab1);
        fab1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i = new Intent(TagActivity.this, ListActivity.class);
                startActivity(i);
            }
        });

        // ----------------------------- MAIN ACTIVITY ------------------------------------------
        FloatingActionButton fabB = (FloatingActionButton) findViewById(R.id.fabB);
        fabB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i = new Intent(TagActivity.this, MainActivity.class);
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
