package com.tforlive.batterysaverpro.adapter;

import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.tforlive.batterysaverpro.R;
import com.tforlive.batterysaverpro.model.Whitelist;

import java.util.List;

public class WhiteListAddAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private class ViewHolder extends RecyclerView.ViewHolder {

        private TextView tvName;
        private CheckBox checkBoxAddItem;
        private ImageView imgIconApp;


        ViewHolder(View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tvName);
            checkBoxAddItem = itemView.findViewById(R.id.checkboxAddItem);
            imgIconApp = itemView.findViewById(R.id.imgIconApp);
        }
    }

    private List<Whitelist> mWhitelists;
    private OnHandleItemClickListener mOnHandleItemClickListener;

    public WhiteListAddAdapter(List<Whitelist> whitelists,
                               OnHandleItemClickListener onHandleItemClickListener) {
        mWhitelists = whitelists;
        mOnHandleItemClickListener = onHandleItemClickListener;
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int type) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_whitelist_add, viewGroup, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof ViewHolder) {
            int i = holder.getBindingAdapterPosition();
            Whitelist whitelist = mWhitelists.get(i);
            final ViewHolder viewHolder = (ViewHolder) holder;

            viewHolder.tvName.setText(whitelist.getApplicationName());
            viewHolder.imgIconApp.setImageDrawable(whitelist.getIcon());
            viewHolder.checkBoxAddItem.setChecked(whitelist.isCheck());
            viewHolder.checkBoxAddItem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mOnHandleItemClickListener.onClickCheckBox(!viewHolder.checkBoxAddItem.isChecked(), i);
                }
            });
        }
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return mWhitelists.size();
    }

    public interface OnHandleItemClickListener {
        void onClickCheckBox(boolean isCheck, int position);
    }
}
