package com.example.myfirstaidkit.data;

import android.arch.persistence.room.Entity;

import com.google.gson.annotations.SerializedName;

@Entity(primaryKeys = {"id"})
public class MedTretRel {

    @SerializedName("_id")
    private String id;
    @SerializedName("treatment_id")
    private String idTreatment;
    @SerializedName("medicine_id")
    private String idMedicine;
    private Integer frequency;
    @SerializedName("date_start")
    private Long initialDate;
    @SerializedName("date_end")
    private Long finalDate;
    private boolean isNew;

    public MedTretRel() {

    }

    public MedTretRel(String idRelation, String idTreatment, String idMedicine, Integer frequency, Long initialDate, Long finalDate) {
        this.id = idRelation;
        this.idTreatment = idTreatment;
        this.idMedicine = idMedicine;
        this.frequency = frequency;
        this.initialDate = initialDate;
        this.finalDate = finalDate;
        this.isNew = false;
    }

    public String getId() {
        return id;
    }

    public void setId(String idRelation) {
        this.id = idRelation;
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

    public Long getInitialDate() {
        return initialDate;
    }

    public void setInitialDate(Long initialDate) {
        this.initialDate = initialDate;
    }

    public Long getFinalDate() {
        return finalDate;
    }

    public void setFinalDate(Long finalDate) {
        this.finalDate = finalDate;
    }

    public boolean isNew() {
        return isNew;
    }

    public void setIsNew(boolean isNew) {
        this.isNew = isNew;
    }
}