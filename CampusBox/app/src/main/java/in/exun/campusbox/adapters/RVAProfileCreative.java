package in.exun.campusbox.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import in.exun.campusbox.R;
import in.exun.campusbox.jsonHandlers.CreativeJsonHandler;

/**
 * Created by Anurag145 on 4/30/2017.
 */

public class RVAProfileCreative extends RecyclerView.Adapter<RVAProfileCreative.ViewHolder> {
 CreativeJsonHandler creativeJsonHandler; Context context;
    public RVAProfileCreative(CreativeJsonHandler creativeJsonHandler, Context context)
    {

        this.creativeJsonHandler=creativeJsonHandler;

        this.context=context;
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {     View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.rc_overview_creativity,parent,false);
         return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Log.e("onBindViewHolder: ",creativeJsonHandler.getTitle(position) );
        holder.textView.setText(creativeJsonHandler.getTitle(position));
        try {
            holder.textView1.setText(creativeJsonHandler.getDate(position));
        }catch (Exception e)
        {

        }
        Glide.with(context)
                .load(creativeJsonHandler.getAuthorImage(position))
                .placeholder(R.drawable.ic_account_placeholder)
                .error(R.drawable.ic_account_placeholder)
                .into(holder.imageView);


    }

    @Override
    public int getItemCount() {
        return creativeJsonHandler.length();
    }

    static class ViewHolder extends RecyclerView.ViewHolder
    {      ImageView imageView;
           TextView textView ,textView1;

        public ViewHolder(View itemView) {
            super(itemView);
            textView=(TextView)itemView.findViewById(R.id.name);
            textView1=(TextView)itemView.findViewById(R.id.date);
            imageView=(ImageView)itemView.findViewById(R.id.image);
        }
    }
}
