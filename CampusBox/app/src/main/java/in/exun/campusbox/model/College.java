package in.exun.campusbox.model;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import in.exun.campusbox.R;

/**
 * Created by ayush on 25/04/17.
 */

public class College {

    public College(){

        try {
            JSONObject object = new JSONObject(String.valueOf(R.raw.colleges));
            JSONArray collegeArray = object.getJSONArray("data");
            ArrayList<String> listdata = new ArrayList<String>();
            if (collegeArray != null) {
                for (int i=0;i<collegeArray.length();i++){
                    Log.d("Adding ", "College: " + collegeArray.getJSONObject(i).getString("name"));
                    listdata.add(collegeArray.getJSONObject(i).getString("name"));
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
