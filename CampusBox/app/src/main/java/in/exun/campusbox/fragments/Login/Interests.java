package in.exun.campusbox.fragments.Login;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import in.exun.campusbox.R;
import in.exun.campusbox.helper.DataSet;
import in.exun.campusbox.activity.Login;

/**
 * Created by ayush on 24/04/17.
 */

public class Interests extends Fragment implements View.OnClickListener{

    private static final String TAG = "Interests";
    private TextView btnBackToLogin, intItem;
    private CardView intItemContainer;
    private Button btnCont;
    private View rootView;
    private DataSet dataSet;
    private View view;
    private List<String> selectedInts;

    private int count = 0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_intrests, container, false);

        initialise();

        clickListeners();

        btnCont.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: " + selectedInts);
                buildJsonArray();
                ((Login)getActivity()).displayView(2,true);
            }
        });

        btnBackToLogin.setText(Html.fromHtml("Already have an account? <u>Login</u>."));
        btnBackToLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((Login)getActivity()).onBackPressed();
            }
        });

        return rootView;
    }

    private void buildJsonArray() {

        JSONArray array = new JSONArray();

        for (int i= 0; i<selectedInts.size();i++){
            JSONObject object = new JSONObject();
            try {
                object.put("id",selectedInts.get(i));
                object.put("intrested",true);
                String id = "t" + selectedInts.get(i);
                int resID = getResources().getIdentifier(id, "id", getActivity().getPackageName());
                String title = ((TextView) view.findViewById(resID)).getText().toString();
                Log.d(TAG, "buildJsonArray: " + title);
                object.put("title",true);
                array.put(object);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        ((Login)getActivity()).interestsArray = array;
    }

    private void clickListeners() {

        view.findViewById(R.id.rl1).setOnClickListener(this);
        view.findViewById(R.id.rl2).setOnClickListener(this);
        view.findViewById(R.id.rl3).setOnClickListener(this);
        view.findViewById(R.id.rl4).setOnClickListener(this);
        view.findViewById(R.id.rl5).setOnClickListener(this);
        view.findViewById(R.id.rl6).setOnClickListener(this);
        view.findViewById(R.id.rl7).setOnClickListener(this);
        view.findViewById(R.id.rl8).setOnClickListener(this);
        view.findViewById(R.id.rl9).setOnClickListener(this);
        view.findViewById(R.id.rl10).setOnClickListener(this);
        view.findViewById(R.id.rl11).setOnClickListener(this);
        view.findViewById(R.id.rl12).setOnClickListener(this);
        view.findViewById(R.id.rl13).setOnClickListener(this);
        view.findViewById(R.id.rl14).setOnClickListener(this);
        view.findViewById(R.id.rl15).setOnClickListener(this);
        view.findViewById(R.id.rl16).setOnClickListener(this);
        view.findViewById(R.id.rl17).setOnClickListener(this);
        view.findViewById(R.id.rl18).setOnClickListener(this);
        view.findViewById(R.id.rl19).setOnClickListener(this);
        view.findViewById(R.id.rl20).setOnClickListener(this);
        view.findViewById(R.id.rl21).setOnClickListener(this);
    }

    private void initialise() {

        btnCont = (Button) rootView.findViewById(R.id.btn_cont);
        btnBackToLogin = (TextView) rootView.findViewById(R.id.static_back_to_login);
        dataSet = new DataSet();
        view = rootView.findViewById(R.id.comp_multi_select);
        selectedInts = new ArrayList<>();
    }

    public void onClick(View view) {
        intItemContainer = (CardView) view;
        intItem = (TextView) view.findViewById(dataSet.list.get(view.getId()));
        onSelect();
    }

    private void onSelect() {

        if (intItem.getCurrentTextColor() == Color.parseColor("#454545")) {
            intItem.setTextColor(Color.WHITE);
            intItemContainer.setCardBackgroundColor(Color.RED);
            selectedInts.add(getResources().getResourceEntryName(intItem.getId()).substring(1));
            count++;
            if (count == 3)
                btnCont.setEnabled(true);

        } else {
            intItem.setTextColor(Color.parseColor("#454545"));
            intItemContainer.setCardBackgroundColor(Color.parseColor("#ececec"));
            selectedInts.remove(getResources().getResourceEntryName(intItem.getId()).substring(1));
            count--;
            if (count < 3 && btnCont.isEnabled())
                btnCont.setEnabled(false);

        }
    }
}
