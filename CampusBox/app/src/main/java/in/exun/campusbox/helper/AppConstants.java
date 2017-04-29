package in.exun.campusbox.helper;

/**
 * Created by ayush on 23/04/17.
 */

public class AppConstants {
    public static final String FACEBOOK_APP_ID = "1250377088376164";

    public static final String TAG_TOKEN = "token";
    public static final String TAG_OBJ = "object";

    public static final String URL_LOGIN = "https://app.campusbox.org/api/public/login";
    public static final String URL_SIGN_UP = "https://app.campusbox.org/api/public/signup";
    public static final String URL_APPRECIATE_EVENT = "https://app.campusbox.org/api/public/bookmarkEvent/";
    public static final String URL_ATTEND = "https://app.campusbox.org/api/public/rsvpEvent/";
    public static final String URL_DASH_EVENTS = "https://app.campusbox.org/api/public//eventsDashboard";
    public static final String URL_DASH_CONTENTS = "https://app.campusbox.org/api/public/contents";
    public static final String URL_BOOKMARK = "https://app.campusbox.org/api/public/bookmarkContent/";
    public static final String URL_APPRECIATE_POST = "https://app.campusbox.org/api/public/appreciateContent/";
    public static final String URL_EVENTS = "http://app.campusbox.org/api/public/events?";
    public static final String URL_CREATIVE = "https://app.campusbox.org/api/public/contents?";
    public static final String URL_SINGLE_EVENT="https://app.campusbox.org/api/public/event/";
    public static final int PROCESS_SUCCESS = 0;
    public static final int PROCESS_FAILURE = 1;
    public static final int PROCESS_LOAD = 2;
    public static final int PROCESS_LOAD_MORE = 3;

    public static final String[] interests = {
            "Articles", "Poetry", "Drama", "Paint and Colour", "Drawing", "Sewing and Fabric",
            "Craft", "Clay", "Singing", "Instrumental", "Music Mixing", "Photography", "Film and Video",
            "Animation", "Graphics", "UI and UX", "Websites", "Programming", "Apps", "Electronics", "DIY"
    };
}
