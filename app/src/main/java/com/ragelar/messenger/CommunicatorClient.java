package com.ragelar.messenger;


import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

public class CommunicatorClient {

    public static JSONObject sendMeetRequest(String publicKey){
        JSONObject request = new JSONObject();
        JSONObject data = new JSONObject();

        try {
            request.put(Constants.REQUEST_TYPE_HEADER, Constants.MEET_REQUEST_HEADER);
            data.put("publicKey", publicKey);
            request.put(Constants.REQUEST_DATA_HEADER, data);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        RequestSender sender = new RequestSender(request);
        sender.start();

        Log.d("TEST", request.toString());

        return sender.getResponse();
    }

    public static JSONObject sendKeepAliveRequest(){
        JSONObject request = new JSONObject();
        try {
            request.put(Constants.REQUEST_TYPE_HEADER, Constants.KEEP_ALIVE_REQUEST_HEADER);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        RequestSender sender = new RequestSender(request);
        sender.start();

        return sender.getResponse();
    }

    public static JSONObject sendAuthRequest(String userName, String sharedKey){
        JSONObject request = new JSONObject();
        JSONObject data = new JSONObject();

        try {
            request.put(Constants.REQUEST_TYPE_HEADER, Constants.AUTH_REQUEST_HEADER);
            data.put("userName", userName);
            request.put(Constants.REQUEST_DATA_HEADER, data);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        RequestSender sender = new RequestSender(request, sharedKey);
        sender.start();

        return sender.getResponse();
    }

    public static JSONObject sendTwistedRequest(String userName, String solution, String sharedKey){
        JSONObject request = new JSONObject();
        JSONObject data = new JSONObject();

        try {
            request.put(Constants.REQUEST_DATA_HEADER, data);
            request.put(Constants.REQUEST_TYPE_HEADER, Constants.TWISTED_REQUEST_HEADER);
            data.put("userName", userName);
            data.put("solution", solution);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        RequestSender sender = new RequestSender(request, sharedKey);
        sender.start();

        return sender.getResponse();
    }

    public static JSONObject sendJoinUsRequest(String userName, String publicKey, String sharedKey){
        JSONObject request = new JSONObject();
        JSONObject data = new JSONObject();

        try {
            request.put(Constants.REQUEST_TYPE_HEADER, Constants.JOIN_US_REQUEST_HEADER);
            data.put("userName", userName);
            data.put("publicKey", publicKey);
            request.put(Constants.REQUEST_DATA_HEADER, data);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        RequestSender sender = new RequestSender(request, sharedKey);
        sender.start();

        return sender.getResponse();
    }

    public static JSONObject sendHypnotizeRequest(String userName, String nickName, String session, String sharedKey){
        JSONObject request = new JSONObject();
        JSONObject data = new JSONObject();

        try {
            request.put(Constants.REQUEST_DATA_HEADER, data);
            request.put(Constants.REQUEST_TYPE_HEADER, Constants.HYPNOTIZE_REQUEST_HEADER);
            data.put("userName", userName);
            data.put("nickName", nickName);
            data.put("session", session);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        RequestSender sender = new RequestSender(request, sharedKey);
        sender.start();

        return sender.getResponse();
    }

    public static JSONObject sendSendRequest(String session, String to, String from, String type, String messageData, String sharedKey){
        JSONObject request = new JSONObject();
        JSONObject data = new JSONObject();

        try {
            request.put(Constants.REQUEST_DATA_HEADER, data);
            request.put(Constants.REQUEST_TYPE_HEADER, Constants.SEND_REQUEST_HEADER);
            data.put("session", session);
            data.put("to", to);
            data.put("from", from);
            data.put("type", type);
            data.put("data", messageData);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        RequestSender sender = new RequestSender(request, sharedKey);
        sender.start();

        return sender.getResponse();
    }

    public static JSONObject sendCheckMailRequest(String userName, String session, String sharedKey){
        JSONObject request = new JSONObject();
        JSONObject data = new JSONObject();

        try {
            request.put(Constants.REQUEST_TYPE_HEADER, Constants.CHECK_MAIL_REQUEST_HEADER);
            data.put("userName", userName);
            data.put("session", session);
            request.put(Constants.REQUEST_DATA_HEADER, data);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        RequestSender sender = new RequestSender(request, sharedKey);
        sender.start();

        return sender.getResponse();
    }

    public static JSONObject sendGetUserNameRequest(String nickName, String sharedKey){
        JSONObject request = new JSONObject();
        JSONObject data = new JSONObject();

        try {
            request.put(Constants.REQUEST_TYPE_HEADER, Constants.GET_USERNAME_REQUEST_HEADER);
            data.put("nickName", nickName);
            request.put(Constants.REQUEST_DATA_HEADER, data);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        RequestSender sender = new RequestSender(request, sharedKey);
        sender.start();

        return sender.getResponse();
    }

    public static JSONObject sendGetMessageRequest(String userName, String session, String id, String sharedKey){
        JSONObject request = new JSONObject();
        JSONObject data = new JSONObject();

        try {
            request.put(Constants.REQUEST_DATA_HEADER, data);
            request.put(Constants.REQUEST_TYPE_HEADER, Constants.GET_MESSAGE_REQUEST_HEADER);
            data.put("userName", userName);
            data.put("session", session);
            data.put("id", id);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        RequestSender sender = new RequestSender(request, sharedKey);
        sender.start();

        return sender.getResponse();
    }


}
