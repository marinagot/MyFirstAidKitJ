package com.example.myfirstaidkit.data;

import java.util.Date;

public class Treatment{

    public Integer id_treatment;
    public Integer idUser;
    public Integer frecuencia;
    public Date fechaInicio;
    public Date fechaFinal;

    public Treatment(Integer id_treatment, Integer idUser, Integer frecuencia, Date fechaInicio, Date fechaFinal) {
        this.id_treatment = id_treatment;
        this.idUser = idUser;
        this.frecuencia = frecuencia;
        this.fechaInicio = fechaInicio;
        this.fechaFinal = fechaFinal;
    }

    public Integer getId_treatment() {return id_treatment;}

    public void setId_treatment(Integer id_treatment) {this.id_treatment = id_treatment;}

    public Integer getIdUser() {return idUser;}

    public void setIdUser(Integer idUser) {this.idUser = idUser;}

    public Integer getFrecuencia() {return frecuencia;}

    public void setFrecuencia(Integer frecuencia) {this.frecuencia = frecuencia;}

    public Date getFechaInicio() {return fechaInicio;}

    public void setFechaInicio(Date fechaInicio) {this.fechaInicio = fechaInicio;}

    public Date getFechaFinal() {return fechaFinal;}

    public void setFechaFinal(Date fechaFinal) {this.fechaFinal = fechaFinal;}
}