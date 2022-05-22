package com.ragelar.messenger;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SecretPhraseFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SecretPhraseFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public SecretPhraseFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SecretPhraseFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SecretPhraseFragment newInstance(String param1, String param2) {
        SecretPhraseFragment fragment = new SecretPhraseFragment();
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
        return inflater.inflate(R.layout.fragment_secret_phrase, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        TextView word1 = view.findViewById(R.id.word1);
        TextView word2 = view.findViewById(R.id.word2);
        TextView word3 = view.findViewById(R.id.word3);
        TextView word4 = view.findViewById(R.id.word4);
        TextView word5 = view.findViewById(R.id.word5);
        TextView word6 = view.findViewById(R.id.word6);
        TextView word7 = view.findViewById(R.id.word7);
        TextView word8 = view.findViewById(R.id.word8);
        Button confirmButton = view.findViewById(R.id.gotPhraseButton);
        ProgressBar progressBar = view.findViewById(R.id.progressBar);;

        RabbitHole rabbitHole = new RabbitHole();
        Sanitizer sanitizer = new Sanitizer();
        byte[] seed = rabbitHole.generateSeed();
        ArrayList<String> words = rabbitHole.dragDown(seed);


        BigInteger privateKey = new BigInteger(seed);

        word1.setText(words.get(0));
        word2.setText(words.get(1));
        word3.setText(words.get(2));
        word4.setText(words.get(3));
        word5.setText(words.get(4));
        word6.setText(words.get(5));
        word7.setText(words.get(6));
        word8.setText(words.get(7));

        confirmButton.setOnClickListener(v -> {
            progressBar.setVisibility(View.VISIBLE);
            UserNameHandler userNameHandler = new UserNameHandler();
            boolean everything_okay = false;

            while(!everything_okay){
                PreferenceManager preferenceManager = new PreferenceManager(this.getContext());
                String sharedKey = preferenceManager.getSharedKey();

                if(sharedKey.equals("undefined")){
                    Toast.makeText(this.getContext(), "Нет соединения с сервером. Проверьте подключение к интернету", Toast.LENGTH_LONG).show();
                    progressBar.setVisibility(View.GONE);
                    return;
                }


                String currentUsername = userNameHandler.generateUserName();
                JSONObject jsonResponse = CommunicatorClient.sendJoinUsRequest(currentUsername, privateKey.toString(), sharedKey);

                try {
                    Toast.makeText(this.getContext(), jsonResponse.getString("result"), Toast.LENGTH_SHORT).show();
                    if(jsonResponse.getString("result").equals("OK")){
                        JSONObject data = jsonResponse.getJSONObject("data");
                        preferenceManager.setUserName(currentUsername);
                        preferenceManager.logIn(privateKey.toString(), data.getString(Constants.SESSION_HEADER));
                        Toast.makeText(this.getContext(), "Вы успешно зарегистрировались!", Toast.LENGTH_SHORT).show();
                        everything_okay = true;
                    }
                    else{
                        if(jsonResponse.getString(Constants.ADDITIONAL_INFO_HEADER).equals(Constants.JOIN_US_USER_EXISTS)){
                            continue; // try to register again with another username
                        }else{
                            Toast.makeText(this.getActivity().getApplicationContext(), "Что-то пошло не так... Пожалуйста, попробуйте снова", Toast.LENGTH_SHORT);
                            progressBar.setVisibility(View.GONE);
                            return;
                        }
                    }
                } catch (JSONException e) {
                    Toast.makeText(this.getContext(), "Что-то пошло не так... Пожалуйста, попробуйте снова", Toast.LENGTH_LONG);
                    progressBar.setVisibility(View.GONE);
                    return;
                }
            }
            progressBar.setVisibility(View.GONE);
            Intent intention = new Intent(this.getActivity(), MainActivity.class);
            intention.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intention);

        });


    }
}