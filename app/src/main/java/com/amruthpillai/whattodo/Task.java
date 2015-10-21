package com.amruthpillai.whattodo;

import com.parse.ParseClassName;
import com.parse.ParseObject;
import com.parse.ParseUser;

/*
    --------------------------
    | Structure of Task Object
    | extends @ParseObject
    --------------------------
    | String:     taskID
    | String:     title
    | String:     description
    | String:     author
    --------------------------
*/

@ParseClassName("Task")
public class Task extends ParseObject {

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

    // Set User of Task
    public void setUser(ParseUser currentUser) {
        put("user", currentUser);
    }

}
