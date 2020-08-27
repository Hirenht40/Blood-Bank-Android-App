package com.example.bloodbank.Adapters;

import static android.Manifest.permission.CALL_PHONE;
import static android.Manifest.permission.READ_EXTERNAL_STORAGE;

import android.Manifest.permission;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.core.content.PermissionChecker;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.example.bloodbank.Adapters.RequestAdapter.ViewHolder;
import com.example.bloodbank.Datamodels.RequestDataModel;
import com.example.bloodbank.R;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.List;

public class RequestAdapter extends RecyclerView.Adapter<ViewHolder> {

    private List<RequestDataModel> dataSet;
    private Context context;

    public RequestAdapter(
            List<RequestDataModel> dataSet, Context context) {
        this.dataSet = dataSet;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.request_item_layout, parent, false);
        return new ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder,
                                 final int position) {
        holder.message.setText(dataSet.get(position).getMessage());
        Glide.with(context).load(dataSet.get(position).getUrl()).into(holder.imageView);
        holder.callButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (PermissionChecker.checkSelfPermission(context, CALL_PHONE)
                        == PermissionChecker.PERMISSION_GRANTED) {
                    Intent intent = new Intent(Intent.ACTION_CALL);
                    intent.setData(Uri.parse("tel:" + dataSet.get(position).getNumber()));
                    context.startActivity(intent);
                } else {
                    ((Activity) context).requestPermissions(new String[]{CALL_PHONE}, 401);
                }
            }
        });

        holder.shareButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent shareIntent = new Intent(Intent.ACTION_SEND);
                shareIntent.setType("text/plain");
                shareIntent.putExtra(Intent.EXTRA_TEXT,
                        holder.message.getText().toString() + "\n\nContact: " + dataSet.get(position)
                                .getNumber());
                shareIntent.putExtra(Intent.EXTRA_SUBJECT, "Hey, could you help here");
                context.startActivity(Intent.createChooser(shareIntent, "Share..."));
            }
        });
    }


    @Override
    public int getItemCount() {
        return dataSet.size();
    }


    class ViewHolder extends RecyclerView.ViewHolder {

        TextView message;
        ImageView imageView, callButton, shareButton;

        ViewHolder(final View itemView) {
            super(itemView);
            message = itemView.findViewById(R.id.message);
            imageView = itemView.findViewById(R.id.image);
            callButton = itemView.findViewById(R.id.call_button);
            shareButton = itemView.findViewById(R.id.share_button);
        }

    }

}