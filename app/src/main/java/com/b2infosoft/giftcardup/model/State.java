package com.b2infosoft.giftcardup.model;

import android.util.Log;

import com.b2infosoft.giftcardup.app.Tags;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Microsoft on 8/25/2016.
 */
public class State {
    private String id;
    private String name;
    private String abbreviation;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAbbreviation() {
        return abbreviation;
    }

    public void setAbbreviation(String abbreviation) {
        this.abbreviation = abbreviation;
    }

    public static State fromJSON(JSONObject jsonObject) {
        Tags tags = Tags.getInstance();
        State stateAll = new State();
        try {
            if (jsonObject.has(tags.STATE_ID)) {
                stateAll.setId(jsonObject.getString(tags.STATE_ID));
            }
            if (jsonObject.has(tags.STATE_NAME)) {
                stateAll.setName(jsonObject.getString(tags.STATE_NAME));
            }
            if (jsonObject.has(tags.STATE_ABBRE)) {
                stateAll.setAbbreviation(jsonObject.getString(tags.STATE_ABBRE));
            }
        } catch (JSONException e) {
            e.printStackTrace();
            Log.d("State", e.getMessage());
        }

        return stateAll;
    }
}
