package in.exun.campusbox.fragments.EventAdd;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.webkit.URLUtil;
import android.widget.EditText;
import android.widget.TextView;

import com.stepstone.stepper.BlockingStep;
import com.stepstone.stepper.StepperLayout;
import com.stepstone.stepper.VerificationError;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import in.exun.campusbox.R;
import in.exun.campusbox.activity.AddEvent;
import mabbas007.tagsedittext.TagsEditText;

/**
 * Created by ayush on 01/05/17.
 */

public class Additional extends Fragment implements BlockingStep {

    private static final String TAG = "Additional";
    private View rootView;
    public TagsEditText inputTags;
    public EditText inputVenue, inputLink, inputOrganiser, inputPhone, inputOrganiserLink;
    private TextView counterVenue, counterName;
    private AddEvent activity;
    private List<String> tags = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_additonal, container, false);

        activity = (AddEvent) getActivity();
        initialise();

        return rootView;
    }

    private void initialise() {
        inputTags = (TagsEditText) rootView.findViewById(R.id.input_tags);
        inputTags.setTagsWithSpacesEnabled(true);
        inputTags.setTagsBackground(R.drawable.background_tag);
        inputTags.setThreshold(5);
        inputTags.setTagsListener(new TagsEditText.TagsEditListener() {
            @Override
            public void onTagsChanged(Collection<String> collection) {
                tags = new ArrayList<String>(collection);
            }

            @Override
            public void onEditingFinished() {

            }
        });

        inputVenue = (EditText) rootView.findViewById(R.id.input_venue);
        inputLink = (EditText) rootView.findViewById(R.id.input_link);
        inputOrganiser = (EditText) rootView.findViewById(R.id.input_name);
        inputPhone = (EditText) rootView.findViewById(R.id.input_phone);
        inputOrganiserLink = (EditText) rootView.findViewById(R.id.input_link_organiser);

        counterName = (TextView) rootView.findViewById(R.id.counter_name);
        counterVenue = (TextView) rootView.findViewById(R.id.counter_venue);

        inputOrganiser.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                counterName.setText(inputOrganiser.length() + "/80");
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        inputVenue.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                counterVenue.setText(inputVenue.length() + "/100");
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    @Override
    public VerificationError verifyStep() {
        return null;
    }

    @Override
    public void onSelected() {
    }

    @Override
    public void onError(@NonNull VerificationError error) {
    }

    @Override
    public void onNextClicked(StepperLayout.OnNextClickedCallback callback) {
        if (validate())
            callback.goToNextStep();
        View view = getActivity().getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    private boolean validate() {
        boolean valid = true;

        if (inputVenue.getText().length() == 0) {
            valid = false;
            inputVenue.setError("This field can't be left empty");
        } else {
            activity.stringVenue = inputVenue.getText().toString();
        }

        if (inputOrganiser.getText().length() == 0) {
            valid = false;
            inputOrganiser.setError("This field can't be left empty");
        } else {
            activity.stringName = inputOrganiser.getText().toString();
        }

        if (inputPhone.getText().length() == 0) {
            valid = false;
            inputPhone.setError("This field can't be left empty");
        } else if (inputPhone.getText().length() != 8 || inputPhone.getText().length() != 10) {
            activity.stringPhone = inputPhone.getText().toString();
        } else {
            Log.d(TAG, "validate: " + inputPhone.getText().length());
            valid = false;
            inputPhone.setError("Please enter a valid phone number");
        }

        if (inputLink.getText().toString().length() != 0)
            if (!URLUtil.isValidUrl("http://" + inputLink.getText().toString())) {
                valid = false;
                inputLink.setError("Please enter a valid URL");
            } else
                activity.stringLink = inputLink.getText().toString();

        if (inputOrganiserLink.getText().toString().length() != 0)
            if (!URLUtil.isValidUrl("http://" + inputOrganiserLink.getText().toString())) {
                valid = false;
                inputOrganiserLink.setError("Please enter a valid URL");
            } else
                activity.stringOrgLInk = inputLink.getText().toString();

        if (inputTags.getText().length() != 0) {
            JSONArray data = new JSONArray();
            try {
                for (int i = 0; i < tags.size(); i++) {
                    JSONObject obj = new JSONObject();
                    obj.put("name", tags.get(i));
                    data.put(obj);
                }
                activity.arrTags = data;
            } catch (JSONException e) {
                e.printStackTrace();
            }
            Log.d(TAG, "validate: " + data.toString());
        }


        return valid;
    }

    @Override
    public void onCompleteClicked(StepperLayout.OnCompleteClickedCallback callback) {

    }

    @Override
    public void onBackClicked(StepperLayout.OnBackClickedCallback callback) {
        callback.goToPrevStep();

    }


}
