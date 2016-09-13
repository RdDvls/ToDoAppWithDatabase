package com.tiy;

/**
 * Created by RdDvls on 9/12/16.
 */
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import jodd.json.JsonParser;
import jodd.json.JsonSerializer;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.ResourceBundle;
import java.util.Scanner;

import static com.tiy.ToDoDatabase.DB_URL;

public class Controller implements Initializable{
    ToDoDatabase tdDatabase = new ToDoDatabase();
    @FXML
    ListView todoList;

    @FXML
    TextField todoText;

    String fileName;


    ObservableList<ToDoItem> todoItems = FXCollections.observableArrayList();
    ArrayList<ToDoItem> savableList = new ArrayList<ToDoItem>();
//    String fileName = "todos.json";

    public String username;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        System.out.print("Hit enter to begin...");
        Scanner inputScanner = new Scanner(System.in);
        username = inputScanner.nextLine();
        if (username != null && !username.isEmpty()) {
            fileName = username + ".json";
        }
//        System.out.println("Checking existing list ...");
//        ToDoItemList retrievedList = retrieveList();
//        if (retrievedList != null) {
//            for (ToDoItem item : retrievedList.todoItems) {
//                todoItems.add(item);
//            }
//        }
        todoList.setItems(todoItems);
        try {
            selectToDosFromDatabase();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void selectToDosFromDatabase() throws SQLException{
        Connection conn = DriverManager.getConnection(DB_URL);
        for(ToDoItem item : tdDatabase.selectToDos(conn)){
            todoItems.add(item);
        }
        todoList.setItems(todoItems);
    }
    public void saveToDoList() {
        if (todoItems != null && todoItems.size() > 0) {
            System.out.println("Saving " + todoItems.size() + " items in the list");
            savableList = new ArrayList<ToDoItem>(todoItems);
            System.out.println("There are " + savableList.size() + " items in my savable list");
            saveList();
        } else {
            System.out.println("No items in the ToDo List");
        }
    }

    public void addItem() throws SQLException {
        System.out.println("Adding item ...");
        todoItems.add(new ToDoItem(todoText.getText()));
        Connection conn = DriverManager.getConnection(DB_URL);
        tdDatabase.insertToDo(conn,todoText.getText());
        todoText.setText("");
    }

    public void removeItem() {
        ToDoItem todoItem = (ToDoItem)todoList.getSelectionModel().getSelectedItem();
        System.out.println("Removing " + todoItem.text + " ...");
        todoItems.remove(todoItem);
    }

    public void toggleItem() throws SQLException {
        System.out.println("Toggling item ...");
        Connection conn = DriverManager.getConnection(DB_URL);
//        tdDatabase.toggleToDo(conn, 1);


        ToDoItem todoItem = (ToDoItem) todoList.getSelectionModel().getSelectedItem();
        tdDatabase.toggleToDo(conn, todoItem.id);
        if (todoItem != null) {
            todoItem.isDone = !todoItem.isDone;
            todoList.setItems(null);
            todoList.setItems(todoItems);

        }
    }
//
    public void saveList() {
        try {

            // write JSON
            JsonSerializer jsonSerializer = new JsonSerializer().deep(true);
            String jsonString = jsonSerializer.serialize(new ToDoItemList(todoItems));

            System.out.println("JSON = ");
            System.out.println(jsonString);

            File sampleFile = new File(fileName);
            FileWriter jsonWriter = new FileWriter(sampleFile);
            jsonWriter.write(jsonString);
            jsonWriter.close();
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    public ToDoItemList retrieveList() {
        try {

            Scanner fileScanner = new Scanner(new File(fileName));
            fileScanner.useDelimiter("\\Z"); // read the input until the "end of the input" delimiter
            String fileContents = fileScanner.next();
            JsonParser ToDoItemParser = new JsonParser();

            ToDoItemList theListContainer = ToDoItemParser.parse(fileContents, ToDoItemList.class);
            System.out.println("==============================================");
            System.out.println("        Restored previous ToDoItem");
            System.out.println("==============================================");
            return theListContainer;
        } catch (IOException ioException) {

            return null;
        }

    }



}
