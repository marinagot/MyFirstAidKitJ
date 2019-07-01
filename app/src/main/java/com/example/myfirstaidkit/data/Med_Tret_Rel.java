package com.example.myfirstaidkit.data;

import android.arch.persistence.room.Entity;

import java.util.Date;

@Entity(primaryKeys = {"idRelation"})
public class Med_Tret_Rel{

    public Integer idRelation;
    public Integer idTreatment;
    public Integer idMedicine;
    public Integer frequency;
    public Date initialDate;
    public Date finalDate;

    public Med_Tret_Rel() {

    }

    public Med_Tret_Rel(Integer idRelation, Integer idTreatment, Integer idMedicine, Integer frequency, Date initialDate, Date finalDate) {
        this.idRelation = idRelation;
        this.idTreatment = idTreatment;
        this.idMedicine = idMedicine;
        this.frequency = frequency;
        this.initialDate = initialDate;
        this.finalDate = finalDate;
    }

    public Integer getIdRelation() {return idRelation;}

    public void setIdRelation(Integer idRelation) {this.idRelation = idRelation;}

    public Integer getIdTreatment() {return idTreatment;}

    public void setIdTreatment(Integer idTreatment) {this.idTreatment = idTreatment;}

    public Integer getIdMedicine() {return idMedicine;}

    public void setIdMedicine(Integer idMedicine) {this.idMedicine = idMedicine;}

    public Integer getFrequency() {return frequency;}

    public void setFrequency(Integer frequency) {this.frequency = frequency;}

    public Date getInitialDate() {return initialDate;}

    public void setInitialDate(Date initialDate) {this.initialDate = initialDate;}

    public Date getFinalDate() {return finalDate;}

    public void setFinalDate(Date finalDate) {this.finalDate = finalDate;}
}