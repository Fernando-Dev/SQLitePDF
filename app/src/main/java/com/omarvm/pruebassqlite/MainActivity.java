package com.omarvm.pruebassqlite;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;

import com.omarvm.pruebassqlite.Adapters.AdapterRegistros;
import com.omarvm.pruebassqlite.Modelo.ModeloAutoRegistro;

import java.util.ArrayList;
import java.util.List;

/*serchview*/
import android.support.v7.widget.SearchView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements android.support.v7.widget.SearchView.OnQueryTextListener{
    DatabaseHelper helper;
    List<ModeloAutoRegistro> dbList;
    RecyclerView recyclerView;
    TextView error_vacio, total_autos;
    //private RecyclerView.Adapter mAdapter;
    private AdapterRegistros mAdapter;
    private RecyclerView.LayoutManager mlayoutManager;
    private  GeneraPDF pdfHandle;
    static boolean bandera;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        /* Monstrando Lista */
        helper = new DatabaseHelper(this);
        dbList = new ArrayList<ModeloAutoRegistro>();
        dbList = helper.getDataFromDB(1);
        int total = dbList.size();

        recyclerView = (RecyclerView) findViewById(R.id.recyclerview_uno);
        error_vacio = (TextView) findViewById(R.id.vacio_dataset);
        total_autos = (TextView) findViewById(R.id.header);
        total_autos.setText("Autos Registrados: "+ total);


        //recyclerView.setHasFixedSize(true);

        mlayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(mlayoutManager);

        mAdapter = new AdapterRegistros(this,dbList);
        //mAdapter.notifyDataSetChanged();
        recyclerView.setAdapter(mAdapter);


        if (dbList.isEmpty()){
            recyclerView.setVisibility(View.INVISIBLE);
            error_vacio.setVisibility(View.VISIBLE);
        }else {
            recyclerView.setVisibility(View.VISIBLE);
            error_vacio.setVisibility(View.INVISIBLE);
        }

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                      //  .setAction("Action", null).show();

                Intent intent = new Intent(getApplicationContext(), AddRegistro.class);
                startActivity(intent);

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        //return true;

        /*Soporte Searchview*/
        final MenuItem item = menu.findItem(R.id.action_buscar_salir);
        final android.support.v7.widget.SearchView searchView = (android.support.v7.widget.SearchView) MenuItemCompat.getActionView(item);
        searchView.setOnQueryTextListener(this);

        MenuItemCompat.setOnActionExpandListener(item, new MenuItemCompat.OnActionExpandListener() {
            @Override
            public boolean onMenuItemActionExpand(MenuItem item) {
                mAdapter.setFiltro(dbList);
                return true;
            }

            @Override
            public boolean onMenuItemActionCollapse(MenuItem item) {
                return true;
            }
        });
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

        if (id == R.id.action_reload){
            Intent intent = getIntent();
            finish();
            startActivity(intent);
        }

        if (id == R.id.action_pdf){
            getCodeAuth();
            if (bandera==true) {
                pdfHandle = new GeneraPDF();
                pdfHandle.ProcesandoPDF(this);
                bandera=false;
            }else {
                Toast.makeText(this,"Codigo Incorrecto",Toast.LENGTH_LONG).show();
            }
        }

        return super.onOptionsItemSelected(item);
    }

    /*Los sisgueintes Metosos Permiten firtar la informacion */
    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        final List<ModeloAutoRegistro> filteredModelList = filtro(dbList,newText);
        mAdapter.setFiltro(filteredModelList);
        return true;
    }

    private List<ModeloAutoRegistro> filtro (List<ModeloAutoRegistro> modelos, String query){
        query = query.toLowerCase();

        final List<ModeloAutoRegistro> listaFiltradaModelos = new ArrayList<>();
        for (ModeloAutoRegistro modelo : modelos){
            final String texto = modelo.getMatricula().toLowerCase();
            if (texto.contains(query)){
                listaFiltradaModelos.add(modelo);
            }
        }
        return listaFiltradaModelos;
    }

    // Peticion de Codigo de Verificacion
    public void getCodeAuth(){
        View view =  (LayoutInflater.from(MainActivity.this)).inflate(R.layout.insert_code_layout,null);
        AlertDialog.Builder alertBuilder =  new AlertDialog.Builder(MainActivity.this);
        alertBuilder.setView(view);
        final EditText userInput = (EditText) view.findViewById(R.id.codigo_value);
        alertBuilder.setCancelable(true).setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String texto = String.valueOf(userInput.getText());
                if (texto.equals("") || texto == null || texto.length() == 0) {
                    Toast.makeText(MainActivity.this, "Debes Ingresar un Codigo", Toast.LENGTH_LONG).show();
                } else if (texto.equals("1234567890")) {
                    bandera = true;
                } else {
                    Toast.makeText(MainActivity.this, "Codigo Incorrecto", Toast.LENGTH_LONG).show();
                    bandera = false;
                }

        }
    });
        Dialog dialog = alertBuilder.create();
        dialog.show();
    }
}
