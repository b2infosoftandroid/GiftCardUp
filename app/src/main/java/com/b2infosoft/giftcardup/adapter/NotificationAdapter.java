package com.b2infosoft.giftcardup.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.b2infosoft.giftcardup.R;
import com.b2infosoft.giftcardup.app.Config;
import com.b2infosoft.giftcardup.app.Format;
import com.b2infosoft.giftcardup.app.Tags;
import com.b2infosoft.giftcardup.app.Urls;
import com.b2infosoft.giftcardup.credential.Active;
import com.b2infosoft.giftcardup.custom.Progress;
import com.b2infosoft.giftcardup.model.Notification;
import com.b2infosoft.giftcardup.volly.DMRRequest;

import java.util.List;

public class NotificationAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private final String TAG = NotificationAdapter.class.getName();
    private Urls urls;
    private Tags tags;
    private Active active;
    private Format format;
    DMRRequest dmrRequest;
    private Progress progress;
    private Context context;
    private List<Notification> notificationList;
    private Config config;

    public NotificationAdapter(Context context, List<Notification> notificationList) {
        this.context = context;
        this.notificationList = notificationList;
        config = Config.getInstance();
        tags = Tags.getInstance();
        dmrRequest = DMRRequest.getInstance(context, TAG);
        urls = Urls.getInstance();
        tags = Tags.getInstance();
        active = Active.getInstance(context);
        format = Format.getInstance();
        progress = new Progress(context);
    }

    public class NotificationHolder extends RecyclerView.ViewHolder {
        TextView message, sender_name, time_ago;

        public NotificationHolder(View view) {
            super(view);
            message = (TextView) view.findViewById(R.id.message);
            sender_name = (TextView) view.findViewById(R.id.sender_name);
            time_ago = (TextView) view.findViewById(R.id.time_ago);
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_notification_item_layout, parent, false);
        return new NotificationHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof NotificationHolder) {
            Notification notification = notificationList.get(position);
            NotificationHolder holder1 = (NotificationHolder) holder;
            holder1.message.setText(notification.getMessage());
            holder1.sender_name.setText(notification.getSenderUserName());
            holder1.time_ago.setText(notification.getTimeAgo());
        }
    }

    @Override
    public int getItemCount() {
        return notificationList == null ? 0 : notificationList.size();
    }

    private void showMessage(String message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }

    public void add(List<Notification> items) {
        int previousDataSize = this.notificationList.size();
        this.notificationList.addAll(items);
        notifyItemRangeInserted(previousDataSize, items.size());
    }
}
