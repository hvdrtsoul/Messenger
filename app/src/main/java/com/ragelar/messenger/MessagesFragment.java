package com.ragelar.messenger;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class MessagesFragment extends Fragment {

    List<Dialog> dialogs = new ArrayList<>();

    public MessagesFragment() {
        // Required empty public constructor
    }

    private void getDialogs(Context appContext){
        class GetTask extends AsyncTask<Void, Void, List<Dialog>> {
            @Override
            protected List<Dialog> doInBackground(Void... voids) {
                List<Dialog> dialogs = DatabaseClient.getInstance(appContext).getAppDatabase()
                        .dialogDao().getAll();

                return dialogs;
            }
        }

        GetTask getTask = new GetTask();
        getTask.execute();

        try {
            this.dialogs = getTask.get();
        } catch (ExecutionException e) {
            e.printStackTrace();
            Toast.makeText(appContext, "Ошибка при загрузке списка диалогов. Пожалуйста, попробуйте ещё раз", Toast.LENGTH_SHORT).show();
        } catch (InterruptedException e) {
            e.printStackTrace();
            Toast.makeText(appContext, "Ошибка при загрузке списка диалогов. Пожалуйста, попробуйте ещё раз", Toast.LENGTH_SHORT).show();
        }
    }

    private void insertDialog(Context appContext){
        class InsertTask extends AsyncTask<Void, Void, Void> {
            @Override
            protected Void doInBackground(Void... voids) {
                Dialog dialog = new Dialog();

                dialog.setDialogId(90);
                dialog.setUserName("TEST");
                dialog.setLastMessage("TEST LAST MESSAGE");
                DatabaseClient.getInstance(appContext).getAppDatabase()
                        .dialogDao().addDialog(dialog);

                return null;
            }
        }

        InsertTask insertTask = new InsertTask();
        insertTask.execute();
    }




    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        RecyclerView recyclerView = view.findViewById(R.id.dialogs_l);
        recyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));

        getDialogs(this.getActivity().getApplicationContext());
        DialogApadter adapter = new DialogApadter(this.getContext(), this.dialogs);

        recyclerView.setAdapter(adapter);

        Button testButton = view.findViewById(R.id.button2);

        testButton.setOnClickListener(v -> {
            insertDialog(this.getActivity().getApplicationContext());
        });


    }


}