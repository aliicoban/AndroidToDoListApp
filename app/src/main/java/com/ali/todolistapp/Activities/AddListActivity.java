package com.ali.todolistapp.Activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.ali.todolistapp.Models.DoList;
import com.ali.todolistapp.R;
import com.ali.todolistapp.Retrofit.RetrofitClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by aliç on 4.10.2019.
 */

public class AddListActivity extends AppCompatActivity {
    private Button addListButton;
    private EditText listName;
    private ImageView close_activity;
    DoList doListClass = new DoList();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_list_activity);
        initViews();
        activityTransactions();
        addDoList();
    }

    public void initViews() {
        close_activity = findViewById(R.id.close);
        addListButton = findViewById(R.id.addListButton);
        listName = findViewById(R.id.listName);
    }

    public void activityTransactions() {
        //goes to to do list
        close_activity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), ToDoListActivity.class);
                startActivity(i);
                  /*slide animation between activity*/
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
            }
        });
    }

    public void addDoList() {
        addListButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //When logging in, the token given by the system is retrieved.
                SharedPreferences preferences = getSharedPreferences("saveToken", MODE_PRIVATE);
                String apiToken = preferences.getString("apiToken", "");
                String getListName = listName.getText().toString();//list name
                //list name must not be empty
                String checkListName = String.valueOf(doListClass.ListNameIsEmpty(getListName));
                if (checkListName.equals("false")) {
                    Toast.makeText(getApplicationContext(), "List name must not be empty", Toast.LENGTH_LONG).show();
                } else {
                    //list name is not empty.
                    //sent request to add list.
                    retrofit2.Call addList = RetrofitClient.getIntance().getApi().add_list(apiToken, getListName);
                    addList.enqueue(new Callback() {
                        @Override
                        public void onResponse(Call call, Response response) {
                            Log.d("onResponse", response.body().toString());
                            Intent i = new Intent(getApplicationContext(), ToDoListActivity.class);
                            startActivity(i);
                          /*slide animation bettween activity*/
                            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                        }

                        @Override
                        public void onFailure(Call call, Throwable t) {
                            //failure
                            Log.d("Failure", t.toString());
                            Toast.makeText(getApplicationContext(), t.toString(), Toast.LENGTH_LONG).show();
                        }
                    });
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        return;
    }
}
