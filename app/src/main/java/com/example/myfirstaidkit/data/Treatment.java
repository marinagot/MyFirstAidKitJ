package com.example.myfirstaidkit.data;

import android.arch.persistence.room.Entity;

import java.util.Date;

@Entity(primaryKeys = {"id_treatment"})
public class Treatment{

    public Integer id;
    public Integer idUser;
    public String name;
    public Integer frecuencia;
    public Date fechaInicio;
    public Date fechaFinal;

    public Treatment() {

    }

    public Treatment(Integer id, Integer idUser, String name, Integer frecuencia, Date fechaInicio, Date fechaFinal) {
        this.id=id;
        this.idUser = idUser;
        this.name = name;
        this.frecuencia = frecuencia;
        this.fechaInicio = fechaInicio;
        this.fechaFinal = fechaFinal;
    }

    public Integer getId() {return id;}

    public void setId(Integer id) {this.id = id;}

    public Integer getIdUser() {return idUser;}

    public void setIdUser(Integer idUser) {this.idUser = idUser;}

    public String getName() {return name;}

    public void setName(String name) {this.name = name;}

    public Integer getFrecuencia() {return frecuencia;}

    public void setFrecuencia(Integer frecuencia) {this.frecuencia = frecuencia;}

    public Date getFechaInicio() {return fechaInicio;}

    public void setFechaInicio(Date fechaInicio) {this.fechaInicio = fechaInicio;}

    public Date getFechaFinal() {return fechaFinal;}

    public void setFechaFinal(Date fechaFinal) {this.fechaFinal = fechaFinal;}
}