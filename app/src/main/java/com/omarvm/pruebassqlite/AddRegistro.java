package com.omarvm.pruebassqlite;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.Calendar;

public class AddRegistro extends AppCompatActivity {
    EditText matricula, detalles, pasajeros, actividades;
    Button btnAgregar;
    DatabaseHelper helper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_registro);

        matricula = (EditText) findViewById(R.id.placas_in);
        detalles = (EditText) findViewById(R.id.detalles_in);
        pasajeros = (EditText) findViewById(R.id.pasajeros_in);
        actividades = (EditText) findViewById(R.id.actividades_in);
        btnAgregar = (Button) findViewById(R.id.addRegistroButton);
        btnAgregar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String Smatricula, Sdetalles, Spasajeros, Sactividades, am_pm;
                Smatricula = matricula.getText().toString();
                Smatricula.toUpperCase();
                Sdetalles = detalles.getText().toString();
                Spasajeros = pasajeros.getText().toString();
                Sactividades = actividades.getText().toString();

                if (Smatricula.equals("") || Sdetalles.equals("") || Spasajeros.equals("") || Sactividades.equals("")){
                    Toast.makeText(getApplicationContext(),"Completa Todo el Formulario",Toast.LENGTH_SHORT).show();
                }else{

                    Calendar calendar = Calendar.getInstance();
                    int cHour = calendar.get(Calendar.HOUR);
                    int cMin = calendar.get(Calendar.MINUTE);
                    int cSec = calendar.get(Calendar.SECOND);

                    int Intam_pm = calendar.get(Calendar.AM_PM);
                    if (Intam_pm == 0){am_pm = "AM";}else{am_pm = "PM";}

                    int day = calendar.get(Calendar.DATE);
                    int month = calendar.get(Calendar.MONTH)+1;
                    int year = calendar.get(Calendar.YEAR);

                    String FechaEntrada = cHour + ":"+cMin+":"+cSec+" "+am_pm+" "+day+"/"+month+"/"+year;

                    helper = new DatabaseHelper(AddRegistro.this);
                    helper.InsertIntoDB(Smatricula,Sdetalles,Spasajeros,Sactividades,FechaEntrada,null);

                    matricula.setText("");
                    detalles.setText("");
                    pasajeros.setText("");
                    actividades.setText("");

                    Toast.makeText(getApplicationContext(),"Registro Correcto",Toast.LENGTH_SHORT).show();

                }





            }
        });
    }
}
