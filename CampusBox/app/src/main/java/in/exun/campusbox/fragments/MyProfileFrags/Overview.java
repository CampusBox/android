package in.exun.campusbox.fragments.MyProfileFrags;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import org.json.JSONObject;
import in.exun.campusbox.R;


/**
 * Created by Anurag145 on 4/30/2017.
 */

public class Overview extends Fragment{
 public String mydata;
    public EditText editText;
    public TextView textView,textView1,textView2;
    public ImageView imageView,imageView1,imageView2;
    public View rootview;
    boolean flag1=false,flag2=false;

    public Overview(){

    }
public static Overview instance(){return new Overview();}
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
         rootview= inflater.inflate(R.layout.fragment_overview,container,false);
          mydata=getArguments().getString("data");

        textView=(TextView)rootview.findViewById(R.id.name);
        textView1=(TextView)rootview.findViewById(R.id.college);
        editText=(EditText) rootview.findViewById(R.id.about_text);

         imageView=(ImageView)rootview.findViewById(R.id.profilepic);
          imageView1=(ImageView)rootview.findViewById(R.id.about_text_edit);
         imageView2=(ImageView)rootview.findViewById(R.id.edit_skill);


                initialize();
        imageView1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (!flag1) {

                    imageView1.setImageResource(R.drawable.ic_checkbox_marked_outline_grey600_18dp);
                    editText.setInputType(InputType.TYPE_CLASS_TEXT);

                } else {
                    imageView1.setImageResource(R.drawable.ic_pencil_grey600_18dp);
                    editText.setInputType(InputType.TYPE_NULL);
                }
            }
        });
       imageView2.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               if (!flag1) {

                   imageView2.setImageResource(R.drawable.ic_checkbox_marked_outline_grey600_18dp);

               } else {
                   imageView2.setImageResource(R.drawable.ic_pencil_grey600_18dp);
               }
           }
       });
                return rootview;
    }
    public void initialize()
    {   try {

        JSONObject jsonObject = new JSONObject(mydata);
       jsonObject=new JSONObject( jsonObject.getString("data"));
        Log.e( "initialize: ","getting Image" );
        Glide.with(getActivity().getApplicationContext())
                .load(jsonObject.getString("photo"))
                .placeholder(R.drawable.ic_account_placeholder)
                .error(R.drawable.ic_account_placeholder)
                .into(imageView);

        textView.setText(jsonObject.getString("name"));
        textView1.setText((new JSONObject(jsonObject.getString("college")).getString("name")));
        Log.e( "initialize: ",jsonObject.getString("subtitle") );
        String str=jsonObject.getString("subtitle");
        if(str!=null) {
            Log.e( "initialize: ",str );
            editText.setText("Apparently, this user prefers to keep an air of mystery about them");
        }
        else {

            editText.setText(jsonObject.getString("subtitle"));
        }
        editText.setInputType(InputType.TYPE_NULL);


    }catch (Exception e)
    {
        Log.e( "initialize: ",e.toString() );
    }
    }
}
