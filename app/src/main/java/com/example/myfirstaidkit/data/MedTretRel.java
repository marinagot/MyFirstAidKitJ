package com.example.myfirstaidkit.data;

import android.arch.persistence.room.Entity;

import java.util.Date;

@Entity(primaryKeys = {"idRelation"})
public class MedTretRel {

    private long idRelation;
    private long idTreatment;
    private long idMedicine;
    private Integer frequency;
    private Date initialDate;
    private Date finalDate;

    public MedTretRel() {

    }

    public MedTretRel(long idRelation, long idTreatment, long idMedicine, Integer frequency, Date initialDate, Date finalDate) {
        this.idRelation = idRelation;
        this.idTreatment = idTreatment;
        this.idMedicine = idMedicine;
        this.frequency = frequency;
        this.initialDate = initialDate;
        this.finalDate = finalDate;
    }

    public long getIdRelation() {
        return idRelation;
    }

    public void setIdRelation(long idRelation) {
        this.idRelation = idRelation;
    }

    public long getIdTreatment() {
        return idTreatment;
    }

    public void setIdTreatment(long idTreatment) {
        this.idTreatment = idTreatment;
    }

    public long getIdMedicine() {
        return idMedicine;
    }

    public void setIdMedicine(long idMedicine) {
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