package github.com.anurag145.campusbox.jsonHandlers;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.StringTokenizer;

/**
 * Created by Anurag145 on 4/8/2017.
 */

public class EventJsonHandler {


    private int n=0;
    private JSONObject jsonObject;
    private JSONArray jsonArray;
    private JSONObject metadata;

    public EventJsonHandler()
    {

    }

    public EventJsonHandler(String myjson)
    {  try
        {
            jsonObject =new JSONObject(myjson);
            metadata=jsonObject.getJSONObject("meta");
            jsonArray=jsonObject.getJSONArray("data");
            n=jsonArray.length();
        }catch (Exception e)
        {
          Log.e("shit",e.toString());
        }
        finally {
            if(n==0)
            {

            }
        }
    }
    public String urlPagination()
    {    try {
        return "limit=" + metadata.getString("limit") + "&offset=" + metadata.getString("offset");
    }catch (Exception e)
    {

    }
    return null;
    }
    public  String Date(int position)throws  ParseException
    { try
    {

        JSONObject jsonObject1=jsonArray.getJSONObject(position);
        jsonObject1=jsonObject1.getJSONObject("timings");
        Date date =new SimpleDateFormat("yyyy-MM-dd", Locale.US).parse(new StringTokenizer(jsonObject1.getJSONObject("from")
                .getString("date"),"T").nextToken());
        Calendar calendar=Calendar.getInstance();
        calendar.setTime(date);
        String from_to=new SimpleDateFormat("dd MMM").format(calendar.getTime());
        date=new SimpleDateFormat("yyyy-MM-dd", Locale.US).parse(new StringTokenizer(jsonObject1.getJSONObject("to")
                .getString("date"),"T").nextToken());
        calendar.setTime(date);
        from_to=from_to+" - "+new SimpleDateFormat("dd MMM").format(calendar.getTime());
        Log.e("date",from_to);
        return from_to;
    }catch (Exception e)
    {   Log.e("date",e.toString());
        return null;
    }

    }
    public  String Venue(int position)
    {try
    {

        JSONObject jsonObject1=jsonArray.getJSONObject(position);
        jsonObject1=jsonObject1.getJSONObject("details");
        return jsonObject1.getString("venue");
    }catch (Exception e)
    {
        return null;
    }
    }
    public String Desc(int position)
    {
        try
        {
            JSONObject jsonObject1=jsonArray.getJSONObject(position);
            return jsonObject1.getString("subtitle");
        }catch (Exception e)
        {
            return null;
        }
    }
    public int Length()
    {
        return n;
    }
    public String Title(int position)
    {   try
    {
        JSONObject jsonObject1=jsonArray.getJSONObject(position);
        return jsonObject1.getString("title");
    }catch (Exception e)
    {
        return null;
    }

    }
    public Bitmap Image(int position) {
        try {
            JSONObject jsonObject1=jsonArray.getJSONObject(position);
            String image=jsonObject1.getString("image");
            byte[] decodedString = Base64.decode(image.substring(image.lastIndexOf(',')+1), Base64.DEFAULT);
            Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
            return decodedByte;

        }catch (Exception e)
        {
             Log.e("error",e.toString());
            return null;

        }
    }

}
