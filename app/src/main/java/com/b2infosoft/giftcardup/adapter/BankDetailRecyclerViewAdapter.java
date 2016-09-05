package com.b2infosoft.giftcardup.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import com.android.volley.VolleyError;
import com.b2infosoft.giftcardup.R;
import com.b2infosoft.giftcardup.activity.AddAccountInfo;
import com.b2infosoft.giftcardup.activity.UpdateAccountInfo;
import com.b2infosoft.giftcardup.app.Tags;
import com.b2infosoft.giftcardup.app.Urls;
import com.b2infosoft.giftcardup.credential.Active;
import com.b2infosoft.giftcardup.model.BankInfo;
import com.b2infosoft.giftcardup.volly.DMRRequest;
import com.b2infosoft.giftcardup.volly.DMRResult;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class BankDetailRecyclerViewAdapter extends RecyclerView.Adapter<BankDetailRecyclerViewAdapter.ViewHolder> {
    Context context;
    private final List<BankInfo> bankInfoList;
    Drawable add, subtruct;
    Tags tags = Tags.getInstance();
    Urls urls = Urls.getInstance();
    Active active;
    DMRRequest dmrRequest;

    public BankDetailRecyclerViewAdapter(Context context, List<BankInfo> bankInfoList) {
        this.context = context;
        this.bankInfoList = bankInfoList;
        add = context.getResources().getDrawable(R.drawable.ic_add_24dp);
        subtruct = context.getResources().getDrawable(R.drawable.ic_subtract_24dp);
        active = Active.getInstance(context);
        dmrRequest = DMRRequest.getInstance(context,"");
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_bank_detail, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        LinearLayout linearLayout = holder.linearLayout;
        ScrollView scrollView = holder.scrollView;
        final BankInfo info = bankInfoList.get(position);

        EditText name = holder.name;
        EditText routing_no = holder.routing_no;
        EditText account_no = holder.account_no;
        //EditText status = holder.status;

         name.setText(info.getName());
        routing_no.setText(info.getRoutingNumber());
        account_no.setText(info.getAccountNumber());
        //status.setText(info.getStatus());

        holder.less.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.save.setVisibility(View.GONE);
                if(holder.linearLayout.getVisibility()==View.GONE){
                    holder.linearLayout.setVisibility(View.VISIBLE);
                    holder.less.setImageDrawable(subtruct);
                }else{
                    holder.linearLayout.setVisibility(View.GONE);
                    holder.less.setImageDrawable(add);
                }
            }
        });
        holder.edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // enableInfo(holder, true);
               // holder.save.setVisibility(View.VISIBLE);
                Intent i = new Intent(context, UpdateAccountInfo.class);
                i.putExtra(tags.BANK_INFO, info);
                context.startActivity(i);
            }
        });
        holder.chooseImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //showFileChooser();
            }
        });
    //    holder.save.setOnClickListener(new View.OnClickListener() {
     //       @Override
     //       public void onClick(View v) {
                //new AddBankAccount().execute();
      //          getUpdate(holder,info);
     //       }
    //    });

    }

    private void enableInfo(ViewHolder holder, boolean isUpdate) {
        holder.name.setEnabled(isUpdate);
        holder.routing_no.setEnabled(isUpdate);
        holder.account_no.setEnabled(isUpdate);
        holder.chooseImage.setEnabled(isUpdate);
        //status.setEnabled(isUpdate);
    }

    @Override
    public int getItemCount() {
        return bankInfoList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        Button save, chooseImage;
        EditText name, routing_no, account_no, status;
        AppCompatImageView imageView;
        ImageView edit, less;
        LinearLayout linearLayout;
        ScrollView scrollView;

        public ViewHolder(View view) {
            super(view);
            this.name = (EditText) view.findViewById(R.id.bank_name);
            this.routing_no = (EditText) view.findViewById(R.id.bank_routing_no);
            this.account_no = (EditText) view.findViewById(R.id.bank_account_no);
            this.status = (EditText) view.findViewById(R.id.bank_status);
            this.linearLayout = (LinearLayout) view.findViewById(R.id.layout_2);
            this.edit = (ImageView) view.findViewById(R.id.bank_info_edit);
            this.less = (ImageView) view.findViewById(R.id.bank_info_less);
            this.save = (Button) view.findViewById(R.id.bank_save_btn);
            this.imageView = (AppCompatImageView) view.findViewById(R.id.void_check_image);
            this.chooseImage = (Button) view.findViewById(R.id.choose_void_image);
        }

    }

}
