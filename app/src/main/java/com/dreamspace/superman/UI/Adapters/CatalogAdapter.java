package com.dreamspace.superman.UI.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.dreamspace.superman.Common.Tools;
import com.dreamspace.superman.R;
import com.dreamspace.superman.model.Catalog;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Wells on 2016/2/6.
 */
public class CatalogAdapter extends RecyclerView.Adapter<CatalogAdapter.catalogViewHolder> {
    private List<Catalog> mCatalogs;
    private Context mContext;
    private LayoutInflater inflater;
    private onCatalogClickListener listener;

    public CatalogAdapter(List<Catalog> mCatalogs, Context mContext) {
        this.mCatalogs = mCatalogs;
        this.mContext = mContext;
        inflater = LayoutInflater.from(mContext);
    }

    @Override
    public catalogViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = inflater.inflate(R.layout.catalog_recyleview_item, parent, false);
        catalogViewHolder holder = new catalogViewHolder(itemView);
        return holder;
    }


    @Override
    public void onBindViewHolder(catalogViewHolder holder, final int position) {
        final Catalog catalog = mCatalogs.get(position);
        Tools.showImageWithGlide(mContext, holder.catalogIv, catalog.getIcon());
        holder.catalogTv.setText(catalog.getName());
        if (listener != null) {
            holder.catalogIv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onCatalogClick(catalog, v, position);
                }
            });
            holder.catalogTv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onCatalogClick(catalog, v, position);
                }
            });
        }
    }

    public void setListener(onCatalogClickListener listener) {
        this.listener = listener;
    }

    @Override
    public int getItemCount() {
        return mCatalogs.size();
    }

    public interface onCatalogClickListener {
        void onCatalogClick(Catalog catalog, View v, int position);
    }

    public static class catalogViewHolder extends RecyclerView.ViewHolder {

        private CircleImageView catalogIv;
        private TextView catalogTv;

        public catalogViewHolder(View itemView) {
            super(itemView);
            catalogIv = (CircleImageView) itemView.findViewById(R.id.catalog_iv);
            catalogTv = (TextView) itemView.findViewById(R.id.catalog_tv);

        }
    }
}
