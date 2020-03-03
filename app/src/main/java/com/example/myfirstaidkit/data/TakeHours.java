package com.example.myfirstaidkit.data;

import android.arch.persistence.room.Entity;

import com.google.gson.annotations.SerializedName;

@Entity(primaryKeys = {"id"})
public class TakeHours {

    @SerializedName("_id")
    private String id;
    @SerializedName("relation_id")
    private String idRelation;
    @SerializedName("hour")
    private Long hour;
    @SerializedName("is_new")
    private boolean isNew;
    @SerializedName("is_removed")
    private boolean isRemoved;

    public TakeHours() {
    }

    public TakeHours(String idRelation, Long hour) {
        this.idRelation = idRelation;
        this.hour = hour;
        isNew = false;
        isRemoved = false;
    }

    public TakeHours(Long hour) {
        this.hour = hour;
        isNew = false;
        isRemoved = false;
    }

    public TakeHours(String id, String idRelation, Long hour) {
        this.id = id;
        this.idRelation = idRelation;
        this.hour = hour;
        isNew = false;
        isRemoved = false;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIdRelation() {
        return idRelation;
    }

    public void setIdRelation(String idRelation) {
        this.idRelation = idRelation;
    }

    public Long getHour() {
        return hour;
    }

    public void setHour(Long hour) {
        this.hour = hour;
    }

    public boolean isNew() {
        return isNew;
    }

    public void setIsNew(boolean isNew) {
        this.isNew = isNew;
    }

    public boolean isRemoved() {
        return isRemoved;
    }

    public void setIsRemoved(boolean isEdited) {
        this.isRemoved = isEdited;
    }

}
