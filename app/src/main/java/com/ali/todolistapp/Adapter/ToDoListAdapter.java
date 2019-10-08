package com.ali.todolistapp.Adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ali.todolistapp.Models.DoList;
import com.ali.todolistapp.R;
import com.daimajia.swipe.SwipeLayout;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by aliç on 4.10.2019.
 */

public class ToDoListAdapter extends RecyclerView.Adapter <ToDoListAdapter.ViewHolder> {
    List <DoList> ClassLists;
    private ToDoListAdapter.RecyclerOnItemClickListener list_clik;
    private ToDoListAdapter.RecyclerOnItemClickListener delete_list;
    private ToDoListAdapter.RecyclerOnItemClickListener send_email;
    private ToDoItemListAdapter.OnItemClickListener mListener;

    public interface OnItemClickListener {
        void onDeleteClick(int position);
    }

    public ToDoListAdapter(ArrayList <DoList> ClassLists) {
        this.ClassLists = ClassLists;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public LinearLayout linearLayout;
        public TextView listName, created_at, Delete, SendEmail;
        public SwipeLayout swipeLayout;

        public ViewHolder(View view, final ToDoItemListAdapter.OnItemClickListener listener) {
            super(view);
            swipeLayout = view.findViewById(R.id.swipe);
            Delete = view.findViewById(R.id.Delete);
            SendEmail = view.findViewById(R.id.SendEmail);
            linearLayout = view.findViewById(R.id.linear);
            listName = view.findViewById(R.id.listName);
            created_at = view.findViewById(R.id.created_at);
        }
    }

    public void setList_clik(ToDoListAdapter.RecyclerOnItemClickListener list_clik) {
        this.list_clik = list_clik;
    }

    public void setDelete_list(ToDoListAdapter.RecyclerOnItemClickListener delete_list) {
        this.delete_list = delete_list;
    }

    public void setSend_email(ToDoListAdapter.RecyclerOnItemClickListener send_email) {
        this.send_email = send_email;
    }

    @Override
    public void onBindViewHolder(@NonNull ToDoListAdapter.ViewHolder holder, final int position) {
        final DoList doList = ClassLists.get(position);
        holder.created_at.setText(doList.getCreated_at());
        holder.listName.setText(doList.list_name);
        holder.swipeLayout.setShowMode(SwipeLayout.ShowMode.PullOut);
        holder.swipeLayout.addDrag(SwipeLayout.DragEdge.Right, holder.swipeLayout.findViewById(R.id.bottom_wraper));
        holder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                list_clik.onItemClick(doList, position);
            }
        });
        holder.swipeLayout.getSurfaceView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        holder.Delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                delete_list.onItemClick(doList, position);

                Toast.makeText(v.getContext(), "Deleted", Toast.LENGTH_SHORT).show();
            }
        });
        holder.SendEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                send_email.onItemClick(doList, position);
            }
        });
    }

    @NonNull
    @Override
    public ToDoListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_to_do_list, parent, false);
        return new ToDoListAdapter.ViewHolder(itemView, mListener);
    }

    @Override
    public int getItemCount() {
        return ClassLists.size();
    }

    public void setFilter(ArrayList <DoList> newList) {
        ClassLists = new ArrayList <>();
        ClassLists.addAll(newList);
        notifyDataSetChanged();
    }

    public static class RecyclerOnItemClickListener {
        public void onItemClick(DoList doList, int position) {
        }
    }
}
