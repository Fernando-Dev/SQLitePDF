package com.omarvm.pruebassqlite.Adapters;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.omarvm.pruebassqlite.DatabaseHelper;
import com.omarvm.pruebassqlite.Modelo.ModeloAutoRegistro;
import com.omarvm.pruebassqlite.R;
import com.omarvm.pruebassqlite.RegistroSalida;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by userdebian on 3/22/16.
 */
public class AdapterRegistros extends RecyclerView.Adapter<AdapterRegistros.ViewHolder> {
    static Context context;
    static List<ModeloAutoRegistro> dbList;
    static RegistroSalida stringSalida;
    static DatabaseHelper helper;
    //int id_tabla;

    public AdapterRegistros(Context context, List<ModeloAutoRegistro> dbList){
        this.dbList = new ArrayList<ModeloAutoRegistro>();
        this.context = context;
        this.dbList = dbList;
    }

    @Override
    public AdapterRegistros.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_registros,null);
        ViewHolder viewHolder = new ViewHolder(itemView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(AdapterRegistros.ViewHolder holder, int position) {
        holder.placas.setText(dbList.get(position).getMatricula());
        holder.detalles.setText(dbList.get(position).getDetalles());
        holder.actividades.setText(dbList.get(position).getActividadesACargo());
        holder.pasajeros.setText(dbList.get(position).getNo_Personas());
        holder.entrada.setText(dbList.get(position).getEntrada());
        if (dbList.get(position).getSalida() == null){holder.salida.setText("--.--.--.--.--");}else {
        holder.salida.setText(dbList.get(position).getSalida());}
        //id_tabla = dbList.get(position).getID_Tabla();
        holder.id_tabla.setText(dbList.get(position).getID_Tabla()+"");
    }

    @Override
    public int getItemCount() {
        return dbList.size();
    }

    /* Metodo Busqueda en Rrecyclerview */
    public void setFiltro(List<ModeloAutoRegistro> listModelos){
        dbList = new ArrayList<>();
        dbList.addAll(listModelos);
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{


        public TextView placas,detalles, actividades, pasajeros,entrada, salida, id_tabla;

        public ViewHolder(View itemView) {
            super(itemView);

            placas = (TextView) itemView.findViewById(R.id.placas);
            detalles = (TextView) itemView.findViewById(R.id.detalles);
            actividades = (TextView) itemView.findViewById(R.id.actividades);
            pasajeros = (TextView) itemView.findViewById(R.id.pasajeros);
            entrada = (TextView) itemView.findViewById(R.id.entrada);
            salida = (TextView) itemView.findViewById(R.id.salida);
            id_tabla = (TextView) itemView.findViewById(R.id.id_tabla_inv);
            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    createDialogo(v).show();
                    return false;
                }
            });


        }


        @Override
        public void onClick(View v) {
            Toast.makeText(context,"Mantenga Precionado para agregar salida... ",Toast.LENGTH_SHORT).show();
            //int position = getAdapterPosition();
            //Log.d("id_table", "ID DE LA TABLA SQL : " + dbList.get(position).getID_Tabla());
        }

        private Dialog createDialogo(View v){
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setCancelable(false);
            builder.setMessage("Auto con Placas: "+dbList.get(getAdapterPosition()).getMatricula());
            builder.setPositiveButton("Validar Salida", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    helper = new DatabaseHelper(context);
                    stringSalida = new RegistroSalida();
                    String datosSalida = stringSalida.getSalida();
                    int id = dbList.get(getAdapterPosition()).getID_Tabla();
                    helper.updateSalida(id, datosSalida);

                    //Toast.makeText(context,"Salida Registrada",Toast.LENGTH_SHORT).show();
                }
            });
            builder.setNegativeButton("Cancelar Salida", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });
            return builder.create();
        }

    }
}
