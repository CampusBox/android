package in.exun.campusbox.fragments.Login;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.facebook.login.LoginManager;
import com.google.android.gms.auth.api.Auth;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
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

public class SignUp extends Fragment {


    View rootView;
    AutoCompleteTextView inputCollege;
    TextView inputRollNo;
    Button btnSignUpFb, btnSignUpG;
    private ProgressDialog pDialog;

    ArrayList<String> colleges;
    String json;
    private String TAG = "SignUp";
    String selectedCollege;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        new PrepareArrayList().execute();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_signup, container, false);

        selectedCollege = "~";
        // Progress dialog
        pDialog = new ProgressDialog(getActivity());
        pDialog.setCancelable(false);
        pDialog.setIndeterminate(true);
        inputCollege = (AutoCompleteTextView) rootView.findViewById(R.id.input_college);
        inputRollNo = (TextView) rootView.findViewById(R.id.input_roll);
        btnSignUpFb = (Button) rootView.findViewById(R.id.btn_signup_fb);
        btnSignUpG = (Button) rootView.findViewById(R.id.btn_signup_g);

        inputCollege.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selectedCollege = (String) parent.getItemAtPosition(position);
                Log.d(TAG, "onItemClick: " + selectedCollege + " " + colleges.indexOf(selectedCollege));
                invalidateSignUpView(inputRollNo.getText().toString().length());
            }
        });

        inputRollNo.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                invalidateSignUpView(s.length());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        btnSignUpFb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((Login) getActivity()).loginType = 0;
                LoginManager.getInstance().logInWithReadPermissions(
                        getActivity(),
                        Arrays.asList("email")
                );
            }
        });

        btnSignUpG.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signIn();
            }
        });

        return rootView;
    }

    private void invalidateSignUpView(int lengthRollNo) {

        if (lengthRollNo > 0 && !selectedCollege.equals("~") && selectedCollege.equals(inputCollege.getText().toString())){
            btnSignUpFb.setVisibility(View.VISIBLE);
            btnSignUpG.setVisibility(View.VISIBLE);
        } else {
            btnSignUpFb.setVisibility(View.GONE);
            btnSignUpG.setVisibility(View.GONE);
        }
    }

    private void signIn() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(((Login) getActivity()).mGoogleApiClient);
        ((Login) getActivity()).loginType = 1;
        startActivityForResult(signInIntent, 9001);
    }

    public void signUp(final Map<String, String> params) {

        params.put("roll",inputRollNo.getText().toString());
        params.put("college",colleges.indexOf(selectedCollege) + "");
        Log.d(TAG, "signUp: " + params.toString());
        final Map<String, String> param = params;

        pDialog.setMessage("Signing up ...");
        showDialog();

        String url = AppConstants.URL_LOGIN;
        Log.d(TAG, "login: " + url);

        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConstants.URL_SIGN_UP, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Sign Up Response: " + response);
                hideDialog();

                try {
                    JSONObject jObj = new JSONObject(response);
                    String status = jObj.getString("status");

                    if ( status.startsWith("Already Registered") || status.equals("Registered Successfully")){
                        Toast.makeText(getActivity(), "Signed up!", Toast.LENGTH_SHORT).show();
                        String token = jObj.getString("token");
                        Log.d(TAG, "onResponse: " + token);
                        ((Login)getActivity()).session.setLoginToken(token);
                        startActivity(new Intent(getActivity(),MainActivity.class).putExtra(AppConstants.TAG_TOKEN,token));
                        ((Login)getActivity()).finish();
                    } else {
                        Toast.makeText(getActivity(), "Couldn't Sign up", Toast.LENGTH_SHORT).show();
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

                return param;
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

    private class PrepareArrayList extends AsyncTask<Void, Void, Integer> {

        @Override
        protected void onPreExecute() {

            StringBuilder sb = new StringBuilder();
            BufferedReader br = null;
            try {
                br = new BufferedReader(new InputStreamReader(getResources().openRawResource(R.raw.colleges)));
                String temp;
                while ((temp = br.readLine()) != null)
                    sb.append(temp);
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    br.close(); // stop reading
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            json = sb.toString();

        }

        @Override
        protected Integer doInBackground(Void... params) {

            try {
                JSONObject object = new JSONObject(json);
                JSONArray collegeArray = object.getJSONArray("data");
                colleges = new ArrayList<String>();
                if (collegeArray != null) {
                    for (int i = 0; i < collegeArray.length(); i++) {
                        colleges.add(collegeArray.getJSONObject(i).getString("name"));
//                        if (collegeArray.getJSONObject(i).getString("name").startsWith("Thapar")){
//                            Log.d(TAG, "doInBackground: " + i);
//                        }
                    }
                    Log.d(TAG, "getArrayList: Done " + collegeArray.length());
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }

            return 1;
        }

        @Override
        protected void onPostExecute(Integer integer) {

            ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),
                    android.R.layout.simple_dropdown_item_1line, colleges);
            inputCollege.setAdapter(adapter);

        }
    }
}
