package com.example.myfirstaidkit.data;

import android.arch.persistence.room.Entity;

import java.util.Date;

@Entity(primaryKeys = {"id_treatment"})
public class Treatment{

    public Integer idUser;
    public Integer frecuencia;
    public Date fechaInicio;
    public Date fechaFinal;

    public Treatment(Integer idUser, Integer frecuencia, Date fechaInicio, Date fechaFinal) {
         this.idUser = idUser;
        this.frecuencia = frecuencia;
        this.fechaInicio = fechaInicio;
        this.fechaFinal = fechaFinal;
    }

    public Integer getIdUser() {return idUser;}

    public void setIdUser(Integer idUser) {this.idUser = idUser;}

    public Integer getFrecuencia() {return frecuencia;}

    public void setFrecuencia(Integer frecuencia) {this.frecuencia = frecuencia;}

    public Date getFechaInicio() {return fechaInicio;}

    public void setFechaInicio(Date fechaInicio) {this.fechaInicio = fechaInicio;}

    public Date getFechaFinal() {return fechaFinal;}

    public void setFechaFinal(Date fechaFinal) {this.fechaFinal = fechaFinal;}
}