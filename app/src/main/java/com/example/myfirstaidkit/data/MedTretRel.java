package com.example.myfirstaidkit.data;

import android.arch.persistence.room.Entity;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

@Entity(primaryKeys = {"id"})
public class MedTretRel {

    @SerializedName("_id")
    private String id;
    @SerializedName("treatment_id")
    private String idTreatment;
    @SerializedName("medicine_id")
    private String idMedicine;
    @SerializedName("date_start")
    private Long initialDate;
    @SerializedName("date_end")
    private Long finalDate;
    private boolean isNew;
    private boolean isEdited;
    @SerializedName("hours")
    private List<TakeHours> hours;

    public MedTretRel() {

    }

    public MedTretRel(String idRelation, String idTreatment, String idMedicine, Long initialDate, Long finalDate) {
        this.id = idRelation;
        this.idTreatment = idTreatment;
        this.idMedicine = idMedicine;
        this.initialDate = initialDate;
        this.finalDate = finalDate;
        this.hours = new ArrayList<>();
        this.isNew = false;
        this.isEdited = false;
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

    public boolean isEdited() {
        return isEdited;
    }

    public void setisEdited(boolean isEdited) {
        this.isEdited = isEdited;
    }

    public List<TakeHours> getHours() {
        return hours;
    }

    public void setHours(List<TakeHours> hours) {
        this.hours = hours;
    }
}