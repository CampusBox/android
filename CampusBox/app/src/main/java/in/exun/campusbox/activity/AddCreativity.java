package in.exun.campusbox.activity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.URLUtil;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.HorizontalScrollView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.bumptech.glide.Glide;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import in.exun.campusbox.R;
import in.exun.campusbox.adapters.LVSetup;
import in.exun.campusbox.adapters.RVACreativeItems;
import in.exun.campusbox.helper.AppConstants;
import in.exun.campusbox.helper.AppController;
import in.exun.campusbox.helper.BitmapUtils;
import in.exun.campusbox.helper.DataSet;
import in.exun.campusbox.helper.SessionManager;
import in.exun.campusbox.model.CreativityItems;
import io.github.mthli.knife.KnifeText;
import mabbas007.tagsedittext.TagsEditText;

public class AddCreativity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "AddCreativity";
    private static final int SET_COVER = 23;
    private static final int SET_IMAGE = 24;
    private static final int TYPE_IMAGE = 0;
    private static final int TYPE_SOUND = 1;
    private static final int TYPE_YOUTUBE = 2;
    private static final int TYPE_VIMEO = 3;
    EditText inputTitle;
    KnifeText inputPost;
    ImageView imgCover, btnClose;
    TextView btnSetCover, textCat, btnPublish;
    RecyclerView mRecyclerView;
    HorizontalScrollView containerTools;
    private AlertDialog alert;
    RVACreativeItems mAdapter;
    private LinearLayoutManager mLayoutManager;
    List<CreativityItems> items = new ArrayList<>();
    private int shouldUpdate = -1;

    private DataSet dataSet;
    private TextView intItem;
    private CardView intItemContainer;
    private AlertDialog dialog;
    private String category;
    private SessionManager session;
    private ProgressDialog pDialog;
    private int categoryId = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_creativity);

        session = new SessionManager(this);
        initialise();
    }

    private void initialise() {

        // Progress dialog
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);
        pDialog.setIndeterminate(true);

        inputTitle = (EditText) findViewById(R.id.input_title);
        inputPost = (KnifeText) findViewById(R.id.input_post);
        imgCover = (ImageView) findViewById(R.id.img_cover);
        btnClose = (ImageView) findViewById(R.id.btn_close);
        textCat = (TextView) findViewById(R.id.text_category);
        textCat.setMovementMethod(LinkMovementMethod.getInstance());
        btnPublish = (TextView) findViewById(R.id.btn_publish);
        btnSetCover = (TextView) findViewById(R.id.btn_add_cover);
        containerTools = (HorizontalScrollView) findViewById(R.id.container_tools);
        mRecyclerView = (RecyclerView) findViewById(R.id.rv_extra);
        dataSet = new DataSet();

        btnSetCover.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startGalleryIntent(SET_COVER);
            }
        });

        imgCover.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startGalleryIntent(SET_COVER);
            }
        });

        inputPost.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus)
                    containerTools.setVisibility(View.VISIBLE);
                else
                    containerTools.setVisibility(View.GONE);
            }
        });

        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btnPublish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getTags();
            }
        });

        showFilters(true);
        setupBold();
        setupItalic();
        setupUnderline();
        setupStrikethrough();
        setupBullet();
        setupQuote();
        setupLink();
        setupHelp();
        setupInsert();
    }

    private void setupBold() {
        ImageButton bold = (ImageButton) findViewById(R.id.btn_bold);

        bold.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                inputPost.bold(!inputPost.contains(KnifeText.FORMAT_BOLD));
            }
        });
    }

    private void setupItalic() {
        ImageButton italic = (ImageButton) findViewById(R.id.btn_italics);

        italic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                inputPost.italic(!inputPost.contains(KnifeText.FORMAT_ITALIC));
            }
        });
    }

    private void setupUnderline() {
        ImageButton underline = (ImageButton) findViewById(R.id.btn_underline);

        underline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                inputPost.underline(!inputPost.contains(KnifeText.FORMAT_UNDERLINED));
            }
        });
    }

    private void setupStrikethrough() {
        ImageButton strikethrough = (ImageButton) findViewById(R.id.btn_strikethrough);

        strikethrough.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                inputPost.strikethrough(!inputPost.contains(KnifeText.FORMAT_STRIKETHROUGH));
            }
        });
    }

    private void setupBullet() {
        ImageButton bullet = (ImageButton) findViewById(R.id.btn_bullet);

        bullet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                inputPost.bullet(!inputPost.contains(KnifeText.FORMAT_BULLET));
            }
        });
    }

    private void setupQuote() {
        ImageButton quote = (ImageButton) findViewById(R.id.btn_quote);

        quote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                inputPost.quote(!inputPost.contains(KnifeText.FORMAT_QUOTE));
            }
        });
    }

    private void setupLink() {
        ImageButton link = (ImageButton) findViewById(R.id.btn_link);

        link.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showLinkDialog(0, null, -1);
            }
        });
    }

    private void setupInsert() {
        inputPost.hideSoftInput();
        ImageButton link = (ImageButton) findViewById(R.id.btn_add);

        link.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAddDialog();
            }
        });
    }

    private void showAddDialog() {

        LayoutInflater inflater = getLayoutInflater();
        View convertView = (View) inflater.inflate(R.layout.comp_list_view, null);
        ListView lv = (ListView) convertView.findViewById(R.id.lv);
        final List<String> settingsList = new ArrayList<String>();
        settingsList.add(AppConstants.ADD_IMAGE);
        settingsList.add(AppConstants.ADD_SOUND);
        settingsList.add(AppConstants.ADD_YOUTUBE);
        settingsList.add(AppConstants.ADD_VIMEO);

        LVSetup customAdapter = new LVSetup(this, R.layout.list_row_add_post, settingsList);

        lv.setAdapter(customAdapter);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                alert.dismiss();

                switch (position) {
                    case 0:
                        startGalleryIntent(SET_IMAGE);
                        break;
                    case 1:
                        showLinkDialog(1, null, -1);
                        break;
                    case 2:
                        showLinkDialog(2, null, -1);
                        break;
                    case 3:
                        showLinkDialog(3, null, -1);
                        break;
                }


            }
        });

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this)
                .setView(convertView)
                .setTitle("Insert ")
                .setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                    }
                })
                .setCancelable(true);

        alert = alertDialog.create();
        alert.show();
    }

    private void setupHelp() {
        inputPost.hideSoftInput();
        ImageButton help = (ImageButton) findViewById(R.id.btn_how);

        help.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showHelpDialog();
            }
        });
    }

    private void showHelpDialog() {

        View view = getLayoutInflater().inflate(R.layout.comp_imageview, null, false);
        ImageView imageView = (ImageView) view.findViewById(R.id.img);
        Glide.with(this)
                .load(R.raw.how)
                .asGif()
                .into(imageView);

        AlertDialog.Builder builder = new AlertDialog.Builder(this)
                .setCancelable(true)
                .setView(view);

        builder.create().show();

    }

    private void showLinkDialog(final int reqCode, final String data, final int pos) {
        final int start = inputPost.getSelectionStart();
        final int end = inputPost.getSelectionEnd();

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(false);

        View view = this.getLayoutInflater().inflate(R.layout.comp_insert_link, null, false);
        final EditText editText = (EditText) view.findViewById(R.id.input_link);
        builder.setView(view);
        if (data != null) {
            builder.setTitle("Edit link");
            editText.setText(data);
        } else
            builder.setTitle("Insert link");

        builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String link = editText.getText().toString().trim();
                if (TextUtils.isEmpty(link)) {
                    return;
                } else if (URLUtil.isValidUrl(link)){
                    return;
                }

                Bitmap mBitmap = null;

                if (reqCode == 0)
                    inputPost.link(link, start, end);
                else if (reqCode == 1) {
                    if (data == null) {
                        CreativityItems item = new CreativityItems(TYPE_SOUND, link, mBitmap);
                        items.add(item);
                        if (items.size() == 1)
                            initRecyclerView();
                        else
                            mAdapter.notifyDataSetChanged();
                    } else {
                        items.get(pos).setData(link);
                        mAdapter.notifyItemChanged(pos);
                    }
                } else if (reqCode == 2) {
                    if (data == null) {
                        CreativityItems item = new CreativityItems(TYPE_YOUTUBE, link, mBitmap);
                        items.add(item);
                        if (items.size() == 1)
                            initRecyclerView();
                        else
                            mAdapter.notifyDataSetChanged();
                    } else {
                        items.get(pos).setData(link);
                        mAdapter.notifyItemChanged(pos);
                    }
                } else if (reqCode == 3) {
                    if (data == null) {
                        CreativityItems item = new CreativityItems(TYPE_VIMEO, link, mBitmap);
                        items.add(item);
                        if (items.size() == 1)
                            initRecyclerView();
                        else
                            mAdapter.notifyDataSetChanged();
                    } else {
                        items.get(pos).setData(link);
                        mAdapter.notifyItemChanged(pos);
                    }
                }
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

    private void startGalleryIntent(int reqCode) {

        Log.d(TAG, "startGalleryIntent: ");

        if (!hasGalleryPermission()) {
            askForGalleryPermission(reqCode);
            return;
        }

        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, reqCode);
    }

    private boolean hasGalleryPermission() {
        return ActivityCompat.checkSelfPermission(this,
                android.Manifest.permission.READ_EXTERNAL_STORAGE)
                == PackageManager.PERMISSION_GRANTED;
    }

    private void askForGalleryPermission(int reqCode) {
        ActivityCompat.requestPermissions(this,
                new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE},
                reqCode);
    }

    @Override
    public void onActivityResult(int requestCode, int responseCode, Intent resultIntent) {
        super.onActivityResult(requestCode, responseCode, resultIntent);

        if (responseCode == RESULT_OK) {
            String absPath = BitmapUtils.getFilePathFromUri(this, resultIntent.getData());
            loadNewImage(absPath, requestCode);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {

        Log.d(TAG, "onRequestPermissionsResult: " + requestCode);
        if (requestCode == SET_COVER || requestCode == SET_IMAGE) {
            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                startGalleryIntent(requestCode);
                return;
            }
        }

        Toast.makeText(this, "Gallery permission not granted", Toast.LENGTH_SHORT).show();
    }

    private void loadNewImage(String filePath, int reqCode) {
        Log.i(TAG, "load image: " + filePath);
        Bitmap mBitmap = BitmapFactory.decodeFile(filePath);
        Log.d(TAG, "loadNewImage: " + mBitmap.getWidth() + " " + mBitmap.getHeight());

        if (!(1000 > mBitmap.getWidth()) && !(800 > mBitmap.getHeight())) {

            mBitmap = resizeBitmapFitXY(1000, 800, mBitmap);
        }

        Log.d(TAG, "loadNewImage: " + mBitmap.getWidth() + " " + mBitmap.getHeight());


        if (reqCode == SET_COVER) {
            imgCover.setImageBitmap(mBitmap);
            btnSetCover.setVisibility(View.GONE);
        } else {
            if (shouldUpdate == -1) {
                CreativityItems item = new CreativityItems(TYPE_IMAGE, "", mBitmap);
                items.add(item);
                if (items.size() == 1)
                    initRecyclerView();
                else
                    mAdapter.notifyDataSetChanged();
            } else {
                items.get(shouldUpdate).setBitmap(mBitmap);
                mAdapter.notifyItemChanged(shouldUpdate);
                shouldUpdate = -1;
            }
        }
    }

    public Bitmap resizeBitmapFitXY(int width, int height, Bitmap bitmap) {
        Bitmap background = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        float originalWidth = bitmap.getWidth(), originalHeight = bitmap.getHeight();
        Canvas canvas = new Canvas(background);
        float scale, xTranslation = 0.0f, yTranslation = 0.0f;

        if ((originalWidth - width) > (originalHeight - height)) {
            scale = width / originalWidth;
            yTranslation = (height - originalHeight * scale) / 2.0f;
        } else {
            scale = height / originalHeight;
            xTranslation = (width - originalWidth * scale) / 2.0f;
        }

        Matrix transformation = new Matrix();
        transformation.postTranslate(xTranslation, yTranslation);
        transformation.preScale(scale, scale);
        Paint paint = new Paint();
        paint.setFilterBitmap(true);
        canvas.drawBitmap(bitmap, transformation, paint);
        return background;
    }

    private void initRecyclerView() {

        mRecyclerView.setNestedScrollingEnabled(false);
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mAdapter = new RVACreativeItems(items);
        mRecyclerView.setAdapter(mAdapter);
    }

    @Override
    protected void onResume() {
        super.onResume();

        initRecyclerView();

        ((RVACreativeItems) mAdapter).setOnItemClickListener(new RVACreativeItems.MyClickListener() {
            @Override
            public void onItemClick(int position, View v, int type) {

                if (type == 1) {
                    Toast.makeText(AddCreativity.this, "Deleted!", Toast.LENGTH_SHORT).show();
                    items.remove(position);
                    mAdapter.notifyItemRemoved(position);
                } else
                    switch (items.get(position).getType()) {
                        case TYPE_IMAGE:
                            shouldUpdate = position;
                            startGalleryIntent(SET_IMAGE);
                            break;
                        case TYPE_SOUND:
                            showLinkDialog(1, items.get(position).getData(), position);
                            break;
                        case TYPE_YOUTUBE:
                            showLinkDialog(2, items.get(position).getData(), position);
                            break;
                        case TYPE_VIMEO:
                            showLinkDialog(3, items.get(position).getData(), position);
                            break;

                    }
            }
        });
    }

    private void getTags() {

        LayoutInflater inflater = getLayoutInflater();
        View convertView = (View) inflater.inflate(R.layout.comp_tags, null);
        TagsEditText input_tags = (TagsEditText) convertView.findViewById(R.id.input_tags);

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this)
                .setView(convertView)
                .setTitle("Pick a category")
                .setCancelable(false)
                .setPositiveButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });

        dialog = alertDialog.create();
        dialog.show();
    }

    private void addContent() {

        if (!validate())
            return;

        pDialog.setMessage("Uploading...");
        showDialog();

        JsonObjectRequest strReq = new JsonObjectRequest(Request.Method.POST,
                AppConstants.URL_ADD_CONTENT, getPostJson(), new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                hideDialog();
                try {
                    String status = response.getString("message");
                    if (status.equals("Registered Successfully")) {
                        Toast.makeText(AddCreativity.this, "Done!", Toast.LENGTH_SHORT).show();
                        finish();
                    } else
                        Toast.makeText(AddCreativity.this, "Couldn't add content", Toast.LENGTH_SHORT).show();
                } catch (JSONException e) {
                    Toast.makeText(AddCreativity.this, "Error in internet connection", Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d(TAG, "onErrorResponse: " + error.toString());
                Toast.makeText(AddCreativity.this, "Error in internet connection", Toast.LENGTH_SHORT).show();
            }
        }) {

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                String token = "Bearer " + session.getLoginToken();
                params.put("Content-Type", "application/json");
                params.put("Authorization", token);
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

    private boolean validate() {
        boolean valid = true;

        if (inputTitle.getText().length() == 0) {
            valid = false;
            inputTitle.setError("This field can't be empty");
        }

        if (inputPost.getText().length() == 0) {
            valid = false;
            inputPost.setError("This field can't be empty");
        }

        return valid;
    }

    private JSONObject getPostJson() {
        JSONObject obj = new JSONObject();
        JSONArray arr = new JSONArray();
        JSONObject object;
        inputPost.clearComposingText();
        try {
            obj.put("title", inputTitle.getText().toString());
            obj.put("type", categoryId);
            object = new JSONObject();
            object.put("mediaType", "text");
            object.put("text", inputPost.toHtml());
            arr.put(object);
            if (imgCover.getDrawable() != null) {
                object.put("mediaType", "cover");
                object.put("image", getEncodedImage(((BitmapDrawable)imgCover.getDrawable()).getBitmap()));
                arr.put(object);
            }
            for (int i = 0; i < items.size(); i++) {
                object = new JSONObject();
                CreativityItems item = items.get(i);
                switch (item.getType()){
                    case TYPE_IMAGE:
                        object.put("mediaType", "image");
                        object.put("image", getEncodedImage(item.getBitmap()));
                        break;
                    case TYPE_YOUTUBE:
                        object.put("mediaType", "soundcloud");
                        object.put("embedUrlIframe", new JSONArray());
                        object.put("embedUrl", item.getData());
                        object.put("embedUrl1", "//w.soundcloud.com/player/?url=" + item.getData());
                        break;
                    case TYPE_SOUND:
                        object.put("mediaType", "text");

                        break;
                    case TYPE_VIMEO:
                        object.put("mediaType", "text");

                        break;
                }
                arr.put(object);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return obj;
    }

    private String getEncodedImage(Bitmap bitmap) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos); //bm is the bitmap object
        byte[] b = baos.toByteArray();
        return "data:image/png;base64," + Base64.encodeToString(b, Base64.URL_SAFE);
    }

    public void showFilters(boolean firstTime) {

        LayoutInflater inflater = getLayoutInflater();
        final View convertView = (View) inflater.inflate(R.layout.comp_interests, null);
        convertView.setPadding(0, 32, 0, 32);
        clickListeners(convertView);

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this)
                .setView(convertView)
                .setTitle("Pick a category")
                .setCancelable(false);

        if (!firstTime) {
            intItem.setTextColor(Color.WHITE);
            intItemContainer.setCardBackgroundColor(Color.RED);
            alertDialog.setPositiveButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                }
            });
        }

        dialog = alertDialog.create();
        dialog.show();

    }

    private void clickListeners(View view) {

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

    @Override
    public void onClick(View view) {
        intItemContainer = (CardView) view;
        intItem = (TextView) view.findViewById(dataSet.list.get(view.getId()));
        onSelect();
    }

    private void onSelect() {

        if (intItem.getCurrentTextColor() == Color.parseColor("#454545")) {
            intItem.setTextColor(Color.WHITE);
            intItemContainer.setCardBackgroundColor(Color.RED);
            categoryId = Integer.parseInt(getResources().getResourceEntryName(intItem.getId()).substring(1));
            category = intItem.getText().toString();

            Spannable spannable = new SpannableString("in " + category);
            int start = 3;
            int end = start + category.length();

            spannable.setSpan(new ClickableSpan() {
                @Override
                public void onClick(View widget) {
                    showFilters(false);
                }
            }, start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            spannable.setSpan(new ForegroundColorSpan(Color.parseColor("#0570C0")), start, end,
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            textCat.setText(spannable);
            textCat.setHighlightColor(Color.TRANSPARENT);
        }

        dialog.dismiss();
    }
}
