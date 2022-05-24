package com.ragelar.messenger;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Looper;
import android.os.SystemClock;
import android.util.Log;
import android.util.Pair;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.ExecutionException;

public class GetMessagesHandler extends Thread{

    Context appContext;
    PreferenceManager preferenceManager;

    GetMessagesHandler(Context appContext){
        this.appContext = appContext;
        this.preferenceManager = new PreferenceManager(appContext);
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

    private String getPrivateKey(Context appContext, String userName) {
        class GetTask extends AsyncTask<String, Void, String> {

            @Override
            protected String doInBackground(String... strings) {
                String privateKey = DatabaseClient.getInstance(appContext).getAppDatabase()
                        .dialogDao().getPrivateKeyForUser(userName);
                return privateKey;
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);

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

    private void setSharedKey(Context appContext, Pair<String, String> info) {
        class SetTask extends AsyncTask<Pair<String, String>, Void, Void> {

            @Override
            protected Void doInBackground(Pair<String, String>... pairs) {
                DatabaseClient.getInstance(appContext).getAppDatabase()
                        .dialogDao().setSharedKey(info.first, info.second);

                return null;
            }
        }

        SetTask task = new SetTask();
        task.execute(info);
    }

    private void setLastMessage(Context appContext, Pair<String, String> info) {
        class SetTask extends AsyncTask<Pair<String, String>, Void, Void> {

            @Override
            protected Void doInBackground(Pair<String, String>... pairs) {
                DatabaseClient.getInstance(appContext).getAppDatabase()
                        .dialogDao().setLastMessage(info.first, info.second);

                return null;
            }
        }

        SetTask task = new SetTask();
        task.execute(info);
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

    private void handleIncomingMessage(String from, String type, String timestamp, String data){
        Sanitizer sanitizer = new Sanitizer();

        if(type.equals("request")){ // if it's a request
            PreferenceManager preferenceManager = new PreferenceManager(appContext);
            DFHProvider dfhProvider = new DFHProvider();
            BigInteger myPrivateKey = dfhProvider.generatePrivateKey();
            BigInteger myPublicKey = dfhProvider.generatePublicKey(myPrivateKey);
            BigInteger hisPublicKey = new BigInteger(sanitizer.unSanitize(data));
            BigInteger sharedKey = dfhProvider.generateSharedKey(myPrivateKey, hisPublicKey);

            JSONObject jsonResponse = CommunicatorClient.sendSendRequest(preferenceManager.getSession(), from,
                    preferenceManager.getUserName(), "answer", sanitizer.sanitize(myPublicKey.toByteArray()),
                    preferenceManager.getSharedKey());

            String newTimestamp = "";
            try {
                if(jsonResponse.getString("result").equals("OK")) {
                    JSONObject jsonData = jsonResponse.getJSONObject("data");
                    newTimestamp = jsonData.getString(Constants.SEND_TIMESTAMP_HEADER);

                }
                else{
                    return;
                }

            } catch (JSONException e) {
                e.printStackTrace();

                return;
            }

            Message incomingRequest = new Message();
            incomingRequest.setTo("me");
            incomingRequest.setFrom(from);
            incomingRequest.setMessageType(type);
            incomingRequest.setTimestamp(Long.valueOf(timestamp));
            incomingRequest.setData("ПОЛУЧЕН ЗАПРОС НА ОБМЕН КЛЮЧАМИ");

            Message outcomingAnswer = new Message();
            outcomingAnswer.setTo(from);
            outcomingAnswer.setFrom("me");
            outcomingAnswer.setMessageType("answer");
            outcomingAnswer.setTimestamp(Long.valueOf(newTimestamp));
            outcomingAnswer.setData("ОТПРАВЛЕН ОТВЕТ");

            Dialog dialogToAdd = new Dialog();
            dialogToAdd.setPrivateKey(myPrivateKey.toString());
            dialogToAdd.setSharedKey(sharedKey.toString());
            dialogToAdd.setUserName(from);
            dialogToAdd.setLastMessage("ОТПРАВЛЕН ОТВЕТ");

            insertDialog(appContext, dialogToAdd);
            insertMessage(appContext, incomingRequest);
            insertMessage(appContext, outcomingAnswer);
        }else if(type.equals("answer")){ // if it's a answer on request, dialog already exists

            if(!dialogExists(appContext, from))
                return;

            String myPrivateKey = getPrivateKey(appContext, from);
            BigInteger hisPublicKey = new BigInteger(sanitizer.unSanitize(data));
            DFHProvider dfhProvider = new DFHProvider();
            BigInteger sharedKey = dfhProvider.generateSharedKey(new BigInteger(myPrivateKey), hisPublicKey);

            setSharedKey(appContext, Pair.create(from, sharedKey.toString()));

            Message incomingMessage = new Message();
            incomingMessage.setTo("me");
            incomingMessage.setFrom(from);
            incomingMessage.setMessageType("answer");
            incomingMessage.setTimestamp(Long.valueOf(timestamp));
            incomingMessage.setData("ПОЛУЧЕН ОТВЕТ");

            insertMessage(appContext, incomingMessage);
            setLastMessage(appContext, Pair.create(from, "ПОЛУЧЕН ОТВЕТ"));
        }else{
            if(!dialogExists(appContext, from))
                return;

            String sharedKey = getSharedKey(appContext, from);
            byte[] messageBytes = sanitizer.unSanitize(data);
            ANomalUSProvider anomalusProvider = new ANomalUSProvider();

            byte[] decodedBytes = anomalusProvider.decodeBytes(messageBytes, new BigInteger(sharedKey));
            String message = new String(decodedBytes, StandardCharsets.UTF_8);

            Message incomingMessage = new Message();
            incomingMessage.setTo("me");
            incomingMessage.setFrom(from);
            incomingMessage.setMessageType(type);
            incomingMessage.setTimestamp(Long.valueOf(timestamp));
            incomingMessage.setData(message);

            insertMessage(appContext, incomingMessage);
            setLastMessage(appContext, Pair.create(from, message));
        }
    }

    @Override
    public void run() {
        while(true){
                Log.d("TEST", "GETTING MESSAGES");
                JSONObject jsonResponse = CommunicatorClient.sendCheckMailRequest(preferenceManager.getUserName(), preferenceManager.getSession(), preferenceManager.getSharedKey());
                String messages = "";

                try {
                    if(jsonResponse.getString("result").equals("OK")) {
                        JSONObject data = jsonResponse.getJSONObject("data");
                        messages = data.getString(Constants.CHECK_MAIL_MESSAGES_HEADER_NAME);//Toast.makeText(KeepAliveService.this, "KEEP ALIVE", Toast.LENGTH_SHORT).show();
                    }
                    else{
                        continue;
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    continue;
                }

                if(messages.equals(Constants.CHECK_MAIL_NO_MESSAGES)){
                    continue;
                }

                String[] messagesIds = messages.split(String.valueOf(Constants.CHECK_MAIL_SEPARATOR));

                for(String messageId : messagesIds){

                    jsonResponse = CommunicatorClient.sendGetMessageRequest(preferenceManager.getUserName(), preferenceManager.getSession(),
                            messageId, preferenceManager.getSharedKey());

                    try {
                        if(jsonResponse.getString("result").equals("OK")) {
                            JSONObject data = jsonResponse.getJSONObject("data");

                            String messageFrom = data.getString(Constants.GET_MESSAGE_FROM_HEADER);
                            String messageType = data.getString(Constants.GET_MESSAGE_TYPE_HEADER);
                            String messageTimestamp = data.getString(Constants.GET_MESSAGE_TIMESTAMP_HEADER);
                            String messageData = data.getString(Constants.GET_MESSAGE_DATA_HEADER);

                            handleIncomingMessage(messageFrom, messageType, messageTimestamp, messageData);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            SystemClock.sleep(1000);
        }

    }
}
