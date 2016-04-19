package com.omarvm.pruebassqlite;

import android.content.Context;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import com.omarvm.pruebassqlite.Modelo.ModeloAutoRegistro;
import com.pdfjet.Align;
import com.pdfjet.Cell;
import com.pdfjet.Color;
import com.pdfjet.CoreFont;
import com.pdfjet.Font;
import com.pdfjet.Letter;
import com.pdfjet.PDF;
import com.pdfjet.Page;
import com.pdfjet.Point;
import com.pdfjet.Table;
import com.pdfjet.TextLine;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by userdebian on 3/23/16.
 */
public class GeneraPDF {
        List<ModeloAutoRegistro> listaFull = new ArrayList<>();
        DatabaseHelper helper;

        public void ProcesandoPDF(Context context){


            String state = Environment.getExternalStorageState();
            if (!Environment.MEDIA_MOUNTED.equals(state)){
                Toast.makeText(context,"No se ha detectado memoria SD",Toast.LENGTH_SHORT).show();
            }else {

            File directorio = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
            if (!directorio.exists()){
                directorio.mkdir();
            }

            try {
                File archivoPDF = new File(directorio,"RV"+getDate()+".pdf");
                //Log.d("RUTA",directorio+"/RegistroVehicular.pdf");
                PDF pdf = new PDF(new FileOutputStream(archivoPDF));
                Page page = new Page(pdf,Letter.PORTRAIT);
                //FUENTES
                Font font1 = new Font(pdf, CoreFont.COURIER);
                font1.setSize(7.0f);
                Font headerF = new Font(pdf,CoreFont.COURIER_BOLD);
                headerF.setSize(8.0f);

                //TextLine titulo = new TextLine(font1,"Registro Vehicular");
                //titulo.setPosition(page.getWidth() / 2 - titulo.getWidth() / 2, 40f);
                //titulo.drawOn(page);

                //Llenar COntenido
                Table tabla = new Table();
                //tabla.DATA_HAS_1_HEADER_ROWS;
                List<List<Cell>> tablaDatos = new ArrayList<List<Cell>>();
                List<Cell> headerRow = new ArrayList<Cell>();

                headerRow.add(new Cell(headerF,"No."));
                headerRow.add(new Cell(headerF,"Matricula"));
                headerRow.add(new Cell(headerF,"Modelo"));
                headerRow.add(new Cell(headerF,"Actividades a Realizar"));
                headerRow.add(new Cell(headerF,"Pasajeros"));
                headerRow.add(new Cell(headerF,"Entrada"));
                headerRow.add(new Cell(headerF,"Salida"));

                for (int j=0;j<7;j++){
                    (headerRow.get(j)).setBgColor(Color.gray);
                    (headerRow.get(j)).setTextAlignment(Align.CENTER);
                }

                tablaDatos.add(headerRow);



                helper=new DatabaseHelper(context);
                listaFull = helper.getDataFromDB(2);
                for (int i =0;i<listaFull.size();i++){
                    List<Cell> row = new ArrayList<Cell>();
                    row.add(new Cell(font1,listaFull.get(i).getID_Tabla()+""));
                    row.add(new Cell(font1,listaFull.get(i).getMatricula()));
                    row.add(new Cell(font1,listaFull.get(i).getDetalles()));
                    row.add(new Cell(font1,listaFull.get(i).getActividadesACargo()));
                    row.add(new Cell(font1,listaFull.get(i).getNo_Personas()));
                    row.add(new Cell(font1,listaFull.get(i).getEntrada()));
                    row.add(new Cell(font1,listaFull.get(i).getSalida()));

                    for (int k =0; k<7;k++){
                        row.get(k).setTextAlignment(Align.CENTER);
                    }
                    tablaDatos.add(row);
                }

                //
                tablaDatos = appendMissingCells(tablaDatos,headerF);

                tabla.setData(tablaDatos);
                //tabla.autoAdjustColumnWidths();
                tabla.wrapAroundCellText();
                tabla.setPosition(page.getWidth() / 2 - tabla.getWidth()/2,40f);
                //tabla.drawOn(page);

                /*Seccion para Crear mas Paginas*/
                int numOfPages =tabla.getNumberOfPages(page);
                while (true){
                    Point point = tabla.drawOn(page);
                    if (!tabla.hasMoreData()){
                        tabla.resetRenderedPagesCount();
                        break;
                    }
                    page = new Page(pdf,Letter.PORTRAIT);
                }
                /**/




                pdf.flush();
                Toast.makeText(context,"PDF Guardado en:"+directorio+"RV"+getDate()+".pdf",Toast.LENGTH_LONG).show();

            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(context,"Hubo un Problema Intentelo Mas Tarde...",Toast.LENGTH_SHORT).show();
            }
        }}

        private String getDate(){
            Calendar calendar = Calendar.getInstance();
            int day = calendar.get(Calendar.DATE);
            int mes = calendar.get(Calendar.MONTH)+1;
            int year = calendar.get(Calendar.YEAR);

            return "_"+day+"_"+mes+"_"+year;
        }

    private List<List<Cell>> appendMissingCells(List<List<Cell>> datosTabla,Font font){
            List<Cell> firstRow = datosTabla.get(0);
            int numOfColumns = firstRow.size();
                for (int i=0; i<datosTabla.size(); i++){
                    List<Cell> dataRow = datosTabla.get(i);
                    int dataRowColumns = dataRow.size();
                    if (dataRowColumns < numOfColumns){
                            for (int j =0; j<(numOfColumns - dataRowColumns); j++){
                                dataRow.add(new Cell(font));
                            }
                        dataRow.get(dataRowColumns-1).setColSpan((numOfColumns-dataRowColumns)+1);
                    }
                }
        return datosTabla;
    }
}
