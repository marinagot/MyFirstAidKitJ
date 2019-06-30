package com.example.myfirstaidkit.data;

import android.arch.persistence.room.Entity;

@Entity(primaryKeys = {"idRelation"})
public class Med_Tret_Rel{

    public Integer idRelation;
    public Integer idTreatment;
    public Integer idMedicine;

    public Med_Tret_Rel() {

    }

    public Med_Tret_Rel(Integer idRelation, Integer idTreatment, Integer idMedicine) {
        this.idRelation = idRelation;
        this.idTreatment = idTreatment;
        this.idMedicine = idMedicine;
    }

    public Integer getIdRelation() {return idRelation;}

    public void setIdRelation(Integer idRelation) {this.idRelation = idRelation;}

    public Integer getIdTreatment() {return idTreatment;}

    public void setIdTreatment(Integer idTreatment) {this.idTreatment = idTreatment;}

    public Integer getIdMedicine() {return idMedicine;}

    public void setIdMedicine(Integer idMedicine) {this.idMedicine = idMedicine;}
}