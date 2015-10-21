package com.amruthpillai.whattodo;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.parse.Parse;
import com.parse.ParseAnalytics;
import com.parse.ParseObject;

public class TodoActivity extends AppCompatActivity {

    // Parse Application ID for WhatToDo
    final String WhatToDoApplicationID = "OtS0RflgUoM13FjeWbtL4BxemkAniHwVbLzVBfRK";
    // Parse Client ID for WhatToDo
    final String WhatToDoClientID = "EoBDlBpMyh6ZduBdXUnPAQTFyX3wtb5IwhQjjE48";
    // Layout Variables
    ListView taskList;
    EditText editTaskTitle, editTaskDescription;
    Button buttonAddNewTask, buttonDismiss;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_todo);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Initializing Layout Variables
        taskList = (ListView) findViewById(R.id.list_task);

        // Register the Task Class with Activity
        ParseObject.registerSubclass(Task.class);

        // Initialize the Parse Application
        Parse.initialize(this, WhatToDoApplicationID, WhatToDoClientID);

        // Analytics and Statistics for the App Usage by Parse
        // Tracks this application being launched (and if this happened as the result of the user opening a push notification, this method sends along information to correlate this open with that push).
        ParseAnalytics.trackAppOpenedInBackground(getIntent());

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.button_newTask);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {

                final AlertDialog.Builder alertDialogBuilder;
                final AlertDialog alertDialog;

                LayoutInflater layoutInflater = (LayoutInflater)
                        TodoActivity.this.getSystemService(LAYOUT_INFLATER_SERVICE);
                final View layoutView = layoutInflater.inflate(R.layout.dialog_new_task,
                        (ViewGroup) findViewById(R.id.relativeLayout_dialogNewTask));

                editTaskTitle = (EditText) layoutView.findViewById(R.id.edit_taskTitle);
                editTaskDescription = (EditText) layoutView.findViewById(R.id.edit_taskDescription);

                alertDialogBuilder = new AlertDialog.Builder(TodoActivity.this);
                alertDialogBuilder.setView(layoutView)
                        .setPositiveButton("Add New Task", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                createTask(layoutView, editTaskTitle.getText().toString(), editTaskDescription.getText().toString());
                            }
                        })
                        .setNegativeButton("Dismiss", null);

                alertDialog = alertDialogBuilder.create();
                alertDialog.show();
            }
        });
    }

    public void createTask(View v, String taskTitle, String taskDescription) {
        if (taskTitle.length() > 0) {
            Task taskObject = new Task();

            // Set a title for the task and set it to be completed later
            taskObject.setTitle(taskTitle);
            taskObject.setDescription(taskDescription);
            taskObject.setCompleted(false);

            // saveEventually() is is a convenience method from Parse, that will queue this object to be saved.
            // That way, if the user does not have a network connection, the task will be uploaded later when they are back online
            taskObject.saveEventually();

        } else {
            Toast.makeText(getApplicationContext(), "You forgot to enter a title!" + taskDescription,
                    Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_todo, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
