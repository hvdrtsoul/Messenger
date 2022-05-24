package com.ragelar.messenger;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link DialogsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DialogsFragment extends Fragment implements DialogApadter.OnDialogListener{

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    List<Dialog> dialogs = new ArrayList<>();
    Context appContext;
    NavController navController;

    public DialogsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment DialogsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static DialogsFragment newInstance(String param1, String param2) {
        DialogsFragment fragment = new DialogsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    private void insertDialog(Context appContext){
        class InsertTask extends AsyncTask<Void, Void, Void> {
            @Override
            protected Void doInBackground(Void... voids) {
                Dialog dialog = new Dialog();

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

    void setDialogs(List<Dialog> newDialog){
        this.dialogs.clear();
        this.dialogs.addAll(newDialog);
    }

    private void getDialogs(Context appContext){
        class GetTask extends AsyncTask<Void, Void, List<Dialog>> {
            @Override
            protected List<Dialog> doInBackground(Void... voids) {
                List<Dialog> dialogs = DatabaseClient.getInstance(appContext).getAppDatabase()
                        .dialogDao().getAll();

                return dialogs;
            }

            @Override
            protected void onPostExecute(List<Dialog> dialogs) {
                super.onPostExecute(dialogs);
                //setDialogs(dialogs);
            }
        }

        GetTask getTask = new GetTask();
        getTask.execute();

        try {
            this.dialogs.clear();
            this.dialogs.addAll(getTask.get());
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
            Toast.makeText(appContext, "Ошибка при загрузке списка диалогов. Пожалуйста, попробуйте ещё раз", Toast.LENGTH_SHORT).show();
        }


    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_dialogs, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        appContext = this.getActivity().getApplicationContext();
        navController = Navigation.findNavController(view);

        RecyclerView recyclerView = view.findViewById(R.id.dialogs_list);
        Button addDialogButton = view.findViewById(R.id.addDialogButton);
        recyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));

        getDialogs(appContext);
        DialogApadter adapter = new DialogApadter(this.getContext(), this.dialogs, this);
        recyclerView.setAdapter(adapter);

        Handler handler = new Handler();
        Runnable refreshAdapterTask = new Runnable() {
            @Override
            public void run() {
                        getDialogs(appContext);
                        adapter.notifyDataSetChanged();
                        handler.postDelayed(this, 5000);
                    }
            };

        refreshAdapterTask.run();

        addDialogButton.setOnClickListener(v -> {
            navController.navigate(R.id.action_dialogsFragment_to_addDialogFragment);
        });
    }

    @Override
    public void onDialogCLick(int position) {
        DialogsFragmentDirections.ActionDialogsFragmentToMessagesFragment action = DialogsFragmentDirections.actionDialogsFragmentToMessagesFragment();
        action.setUserName(dialogs.get(position).getUserName());
        navController.navigate(action);
    }
}