package com.ragelar.messenger;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class DialogApadter extends RecyclerView.Adapter<DialogApadter.ViewHolder> {

    private final LayoutInflater inflater;
    private final List<Dialog> dialogs;
    private OnDialogListener onDialogListener;

    public DialogApadter(Context context, List<Dialog> dialogs, OnDialogListener onDialogListener) {
        this.dialogs = dialogs;
        this.inflater = LayoutInflater.from(context);
        this.onDialogListener = onDialogListener;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.fragment_dialog, parent, false);
        return new ViewHolder(view, onDialogListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Dialog dialog = dialogs.get(position);
        holder.userName.setText(dialog.getUserName());
        holder.lastMessage.setText(dialog.getLastMessage());
    }

    @Override
    public int getItemCount() {
        return dialogs.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        final TextView userName;
        final TextView lastMessage;
        OnDialogListener onDialogListener;

        public ViewHolder(@NonNull View itemView, OnDialogListener onDialogListener) {
            super(itemView);
            userName = itemView.findViewById(R.id.userNameText);
            lastMessage = itemView.findViewById(R.id.lastMessageText);
            this.onDialogListener = onDialogListener;

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            onDialogListener.onDialogCLick(getAbsoluteAdapterPosition());
        }
    }

    public interface OnDialogListener{
        void onDialogCLick(int position);
    }
}
