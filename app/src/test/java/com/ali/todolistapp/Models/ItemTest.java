package com.ali.todolistapp.Models;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by aliç on 8.10.2019.
 */
public class ItemTest {
    private Item itemClass;

    @Before
    public void setUp() throws Exception {
        itemClass = new Item();
    }

    @Test
    public void itemAttrIsEmpty() throws Exception {
        boolean resul1 = itemClass.ItemAttrIsEmpty("item_name", "item_desc", "item_status",
                "item_deadline_date", "item_deadline_time", "item_expired");
        assertTrue("", resul1);

        boolean result2 = itemClass.ItemAttrIsEmpty("", "item_desc", "item_status",
                "item_deadline_date", "item_deadline_time", "item_expired");
        assertFalse("", result2);
    }

}