package com.b2infosoft.giftcardup.model;

import android.util.Log;

import com.b2infosoft.giftcardup.app.Tags;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by rajesh on 10/5/2016.
 */

public class Notification {
    private int id;
    private String message;
    private String image;
    private int senderID;
    private int receiverID;
    private int status;
    private String date;
    private String senderUserName;
    private String timeAgo;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public int getSenderID() {
        return senderID;
    }

    public void setSenderID(int senderID) {
        this.senderID = senderID;
    }

    public int getReceiverID() {
        return receiverID;
    }

    public void setReceiverID(int receiverID) {
        this.receiverID = receiverID;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getSenderUserName() {
        return senderUserName;
    }

    public void setSenderUserName(String senderUserName) {
        this.senderUserName = senderUserName;
    }

    public String getTimeAgo() {
        return timeAgo;
    }

    public void setTimeAgo(String timeAgo) {
        this.timeAgo = timeAgo;
    }

    public static Notification fromJSON(JSONObject object) {
        Notification notification = new Notification();
        Tags tags = Tags.getInstance();
        try {
            if (object.has(tags.ID)) {
                notification.setId(object.getInt(tags.ID));
            }

            if (object.has(tags.MESSAGE)) {
                notification.setMessage(object.getString(tags.MESSAGE));
            }
            if (object.has(tags.IMAGE)) {
                notification.setImage(object.getString(tags.IMAGE));
            }
            if (object.has(tags.SENDER_ID)) {
                notification.setSenderID(object.getInt(tags.SENDER_ID));
            }
            if (object.has(tags.RECEIVER_ID)) {
                notification.setReceiverID(object.getInt(tags.RECEIVER_ID));
            }
            if (object.has(tags.STATUS)) {
                notification.setStatus(object.getInt(tags.STATUS));
            }
            if (object.has(tags.DATE)) {
                notification.setDate(object.getString(tags.DATE));
            }
            if (object.has(tags.SENDER_NAME)) {
                notification.setSenderUserName(object.getString(tags.SENDER_NAME));
            }
            if (object.has(tags.TIME_AGO)) {
                notification.setTimeAgo(object.getString(tags.TIME_AGO));
            }
        } catch (JSONException e) {
            e.printStackTrace();
            Log.e(Notification.class.getName(), e.getMessage());
        }
        return notification;
    }
}
