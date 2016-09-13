package com.tiy;


import java.util.ArrayList;
import java.util.List;

public class ToDoItemList {
    public ArrayList<ToDoItem> todoItems = new ArrayList<ToDoItem>();

    public ToDoItemList(List<ToDoItem> incomingList) {
        todoItems = new ArrayList<ToDoItem>(incomingList);
    }

    public ToDoItemList() {

    }
}
