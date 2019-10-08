package com.ali.todolistapp.Models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by aliç on 4.10.2019.
 */

public class DoList {
    public DoList() {
    }

    @SerializedName("created_at")
    public String created_at;
    @SerializedName("id")
    public String id;
    @SerializedName("list_name")
    public String list_name;

    public boolean ListNameIsEmpty(String listName) {
        if (listName.equals("")) {
            return false;//list name empty
        }
        return true;//list name is not empty
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getList_name() {
        return list_name;
    }

    public void setList_name(String list_name) {
        this.list_name = list_name;
    }
}
