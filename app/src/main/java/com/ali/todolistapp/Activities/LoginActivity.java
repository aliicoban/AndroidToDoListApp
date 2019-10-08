package com.ali.todolistapp.Activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.ali.todolistapp.Models.UserClass;
import com.ali.todolistapp.R;
import com.ali.todolistapp.Retrofit.RetrofitClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by aliç on 4.10.2019.
 */

public class LoginActivity extends AppCompatActivity {
    private EditText email, password;
    private Button loginButton;
    private String getEmail, getPassword;
    private TextView goToRegisterAct;
    private UserClass userClass = new UserClass();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);
        initViews();
        activtyTransaction();
        UserLogin();
    }

    public void initViews() {
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        loginButton = findViewById(R.id.loginButton);
        goToRegisterAct = findViewById(R.id.goToReg);
        loginButton = findViewById(R.id.loginButton);
    }

    public void activtyTransaction() {
        //goToRegisterAct is clicked,goes to RegisterActivity.
        goToRegisterAct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), RegisterActivity.class);
                startActivity(i);
                  /*slide animation between activity*/
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        });
    }

    public void UserLogin() {
    /* e-mail and password are retrieved from EditText.
    Then,It is then added as a parameter to the login function
     */
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getEmail = email.getText().toString();
                getPassword = password.getText().toString();
                login(getEmail, getPassword);
            }
        });
    }

    public void login(final String getEmail, final String getPassword) {
        //email and password must not be empty.If they're empty,being warning with Toast.
        String validPassword = String.valueOf(userClass.LoginNullCheck(getEmail, getPassword));
        if (validPassword.equals("false")) {
            Toast.makeText(getApplicationContext(), "* Fields cannot be crossed empty", Toast.LENGTH_LONG).show();
        } else {
            //request sent to the service with email and password
            retrofit2.Call login = RetrofitClient.getIntance().getApi().user_login(getEmail, getPassword);
            login.enqueue(new Callback() {
                @Override
                public void onResponse(Call call, Response response) {
                    Log.d("Response", "Response is Success");//response is a success
                    UserClass userClass = (UserClass) response.body();
                    //response datas for test is written to logcat
                    //Log.d("email", userClass.email);
                    //Log.d("apiToken", userClass.apiToken);
                    //checking with response email.They're same,it s login is success
                    if (getEmail.equals(userClass.email)) {
                    /*For each login, the system gives a unique token.Token must be saved.
                    This token is used as parameter for all personal transactions.(list,
                    add,remove,logout).
                    */
                        //saving Token with SharedPreferences.
                        SharedPreferences preferences = getSharedPreferences("saveToken", MODE_PRIVATE);
                        SharedPreferences.Editor editor = preferences.edit();
                        editor.putString("apiToken", userClass.apiToken);
                        editor.commit();

                        //goes to ToDolistActivity
                        Toast.makeText(getApplicationContext(), "Login is Success", Toast.LENGTH_SHORT).show();
                        Intent goTodoList = new Intent(getApplicationContext(), ToDoListActivity.class);
                        startActivity(goTodoList);
                          /*slide animation bettween activity transaction*/
                        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                    } else {
                        //failure
                        Toast.makeText(getApplicationContext(), "Email or Password Incorrect", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call call, Throwable t) {
                    Toast.makeText(getApplicationContext(), "Login is Failure,Try again", Toast.LENGTH_SHORT).show();
                }
            });

        }
    }

    @Override
    public void onBackPressed() {
        return;
    }
}
