package com.amruthpillai.whattodo;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseACL;
import com.parse.ParseAnalytics;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

public class TodoActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {

    // Layout Variables
    ListView taskList;
    EditText editTaskTitle, editTaskDescription;
    TaskAdapter taskAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_todo);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Initializing Layout Variables
        taskList = (ListView) findViewById(R.id.list_task);
        taskAdapter = new TaskAdapter(this, new ArrayList<Task>());
        taskList.setAdapter(taskAdapter);

        // Register the Task Class with Activity
        ParseObject.registerSubclass(Task.class);

        // Check if a User is Logged In
        ParseUser currentUser = ParseUser.getCurrentUser();
        if (currentUser == null) {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
            finish();
        }

        // Analytics and Statistics for the App Usage by Parse
        // Tracks this application being launched (and if this happened as the result of the user opening a push notification, this method sends along information to correlate this open with that push).
        ParseAnalytics.trackAppOpenedInBackground(getIntent());

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.button_newTask);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {

                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(TodoActivity.this);
                AlertDialog alertDialog;

                LayoutInflater layoutInflater = (LayoutInflater)
                        TodoActivity.this.getSystemService(LAYOUT_INFLATER_SERVICE);
                View layoutView = layoutInflater.inflate(R.layout.dialog_new_task,
                        (ViewGroup) findViewById(R.id.relativeLayout_dialogNewTask));

                editTaskTitle = (EditText) layoutView.findViewById(R.id.edit_taskTitle);
                editTaskDescription = (EditText) layoutView.findViewById(R.id.edit_taskDescription);

                alertDialogBuilder.setView(layoutView)
                        .setPositiveButton("Add New Task", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                createTask(editTaskTitle.getText().toString(), editTaskDescription.getText().toString());
                            }
                        })
                        .setNegativeButton("Dismiss", null);

                alertDialog = alertDialogBuilder.create();
                alertDialog.show();

            }
        });

        // Initialize the Listeners to Listen for Clicks and Long Clicks
        taskList.setOnItemClickListener(this);

        // Call data from Parse as soon as onCreate ends
        updateData();
    }

    public void createTask(String taskTitle, String taskDescription) {
        if (taskTitle.length() > 0) {

            Task taskObject = new Task();

            // Set User Access Ownership Settings
            taskObject.setACL(new ParseACL(ParseUser.getCurrentUser()));
            taskObject.setUser(ParseUser.getCurrentUser());

            // Set a title for the task and set it to be completed later
            taskObject.setTitle(taskTitle);
            taskObject.setDescription(taskDescription);

            taskAdapter.insert(taskObject, 0);

            // saveEventually() is is a convenience method from Parse, that will queue this object to be saved.
            // That way, if the user does not have a network connection, the task will be uploaded later when they are back online
            taskObject.saveEventually();

        } else {
            Toast.makeText(getApplicationContext(), "You forgot to enter a title!" + taskDescription,
                    Toast.LENGTH_SHORT).show();
        }
    }

    public void updateData() {
        ParseQuery<Task> parseQuery = ParseQuery.getQuery(Task.class);
        parseQuery.whereEqualTo("user", ParseUser.getCurrentUser());
        parseQuery.setCachePolicy(ParseQuery.CachePolicy.CACHE_THEN_NETWORK);
        parseQuery.findInBackground(new FindCallback<Task>() {

            @Override
            public void done(List<Task> taskItemList, ParseException e) {
                if (taskItemList != null) {
                    taskAdapter.clear();
                    taskAdapter.addAll(taskItemList);
                }
            }

        });
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        final Task taskObject = taskAdapter.getItem(position);

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(TodoActivity.this);
        AlertDialog alertDialog;

        alertDialogBuilder
                .setTitle("Delete Task")
                .setMessage("Are you sure you want to delete this task?")
                .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        taskAdapter.remove(taskObject);
                        taskAdapter.notifyDataSetChanged();
                        taskObject.deleteEventually();
                    }
                })
                .setNegativeButton("Dismiss", null);

        alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_todo, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent;

        switch (item.getItemId()) {
            case R.id.action_logout:
                ParseUser.logOut();
                intent = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(intent);
                finish();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

}
