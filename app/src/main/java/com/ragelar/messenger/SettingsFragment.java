package com.ragelar.messenger;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SettingsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SettingsFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public SettingsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SettingsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SettingsFragment newInstance(String param1, String param2) {
        SettingsFragment fragment = new SettingsFragment();
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
        return inflater.inflate(R.layout.fragment_settings, container, false);
    }

    private void clearDatabase(Context appContext) {
        class SetTask extends AsyncTask<Void, Void, Void> {

            @Override
            protected Void doInBackground(Void... voids) {
                DatabaseClient.getInstance(appContext).getAppDatabase().clearAllTables();
                return null;
            }
        }

        SetTask task = new SetTask();
        task.execute();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Context appContext = this.getContext().getApplicationContext();

        PreferenceManager preferenceManager = new PreferenceManager(this.getContext());
        final NavController navController = Navigation.findNavController(view);

        TextView usernameLabel = view.findViewById(R.id.userNameLabel);
        TextView nickNameLabel = view.findViewById(R.id.nickNameLabel);
        Button setNickNameButton = view.findViewById(R.id.setNicknameButton);
        Button logOutButton = view.findViewById(R.id.logOutButton);

        usernameLabel.setText(preferenceManager.getUserName());
        nickNameLabel.setText(preferenceManager.getNickName());

        setNickNameButton.setOnClickListener(v -> {
            navController.navigate(R.id.action_settingsFragment_to_setNickNameFragment);
        });

        FragmentActivity activity = this.getActivity();

        logOutButton.setOnClickListener(v -> {
            new AlertDialog.Builder(this.getContext()).setMessage("Вы действительно хотите выйти? Сообщения, сохранённые на устройстве, также будут удалены без возможности восстановления").setPositiveButton("ДА",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            preferenceManager.clearSession();
                            clearDatabase(appContext);
                            activity.finishAffinity();

                        }
                    }).setNegativeButton("НЕТ", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            }).create().show();
        });



    }
}