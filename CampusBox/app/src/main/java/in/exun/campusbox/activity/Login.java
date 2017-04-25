package in.exun.campusbox.activity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.gms.auth.GoogleAuthException;
import com.google.android.gms.auth.GoogleAuthUtil;
import com.google.android.gms.auth.UserRecoverableAuthException;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;

import org.json.JSONArray;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import in.exun.campusbox.R;
import in.exun.campusbox.fragments.Login.Interests;
import in.exun.campusbox.fragments.Login.Landing;
import in.exun.campusbox.fragments.Login.SignUp;
import in.exun.campusbox.helper.SessionManager;

public class Login extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener{

    private static final String TAG = "Login";
    private static final int REQUEST_CODE_TOKEN_AUTH = 10001;
    public SessionManager session;
    private CallbackManager callbackManager;
    private Fragment fragment;
    private FragmentManager fragmentManager;
    private FragmentTransaction fragmentTransaction;
    public GoogleApiClient mGoogleApiClient;
    private int currIndex = 0;
    public int loginType = 0; // 0: Facebook 1: Google
    private Map<String, String> params;
    public JSONArray interestsArray;
    public String accntName = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        initialise();
        initialiseFb();
        initialiseG();
    }

    private void initialise() {

        // Session manager
        session = new SessionManager(getApplicationContext());
        interestsArray = new JSONArray();

        displayView(0, false);
    }

    private void initialiseFb() {

        callbackManager = CallbackManager.Factory.create();

        // Callback registration
        LoginManager.getInstance().registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                // App code
                Toast.makeText(Login.this, "Success", Toast.LENGTH_SHORT).show();

                params = new HashMap<String, String>();
                params.put("type", "facebook");
                if (currIndex == 0){
                    params.put("access_token", loginResult.getAccessToken().getToken());
                    ((Landing)fragment).login(params);
                    params = new HashMap<String, String>();
                } else {
                    params.put("token", loginResult.getAccessToken().getToken());
                    params.put("intrests",interestsArray.toString());
                    ((SignUp)fragment).signUp(params);
                }
            }

            @Override
            public void onCancel() {
                // App code
                Toast.makeText(Login.this, "Cancelled", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(FacebookException exception) {
                // App code
                Toast.makeText(Login.this, "Error", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void initialiseG() {
        // Configure sign-in to request the user's ID, email address, and basic
        // profile. ID and basic profile are included in DEFAULT_SIGN_IN.
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .requestServerAuthCode(getString(R.string.server_client_id))
                .requestIdToken(getString(R.string.server_client_id))
                .build();

        // Build a GoogleApiClient with access to the Google Sign-In API and the
        // options specified by gso.
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this /* FragmentActivity */, this /* OnConnectionFailedListener */)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();
    }

    public void displayView(int index, boolean allow) {

        fragment = null;

        switch (index) {
            case 0:
                fragment = new Landing();
                break;
            case 1:
                fragment = new Interests();
                break;
            case 2:
                fragment = new SignUp();
                break;
        }

        fragmentManager = getSupportFragmentManager();

        if (fragment != null) {
            fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.disallowAddToBackStack();

            if (allow)
                if (index == 1)
                    if (currIndex == 0)
                        fragmentTransaction.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left);
                    else
                        fragmentTransaction.setCustomAnimations(R.anim.enter_from_left, R.anim.exit_to_right);
                else if (index == 2)
                    fragmentTransaction.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left);
                else
                    fragmentTransaction.setCustomAnimations(R.anim.enter_from_left, R.anim.exit_to_right);
            fragmentTransaction.replace(R.id.frame, fragment);
            fragmentTransaction.commit();
            invalidateOptionsMenu();
        }

        currIndex = index;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Log.d(TAG, "onActivityResult: " + requestCode);

        if (requestCode == REQUEST_CODE_TOKEN_AUTH){
            Log.d(TAG, "onActivityResult: nachp");
        }

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (loginType == 1) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(result);
        } else {

            callbackManager.onActivityResult(requestCode, resultCode, data);
        }
    }

    private void handleSignInResult(GoogleSignInResult result) {
        Log.d(TAG, "handleSignInResult:" + result.isSuccess());
        if (result.isSuccess()) {
            // Signed in successfully, show authenticated UI.
            GoogleSignInAccount acct = result.getSignInAccount();
            Toast.makeText(Login.this, "Success", Toast.LENGTH_SHORT).show();
            accntName = acct.getEmail();

            new getTokenTask().execute();
            //SignOut
            Auth.GoogleSignInApi.signOut(mGoogleApiClient);

        } else {
            // Signed out, show unauthenticated UI.
            Toast.makeText(this, "Fail", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onBackPressed() {
        if (currIndex != 0) {
            displayView(currIndex-1,true);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        // An unresolvable error has occurred and Google APIs (including Sign-In) will not
        // be available.
        Toast.makeText(this, "Failed, sed", Toast.LENGTH_SHORT).show();
        Log.d(TAG, "onConnectionFailed:" + connectionResult);
    }

    private class getTokenTask extends AsyncTask<Void, Void, String> {

        @Override
        protected String doInBackground(Void... params) {
            String token = null;
            String SCOPES = "https://www.googleapis.com/auth/plus.login "
                    + "https://www.googleapis.com/auth/drive.file";

            try {
                token = GoogleAuthUtil.getToken(
                        Login.this,
                        accntName,
                        "oauth2:" + SCOPES);
            } catch (IOException transientEx) {
                // Network or server error, try later
                Log.e(TAG, transientEx.toString());
            } catch (UserRecoverableAuthException e) {
                // Recover (with e.getIntent())
                Log.e(TAG, e.toString());
                Intent recover = e.getIntent();
                startActivityForResult(recover, REQUEST_CODE_TOKEN_AUTH);
            } catch (GoogleAuthException authEx) {
                // The call is not ever expected to succeed
                // assuming you have already verified that
                // Google Play services is installed.
                Log.e(TAG, authEx.toString());
            }

            return token;
        }

        @Override
        protected void onPostExecute(String token) {
            Log.i(TAG, "Access token retrieved:" + token);

            params = new HashMap<String, String>();
            params.put("token", token);
            params.put("type", "google");

            if (currIndex == 0){
                ((Landing)fragment).login(params);
            } else {
                params.put("intrests",interestsArray.toString());
                ((SignUp)fragment).signUp(params);
            }
        }
    }
}
