package com.example.sweetshare;

import android.app.Activity;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.type.Color;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ProfileTab#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProfileTab extends Fragment {

    private TextView userFullName;
    private TextView userReputation;
    private Button editProfileBtn;

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    public ProfileTab() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static ProfileTab newInstance(String param1, String param2) {
        ProfileTab fragment = new ProfileTab();
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
        return inflater.inflate(R.layout.fragment_profile_tab, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(UserConstants.USER_FETCHED_DATA_SHARED_PREFERENCES_FILE, Context.MODE_PRIVATE);

        userFullName = view.findViewById(R.id.userFullName);
        userReputation = view.findViewById(R.id.userReputation);
        editProfileBtn = view.findViewById(R.id.editProfileButton);

        userFullName.setText(sharedPreferences.getString(UserConstants.USER_FULL_NAME, "Default"));
        userReputation.setText(String.format("Reputation: %s", sharedPreferences.getLong(UserConstants.USER_REPUTATION, 404)));

        setReviewStarsFill(view, sharedPreferences.getLong(UserConstants.USER_REPUTATION, 404));

        editProfileBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivityForResult(new Intent(getContext(), EditUserProfile.class), RequestCodes.EDIT_PROFILE_ACTIVITY_REQUEST_CODE);
            }
        });



    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
            if (requestCode == RequestCodes.EDIT_PROFILE_ACTIVITY_REQUEST_CODE) {
                SharedPreferences sharedPreferences = getActivity().getSharedPreferences(UserConstants.USER_FETCHED_DATA_SHARED_PREFERENCES_FILE, Context.MODE_PRIVATE);
                userFullName.setText(sharedPreferences.getString(UserConstants.USER_FULL_NAME, "Default"));
            }
    }


    private void setReviewStarsFill(View view, Long userRep) {
        int[] viewList = {R.id.ic_review_star1, R.id.ic_review_star2, R.id.ic_review_star3, R.id.ic_review_star4, R.id.ic_review_star5};
        for (int i = 1; i <= 5; i++) {
            int drawableId = ServicesHelper.getStarIconFill(userRep, i);
            Drawable drawable = ContextCompat.getDrawable(view.getContext(), drawableId);
            view.findViewById(viewList[i - 1]).setBackground(drawable);
        }
    }

}