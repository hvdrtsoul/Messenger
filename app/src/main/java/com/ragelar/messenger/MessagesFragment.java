package com.ragelar.messenger;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class MessagesFragment extends Fragment {

    String userName;
    List<Message> messages = new ArrayList<>();
    Context appContext;

    public MessagesFragment() {
        // Required empty public constructor
    }

    class MessageComparator implements Comparator<Message> {
        @Override
        public int compare(Message message, Message t1) {
            return (int) (message.getTimestamp() - t1.getTimestamp());
        }
    }

    private void setMessages(List<Message> messages){
        this.messages.clear();
        this.messages.addAll(messages);
        this.messages.sort(new MessageComparator());
    }

    private void getMessages(Context appContext, String userName){
        class GetTask extends AsyncTask<Void, Void, List<Message>>{

            @Override
            protected List<Message> doInBackground(Void... voids) {
                List<Message> messages = DatabaseClient.getInstance(appContext).getAppDatabase()
                        .messageDao().getMessagesFromUser(userName);

                return messages;
            }

            @Override
            protected void onPostExecute(List<Message> messages) {
                super.onPostExecute(messages);
                //setMessages(messages);
            }
        }

        GetTask getTask = new GetTask();
        getTask.execute();

        try{
            this.messages.clear();
            this.messages.addAll(getTask.get());
            this.messages.sort(new MessageComparator());
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
            Toast.makeText(appContext, "Ошибка при загрузке списка сообщений. Пожалуйста, попробуйте открыть диалог ещё раз", Toast.LENGTH_SHORT).show();
        }


    }

    private void insertMessage(Context appContext, Message message){
        class InsertTask extends AsyncTask<Message, Void, Void> {

            @Override
            protected Void doInBackground(Message... messages) {
                DatabaseClient.getInstance(appContext).getAppDatabase()
                        .messageDao().addMessage(message);
                return null;
            }
        }

        InsertTask insertTask = new InsertTask();
        insertTask.execute();
    }

    private String getSharedKey(Context appContext, String userName) {
        class GetTask extends AsyncTask<String, Void, String> {

            @Override
            protected String doInBackground(String... strings) {
                return DatabaseClient.getInstance(appContext).getAppDatabase()
                        .dialogDao().getSharedKeyForUser(userName);

            }
        }

        GetTask task = new GetTask();
        task.execute(userName);

        try {
            return task.get();
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
            return "undefined";
        }
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_messages, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if(getArguments() != null) {
            MessagesFragmentArgs args = MessagesFragmentArgs.fromBundle(getArguments());
            userName = args.getUserName();
        }
        appContext = this.getActivity().getApplicationContext();

        TextView recepientTextView = view.findViewById(R.id.recepientTextView);
        recepientTextView.setText(" " + this.userName);
        RecyclerView recyclerView = view.findViewById(R.id.messagesContainer);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this.getContext());
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(linearLayoutManager);

        getMessages(appContext, userName);
        MessageAdapter adapter = new MessageAdapter(this.getContext(), this.messages);
        recyclerView.setAdapter(adapter);

        Handler handler = new Handler();
        Runnable refreshAdapterTask = new Runnable() {
            @Override
            public void run() {
                getMessages(appContext, userName);
                adapter.notifyDataSetChanged();
                recyclerView.scrollToPosition(adapter.getItemCount()-1);
                handler.postDelayed(this, 1000);
            }
        };

        refreshAdapterTask.run();

        ImageButton sendMessageButton = view.findViewById(R.id.sendMessageButton);
        sendMessageButton.setOnClickListener(v -> {
            PreferenceManager preferenceManager = new PreferenceManager(getContext());
            EditText inputMessage = view.findViewById(R.id.inputMessageEditText);
            BigInteger sharedKey = new BigInteger(getSharedKey(appContext, userName));
            ANomalUSProvider anomalusProvider = new ANomalUSProvider();
            Sanitizer sanitizer = new Sanitizer();

            String messageToSend = inputMessage.getText().toString();

            if(messageToSend.length() > 50){
                Toast.makeText(appContext, "Длина сообщения не может превышать 50 символов", Toast.LENGTH_SHORT).show();
                return;
            }

            byte[] bytesToEncode = messageToSend.getBytes(StandardCharsets.UTF_8);
            byte[] encodedBytes = anomalusProvider.encodeBytes(bytesToEncode, sharedKey);

            JSONObject jsonResponse = CommunicatorClient.sendSendRequest(preferenceManager.getSession(), userName,
                    preferenceManager.getUserName(), "text", sanitizer.sanitize(encodedBytes),
                    preferenceManager.getSharedKey());

            String newTimestamp = "";
            try {
                if(jsonResponse.getString("result").equals("OK")) {
                    JSONObject jsonData = jsonResponse.getJSONObject("data");
                    newTimestamp = jsonData.getString(Constants.SEND_TIMESTAMP_HEADER);

                }
                else{
                    Toast.makeText(getContext(), "Что-то пошло не так при отправке сообщения...", Toast.LENGTH_SHORT).show();
                    Toast.makeText(getContext(), jsonResponse.getString(Constants.ADDITIONAL_INFO_HEADER), Toast.LENGTH_SHORT).show();
                    return;
                }

            } catch (JSONException e) {
                e.printStackTrace();
                Toast.makeText(getContext(), "Что-то пошло не так при отправке сообщения...", Toast.LENGTH_SHORT).show();
                return;
            }

            Message messageToInsert = new Message();
            messageToInsert.setTo(userName);
            messageToInsert.setFrom("me");
            messageToInsert.setMessageType("text");
            messageToInsert.setTimestamp(Long.valueOf(newTimestamp));
            messageToInsert.setData(messageToSend);

            insertMessage(appContext, messageToInsert);
            inputMessage.getText().clear();
            adapter.notifyDataSetChanged();
        });

    }


}