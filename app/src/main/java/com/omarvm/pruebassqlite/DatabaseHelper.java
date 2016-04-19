package com.omarvm.pruebassqlite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

import com.omarvm.pruebassqlite.Modelo.ModeloAutoRegistro;

import java.util.ArrayList;
import java.util.List;

import static android.provider.BaseColumns._ID;

/**
 * Created by userdebian on 3/21/16.
 */
public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "RegistroVehicular";
    private static final int DATABASE_VERSION = 1;
    private static final String NAME_TABLE = "Registros";
    private static final String CREATE_TABLE = "CREATE TABLE "+NAME_TABLE+" ("+_ID+" INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "Matricula TEXT," +
                    "Detalles TEXT," +
                    "NoPersonas TEXT," +
                    "Actividades TEXT," +
                    "Entrada TEXT," +
                    "Salida TEXT)";
    Context context;

   /* public DatabaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }*/

    public DatabaseHelper(Context ctx){
        super(ctx,DATABASE_NAME,null,DATABASE_VERSION);
        this.context = ctx;

    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + NAME_TABLE);

        onCreate(db);
    }

    public void InsertIntoDB(String matricula,String detalles, String no_user, String actividades, String entrada, String salida){

        SQLiteDatabase database = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("Matricula",matricula);
        values.put("Detalles",detalles);
        values.put("NoPersonas",no_user);
        values.put("Actividades",actividades);
        values.put("Entrada",entrada);
        values.put("Salida",salida);

        database.insert(NAME_TABLE, null, values);
        database.close();

        Toast.makeText(context,"Registro Correcto",Toast.LENGTH_SHORT).show();
        Log.d("Insertar","Datos insertados en la base de datos");
    }

    public List<ModeloAutoRegistro> getDataFromDB(int opcion){
        String query = null;
        List<ModeloAutoRegistro> modelList = new ArrayList<ModeloAutoRegistro>();

        if (opcion ==1) {
            query = "SELECT * FROM " + NAME_TABLE + " ORDER BY CAST(_ID AS REAL) DESC";
        }
        if (opcion ==2){
            query = "SELECT * FROM " + NAME_TABLE + " ORDER BY CAST(_ID AS REAL)";
        }

        SQLiteDatabase DB = this.getWritableDatabase();
        Cursor cursor = DB.rawQuery(query,null);

        if (cursor.moveToFirst()){
            do {
                ModeloAutoRegistro modelo = new ModeloAutoRegistro();
                modelo.setID_Tabla(cursor.getInt(0));
                modelo.setMatricula(cursor.getString(1));
                modelo.setDetalles(cursor.getString(2));
                modelo.setNo_Personas(cursor.getString(3));
                modelo.setActividadesACargo(cursor.getString(4));
                modelo.setEntrada(cursor.getString(5));
                modelo.setSalida(cursor.getString(6));

                modelList.add(modelo);
            }while (cursor.moveToNext());
        }

        return modelList;
    }

    public void updateSalida(int id_tabla,String salida){
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("Salida", salida);
        //database.update(NAME_TABLE,values,"Matricula = " + matricula,null);
        if (database.update(NAME_TABLE, values, "_ID = ? AND Salida IS NULL", new String[]{String.valueOf(id_tabla)})>0)
        {Toast.makeText(context, "Salida Registrada",Toast.LENGTH_SHORT).show();}else{Toast.makeText(context,"Salida Registrada Previamente",Toast.LENGTH_SHORT).show();}
        database.close();
    }
}
