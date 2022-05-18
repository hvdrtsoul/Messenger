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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.PrivateKey;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link LogInFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class LogInFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public LogInFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment LogInFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static LogInFragment newInstance(String param1, String param2) {
        LogInFragment fragment = new LogInFragment();
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
        return inflater.inflate(R.layout.fragment_log_in, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        EditText word1 = view.findViewById(R.id.editWord1);
        EditText word2 = view.findViewById(R.id.editWord2);
        EditText word3 = view.findViewById(R.id.editWord3);
        EditText word4 = view.findViewById(R.id.editWord4);
        EditText word5 = view.findViewById(R.id.editWord5);
        EditText word6 = view.findViewById(R.id.editWord6);
        EditText word7 = view.findViewById(R.id.editWord7);
        EditText word8 = view.findViewById(R.id.editWord8);
        EditText userName = view.findViewById(R.id.editUserName);

        ImageView check1 = view.findViewById(R.id.check1);
        ImageView check2 = view.findViewById(R.id.check);
        ImageView check3 = view.findViewById(R.id.check2);
        ImageView check4 = view.findViewById(R.id.check3);
        ImageView check5 = view.findViewById(R.id.check4);
        ImageView check6 = view.findViewById(R.id.check5);
        ImageView check7 = view.findViewById(R.id.check6);
        ImageView check8 = view.findViewById(R.id.check7);
        Button continueButton = view.findViewById(R.id.tryLogInButton);

        continueButton.setOnClickListener(v -> {
            RabbitHole rabbitHole = new RabbitHole();
            String word1Text = word1.getText().toString();
            String word2Text = word2.getText().toString();
            String word3Text = word3.getText().toString();
            String word4Text = word4.getText().toString();
            String word5Text = word5.getText().toString();
            String word6Text = word6.getText().toString();
            String word7Text = word7.getText().toString();
            String word8Text = word8.getText().toString();
            String userNameText = userName.getText().toString();

            boolean wordRight1 = rabbitHole.findWord(word1Text) != -1;
            boolean wordRight2 = rabbitHole.findWord(word2Text) != -1;
            boolean wordRight3 = rabbitHole.findWord(word3Text) != -1;
            boolean wordRight4 = rabbitHole.findWord(word4Text) != -1;
            boolean wordRight5 = rabbitHole.findWord(word5Text) != -1;
            boolean wordRight6 = rabbitHole.findWord(word6Text) != -1;
            boolean wordRight7 = rabbitHole.findWord(word7Text) != -1;
            boolean wordRight8 = rabbitHole.findWord(word8Text) != -1;

            if (wordRight1) check1.setVisibility(View.VISIBLE); else check1.setVisibility(View.INVISIBLE);
            if (wordRight2) check2.setVisibility(View.VISIBLE); else check2.setVisibility(View.INVISIBLE);
            if (wordRight3) check3.setVisibility(View.VISIBLE); else check3.setVisibility(View.INVISIBLE);
            if (wordRight4) check4.setVisibility(View.VISIBLE); else check4.setVisibility(View.INVISIBLE);
            if (wordRight5) check5.setVisibility(View.VISIBLE); else check5.setVisibility(View.INVISIBLE);
            if (wordRight6) check6.setVisibility(View.VISIBLE); else check6.setVisibility(View.INVISIBLE);
            if (wordRight7) check7.setVisibility(View.VISIBLE); else check7.setVisibility(View.INVISIBLE);
            if (wordRight8) check8.setVisibility(View.VISIBLE); else check8.setVisibility(View.INVISIBLE);

            if (!(wordRight1 && wordRight2 && wordRight3 && wordRight4 &&
                    wordRight5 && wordRight6 && wordRight7 && wordRight8)) {
                Toast.makeText(this.getContext(), "Некорректная секретная фраза, проверьте вашу секретную фразу и попробуйте снова.",
                        Toast.LENGTH_LONG).show();
                return;
            }

            PreferenceManager preferenceManager = new PreferenceManager(this.getContext());
            String sharedKey = preferenceManager.getSharedKey();

            if(sharedKey.equals("undefined")){
                Toast.makeText(this.getContext(), "Нет соединения с сервером. Проверьте подключение к интернету", Toast.LENGTH_LONG).show();
                return;
            }

            ArrayList<String> words = new ArrayList<>();
            words.add(word1Text);
            words.add(word2Text);
            words.add(word3Text);
            words.add(word4Text);
            words.add(word5Text);
            words.add(word6Text);
            words.add(word7Text);
            words.add(word8Text);

            if(userNameText.charAt(0) == '!') { // if it's a nickname
                JSONObject jsonResponse = CommunicatorClient.sendGetUserNameRequest(userNameText.substring(1), sharedKey);
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

            String challenge = "";
            JSONObject jsonResponse = CommunicatorClient.sendAuthRequest(userNameText, sharedKey);
            try{
                if(jsonResponse.getString("result").equals("OK")){
                    JSONObject data = jsonResponse.getJSONObject("data");
                    challenge = data.getString(Constants.AUTH_CHALLENGE_HEADER);
                }
                else{
                    if(jsonResponse.getString(Constants.ADDITIONAL_INFO_HEADER).equals(Constants.AUTH_USER_DOES_NOT_EXIST)){
                        Toast.makeText(this.getContext(), "Такого пользователя не существует...", Toast.LENGTH_LONG).show();
                        return;
                    }
                    if(jsonResponse.getString(Constants.ADDITIONAL_INFO_HEADER).equals(Constants.AUTH_PUBLIC_KEY_NOT_FOUND)){
                        Toast.makeText(this.getContext(), "Данные о пользователе повреждены. Повторный вход невозможен", Toast.LENGTH_LONG).show();
                        return;
                    }
                }
            } catch (JSONException e) {
                Toast.makeText(this.getContext(), "Что-то пошло не так... Пожалуйста, попробуйте снова", Toast.LENGTH_LONG);
                return;
            }


            byte[] seed = rabbitHole.getOut(words);
            ANomalUSProvider anomalusProvider = new ANomalUSProvider();
            Sanitizer sanitizer = new Sanitizer();


            Arrays.asList(seed).add(0, new byte[]{(byte)0x4f});

            BigInteger privateKey = new BigInteger(seed);

            byte[] decodedBytes = anomalusProvider.decodeBytes(sanitizer.unSanitize(challenge), privateKey);



            jsonResponse = CommunicatorClient.sendTwistedRequest(userNameText, new String(decodedBytes, StandardCharsets.UTF_8), sharedKey);

            try{
                if(jsonResponse.getString("result").equals("OK")){
                    JSONObject data = jsonResponse.getJSONObject("data");
                     String newSession = data.getString(Constants.TWISTED_NEW_SESSION_HEADER);

                     preferenceManager.logIn(privateKey.toString(), newSession);
                     preferenceManager.setUserName(userNameText);
                     Toast.makeText(this.getContext(), "Авторизация прошла успешно", Toast.LENGTH_LONG).show();
                     Intent intention = new Intent(this.getActivity(), MainActivity.class);
                     intention.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                     startActivity(intention);
                }
                else{
                    if(jsonResponse.getString(Constants.ADDITIONAL_INFO_HEADER).equals(Constants.TWISTED_SECRET_NOT_FOUND)){
                        Toast.makeText(this.getContext(), "Время на попытку входа истекло. Пожалуйста, проверьте своё интернет-соединение и попробуйте снова", Toast.LENGTH_LONG).show();
                        return;
                    }
                    if(jsonResponse.getString(Constants.ADDITIONAL_INFO_HEADER).equals(Constants.TWISTED_USER_DOES_NOT_EXIST)){
                        Toast.makeText(this.getContext(), "Такого пользователя не существует...", Toast.LENGTH_LONG).show();
                        return;
                    }
                    if(jsonResponse.getString(Constants.ADDITIONAL_INFO_HEADER).equals(Constants.TWISTED_WRONG_SOLUTION)){
                        Toast.makeText(this.getContext(), "Секретная фраза неверна. Пожалуйста, проверьте введённую секретную фразу и попробуйте снова", Toast.LENGTH_LONG).show();
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