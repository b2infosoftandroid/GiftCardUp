package com.b2infosoft.giftcardup.model;

import android.util.Log;

import com.b2infosoft.giftcardup.app.Tags;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

/**
 * Created by rajesh on 8/22/2016.
 */
public class CompanyCategory implements Serializable {
    private int categoryID;
    private String categoryName;

    public int getCategoryID() {
        return categoryID;
    }

    public void setCategoryID(int categoryID) {
        this.categoryID = categoryID;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public CompanyCategory fromJSON(JSONObject object) {
        Tags tags = Tags.getInstance();
        CompanyCategory category = new CompanyCategory();
        try{
            if(object.has(tags.COMPANY_CATEGORY_ID)){
                category.setCategoryID(object.getInt(tags.COMPANY_CATEGORY_ID));
            }
            if(object.has(tags.COMPANY_CATEGORY_NAME)){
                category.setCategoryName(object.getString(tags.COMPANY_CATEGORY_NAME));
            }
        }catch (JSONException e){
            e.printStackTrace();
            Log.e(CompanyCategory.class.getName(),e.getMessage());
        }
        return category;
    }
}
