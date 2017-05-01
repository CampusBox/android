package in.exun.campusbox.jsonHandlers;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Created by Anurag145 on 5/1/2017.
 */

public class FollowJsonHandler {
    private JSONArray data;
    public FollowJsonHandler(JSONArray data)
    {
        this.data=data;
    }
    public int Length()
    {
        return data.length();

    }
    public String getTitle(int position)
    {
        try {
            return (new JSONObject(data.getString(position)).getString("title"));
        }catch (Exception e)
        {
           return null;
        }
    }
    public String getCollege(int position)
    {
        try {
            return (new JSONObject(new JSONObject(data.getString(position)).getString("college"))).getString("name");
        }catch (Exception e)
        {
            return null;
        }
           }
    public String getPhoto(int position)
    {
        try {
            return (new JSONObject(data.getString(position)).getString("photo"));
        }catch (Exception e)
        {
            return null;
        }
    }
    public  String getAbout(int position)
    {
        try
        {
             return (new JSONObject(data.getString(position)).getString("about"));
        }catch (Exception e)
        {
            return null;
        }
    }
}
