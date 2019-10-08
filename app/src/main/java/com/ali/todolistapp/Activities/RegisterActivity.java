package com.ali.todolistapp.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.ali.todolistapp.Models.UserClass;
import com.ali.todolistapp.R;
import com.ali.todolistapp.Retrofit.RetrofitClient;
import com.google.android.gms.tasks.Task;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by aliç on 4.10.2019.
 */

public class RegisterActivity extends AppCompatActivity {
    private EditText name, email, password, confirm_password;
    private String getName, getEmail, getPassword, getConfirmPassword;
    private Button registerButton;
    private ImageView left;
    private UserClass userClass = new UserClass();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_activity);
        initViews();
        activityTransactions();
        UserRegister();
    }

    public void initViews() {
        name = findViewById(R.id.name);
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        confirm_password = findViewById(R.id.confirmPassword);
        registerButton = findViewById(R.id.registerButton);
        left = findViewById(R.id.left);
    }

    public void activityTransactions() {
        //goes to LoginActivity.
        left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent goLogin = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(goLogin);
                  /*slide animation between activity transaction*/
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
            }
        });
    }

    public void UserRegister() {
    /*name,e-mail,password and confirm password are retrieved from EditText.
      Then,It is then added as a parameter to the register function  */
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getName = name.getText().toString();
                getEmail = email.getText().toString();
                getPassword = password.getText().toString();
                getConfirmPassword = confirm_password.getText().toString();
                register(getName, getEmail, getPassword, getConfirmPassword);
            }
        });
    }

    public void register(final String getName, final String getEmail, final String getPassword, final String getConfirmPassword) {

        //name,email,password and confirm password must not be empty.If they're empty,being warning with Toast.
        String checkInput = String.valueOf(userClass.RegisterNullCheck(getName, getEmail, getPassword, getConfirmPassword));
        if (checkInput.equals("false")) {
            Toast.makeText(getApplicationContext(), "* Fields cannot be crossed empty", Toast.LENGTH_LONG).show();
        } else {
            //email validation section
            /*Validation rules:
            1-)correct email : ali@gmail.com
            2-)with subdomain :ali@gmail.c. (this wrong)
            3-)without ".com" ali@gmail .(this wrong)
            4-)without no user name : @gmail.com (this wrong)
             */
            email.addTextChangedListener(userClass);
            if (!userClass.isValid()) {
                //invalid email
                Toast.makeText(getApplicationContext(), "Invalid email,please enter a valid email", Toast.LENGTH_LONG).show();
            } else {
                //email is valid
                //checking password and confirm password
                String validPassword = String.valueOf(userClass.ConfirmPasswords(getPassword, getConfirmPassword));
                if (validPassword.equals("false")) {
                    //passwords are not match.Being warning with Toast.
                    Toast.makeText(getApplicationContext(), "Passwords are not match", Toast.LENGTH_LONG).show();
                } else {
                    //passwords are match.
                    // request sent to the service with name,email/user name, password,confirm password
                    retrofit2.Call <UserClass> register = RetrofitClient.getIntance().getApi().user_register(getName, getEmail, getPassword, getConfirmPassword);
                    register.enqueue(new Callback <UserClass>() {
                        @Override
                        public void onResponse(Call <UserClass> call, Response <UserClass> response) {
                            UserClass userClass = response.body();
                            //response datas for test is written to logcat
                            Log.d("name", userClass.name);//response name
                            Log.d("email", userClass.email);//response email
                            //checking with response email.They're same,it s register is success
                            if (getEmail.equals(userClass.email)) {
                                //emails are verification success
                                Toast.makeText(getApplicationContext(), "Registration is Success", Toast.LENGTH_LONG).show();
                                Intent goLogin = new Intent(getApplicationContext(), LoginActivity.class);
                                startActivity(goLogin);
                              /*slide animation bettween activity*/
                                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                            } else {
                                //register unsuccessfully
                                Toast.makeText(getApplicationContext(), "Register Unsuccessfully", Toast.LENGTH_LONG).show();
                            }
                        }

                        @Override
                        public void onFailure(Call <UserClass> call, Throwable t) {
                            //register is failure.Being warning with Toast.
                            Toast.makeText(getApplicationContext(), "Failure , try again", Toast.LENGTH_LONG).show();
                        }
                    });
                }
            }
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        return;
    }
}
