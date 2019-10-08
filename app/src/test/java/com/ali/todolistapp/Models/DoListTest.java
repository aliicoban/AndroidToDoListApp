package com.ali.todolistapp.Models;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by aliç on 8.10.2019.
 */
public class DoListTest {
    DoList doListClass;

    @Before
    public void setUp() throws Exception {
        doListClass = new DoList();

    }

    @Test
    public void listNameIsEmpty() throws Exception {
        boolean result1 = doListClass.ListNameIsEmpty("to_do_list_name");
        assertTrue("", result1);

        boolean result2 = doListClass.ListNameIsEmpty("");
        assertFalse("", result2);
    }
}