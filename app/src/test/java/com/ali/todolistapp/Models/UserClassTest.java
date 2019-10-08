package com.ali.todolistapp.Models;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by aliç on 8.10.2019.
 */
public class UserClassTest {
    UserClass userClass;

    @Before
    public void setup() {
        userClass = new UserClass();
    }

    @Test
    public void EmailCorrectInput() {
        assertTrue(userClass.isValidEmail("ali@gmail.com"));
    }

    @Test
    public void EmailWithSubDomain() {
        assertTrue(userClass.isValidEmail("ali@gmail.c"));
    }

    @Test
    public void EmailWithoutCom() {
        assertFalse(userClass.isValidEmail("ali@gmail"));
    }

    @Test
    public void EmailExtraCharacter() {
        assertFalse(userClass.isValidEmail("ali@gmail..com"));
    }

    @Test
    public void EmailWithoutUserName() {
        assertFalse(userClass.isValidEmail("@gmail.com"));
    }

    @Test
    public void loginNullCheck() throws Exception {
        boolean result1 = userClass.LoginNullCheck("ali", "0123456789");
        assertTrue("", result1);
        boolean result2 = userClass.LoginNullCheck("ali", "");
        assertFalse("", result2);
    }

    @Test
    public void registerNullCheck() throws Exception {
        boolean result1 = userClass.RegisterNullCheck("ali", "ali@gmail.com", "0123456789", "0123456789");
        assertTrue("", result1);

        boolean result2 = userClass.RegisterNullCheck("", "ali@gmail.com", "0123456789", "0123456789");
        assertFalse("", result2);
    }

    @Test
    public void confirmPasswords() throws Exception {
        boolean result1 = userClass.ConfirmPasswords("000000", "000000");
        assertTrue("", result1);
        boolean result2 = userClass.ConfirmPasswords("000000", "000001");
        assertFalse("", result2);
    }
}