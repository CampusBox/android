package in.exun.campusbox.fragments.EventAdd;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.stepstone.stepper.BlockingStep;
import com.stepstone.stepper.StepperLayout;
import com.stepstone.stepper.VerificationError;

import in.exun.campusbox.R;
import in.exun.campusbox.activity.AddEvent;
import io.github.mthli.knife.KnifeText;

/**
 * Created by ayush on 01/05/17.
 */

public class Desc extends Fragment implements BlockingStep {

    private static final String TAG = "Desc";
    private View rootView;
    private KnifeText knife;
    private String stringDesc = "";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_desc, container, false);

        initialise();

        return rootView;
    }

    private void initialise() {
        knife = (KnifeText) rootView.findViewById(R.id.knife);
        knife.fromHtml(stringDesc);
        knife.setSelection(knife.getEditableText().length());

        setupBold();
        setupItalic();
        setupUnderline();
        setupStrikethrough();
        setupBullet();
        setupQuote();
        setupLink();
        setupHelp();
    }

    private void setupBold() {
        ImageButton bold = (ImageButton) rootView.findViewById(R.id.btn_bold);

        bold.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                knife.bold(!knife.contains(KnifeText.FORMAT_BOLD));
            }
        });
    }

    private void setupItalic() {
        ImageButton italic = (ImageButton) rootView.findViewById(R.id.btn_italics);

        italic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                knife.italic(!knife.contains(KnifeText.FORMAT_ITALIC));
            }
        });
    }

    private void setupUnderline() {
        ImageButton underline = (ImageButton) rootView.findViewById(R.id.btn_underline);

        underline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                knife.underline(!knife.contains(KnifeText.FORMAT_UNDERLINED));
            }
        });
    }

    private void setupStrikethrough() {
        ImageButton strikethrough = (ImageButton) rootView.findViewById(R.id.btn_strikethrough);

        strikethrough.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                knife.strikethrough(!knife.contains(KnifeText.FORMAT_STRIKETHROUGH));
            }
        });
    }

    private void setupBullet() {
        ImageButton bullet = (ImageButton) rootView.findViewById(R.id.btn_bullet);

        bullet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                knife.bullet(!knife.contains(KnifeText.FORMAT_BULLET));
            }
        });
    }

    private void setupQuote() {
        ImageButton quote = (ImageButton) rootView.findViewById(R.id.btn_quote);

        quote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                knife.quote(!knife.contains(KnifeText.FORMAT_QUOTE));
            }
        });
    }

    private void setupLink() {
        ImageButton link = (ImageButton) rootView.findViewById(R.id.btn_link);

        link.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showLinkDialog();
            }
        });
    }

    private void setupHelp() {
        ImageButton help = (ImageButton) rootView.findViewById(R.id.btn_how);

        help.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showHelpDialog();
            }
        });
    }

    private void showHelpDialog() {

        View view = getActivity().getLayoutInflater().inflate(R.layout.comp_imageview, null, false);
        ImageView imageView = (ImageView) view.findViewById(R.id.img);
        Glide.with(getActivity())
                .load(R.raw.how)
                .asGif()
                .into(imageView);

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity())
                .setCancelable(true)
                .setView(view);

        builder.create().show();

    }

    private void showLinkDialog() {
        final int start = knife.getSelectionStart();
        final int end = knife.getSelectionEnd();

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setCancelable(false);

        View view = getActivity().getLayoutInflater().inflate(R.layout.comp_insert_link, null, false);
        final EditText editText = (EditText) view.findViewById(R.id.input_link);
        builder.setView(view);
        builder.setTitle("Insert link");

        builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String link = editText.getText().toString().trim();
                if (TextUtils.isEmpty(link)) {
                    return;
                }

                // When KnifeText lose focus, use this method
                knife.link(link, start, end);
            }
        });

        builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // DO NOTHING HERE
            }
        });

        builder.create().show();
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
        knife.clearComposingText();
        stringDesc = knife.toHtml();

        if (stringDesc.length() == 0)
            knife.setError("This field can't be left empty");
        else {
            ((AddEvent)getActivity()).stringDesc = stringDesc;
            Log.d(TAG, "onNextClicked: " + stringDesc);
            Log.d(TAG, "onNextClicked: " + knife.getText());
            callback.goToNextStep();
        }

    }

    @Override
    public void onCompleteClicked(StepperLayout.OnCompleteClickedCallback callback) {

    }

    @Override
    public void onBackClicked(StepperLayout.OnBackClickedCallback callback) {
        callback.goToPrevStep();
    }
}
