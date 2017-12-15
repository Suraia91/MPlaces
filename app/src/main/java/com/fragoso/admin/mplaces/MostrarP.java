package com.fragoso.admin.mplaces;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

/**
 * Created by admin on 12/15/17.
 */

public class MostrarP extends  RecyclerView.Adapter<MostrarP.ViewHolder> {
    private List<Postagens> feedItemList;
    private Context mContext;

    public MostrarP(Context context, List<Postagens> feedItemList) {
        this.feedItemList = feedItemList;
        this.mContext = context;
    }
    private String[] titles = {"Chapter One",
            "Chapter Two",
            "Chapter Three",
            "Chapter Four",
            "Chapter Five",
            "Chapter Six",
            "Chapter Seven",
            "Chapter Eight"};
    private String[] details = {"Item one details",
            "Item two details", "Item three details",
            "Item four details", "Item file details",
            "Item six details","Item seven details",
            "Item eight details"};
    @Override
    public MostrarP.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.cardview, viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }


    @Override
    public void onBindViewHolder(MostrarP.ViewHolder viewHolder, int i) {
        // Reclamcao feedItem = feedItemList.get(i);
        viewHolder.itemTitle.setText(feedItemList.get(i).getComentarios());

        viewHolder.itemDetail.setText(feedItemList.get(i).getComentarios());
    }

    @Override
    public int getItemCount() {
        return feedItemList.size();
    }
    class ViewHolder extends RecyclerView.ViewHolder {

        public TextView itemTitle;
        public TextView itemDetail;
        public ViewHolder(View itemView) {
            super(itemView);

            itemTitle =
                    (TextView)itemView.findViewById(R.id.tvComentario);
            itemDetail =
                    (TextView)itemView.findViewById(R.id.tvData);
        }
    }
}
