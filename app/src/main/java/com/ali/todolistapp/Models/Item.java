package com.ali.todolistapp.Models;

import android.content.Intent;

import com.google.gson.annotations.SerializedName;

import java.util.Comparator;

/**
 * Created by aliç on 5.10.2019.
 */

public class Item {
    public Item() {
    }

    @SerializedName("id")
    public String id;
    @SerializedName("list_name")
    public String list_name;
    @SerializedName("item_name")
    public String item_name;
    @SerializedName("item_desc")
    public String item_desc;
    @SerializedName("item_status")
    public String item_status;
    @SerializedName("deadline_date")
    public String deadline_date;
    @SerializedName("deadline_time")
    public String deadline_time;
    @SerializedName("created_at")
    public String created_at;
    @SerializedName("expired")
    public String expired;

    public String getItem_name() {
        return item_name;
    }

    public String getList_name() {
        return list_name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setList_name(String list_name) {
        this.list_name = list_name;
    }

    public void setItem_name(String item_name) {
        this.item_name = item_name;
    }

    public String getItem_desc() {
        return item_desc;
    }

    public void setItem_desc(String item_desc) {
        this.item_desc = item_desc;
    }

    public String getItem_status() {
        return item_status;
    }

    public void setItem_status(String item_status) {
        this.item_status = item_status;
    }

    public String getDeadline_date() {
        return deadline_date;
    }

    public void setDeadline_date(String deadline_date) {
        this.deadline_date = deadline_date;
    }

    public String getDeadline_time() {
        return deadline_time;
    }

    public void setDeadline_time(String deadline_time) {
        this.deadline_time = deadline_time;
    }

    public String getExpired() {
        return expired;
    }

    public void setExpired(String expired) {
        this.expired = expired;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public static Comparator <Item> byListName = new Comparator <Item>() {
        @Override
        public int compare(Item o1, Item o2) {
            return o1.getItem_name().compareTo(o2.getItem_name());
        }
    };
    public static Comparator <Item> byCreated_At = new Comparator <Item>() {
        @Override
        public int compare(Item o1, Item o2) {
            return o1.getCreated_at().compareTo(o2.getCreated_at());
        }
    };
    public static Comparator <Item> byDeadline_Time = new Comparator <Item>() {
        @Override
        public int compare(Item o1, Item o2) {
            return o1.getDeadline_time().compareTo(o2.getDeadline_time());
        }
    };
    public static Comparator <Item> byStatus = new Comparator <Item>() {
        @Override
        public int compare(Item o1, Item o2) {
            return o1.getItem_status().compareTo(o2.getItem_status());
        }
    };

    public boolean ItemAttrIsEmpty(String itemName, String itemDesc, String itemStatus, String itemDeadlineDate, String itemDeadlineTime, String itemExpired) {
        if (itemName.equals("") || itemDesc.equals("") || itemStatus.equals("") || itemDeadlineDate.equals("")
                || itemDeadlineTime.equals("") || itemExpired.equals("")) {
            return false;
        }
        return true;
    }

}
