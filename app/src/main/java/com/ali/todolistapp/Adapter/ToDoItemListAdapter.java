package com.ali.todolistapp.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ali.todolistapp.Models.DoList;
import com.ali.todolistapp.Models.Item;
import com.ali.todolistapp.R;
import com.daimajia.swipe.SwipeLayout;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by aliç on 4.10.2019.
 */

public class ToDoItemListAdapter extends RecyclerView.Adapter <ToDoItemListAdapter.ViewHolder> {
    private ToDoItemListAdapter.RecyclerOnItemClickListener delete;
    private ToDoItemListAdapter.RecyclerOnItemClickListener completed;
    private ToDoItemListAdapter.RecyclerOnItemClickListener expired;
    List <Item> itemList;

    private OnItemClickListener mListener;

    public interface OnItemClickListener {
        void onDeleteClick(int position);
    }

    public ToDoItemListAdapter(ArrayList <Item> itemList) {
        this.itemList = itemList;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public LinearLayout linearLayout;
        public TextView item_name, item_desc, item_deadline_date, item_deadline_time, item_status, created_at, item_expired;
        public ImageView delete, completed, expired;

        public ViewHolder(View view, final OnItemClickListener listener) {
            super(view);
            linearLayout = view.findViewById(R.id.linear);
            item_name = view.findViewById(R.id.item_name);
            item_desc = view.findViewById(R.id.item_desc);
            item_deadline_date = view.findViewById(R.id.item_deadline_date);
            item_deadline_time = view.findViewById(R.id.item_deadline_time);
            item_status = view.findViewById(R.id.item_status);
            item_expired = view.findViewById(R.id.item_expired);
            created_at = view.findViewById(R.id.created_at);
            completed = view.findViewById(R.id.complete);
            expired = view.findViewById(R.id.expired);
            delete = view.findViewById(R.id.delete);
            delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        int position = getAdapterPosition();
                        // Log.d("position", String.valueOf(position));
                        if (position != RecyclerView.NO_POSITION) {
                            listener.onDeleteClick(position);
                        }
                    }
                }
            });

        }
    }

    public void setCompleted(ToDoItemListAdapter.RecyclerOnItemClickListener completed) {
        this.completed = completed;
    }

    public void setDelete(ToDoItemListAdapter.RecyclerOnItemClickListener delete) {
        this.delete = delete;
    }

    public void setExpired(ToDoItemListAdapter.RecyclerOnItemClickListener expired) {
        this.expired = expired;
    }

    @Override
    public void onBindViewHolder(@NonNull final ToDoItemListAdapter.ViewHolder holder, final int position) {
        final Item item = itemList.get(position);
        holder.item_name.setText(item.item_name);
        holder.item_desc.setText(item.item_desc);
        holder.item_deadline_time.setText(item.deadline_time);
        holder.item_deadline_date.setText(item.deadline_date);
        holder.item_status.setText(item.item_status);
        holder.item_expired.setText(item.expired);
        holder.created_at.setText(item.created_at);

        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                delete.onItemClick(item, position);
            }
        });
        holder.completed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                completed.onItemClick(item, position);
            }
        });
        holder.expired.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                expired.onItemClick(item, position);
            }
        });

    }

    @NonNull
    @Override
    public ToDoItemListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_do_item, parent, false);
        return new ToDoItemListAdapter.ViewHolder(itemView, mListener);
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public void setFilter(ArrayList <Item> newList) {
        itemList = new ArrayList <>();
        itemList.addAll(newList);
        notifyDataSetChanged();
    }

    public static class RecyclerOnItemClickListener {
        public void onItemClick(Item itemList, int position) {
        }
    }
}
