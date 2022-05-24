package com.ragelar.messenger;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigInteger;
import java.util.concurrent.ExecutionException;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AddDialogFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AddDialogFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public AddDialogFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AddDialogFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AddDialogFragment newInstance(String param1, String param2) {
        AddDialogFragment fragment = new AddDialogFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
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
        return inflater.inflate(R.layout.fragment_add_dialog, container, false);
    }

    private void insertDialog(Context appContext, Dialog dialog) {
        class InsertTask extends AsyncTask<Dialog, Void, Void> {

            @Override
            protected Void doInBackground(Dialog... dialogs) {
                DatabaseClient.getInstance(appContext).getAppDatabase()
                        .dialogDao().addDialog(dialog);

                return null;
            }
        }

        InsertTask task = new InsertTask();
        task.execute(dialog);

    }

    private void insertMessage(Context appContext, Message message) {
        class InsertTask extends AsyncTask<Message, Void, Void> {

            @Override
            protected Void doInBackground(Message... messages) {
                DatabaseClient.getInstance(appContext).getAppDatabase()
                        .messageDao().addMessage(message);

                return null;
            }
        }

        InsertTask task = new InsertTask();
        task.execute(message);
    }

    private Boolean dialogExists(Context appContext, String userName) {
        class GetTask extends AsyncTask<String, Void, Boolean> {

            @Override
            protected Boolean doInBackground(String... strings) {
                return DatabaseClient.getInstance(appContext).getAppDatabase()
                        .dialogDao().dialogExists(userName);

            }
        }

        GetTask task = new GetTask();
        task.execute(userName);

        try {
            return task.get();
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
            return true;
        }
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Button confirmButton = view.findViewById(R.id.confirmAddDialogButton);
        EditText userName = view.findViewById(R.id.editNewDialogUsername);
        PreferenceManager preferenceManager = new PreferenceManager(this.getContext());


        confirmButton.setOnClickListener(v -> {
            String userNameText = userName.getText().toString();

            if(userNameText.charAt(0) == '!') { // if it's a nickname
                JSONObject jsonResponse = CommunicatorClient.sendGetUserNameRequest(userNameText.substring(1), preferenceManager.getSharedKey());
                try{
                    if(jsonResponse.getString("result").equals("OK")){
                        JSONObject data = jsonResponse.getJSONObject("data");
                        userNameText = data.getString("username");
                    }
                    else{
                        if(jsonResponse.getString(Constants.ADDITIONAL_INFO_HEADER).equals(Constants.NICKNAME_NOT_FOUND)){
                            Toast.makeText(this.getContext(), "Данный псевдоним не соответствует ни одному пользователю", Toast.LENGTH_SHORT).show();
                            return;
                        }
                    }
                } catch (JSONException e) {
                    Toast.makeText(this.getContext(), "Что-то пошло не так... Пожалуйста, попробуйте снова", Toast.LENGTH_LONG);
                    return;
                }

            }

            if(dialogExists(this.getActivity().getApplicationContext(), userNameText)){
                Toast.makeText(this.getContext(), "Диалог с этим пользователем уже существует", Toast.LENGTH_SHORT).show();
                return;
            }


            DFHProvider dfhProvider = new DFHProvider();
            Sanitizer sanitizer = new Sanitizer();

            BigInteger privateKey = dfhProvider.generatePrivateKey();
            BigInteger publicKey = dfhProvider.generatePublicKey(privateKey);

            JSONObject jsonResponse = CommunicatorClient.sendSendRequest(preferenceManager.getSession(),
                    userNameText, preferenceManager.getUserName(), "request", sanitizer.sanitize(publicKey.toByteArray()), preferenceManager.getSharedKey());

            try{
                if(jsonResponse.getString("result").equals("OK")){
                    JSONObject data = jsonResponse.getJSONObject("data");
                    String timestampString = data.getString(Constants.SEND_TIMESTAMP_HEADER);

                    Dialog dialog = new Dialog();

                    dialog.setUserName(userNameText);
                    dialog.setLastMessage("ОТПРАВЛЕН ПУБЛИЧНЫЙ КЛЮЧ");
                    dialog.setPrivateKey(privateKey.toString());

                    Message message = new Message();

                    message.setMessageType("request");
                    message.setTo(userNameText);
                    message.setFrom("me");
                    message.setTimestamp(Long.valueOf(timestampString));
                    message.setData("ОТПРАВЛЕН ПУБЛИЧНЫЙ КЛЮЧ");

                    insertDialog(this.getActivity().getApplicationContext(), dialog);
                    insertMessage(this.getActivity().getApplicationContext(), message);
                    Toast.makeText(this.getContext(),"Новый собеседник успешно добавлен!", Toast.LENGTH_SHORT).show();
                }
                else{
                    if(jsonResponse.getString(Constants.ADDITIONAL_INFO_HEADER).equals(Constants.SEND_RECIPIENT_DOES_NOT_EXIST)){
                        Toast.makeText(this.getContext(), "Данный псевдоним не соответствует ни одному пользователю", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    else{
                        Toast.makeText(this.getContext(), "Что-то пошло не так... Пожалуйста, попробуйте снова", Toast.LENGTH_SHORT).show();
                        return;
                    }
                }
            } catch (JSONException e) {
                Toast.makeText(this.getContext(), "Что-то пошло не так... Пожалуйста, попробуйте снова", Toast.LENGTH_LONG);
                return;
            }
        });



    }


}