package in.exun.campusbox.fragments.EventAdd;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.graphics.Color;
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
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.stepstone.stepper.BlockingStep;
import com.stepstone.stepper.StepperLayout;
import com.stepstone.stepper.VerificationError;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import in.exun.campusbox.R;
import in.exun.campusbox.activity.AddEvent;

/**
 * Created by ayush on 01/05/17.
 */

public class Basic extends Fragment implements BlockingStep {

    private static final String TAG = "Basic";
    private View rootView;
    private AddEvent activity;
    private EditText inputTitle, inputSubtitle, inputFees;
    private TextView counterTitle, counterSubtitle, timeStart, dateStart, timeEnd, dateEnd;
    private Spinner spinType, spinTarget;
    private Calendar mcurrentTime;
    String myFormat = "d-MMM-yy";
    String exportFormat = "yyyy-MM-dd'T'HH:mm:ss.SSSZ";
    SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
    String myFormatTime = "hh:mm a"; //In which you need put here
    SimpleDateFormat sdfTime = new SimpleDateFormat(myFormatTime, Locale.US);

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_basic, container, false);

        activity = (AddEvent) getActivity();
        mcurrentTime = Calendar.getInstance();
        initialise();

        return rootView;
    }

    private void initialise() {

        inputTitle = (EditText) rootView.findViewById(R.id.input_title);
        inputSubtitle = (EditText) rootView.findViewById(R.id.input_subtitle);
        inputFees = (EditText) rootView.findViewById(R.id.input_fees);
        counterTitle = (TextView) rootView.findViewById(R.id.counter_title);
        counterSubtitle = (TextView) rootView.findViewById(R.id.counter_subtitle);
        timeStart = (TextView) rootView.findViewById(R.id.input_start_time);
        dateStart = (TextView) rootView.findViewById(R.id.input_start_date);
        timeEnd = (TextView) rootView.findViewById(R.id.input_end_time);
        dateEnd = (TextView) rootView.findViewById(R.id.input_end_date);

        if (activity.stringDateEnd != null) {
            dateEnd.setText(activity.stringDateEnd);
            dateEnd.setTextColor(Color.BLACK);
        }
        if (activity.stringDateStart != null)
            dateStart.setText(activity.stringDateStart);
        else
            dateStart.setText(sdf.format(mcurrentTime.getTime()));
        if (activity.stringTimeEnd != null) {
            timeEnd.setText(activity.stringTimeEnd);
            timeEnd.setTextColor(Color.BLACK);
        }
        if (activity.stringTimeStart != null)
            timeStart.setText(activity.stringTimeStart);
        else
            timeStart.setText("12:30 PM");

        spinType = (Spinner) rootView.findViewById(R.id.spinner_type);
        ArrayAdapter<?> adapterType = ArrayAdapter.createFromResource(getActivity(), R.array.add_type_array, android.R.layout.simple_spinner_item);
        adapterType.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinType.setAdapter(adapterType);

        spinTarget = (Spinner) rootView.findViewById(R.id.spinner_audience);
        ArrayAdapter<?> adapterTarget = ArrayAdapter.createFromResource(getActivity(), R.array.audience_array, android.R.layout.simple_spinner_item);
        adapterTarget.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinTarget.setAdapter(adapterTarget);

        inputTitle.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                counterTitle.setText(inputTitle.length() + "/40");
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        inputSubtitle.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                counterSubtitle.setText(inputSubtitle.length() + "/160");
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        timeStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pickTime(0);
            }
        });

        timeEnd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pickTime(1);
            }
        });

        dateStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pickDate(0);
            }
        });

        dateEnd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pickDate(1);
            }
        });
    }

    private void pickTime(final int code) {

        TimePickerDialog mTimePicker;
        mTimePicker = new TimePickerDialog(activity, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                Log.d(TAG, "onTimeSet: " + selectedHour + ":" + selectedMinute);
                Calendar myCalendar = Calendar.getInstance();
                myCalendar.set(Calendar.HOUR_OF_DAY, selectedHour);
                myCalendar.set(Calendar.MINUTE, selectedMinute);

                if (code == 0) {
                    timeStart.setText(sdfTime.format(myCalendar.getTime()));
                    timeStart.setTextColor(Color.BLACK);
                } else {
                    timeEnd.setText(sdfTime.format(myCalendar.getTime()));
                    timeEnd.setTextColor(Color.BLACK);
                }
            }
        }, mcurrentTime.get(Calendar.HOUR_OF_DAY),
                mcurrentTime.get(Calendar.MINUTE), false);//Yes 24 hour time
        mTimePicker.setTitle("Select Time");
        mTimePicker.show();
    }

    private void pickDate(final int code) {

        DatePickerDialog mDatePicker = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                Calendar myCalendar = Calendar.getInstance();
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, month);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                if (code == 0) {
                    dateStart.setText(sdf.format(myCalendar.getTime()));
                    dateStart.setTextColor(Color.BLACK);
                } else {
                    dateEnd.setText(sdf.format(myCalendar.getTime()));
                    dateEnd.setTextColor(Color.BLACK);
                }
            }
        }, mcurrentTime.get(Calendar.YEAR), mcurrentTime.get(Calendar.MONTH),
                mcurrentTime.get(Calendar.DAY_OF_MONTH));

        mDatePicker.setTitle("Select date");
        mDatePicker.show();

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

        View view = getActivity().getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }

        if (validate())
            callback.goToNextStep();

    }

    @Override
    public void onCompleteClicked(StepperLayout.OnCompleteClickedCallback callback) {

    }

    @Override
    public void onBackClicked(StepperLayout.OnBackClickedCallback callback) {
        callback.goToPrevStep();


    }

    private boolean validate() {
        boolean valid = true;

        if (inputTitle.getText().length() == 0) {
            valid = false;
            inputTitle.setError("This field can't be empty");
        } else {
            activity.stringTitle = inputTitle.getText().toString();
        }

        if (inputSubtitle.getText().length() == 0) {
            valid = false;
            inputSubtitle.setError("This field can't be empty");
        } else {
            activity.stringSubtitle = inputSubtitle.getText().toString();
        }

        if (inputFees.getText().length() == 0) {
            valid = false;
            inputFees.setError("This field can't be empty");
        } else {
            activity.stringFees = inputFees.getText().toString();
        }


        SimpleDateFormat exportDateFormat = new SimpleDateFormat(exportFormat);
        Date exportDate = mcurrentTime.getTime();
        try {
            exportDate = sdf.parse(dateStart.getText().toString());
            Log.d(TAG, "validate: " + exportDate.toString());
        } catch (ParseException e) {
            e.printStackTrace();
        }

        activity.stringDateStart = exportDateFormat.format(exportDate);
        activity.stringTimeStart = timeStart.getText().toString();
        Log.d(TAG, "validate: " + activity.stringDateStart + " " + activity.stringTimeStart);

        if (dateEnd.getText().toString().equals("Set date") || timeEnd.getText().toString().equals("Set time")) {
            if (dateEnd.getText().toString().equals("Set date") && timeEnd.getText().toString().equals("Set time")) {
                Log.d(TAG, "validate: Both empty");
                activity.stringDateEnd = exportDateFormat.format(exportDate);
                activity.stringTimeEnd = timeStart.getText().toString();
                Log.d(TAG, "validate: " + activity.stringDateEnd + " " + activity.stringTimeEnd);
            } else if (dateEnd.getText().toString().equals("Set date")) {
                if (valid)
                    Toast.makeText(activity, "Please set an end date", Toast.LENGTH_SHORT).show();
                valid = false;
            } else if (timeEnd.getText().toString().equals("Set time")) {
                if (valid)
                    Toast.makeText(activity, "Please set an end time", Toast.LENGTH_SHORT).show();
                valid = false;
            }
        } else {
            String pattern = myFormat + " " + myFormatTime;
            SimpleDateFormat dateFormat = new SimpleDateFormat(pattern);
            try {
                Date one = dateFormat.parse(dateStart.getText().toString() + " " + activity.stringTimeStart);
                Date two = dateFormat.parse(dateEnd.getText().toString() + " " + timeEnd.getText().toString());
                Date current = mcurrentTime.getTime();
                if (one.compareTo(current) < 0) {
                    if (valid)
                        Toast.makeText(activity, "Please set a valid start time", Toast.LENGTH_SHORT).show();
                    valid = false;
                } else if (one.compareTo(two) == 0) {
                    if (valid)
                        Toast.makeText(activity, "Please set a valid end time", Toast.LENGTH_SHORT).show();
                    valid = false;
                } else if (one.compareTo(two) > 0) {
                    if (valid)
                        Toast.makeText(activity, "Please set a valid end time", Toast.LENGTH_SHORT).show();
                    valid = false;
                } else {
                    try {
                        exportDate = sdf.parse(dateEnd.getText().toString());
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    activity.stringDateEnd = exportDateFormat.format(exportDate);
                    activity.stringTimeEnd = timeEnd.getText().toString();
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        activity.intType = spinType.getSelectedItemPosition();
        activity.intTarget = spinTarget.getSelectedItemPosition();

        return valid;
    }
}
