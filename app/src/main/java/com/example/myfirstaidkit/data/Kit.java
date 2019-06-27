package com.example.myfirstaidkit.data;

public class Kit{

    Interger idRelacion;
    Integer idTratamiento;
    Integer idMedicamento;

    public Kit(Interger idRelacion, Integer idTratamiento, Integer idMedicamento) {
        this.idRelacion = idRelacion;
        this.idTratamiento = idTratamiento;
        this.idMedicamento = idMedicamento;
    }

    public Interger getIdRelacion() {return idRelacion;}

    public void setIdRelacion(Interger idRelacion) {this.idRelacion = idRelacion;}

    public Integer getIdTratamiento() {return idTratamiento;}

    public void setIdTratamiento(Integer idTratamiento) {this.idTratamiento = idTratamiento;}

    public Integer getIdMedicamento() {return idMedicamento;}

    public void setIdMedicamento(Integer idMedicamento) {this.idMedicamento = idMedicamento;}
}