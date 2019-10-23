package com.example.myfirstaidkit.data;

import android.arch.persistence.room.Entity;

import java.util.Date;

@Entity(primaryKeys = {"idRelation"})
public class MedTretRel {

    private String idRelation;
    private String idTreatment;
    private String idMedicine;
    private Integer frequency;
    private Date initialDate;
    private Date finalDate;

    public MedTretRel() {

    }

    public MedTretRel(String idRelation, String idTreatment, String idMedicine, Integer frequency, Date initialDate, Date finalDate) {
        this.idRelation = idRelation;
        this.idTreatment = idTreatment;
        this.idMedicine = idMedicine;
        this.frequency = frequency;
        this.initialDate = initialDate;
        this.finalDate = finalDate;
    }

    public String getIdRelation() {
        return idRelation;
    }

    public void setIdRelation(String idRelation) {
        this.idRelation = idRelation;
    }

    public String getIdTreatment() {
        return idTreatment;
    }

    public void setIdTreatment(String idTreatment) {
        this.idTreatment = idTreatment;
    }

    public String getIdMedicine() {
        return idMedicine;
    }

    public void setIdMedicine(String idMedicine) {
        this.idMedicine = idMedicine;
    }

    public Integer getFrequency() {
        return frequency;
    }

    public void setFrequency(Integer frequency) {
        this.frequency = frequency;
    }

    public Date getInitialDate() {
        return initialDate;
    }

    public void setInitialDate(Date initialDate) {
        this.initialDate = initialDate;
    }

    public Date getFinalDate() {
        return finalDate;
    }

    public void setFinalDate(Date finalDate) {
        this.finalDate = finalDate;
    }
}