package com.mgfdev.elcaminodelacerveza.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.mgfdev.elcaminodelacerveza.R;
import com.mgfdev.elcaminodelacerveza.activities.BrewerDetail;
import com.mgfdev.elcaminodelacerveza.activities.DownloadImageTask;
import com.mgfdev.elcaminodelacerveza.data.BrewerInfo;
import com.mgfdev.elcaminodelacerveza.services.MemoryCache;
import com.squareup.picasso.Picasso;

import org.apache.commons.lang3.StringUtils;

import java.util.List;

/**
 * Created by Maxi on 16/12/2017.
 */

public class BrewerListAdapter extends ArrayAdapter<BrewerInfo> {
    private int layoutResourceId;
    private  LayoutInflater inflater;
    private  List<BrewerInfo> data;

    public BrewerListAdapter(Context context, int layoutResourceId, List<BrewerInfo> items) {
        super(context, layoutResourceId, items);
        this.layoutResourceId = layoutResourceId;
        inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        data = items;
    }
    public int getCount() {
        return data.size();
    }

    public BrewerInfo  getItem(int position) {
        return data.get(position);
    }

    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        String imageURL = data.get(position).getDetailImages();
        if(imageURL == null || imageURL.isEmpty()){
            return 1; // text based
        }else{
            return 0; // image based
        }
    }

    private static class ViewHolder {
        protected TextView txtTitle, txtAddress;
        protected ImageView thumbnailIcon;
        boolean isSet;

    }


    public View getView(int position, View convertView, ViewGroup parent) {
        View vi=convertView;
        final BrewerInfo currentItem = data.get(position);
        int type = getItemViewType(position);
        ViewHolder holder = new ViewHolder();

        vi = inflater.inflate(R.layout.brewer_list_row, null);
        holder.thumbnailIcon = (ImageView) vi.findViewById(R.id.beerThumbnail);

        holder.txtTitle = (TextView) vi.findViewById(R.id.brewerNameText);
        holder.txtAddress = (TextView) vi.findViewById(R.id.addressText);
        Picasso.with(getContext())
                .load(currentItem.getDetailImages())
                .into(holder.thumbnailIcon);
        holder.txtTitle.setText(currentItem.getBrewery());
        holder.txtAddress.setText("Direccion: " + currentItem.getAddress());
        vi.setTag(holder);
        ImageView arrow = (ImageView) vi.findViewById(R.id.detailImageButton);
        arrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent detailIntent = new Intent(getContext(), BrewerDetail.class);
                detailIntent.putExtra("Brewer", currentItem);
                getContext().startActivity(detailIntent);
            }
        });
        //getImageBitmap(data.get(position), thumbnailIcon);

       // Picasso.with(getContext()).load(currentItem.getDetailImages()).into(thumbnailIcon);
        return vi;
    }

    private void getImageBitmap(BrewerInfo p, ImageView thumbnailIcon ){
        if (StringUtils.isNotBlank(p.getDetailImages())
                && MemoryCache.getInstance().get(p.getDetailImages())== null){
            DownloadImageTask task = new DownloadImageTask(thumbnailIcon, R.dimen.brewer_thumb_height,
                    R.dimen.brewer_thumb_width,p.getDetailImages());
            task.execute();
        }else if(StringUtils.isNotBlank(p.getDetailImages())) {
            thumbnailIcon.setImageBitmap(MemoryCache.getInstance().get(p.getDetailImages()));
        }
    }

}


