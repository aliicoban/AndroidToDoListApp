package com.ali.todolistapp.Activities;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.ali.todolistapp.Adapter.ToDoItemListAdapter;
import com.ali.todolistapp.Models.Item;
import com.ali.todolistapp.R;
import com.ali.todolistapp.Retrofit.RetrofitClient;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by aliç on 4.10.2019.
 */

public class ItemListActivity extends AppCompatActivity implements android.support.v7.widget.SearchView.OnQueryTextListener {
    private TextView list_name;
    private ImageView add_item;
    private ArrayList <Item> itemArrayList = new ArrayList <>();
    private ToDoItemListAdapter toDoItemListAdapter;
    private RecyclerView recyclerView;
    private ImageView left;
    private Button order_item_name, order_item_createdAt, order_item_deadline_time, order_item_status;
    private SearchManager searchManager;
    private android.support.v7.widget.SearchView searchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.item_list_activity);
        initViews();
        activity_transaction();
        viewRecycler();
        getItemList();
        orderItems();
        completedItem();
        expiredItem();
        recyclerRemoveItem();
        searchView();
    }

    public void initViews() {
        SharedPreferences preferences_listName = getSharedPreferences("SaveListName", MODE_PRIVATE);
        String listname = preferences_listName.getString("getListName", "");
        order_item_name = findViewById(R.id.order_item_name);
        order_item_createdAt = findViewById(R.id.order_item_createdAt);
        order_item_deadline_time = findViewById(R.id.order_item_deadline_time);
        order_item_status = findViewById(R.id.order_item_status);
        left = findViewById(R.id.left);
        list_name = findViewById(R.id.listName);
        add_item = findViewById(R.id.add_item);
        list_name.setText(listname);
    }

    public void activity_transaction() {
        //when clicked Add item ,goes to ToDoListActivity
        add_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), AddItemToListActivity.class);
                startActivity(i);
                  /*slide animation between activity*/
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        });
        //when clicked left,goes to ToDoListActivity
        left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), ToDoListActivity.class);
                startActivity(i);
                  /*slide animation bettween activity*/
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
            }
        });
    }

    public void viewRecycler() {
        toDoItemListAdapter = new ToDoItemListAdapter(itemArrayList);
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(toDoItemListAdapter);
    }

    public void getItemList() {
        //When logging in, the token given by the system is retrieved.
        SharedPreferences preferences_token = getSharedPreferences("saveToken", MODE_PRIVATE);
        String apiToken = preferences_token.getString("apiToken", "");
        //list id retrieved
        SharedPreferences preferences_id = getSharedPreferences("list_idSave", MODE_PRIVATE);
        String list_id = preferences_id.getString("list_idGet", "");

        //First accesses its own item list with token and list by list id.And get all item lists
        final retrofit2.Call <List <Item>> itemListCall = RetrofitClient.getIntance().getApi().get_item_list(apiToken, list_id);
        itemListCall.enqueue(new Callback <List <Item>>() {
            @Override
            public void onResponse(Call <List <Item>> call, Response <List <Item>> response) {
                List <Item> items = response.body();
                for (int i = 0; i < items.size(); i++) {
                    itemArrayList.add(items.get(i));//adding items
                }
                toDoItemListAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call <List <Item>> call, Throwable t) {
                Log.d("failure", t.toString());
            }
        });
    }

    public void orderItems() {
        //order by item name
        order_item_name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Collections.sort(itemArrayList, Item.byListName);
                toDoItemListAdapter.notifyDataSetChanged();
            }
        });
        //order by createt At
        order_item_createdAt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Collections.sort(itemArrayList, Item.byCreated_At);
                toDoItemListAdapter.notifyDataSetChanged();
            }
        });
        //order by deadline time
        order_item_deadline_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Collections.sort(itemArrayList, Item.byDeadline_Time);
                toDoItemListAdapter.notifyDataSetChanged();
            }
        });
        //order by status
        order_item_status.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Collections.sort(itemArrayList, Item.byStatus);
                toDoItemListAdapter.notifyDataSetChanged();
            }
        });
    }

    public void recyclerRemoveItem() {
         /*  when click the delete button,
        position and item id is added to the deleteItem() function as a parameter*/
        toDoItemListAdapter.setDelete(new ToDoItemListAdapter.RecyclerOnItemClickListener() {
            @Override
            public void onItemClick(final Item item, int position) {
                deleteItem(position, item.getId());
            }
        });
    }

    public void deleteItem(int position, String id) {
        //item id and position are retrieved.
        itemArrayList.remove(position);
        toDoItemListAdapter.notifyDataSetChanged();
        //foodAdapter.notifyItemRemoved(position);
        SharedPreferences preferences = getSharedPreferences("saveToken", MODE_PRIVATE);
        String apiToken = preferences.getString("apiToken", "");

        //sent request and deleting item by id
        retrofit2.Call delete_item = RetrofitClient.getIntance().getApi().delete_item(apiToken, id);
        delete_item.enqueue(new Callback() {
            @Override
            public void onResponse(Call call, Response response) {
                Item item = (Item) response.body();
                Log.d("response body", response.body().toString());
            }

            @Override
            public void onFailure(Call call, Throwable t) {
                Toast.makeText(getApplicationContext(), "An error has occurred", Toast.LENGTH_LONG).show();
            }
        });
    }

    public void completedItem() {
        //when clicked completed item button,updating status on database and mark Completed
        toDoItemListAdapter.setCompleted(new ToDoItemListAdapter.RecyclerOnItemClickListener() {
            @Override
            public void onItemClick(final Item item, int position) {
                retrofit2.Call completed_item = RetrofitClient.getIntance().getApi().item_completed(item.id);
                completed_item.enqueue(new Callback() {
                    @Override
                    public void onResponse(Call call, Response response) {
                        /*if ,completed is a success.
                        Status updating "Completed"
                          */
                        Intent passAct = new Intent(getApplicationContext(), ItemListActivity.class);
                        startActivity(passAct);
                    }

                    @Override
                    public void onFailure(Call call, Throwable t) {
                        //Failure
                        Toast.makeText(getApplicationContext(), "Failure,tyr again", Toast.LENGTH_LONG).show();
                    }
                });
            }
        });
    }

    public void expiredItem() {
        //when clicked expired item button,updating expired on database and mark Expired
        toDoItemListAdapter.setExpired(new ToDoItemListAdapter.RecyclerOnItemClickListener() {
            @Override
            public void onItemClick(final Item item, int position) {
                retrofit2.Call completed_item = RetrofitClient.getIntance().getApi().item_expired(item.id);
                completed_item.enqueue(new Callback() {
                    @Override
                    public void onResponse(Call call, Response response) {
                        /*if ,request is a success.
                        Expired updating "Expired"
                          */
                        Intent passAct = new Intent(getApplicationContext(), ItemListActivity.class);
                        startActivity(passAct);
                    }
                    @Override
                    public void onFailure(Call call, Throwable t) {
                        //Failure
                        Toast.makeText(getApplicationContext(), "Failure,tyr again", Toast.LENGTH_LONG).show();
                    }
                });
            }
        });
    }

    //Items are filters
    public void searchView() {
        searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        searchView = findViewById(R.id.search_view);
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setSubmitButtonEnabled(true);
        searchView.setOnQueryTextListener(this);
        searchView.setQueryHint("List Items");
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        //items are filter by ; item name,ştem status,item deadline and item expired
        newText = newText.toLowerCase();
        ArrayList <Item> newList = new ArrayList <>();
        for (Item itemListClass : itemArrayList) {
            String item_name = itemListClass.getItem_name().toLowerCase();//by item name
            String item_status = itemListClass.getItem_status().toLowerCase();//by item status
            String item_deadline_date = itemListClass.getDeadline_date().toLowerCase();//by item deadline date
            String item_deadline_time = itemListClass.getDeadline_time().toLowerCase();//by item deadline time
            String item_expired = itemListClass.getExpired().toLowerCase();//by item expired

            if (item_name.contains(newText) || item_status.contains(newText) || item_expired.contains(newText) ||
                    item_deadline_date.contains(newText) || item_deadline_time.contains(newText)) {
                newList.add(itemListClass);
            }
        }
        toDoItemListAdapter.setFilter(newList);
        return true;
    }

    @Override
    public void onBackPressed() {
        return;
    }
}
