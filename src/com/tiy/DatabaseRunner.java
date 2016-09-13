package com.tiy;


import java.sql.Connection;
import java.sql.DriverManager;

import static com.tiy.ToDoDatabase.DB_URL;

/**
 * Created by RdDvls on 9/9/16.
 */
public class DatabaseRunner  {
    public static void main(String[] args) throws Exception {
        ToDoDatabase db = new ToDoDatabase();
        db.init();
    }
}