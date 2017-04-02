package github.com.anurag145.campusbox;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

public class HOME extends AppCompatActivity {
    private int count = 0;
    private TextView textView;
    private CardView cardView;
    DataSet ob;
    View view ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        ob = new DataSet();
         view =findViewById(R.id.loginSequence);
    }

    public void onClick(View view) {
        cardView = (CardView) view;
        textView = (TextView) findViewById(ob.list.get(view.getId()));
        onSelect();
            }

    public void onClickLogin(View view) {

    }

    public void onSelect()

    {
        if (textView.getCurrentTextColor() == Color.parseColor("#454545")) {
            textView.setTextColor(Color.WHITE);
             cardView.setCardBackgroundColor(Color.RED);
            count++;
            if(count==3)
             view.setVisibility(View.VISIBLE);

        }else
        {
            textView.setTextColor(Color.parseColor("#454545"));
            cardView.setCardBackgroundColor(Color.parseColor("#ececec"));
            count--;
            if(count<3&&view.getVisibility()==View.VISIBLE)
                view.setVisibility(View.GONE);

        }
    }
}


