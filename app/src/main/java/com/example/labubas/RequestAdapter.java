package com.example.labubas;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;

public class RequestAdapter extends RecyclerView.Adapter<RequestAdapter.RequestViewHolder> {

    Context context;
    ArrayList<RequestModel> list;
    OnEditListener onEditListener;

    public interface OnEditListener {
        void onEditClick(RequestModel request);
    }

    public RequestAdapter(Context context, ArrayList<RequestModel> list, OnEditListener onEditListener) {
        this.context = context;
        this.list = list;
        this.onEditListener = onEditListener;
    }

    @NonNull
    @Override
    public RequestViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.item_request_card, parent, false);
        return new RequestViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull RequestViewHolder holder, int position) {
        RequestModel request = list.get(position);

        holder.tvDescription.setText(request.getDescriptionForList());
        String statusText = "Статус: " + request.getStatus() + "\nДата: " + request.getDate();
        holder.tvStatus.setText(statusText);

        if (onEditListener == null) {
            holder.btnEdit.setVisibility(View.GONE);
        } else {
            holder.btnEdit.setVisibility(View.VISIBLE);
            holder.btnEdit.setOnClickListener(v -> {
                onEditListener.onEditClick(request);
            });
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class RequestViewHolder extends RecyclerView.ViewHolder {
        TextView tvDescription, tvStatus;
        Button btnEdit;

        public RequestViewHolder(@NonNull View itemView) {
            super(itemView);
            tvDescription = itemView.findViewById(R.id.tvDescription);
            tvStatus = itemView.findViewById(R.id.tvStatus);
            btnEdit = itemView.findViewById(R.id.btnEdit);
        }
    }
}