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
import in.exun.campusbox.jsonHandlers.CreativeJsonHandler;
import in.exun.campusbox.jsonHandlers.EventJsonHandler;

/**
 * Created by Anurag145 on 4/30/2017.
 */

public class RVAProfileEvents extends RecyclerView.Adapter<RVAProfileEvents.ViewHolder> {
    EventJsonHandler eventJsonHandler; Context context;
    public RVAProfileEvents(EventJsonHandler eventJsonHandler, Context context)
                {


        this.eventJsonHandler=eventJsonHandler;

        this.context=context;
    }
    @Override
    public RVAProfileEvents.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {     View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.rc_overview_eventscreated,parent,false);
        return new RVAProfileEvents.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RVAProfileEvents.ViewHolder holder, int position) {
        Log.e("onBindViewHolder: ",eventJsonHandler.getTitle(position) );
        holder.textView.setText(eventJsonHandler.getTitle(position));

            holder.textView1.setText(eventJsonHandler.getDesc(position));




    }

    @Override
    public int getItemCount() {
        return eventJsonHandler.Length();
    }

    static class ViewHolder extends RecyclerView.ViewHolder
    {
        TextView textView ,textView1;

        public ViewHolder(View itemView) {
            super(itemView);
            textView=(TextView)itemView.findViewById(R.id.name);
            textView1=(TextView)itemView.findViewById(R.id.desc);

        }
    }
}


