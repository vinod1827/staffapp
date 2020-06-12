package com.kleverowl.staffapp.adapters;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;
import com.kleverowl.staffapp.R;
import com.kleverowl.staffapp.listeners.ItemClickListener;
import com.kleverowl.staffapp.models.AccountsSettingModel;
import com.kleverowl.staffapp.utils.Common;

import java.util.ArrayList;
import java.util.List;

public class ApplianceCustomRecyclerViewAdapterr extends RecyclerView.Adapter<ApplianceCustomViewHolder> {

    List<AccountsSettingModel> mItems;
    Context context;

    int row_index = -1;

    public ApplianceCustomRecyclerViewAdapterr(Context context, ArrayList<AccountsSettingModel> mItems) {
        this.context = context;
        this.mItems = mItems;
    }

    @NonNull
    @Override
    public ApplianceCustomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        return new ApplianceCustomViewHolder(inflater.inflate(R.layout.single_accounts_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ApplianceCustomViewHolder holder, int position) {
        AccountsSettingModel item = mItems.get(position);
        holder.accountSettingTitle.setText(item.getTitle());
        holder.imageView.setImageDrawable(ContextCompat.getDrawable(context,item.getDrawable()));
        holder.setItemClickListener(new ItemClickListener() {
            @Override
            public void onClick(View view, int position) {
                row_index = position;
                Common.currentItem = mItems.get(position);
                applianceClickListener.onApplianceClickListener(position);
                notifyDataSetChanged();
            }
        });

        if (item.isEnabled()) {
            holder.accountSettingTitle.setTextColor(Color.parseColor("#4285f4"));
        } else {
            holder.accountSettingTitle.setTextColor(Color.parseColor("#de000000"));
        }

        if (row_index == position) {
            holder.accountSettingTitle.setTextColor(Color.parseColor("#4285f4"));
        } else {
            holder.accountSettingTitle.setTextColor(Color.parseColor("#de000000"));
        }
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    public void setApplianceClickListener(ApplianceClickListener applianceClickListener) {
        this.applianceClickListener = applianceClickListener;
    }

    private ApplianceClickListener applianceClickListener;

    public interface ApplianceClickListener {
        void onApplianceClickListener(int position);
    }
}


class ApplianceCustomViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    final ImageView imageView;
    ItemClickListener itemClickListener;

    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }


    TextView accountSettingTitle;

    public ApplianceCustomViewHolder(@NonNull View itemView) {
        super(itemView);

        accountSettingTitle = itemView.findViewById(R.id.accountSettingTitle);
        imageView = itemView.findViewById(R.id.imageView2);
        itemView.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        itemClickListener.onClick(v, getAdapterPosition());
    }
}
