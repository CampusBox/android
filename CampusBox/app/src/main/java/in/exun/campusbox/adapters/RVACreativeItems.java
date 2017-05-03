package in.exun.campusbox.adapters;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;

import in.exun.campusbox.R;
import in.exun.campusbox.model.CreativityItems;

/**
 * Created by ayush on 03/05/17.
 */

public class RVACreativeItems extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final String TAG = "RVACreative";

    private static final int TYPE_IMAGE = 0;
    private static final int TYPE_SOUND = 1;
    private static final int TYPE_YOUTUBE = 2;

    private List<CreativityItems> items = new ArrayList<>();
    private static MyClickListener myClickListener;

    public void setOnItemClickListener(RVACreativeItems.MyClickListener myClickListener) {
        RVACreativeItems.myClickListener = myClickListener;
    }

    public interface MyClickListener {
        void onItemClick(int position, View v, int type);
    }

    public RVACreativeItems(List<CreativityItems> items) {
        this.items = items;
    }

    static class ImageViewHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener, View.OnLongClickListener {

        ImageView iv;

        ImageViewHolder(View view) {
            super(view);
            iv = (ImageView) view.findViewById(R.id.layout_iv);

            view.setOnClickListener(this);
            view.setOnLongClickListener(this);
        }

        @Override
        public void onClick(View v) {
            myClickListener.onItemClick(getAdapterPosition(), v, 0);
        }

        @Override
        public boolean onLongClick(View v) {
            myClickListener.onItemClick(getAdapterPosition(), v, 1);
            return true;
        }
    }

    static class WebViewHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener, View.OnLongClickListener {

        WebView wv;

        WebViewHolder(View view) {
            super(view);
            wv = (WebView) view.findViewById(R.id.webview);
            view.setOnClickListener(this);
            view.setOnLongClickListener(this);
        }

        @Override
        public void onClick(View v) {
            myClickListener.onItemClick(getAdapterPosition(), v, 0);
        }

        @Override
        public boolean onLongClick(View v) {
            myClickListener.onItemClick(getAdapterPosition(), v, 1);
            return true;
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;

        if (viewType == TYPE_IMAGE) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_image, parent, false);
            return new RVACreativeItems.ImageViewHolder(view);
        } else {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_webview, parent, false);
            return new RVACreativeItems.WebViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {

        if (viewHolder instanceof ImageViewHolder) {
            ImageViewHolder holder = (ImageViewHolder) viewHolder;
            int counter = holder.getAdapterPosition();
            holder.iv.setImageBitmap(items.get(counter).getBitmap());
        } else {
            WebViewHolder holder = (WebViewHolder) viewHolder;
            int counter = holder.getAdapterPosition();
            if (items.get(counter).getType() == TYPE_SOUND) {

                String link = items.get(counter).getData();
                link = link.substring(link.indexOf("://") + 3);

                String html = "<iframe width=\"100%\" height=\"200\" scrolling=\"no\" frameborder=\"no\" " +
                        "src=\"https://w.soundcloud.com/player/?url=https%3A//" + link +
                        "&color=ff5500&auto_play=false&hide_related=false&show_comments=true&show_user=true&show_reposts=false\"></iframe>\n";

                Log.d(TAG, "onBindViewHolder: " + html);

                holder.wv.getSettings().setJavaScriptEnabled(true);
                holder.wv.getSettings().setLoadWithOverviewMode(true);
                holder.wv.getSettings().setUseWideViewPort(true);
                holder.wv.loadData(html, "text/html", "UTF-8");
            } else if (items.get(counter).getType() == TYPE_YOUTUBE){
                String link = items.get(counter).getData();
                String html = "<iframe width=\"420\" height=\"315\" src=" +
                        link.replace("watch?v=","embed/") +
                        " frameborder=\"0\" allowfullscreen></iframe>";
                holder.wv.getSettings().setJavaScriptEnabled(true);
                holder.wv.loadData(html, "text/html", "utf-8");

            } else {
                String link = items.get(counter).getData();
                String html = "<iframe src=\"https://player.vimeo.com/video/" +
                        link.substring(link.lastIndexOf("/")+1) + "\" width=\"400\" height=\"225\" frameborder=\"0\" " +
                        "webkitallowfullscreen mozallowfullscreen allowfullscreen></iframe>";
                Log.d(TAG, "onBindViewHolder: " + html);
                holder.wv.getSettings().setJavaScriptEnabled(true);
                holder.wv.loadData(html, "text/html", "utf-8");
            }
        }
    }

    @Override
    public int getItemViewType(int position) {
        return items.get(position).getType();
    }

    @Override
    public int getItemCount() {
        return items.size();
    }
}
