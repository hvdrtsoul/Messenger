package com.ragelar.messenger;


import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.math.BigInteger;
import java.net.Socket;
import java.net.UnknownHostException;
import java.nio.charset.StandardCharsets;
import java.text.ParseException;

public class RequestSender extends Thread{

    private final JSONObject request;
    private JSONObject response;
    private BigInteger sharedKey=null;
    //private Object syncObject="SYNCME";
    private volatile boolean gotResponse;

    RequestSender(JSONObject request){
        this.request = request;
    }

    RequestSender(JSONObject request, String sharedKey){
        this.request = request;
        this.sharedKey = new BigInteger(sharedKey);
    }

    public JSONObject getResponse(){
        synchronized (this){
            try {
                if(!gotResponse)
                    this.wait();
            } catch (InterruptedException e) {
                JSONObject errorResponse = new JSONObject();
                try {
                    errorResponse.put(Constants.RESPONSE_HEADER_NAME, Constants.RESPONSE_HEADER_ERROR);
                    errorResponse.put(Constants.ADDITIONAL_INFO_HEADER, Constants.SOMETHING_WENT_WRONG_MESSAGE);
                } catch (JSONException ex) {
                    ex.printStackTrace();
                }
                return errorResponse;
            }
        }

        return response;
    }

    @Override
    public void run(){
        try(Socket client = new Socket(Constants.SERVER_HOST, 9674)){
            String requestType = (String)request.get("request-type");

            if(requestType.equals("meet") || requestType.equals("keep_alive")){ // request is not encoded such as response
                PrintWriter writer = new PrintWriter(client.getOutputStream(), true);
                BufferedReader input = new BufferedReader(new InputStreamReader(client.getInputStream()));

                writer.write(request.toString() + '\n');
                writer.flush();
                String responseString = input.readLine();

                if(responseString == null){
                    response.put(Constants.RESPONSE_HEADER_NAME, Constants.RESPONSE_HEADER_ERROR);
                    response.put(Constants.ADDITIONAL_INFO_HEADER, Constants.SOMETHING_WENT_WRONG_MESSAGE);
                    gotResponse = true;

                    synchronized (this){
                        this.notify();
                    }

                    input.close();
                    writer.close();
                    return;
                }

                response = new JSONObject(responseString);
                gotResponse = true;

                synchronized (this){
                    this.notify();
                }
                input.close();
                writer.close();
                return;
            }
            else{ // request data is encoded such as response (but NOT in hypnotize)
                JSONObject encodedRequest = new JSONObject();
                encodedRequest.put("request-type", requestType);

                String dataToEncode = ((JSONObject)request.get("data")).toString();
                ANomalUSProvider anomalus = new ANomalUSProvider();
                Sanitizer sanitizer = new Sanitizer();

                byte[] data = anomalus.encodeBytes(dataToEncode.getBytes(StandardCharsets.UTF_8), sharedKey);
                encodedRequest.put(Constants.RESPONSE_HEADER_DATA, sanitizer.sanitize(data));
                // request is encoded

                PrintWriter writer = new PrintWriter(client.getOutputStream(), true);
                BufferedReader input = new BufferedReader(new InputStreamReader(client.getInputStream()));

                writer.write(encodedRequest.toString() + '\n');
                writer.flush();
                String responseString = input.readLine();

                if(responseString == null){
                    response.put(Constants.RESPONSE_HEADER_NAME, Constants.RESPONSE_HEADER_ERROR);
                    response.put(Constants.ADDITIONAL_INFO_HEADER, Constants.SOMETHING_WENT_WRONG_MESSAGE);
                    gotResponse = true;

                    synchronized (this){
                        this.notify();
                    }
                    input.close();
                    writer.close();
                    return;
                }

                JSONObject encodedResponse = new JSONObject(responseString);

                if(requestType.equals("hypnotize")){ // response is not encoded
                    response = encodedResponse;
                    gotResponse = true;

                    synchronized (this){
                        this.notify();
                    }
                    input.close();
                    writer.close();
                    return;
                }

                if(encodedResponse.get(Constants.RESPONSE_HEADER_NAME).equals(Constants.RESPONSE_HEADER_ERROR)){ // if response contains error it's not encoded...
                    response = encodedResponse;
                    gotResponse = true;

                    synchronized (this){
                        this.notify();
                    }
                    input.close();
                    writer.close();
                    return;
                }

                String encodedData = (String)encodedResponse.get(Constants.RESPONSE_HEADER_DATA);

                JSONObject decodedResponse = new JSONObject();
                decodedResponse.put(Constants.RESPONSE_HEADER_NAME, encodedResponse.getString(Constants.RESPONSE_HEADER_NAME));
                byte[] decodedData = anomalus.decodeBytes(sanitizer.unSanitize(encodedData), sharedKey);
                decodedResponse.put(Constants.RESPONSE_HEADER_DATA, new JSONObject(new String(decodedData, StandardCharsets.UTF_8)));
                response = decodedResponse;
                gotResponse = true;

                synchronized (this){
                    this.notify();
                }
                input.close();
                writer.close();
                return;
            }
        } catch (UnknownHostException e){
            if(response == null)
                response = new JSONObject();
            try {
                response.put(Constants.RESPONSE_HEADER_NAME, Constants.RESPONSE_HEADER_ERROR);
                response.put(Constants.ADDITIONAL_INFO_HEADER, Constants.UNKNOWN_HOST_MESSAGE);
            } catch (JSONException ex) {
                ex.printStackTrace();
            }
            gotResponse = true;

            synchronized (this){
                this.notify();
            }

            return;
        } catch (IOException e){
            if(response == null)
                response = new JSONObject();
            e.printStackTrace();
            try {
                response.put(Constants.RESPONSE_HEADER_NAME, Constants.RESPONSE_HEADER_ERROR);
                response.put(Constants.ADDITIONAL_INFO_HEADER, Constants.SOMETHING_WENT_WRONG_MESSAGE);
            } catch (JSONException ex) {
                ex.printStackTrace();
            }
            gotResponse = true;

            synchronized (this){
                this.notify();
            }

            return;
        } catch (JSONException e) {
            if(response == null)
                response = new JSONObject();
            try {
                e.printStackTrace();
                response.put(Constants.RESPONSE_HEADER_NAME, Constants.RESPONSE_HEADER_ERROR);
                response.put(Constants.ADDITIONAL_INFO_HEADER, Constants.SOMETHING_WENT_WRONG_MESSAGE);
            } catch (JSONException ex) {
                ex.printStackTrace();
            }
            gotResponse = true;

            synchronized (this){
                this.notify();
            }
        }
    }

}
