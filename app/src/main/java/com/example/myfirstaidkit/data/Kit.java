package com.example.myfirstaidkit.data;

public class Kit{

    public Integer idRelacion;
    public Integer idTratamiento;
    public Integer idMedicamento;

    public Kit(Integer idRelacion, Integer idTratamiento, Integer idMedicamento) {
        this.idRelacion = idRelacion;
        this.idTratamiento = idTratamiento;
        this.idMedicamento = idMedicamento;
    }

    public Integer getIdRelacion() {return idRelacion;}

    public void setIdRelacion(Integer idRelacion) {this.idRelacion = idRelacion;}

    public Integer getIdTratamiento() {return idTratamiento;}

    public void setIdTratamiento(Integer idTratamiento) {this.idTratamiento = idTratamiento;}

    public Integer getIdMedicamento() {return idMedicamento;}

    public void setIdMedicamento(Integer idMedicamento) {this.idMedicamento = idMedicamento;}
}