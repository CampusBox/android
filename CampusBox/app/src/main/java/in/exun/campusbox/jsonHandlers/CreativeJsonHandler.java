package in.exun.campusbox.jsonHandlers;

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

import in.exun.campusbox.helper.AppConstants;

/**
 * Created by Anurag145 on 4/14/2017.
 */

public class CreativeJsonHandler {

    private static final String TAG = "CreativeJsonHandler";
    private int n = 0;
    private JSONArray data;
    private JSONObject metadata;
    private boolean allowPagination = true;

    public CreativeJsonHandler(String myjson) {
        try {
            JSONObject jsonObject = new JSONObject(myjson);
            metadata = jsonObject.getJSONObject("meta");
            data = jsonObject.getJSONArray("data");
            allowPagination = true;
            n = data.length();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String getJsonArray(int pos){
        try {
            JSONArray array = new JSONArray();
            return array.put(data.getJSONObject(pos)).toString();
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    public CreativeJsonHandler(JSONArray data, JSONObject metadata) {
        this.data = data;
        this.metadata = metadata;
        if (metadata == null) {
            allowPagination = false;
        }
        n = data.length();
    }

    public String urlPagination() {
        try {
            return "limit=" + metadata.getString("limit") + "&offset=" + metadata.getString("offset");
        } catch (Exception e) {
            e.printStackTrace();
            return "limit=6&offset=0";
        }
    }

    public boolean isAllowedPagination() {
        return allowPagination;
    }

    public int getId(int pos) {
        try {
            JSONObject obj = data.getJSONObject(pos);
            return obj.getInt("id");
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    public String getAuthor(int position) {
        try {
            JSONObject user = data.getJSONObject(position).getJSONObject("created").getJSONObject("by");
            return toTitleCase(user.getString("name"));
        } catch (Exception e) {
            return "Ayush Pahwa";
        }
    }

    public String getAuthorId(int position) {
        try {
            JSONObject user = data.getJSONObject(position).getJSONObject("created").getJSONObject("by");
            return user.getString("username");
        } catch (Exception e) {
            return null;
        }
    }

    public String getAuthorImage(int position) {
        try {
            JSONObject user = data.getJSONObject(position).getJSONObject("created").getJSONObject("by");
            return user.getString("image");
        } catch (Exception e) {
            return null;
        }
    }

    public String getPostType(int position) {
        try {
            JSONObject postData = data.getJSONObject(position).getJSONObject("content");
            int typeID = postData.getInt("type");
            return AppConstants.interests[typeID];
        } catch (Exception e) {
            return AppConstants.interests[0];
        }
    }

    public String getAuthorData(int pos) {

        return "<font color='#0570C0'; text-transform = uppercase>" + getAuthor(pos) + "</font> in <font color='#0570C0'>" + getPostType(pos) + "</font>.";
    }

    public String getDate(int position) throws ParseException {
        try {

            JSONObject user = data.getJSONObject(position).getJSONObject("created");
            Date date = new SimpleDateFormat("yyyy-MM-dd", Locale.US).parse(new StringTokenizer(user.getString("at"), "T").nextToken());
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            return new SimpleDateFormat("dd MMM").format(calendar.getTime());
        } catch (Exception e) {
            Log.e("date", e.toString());
            return "29 Apr";
        }

    }

    public Bitmap getCoverImage(int position) {
        try {
            JSONObject obj = data.getJSONObject(position).getJSONObject("Items").getJSONArray("data").getJSONObject(1);
            String image = obj.getString("image");
            byte[] decodedString = Base64.decode(image.substring(image.lastIndexOf(',') + 1), Base64.DEFAULT);
            return BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
        } catch (Exception e) {
            Log.e("error", e.toString());
            return null;

        }
    }

    public String getTitle(int position) {
        try {
            JSONObject jsonObject1 = data.getJSONObject(position);
            return jsonObject1.getString("title");
        } catch (Exception e) {
            return null;
        }
    }

    public String getDesc(int position) {
        try {
            JSONObject obj = data.getJSONObject(position).getJSONObject("Items").getJSONArray("data").getJSONObject(0);
            return obj.getString("description");
        } catch (Exception e) {
            return null;
        }
    }

    public boolean isAppreciated(int pos) {
        try {
            JSONObject obj = data.getJSONObject(pos).getJSONObject("Actions").getJSONObject("Appriciate");
            return obj.getBoolean("status");
        } catch (Exception e) {
            return false;
        }
    }

    public int getAppreciatedCount(int pos) {
        try {
            JSONObject obj = data.getJSONObject(pos).getJSONObject("Actions").getJSONObject("Appriciate");
            return obj.getInt("total");
        } catch (Exception e) {
            return 0;
        }
    }

    public void setAppreciated(int pos, boolean value, int count) {
        try {
            JSONObject obj = data.getJSONObject(pos).getJSONObject("Actions").getJSONObject("Appriciate");
            obj.put("status", value);
            obj.put("total", count);
        } catch (Exception e) {
            e.printStackTrace();
            Log.d(TAG, "setAppreciated: failed");
        }
    }

    public boolean isBookmarked(int pos) {
        try {
            JSONObject obj = data.getJSONObject(pos).getJSONObject("Actions").getJSONObject("Bookmarked");
            return obj.getBoolean("status");
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public void setBookmarked(int pos, boolean value) {
        try {
            JSONObject obj = data.getJSONObject(pos).getJSONObject("Actions").getJSONObject("Bookmarked");
            obj.put("status", value);
        } catch (Exception e) {
            Log.d(TAG, "setBookmarked: failed");
            e.printStackTrace();
        }
    }

    public static String toTitleCase(String givenString) {
        String[] arr = givenString.split(" ");
        StringBuffer sb = new StringBuffer();

        for (int i = 0; i < arr.length; i++) {
            sb.append(Character.toUpperCase(arr[i].charAt(0)))
                    .append(arr[i].substring(1)).append(" ");
        }
        return sb.toString().trim();
    }
}
