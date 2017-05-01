package in.exun.campusbox.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import in.exun.campusbox.R;
import in.exun.campusbox.jsonHandlers.FollowJsonHandler;

/**
 * Created by Anurag145 on 5/1/2017.
 */

public class RVAFollow extends RecyclerView.Adapter<RVAFollow.ViewHolder> {
        FollowJsonHandler followJsonHandler; Context context;
public RVAFollow(FollowJsonHandler followJsonHandler, Context context)
        {


        this.followJsonHandler=followJsonHandler;

        this.context=context;
        }
@Override
public RVAFollow.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
        {     View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.rc_follow,parent,false);
        return new RVAFollow.ViewHolder(view);
        }

@Override
public void onBindViewHolder(RVAFollow.ViewHolder holder, int position) {

        holder.textView.setText(followJsonHandler.getTitle(position));

        holder.textView1.setText(followJsonHandler.getCollege(position).trim());
        if(followJsonHandler.getAbout(position)!=null)
        holder.textView2.setText(followJsonHandler.getAbout(position));
        else
            holder.textView2.setText("Apparently, this user prefers to keep an air of mystery about them");
    Glide.with(context)
            .load(followJsonHandler.getPhoto(position))
            .placeholder(R.drawable.ic_account_placeholder)
            .error(R.drawable.ic_account_placeholder)
            .into(holder.imageView);




        }

@Override
public int getItemCount() {

    Log.e( "getItemCount: ",String.valueOf(followJsonHandler.Length()) );
    return followJsonHandler.Length();
        }

static class ViewHolder extends RecyclerView.ViewHolder
{    ImageView imageView;
    TextView textView ,textView1,textView2;

    public ViewHolder(View itemView) {
        super(itemView);
        textView=(TextView)itemView.findViewById(R.id.name);
        textView1=(TextView)itemView.findViewById(R.id.college);
        textView2=(TextView)itemView.findViewById(R.id.about);
        imageView=(ImageView)itemView.findViewById(R.id.image);
    }
}
}



