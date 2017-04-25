package in.exun.campusbox.fragments.Login;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.facebook.login.LoginManager;
import com.google.android.gms.auth.api.Auth;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.Map;

import in.exun.campusbox.R;
import in.exun.campusbox.activity.Login;
import in.exun.campusbox.activity.MainActivity;
import in.exun.campusbox.helper.AppConstants;
import in.exun.campusbox.helper.AppController;

import static com.facebook.FacebookSdk.getApplicationContext;

/**
 * Created by ayush on 24/04/17.
 */

public class Landing extends Fragment {

    private static final String TAG = "Landing";
    private static final int RC_SIGN_IN = 9001;
    private Button btnLoginFb, btnLoginG, btnGetStarted;
    private View rootView;
    private ProgressDialog pDialog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_landing, container, false);

        initialise();

        btnLoginFb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((Login) getActivity()).loginType = 0;
                LoginManager.getInstance().logInWithReadPermissions(
                        getActivity(),
                        Arrays.asList("email")
                );
            }
        });

        btnLoginG.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signIn();
            }
        });

        btnGetStarted.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((Login) getActivity()).displayView(1, true);
            }
        });
        return rootView;
    }

    private void initialise() {

        // Progress dialog
        pDialog = new ProgressDialog(getActivity());
        pDialog.setCancelable(false);
        pDialog.setIndeterminate(true);

        btnLoginFb = (Button) rootView.findViewById(R.id.btn_login_fb);
        btnGetStarted = (Button) rootView.findViewById(R.id.btn_get_started);


        btnLoginG = (Button) rootView.findViewById(R.id.btn_login_g);

    }

    private void signIn() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(((Login) getActivity()).mGoogleApiClient);
        ((Login) getActivity()).loginType = 1;
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    public void login(final Map<String, String> params) {

        pDialog.setMessage("Logging in ...");
        showDialog();

        String url = AppConstants.URL_LOGIN;
        Log.d(TAG, "login: " + url);

        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConstants.URL_LOGIN, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Login Response: " + response);
                hideDialog();

                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean registered = jObj.getBoolean("registered");

                    if ( registered){
                        Toast.makeText(getActivity(), "Logged in!", Toast.LENGTH_SHORT).show();
                        String token = jObj.getString("token");
                        Log.d(TAG, "onResponse: " + token);
                        ((Login)getActivity()).session.setLoginToken(token);
                        startActivity(new Intent(getActivity(),MainActivity.class).putExtra(AppConstants.TAG_TOKEN,token));
                        ((Login)getActivity()).finish();
                    } else {
                        Toast.makeText(getActivity(), "Not registered", Toast.LENGTH_SHORT).show();
                    }


                } catch (JSONException e) {
                    // JSON error
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Login Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(),
                        "Error in internet connection", Toast.LENGTH_SHORT).show();
                hideDialog();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting parameters to login url

                return params;
            }
        };

        AppController.getInstance().addToRequestQueue(strReq, TAG);
    }

    private void showDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hideDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }
}