package com.ali.todolistapp.Models;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

import com.google.gson.annotations.SerializedName;

import java.util.regex.Pattern;

/**
 * Created by aliç on 4.10.2019.
 */

public class UserClass implements TextWatcher {
    public UserClass() {
    }

    @SerializedName("name")
    public String name;
    @SerializedName("email")
    public String email;
    @SerializedName("apiToken")
    public String apiToken;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getApiToken() {
        return apiToken;
    }

    public void setApiToken(String apiToken) {
        this.apiToken = apiToken;
    }

    //email valid section
    public static final Pattern EMAIL_PATTERN = Pattern.compile(
            "[a-zA-Z0-9\\+\\.\\_\\%\\-\\+]{1,256}" +
                    "\\@" +
                    "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}" +
                    "(" +
                    "\\." +
                    "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,25}" +
                    ")+"
    );

    public static boolean isValidEmail(CharSequence email) {
        return email != null && EMAIL_PATTERN.matcher(email).matches();
    }

    private boolean EmailIsValid = false;

    public boolean isValid() {
        return EmailIsValid;
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
    }

    @Override
    public void afterTextChanged(Editable email) {
        EmailIsValid = isValidEmail(email);
    }

    public boolean LoginNullCheck(String email, String password) {

        if (email.equals("") || password.equals("")) {
            return false;
        }
        return true;//true
    }

    public boolean RegisterNullCheck(String name, String email, String password, String confirm_password) {

        if (name.equals("") || email.equals("") || password.equals("") || confirm_password.equals("")) {
            return false;
        }
        return true;//true
    }

    public boolean ConfirmPasswords(String password, String confirm_password) {
        if (password.equals(confirm_password)) {
            return true;
        }
        return false;
    }
}
