package com.omarvm.pruebassqlite.Modelo;

/**
 * Created by userdebian on 3/21/16.
 */
public class ModeloAutoRegistro {
    private int ID_Tabla;
    private String Matricula;
    private String Detalles;
    private String No_Personas;
    private String ActividadesACargo;
    private String entrada;
    private String Salida;


    public int getID_Tabla() {
        return ID_Tabla;
    }

    public void setID_Tabla(int ID_Tabla) {
        this.ID_Tabla = ID_Tabla;
    }

    public String getMatricula() {
        return Matricula;
    }

    public void setMatricula(String matricula) {
        Matricula = matricula;
    }

    public String getDetalles() {
        return Detalles;
    }

    public void setDetalles(String detalles) {
        Detalles = detalles;
    }

    public String getNo_Personas() {
        return No_Personas;
    }

    public void setNo_Personas(String no_Personas) {
        No_Personas = no_Personas;
    }

    public String getActividadesACargo() {
        return ActividadesACargo;
    }

    public void setActividadesACargo(String actividadesACargo) {
        ActividadesACargo = actividadesACargo;
    }

    public String getEntrada() {
        return entrada;
    }

    public void setEntrada(String entrada) {
        this.entrada = entrada;
    }

    public String getSalida() {
        return Salida;
    }

    public void setSalida(String salida) {
        Salida = salida;
    }
}
