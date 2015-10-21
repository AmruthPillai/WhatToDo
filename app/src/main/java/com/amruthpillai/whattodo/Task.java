package com.amruthpillai.whattodo;

import com.parse.ParseClassName;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.UUID;

/*
    --------------------------
    | Structure of Task Object
    | extends @ParseObject
    --------------------------
    | String:     taskID
    | String:     title
    | String:     author
    | boolean:    isDraft
    | boolean:    isCompleted
    --------------------------
*/

@ParseClassName("Task")
public class Task extends ParseObject {

    public static ParseQuery<Task> getQuery() {
        return ParseQuery.getQuery(Task.class);
    }

    // Set ID for Task
    public void setTaskID() {
        UUID taskID = UUID.randomUUID();
        put("taskID", taskID.toString());
    }

    // Get ID for Task
    public String getTaskId() {
        return getString("taskID");
    }

    // Get Title of Task
    public String getTitle() {
        return getString("title");
    }

    // Set Title of Task
    public void setTitle(String title) {
        put("title", title);
    }

    // Get Description of Task
    public String getDescription() {
        return getString("description");
    }

    // Set Description of Task
    public void setDescription(String description) {
        put("description", description);
    }

    // Get Author of Task
    public ParseUser getAuthor() {
        return getParseUser("author");
    }

    // Set Author of Task
    public void setAuthor(ParseUser currentUser) {
        put("author", currentUser);
    }

    // Check if Task is in Draft Mode
    public boolean isDraft() {
        return getBoolean("isDraft");
    }

    // Set Task to Draft Mode
    public void setDraft(boolean isDraft) {
        put("isDraft", isDraft);
    }

    // Check if Task is Completed
    public boolean isCompleted() {
        return getBoolean("completed");
    }

    // Set a Task to Completed State
    public void setCompleted(boolean complete) {
        put("completed", complete);
    }

}
