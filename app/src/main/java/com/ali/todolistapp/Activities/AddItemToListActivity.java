package com.ali.todolistapp.Activities;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.ali.todolistapp.Models.Item;
import com.ali.todolistapp.R;
import com.ali.todolistapp.Retrofit.RetrofitClient;

import java.util.Calendar;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by aliç on 5.10.2019.
 */

public class AddItemToListActivity extends AppCompatActivity {
    private EditText item_name, item_desc;
    private TextView select_deadline_date, select_deadline_time, item_status, item_expired;
    private Button add_item_button;
    private TimePickerDialog timePickerDialog;
    private Calendar calendar;
    int currentHour;
    int currentMinute;
    private String amPm;
    private DatePickerDialog.OnDateSetListener onDateSetListener;
    private ImageView left, item_deadline_time, item_deadline_date;
    private Context context = this;
    Item item = new Item();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_item_tolist_activity);
        initViews();
        activityTransactions();
        addNewItem();
        addDeadLineTime();
        addDeadLineDate();
    }

    public void activityTransactions() {
        left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), ItemListActivity.class);
                startActivity(i);
                  /*slide animation bettween activity*/
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
            }
        });
    }

    public void initViews() {
        left = findViewById(R.id.left);
        item_name = findViewById(R.id.item_name);
        item_desc = findViewById(R.id.item_desc);
        item_status = findViewById(R.id.item_status);
        item_expired = findViewById(R.id.item_expired);
        item_deadline_time = findViewById(R.id.item_deadline_time);
        item_deadline_date = findViewById(R.id.item_deadline_date);
        select_deadline_date = findViewById(R.id.select_deadline_date);
        select_deadline_time = findViewById(R.id.select_deadline_time);
        add_item_button = findViewById(R.id.add_item_button);
    }

    public void addNewItem() {
        add_item_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String getItemName = item_name.getText().toString();
                String getItemDesc = item_desc.getText().toString();
                String getItemStatus = item_status.getText().toString();
                String getDeadlineDate = select_deadline_date.getText().toString();
                String getDeadlineTime = select_deadline_time.getText().toString();
                String getExpired = item_expired.getText().toString();
                //item attributes are not empty.Getting attributes
                String checkItemsIsEmpty = String.valueOf(item.ItemAttrIsEmpty(getItemName, getItemDesc, getItemStatus, getDeadlineDate, getDeadlineTime,getExpired));
                if (checkItemsIsEmpty.equals("false")) {
                    Toast.makeText(getApplicationContext(), "Completely fill out all fields", Toast.LENGTH_LONG).show();
                } else {
                    //When logging in, the token given by the system is retrieved.
                    SharedPreferences preferences_token = getSharedPreferences("saveToken", MODE_PRIVATE);
                    String apiToken = preferences_token.getString("apiToken", "");
                    //id of list to add items to
                    SharedPreferences preferences_id = getSharedPreferences("list_idSave", MODE_PRIVATE);
                    String list_id = preferences_id.getString("list_idGet", "");

                    //sent request to add item
                    retrofit2.Call <Item> add_item = RetrofitClient.getIntance().getApi().add_item(apiToken, list_id, getItemName,
                            getItemDesc, getItemStatus, getDeadlineDate, getDeadlineTime,getExpired);
                    add_item.enqueue(new Callback <Item>() {
                        @Override
                        public void onResponse(Call <Item> call, Response <Item> response) {
                            /*add items created are success,pass a ItemListActivity
                            * */
                            Intent i = new Intent(getApplicationContext(), ItemListActivity.class);
                            startActivity(i);
                              /*slide animation bettween activity*/
                            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                        }

                        @Override
                        public void onFailure(Call <Item> call, Throwable t) {
                            //failure
                            Toast.makeText(getApplicationContext(), "An error has occurred", Toast.LENGTH_LONG).show();
                        }
                    });
                }
            }
        });
    }

    //get time with Calendar Time
    public void addDeadLineTime() {
        item_deadline_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calendar = Calendar.getInstance();
                currentHour = calendar.get(Calendar.HOUR_OF_DAY);
                currentMinute = calendar.get(Calendar.MINUTE);
                timePickerDialog = new TimePickerDialog(AddItemToListActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int hourOfDay, int minutes) {
                        if (hourOfDay >= 12) {
                            amPm = "PM";
                        } else {
                            amPm = "AM";
                        }
                        select_deadline_time.setText(String.format("%02d:%02d", hourOfDay, minutes) + amPm);
                    }
                }, currentHour, currentMinute, false);

                timePickerDialog.show();
            }
        });
    }

    //get date with Calendar View
    public void addDeadLineDate() {
        item_deadline_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Şimdiki zaman bilgilerini alıyoruz. güncel yıl, güncel ay, güncel gün.
                final Calendar calendar = Calendar.getInstance();
                int year = calendar.get(Calendar.YEAR);
                int month = calendar.get(Calendar.MONTH);
                int day = calendar.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dpd = new DatePickerDialog(context,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                                // month value start to 0 (Jan=0, Feb=1,..,Dec=11)
                                // Therefore add 1
                                month += 1;
                                // setEdittext selected day,month and year.
                                select_deadline_date.setText(dayOfMonth + "-" + month + "-" + year);
                            }
                        }, year, month, day); //setDatePicker values
                dpd.setButton(DatePickerDialog.BUTTON_POSITIVE, "Ok", dpd);
                dpd.setButton(DatePickerDialog.BUTTON_NEGATIVE, "Cancel", dpd);
                dpd.show();
            }
        });
    }

    @Override
    public void onBackPressed() {
        return;
    }
}
