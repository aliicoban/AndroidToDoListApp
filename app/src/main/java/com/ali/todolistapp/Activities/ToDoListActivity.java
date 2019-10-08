package com.ali.todolistapp.Activities;

import android.app.SearchManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.ali.todolistapp.Adapter.ToDoListAdapter;
import com.ali.todolistapp.Models.DoList;
import com.ali.todolistapp.Models.Item;
import com.ali.todolistapp.R;
import com.ali.todolistapp.Retrofit.RetrofitClient;
import com.google.android.gms.tasks.Task;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by aliç on 4.10.2019.
 */

public class ToDoListActivity extends AppCompatActivity implements android.support.v7.widget.SearchView.OnQueryTextListener {
    private ToDoListAdapter toDoListAdapter;
    private ArrayList <DoList> doListArrayList = new ArrayList <DoList>();
    private RecyclerView recyclerView;
    private FloatingActionButton floatingActionButton;
    private Intent intent = null, chooser = null;
    private SearchManager searchManager;
    private android.support.v7.widget.SearchView searchView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.to_do_lists_content);
        initViews();
        viewRecycler();
        activityTransactions();
        myAllToDoLists();
        seachView();
        recyclerRemoveItem();
        sendEmail();
    }

    public void initViews() {
        floatingActionButton = findViewById(R.id.fab);
        recyclerView = findViewById(R.id.recycler_view);
    }

    //Recycler View used to list data.
    public void viewRecycler() {
        toDoListAdapter = new ToDoListAdapter(doListArrayList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(toDoListAdapter);
    }

    public void activityTransactions() {
        //if clicked floatingActionButton,goes AddListActivity.
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), AddListActivity.class);
                startActivity(i);
                  /*slide animation bettween activity*/
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        });

        /*When clicking on any created list;
         goes ItemListActivity and displays a list of the list's own items.
        */
        toDoListAdapter.setList_clik(new ToDoListAdapter.RecyclerOnItemClickListener() {
            @Override
            public void onItemClick(final DoList doList, int position) {

                //The id is saved because there will be a list by id of the list.
                SharedPreferences preferences = getSharedPreferences("list_idSave", MODE_PRIVATE);
                SharedPreferences.Editor editor = preferences.edit();
                editor.putString("list_idGet", doList.getId());
                editor.commit();

                //Saving list name,and setText to ItemListActivity
                SharedPreferences preferences_listName = getSharedPreferences("SaveListName", MODE_PRIVATE);
                SharedPreferences.Editor editor_list_name = preferences_listName.edit();
                editor_list_name.putString("getListName", doList.getList_name());
                editor_list_name.commit();

                Intent i = new Intent(getApplicationContext(), ItemListActivity.class);
                startActivity(i);
                  /*slide animation between activity*/
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);

            }
        });
    }

    public void sendEmail() {
        //when clicked send email button;
        toDoListAdapter.setSend_email(new ToDoListAdapter.RecyclerOnItemClickListener() {
            @Override
            public void onItemClick(DoList doList, int position) {

                //When logging in, the token given by the system is retrieved.
                SharedPreferences preferences = getSharedPreferences("saveToken", MODE_PRIVATE);
                String apiToken = preferences.getString("apiToken", "");
                //email subject is specified as list name.Therefore list name is retrieved.
                SharedPreferences preferences_listName = getSharedPreferences("SaveListName", MODE_PRIVATE);
                final String listname = preferences_listName.getString("getListName", "");

                //items added to the list are retrieved with the list id of the list
                retrofit2.Call get_send_to_list = RetrofitClient.getIntance().getApi().get_send_to_list(apiToken, doList.id);
                get_send_to_list.enqueue(new Callback() {
                    @Override
                    public void onResponse(Call call, Response response) {
                        List <Item> lists = (List <Item>) response.body();
                        String[] itemLists = new String[lists.size()];
                        for (int i = 0; i < lists.size(); i++) {
                            itemLists[i] = lists.get(i).getItem_name();//List's items are stored in  array
                        }
                        /*Send Mail section.
                         Getting itemLists are stored to String[] itemLists.
                         Mail subject is a listName.
                         Mail texts are itemLists.
                         */
                        intent = new Intent(Intent.ACTION_SEND);
                        intent.setData(Uri.parse("mailto:"));
                        String[] to = {"diyetblogg@gmail.com"};//to default mail
                        intent.putExtra(Intent.EXTRA_EMAIL, to);//set to
                        intent.putExtra(Intent.EXTRA_SUBJECT, "My To-Do ListName : " + listname);// Mail subject is a listName.
                        intent.putExtra(Intent.EXTRA_TEXT, Arrays.toString(itemLists));//all item list
                        intent.setType("message/rfc822");
                        chooser = Intent.createChooser(intent, "Send Email");
                        startActivity(chooser);
                    }

                    @Override
                    public void onFailure(Call call, Throwable t) {

                    }
                });

            }
        });
    }

    public void recyclerRemoveItem() {
      /*  when click the delete button,
        position and list id is added to the deleteItem() function as a parameter*/
        toDoListAdapter.setDelete_list(new ToDoListAdapter.RecyclerOnItemClickListener() {
            @Override
            public void onItemClick(final DoList doList, int position) {
                deleteItem(position, doList.getId());
            }
        });
    }

    public void deleteItem(int position, String id) {
        /*list id and position are taking.
         */
        doListArrayList.remove(position);//recyclerview removing by position
        toDoListAdapter.notifyDataSetChanged();

        //When logging in, the token given by the system is retrieved.
        SharedPreferences preferences = getSharedPreferences("saveToken", MODE_PRIVATE);
        String apiToken = preferences.getString("apiToken", "");

        //First accesses its own list with token and deletes the list by id
        retrofit2.Call delete_list = RetrofitClient.getIntance().getApi().list_delete(apiToken, id);
        delete_list.enqueue(new Callback() {
            @Override
            public void onResponse(Call call, Response response) {
                //response success and write logcat
                Log.d("Response", "Success");
            }

            @Override
            public void onFailure(Call call, Throwable t) {
                //failure
                Toast.makeText(getApplicationContext(), "An error has occurred", Toast.LENGTH_LONG).show();
            }
        });

    }

    public void myAllToDoLists() {
        //When logging in, the token given by the system is retrieved.
        SharedPreferences preferences = getSharedPreferences("saveToken", MODE_PRIVATE);
        String apiToken = preferences.getString("apiToken", "");

        //when logging, the request is sent with the token given by the system and the to-do list we have added is listed
        final retrofit2.Call <List <DoList>> listCall = RetrofitClient.getIntance().getApi().myLists(apiToken);
        listCall.enqueue(new retrofit2.Callback <List <DoList>>() {
            @Override
            public void onResponse(retrofit2.Call <List <DoList>> call, retrofit2.Response <List <DoList>> response) {
                //Response is a success
                List <DoList> doLists = response.body();
                for (int i = 0; i < doLists.size(); i++) {
                    doListArrayList.add(doLists.get(i));//datas are adding to lists
                }
                toDoListAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(retrofit2.Call <List <DoList>> call, Throwable t) {
                //Failure.Being warning with Toast.
                Toast.makeText(getApplicationContext(), "Failure", Toast.LENGTH_LONG).show();
            }
        });

    }

    //Lists are filter by list name
    public void seachView() {
        searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        searchView = findViewById(R.id.search_view);
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setSubmitButtonEnabled(true);
        searchView.setOnQueryTextListener(this);
        searchView.setQueryHint("List Name");
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        //Lists are filter by list name
        newText = newText.toLowerCase();
        ArrayList <DoList> newList = new ArrayList <>();
        for (DoList doListClass : doListArrayList) {
            String name = doListClass.getList_name().toLowerCase();
            if (name.contains(newText)) {
                newList.add(doListClass);
            }
        }
        toDoListAdapter.setFilter(newList);
        return true;
    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(ToDoListActivity.this);
        builder.setTitle("");
        builder.setMessage("Do you want to close it ?");
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                //exit app
                Intent a = new Intent(Intent.ACTION_MAIN);
                a.addCategory(Intent.CATEGORY_HOME);
                a.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(a);
            }
        });
        builder.show();
    }
}
