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

    public DialogApadter(Context context, List<Dialog> dialogs) {
        this.dialogs = dialogs;
        this.inflater = LayoutInflater.from(context);
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.fragment_dialog, parent, false);
        return new ViewHolder(view);
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

    public static class ViewHolder extends RecyclerView.ViewHolder{

        final TextView userName;
        final TextView lastMessage;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            userName = itemView.findViewById(R.id.userNameText);
            lastMessage = itemView.findViewById(R.id.lastMessageText);
        }
    }
}
