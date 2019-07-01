package com.example.myfirstaidkit.data;

import android.arch.persistence.room.Entity;

import java.util.Date;

@Entity(primaryKeys = {"id"})
public class Treatment{

    public Integer id;
    public Integer idUser;
    public String name;
    public Integer frequency;
    public Date initialDate;
    public Date finalDate;

    public Treatment() {

    }

    public Treatment(Integer id, Integer idUser, String name, Integer frequency, Date initialDate, Date finalDate) {
        this.id=id;
        this.idUser = idUser;
        this.name = name;
        this.frequency = frequency;
        this.initialDate = initialDate;
        this.finalDate = finalDate;
    }

    public Integer getId() {return id;}

    public void setId(Integer id) {this.id = id;}

    public Integer getIdUser() {return idUser;}

    public void setIdUser(Integer idUser) {this.idUser = idUser;}

    public String getName() {return name;}

    public void setName(String name) {this.name = name;}

    public Integer getFrequency() {return frequency;}

    public void setFrequency(Integer frecuency) {this.frequency = frecuency;}

    public Date getInitialDate() {return initialDate;}

    public void setInitialDate(Date fechaInicio) {this.initialDate = initialDate;}

    public Date getFinalDate() {return finalDate;}

    public void setFinalDate(Date fechaFinal) {this.finalDate = finalDate;}


}