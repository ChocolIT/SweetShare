package com.chocolit.sweetshare;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

    public class ExploreTab extends Fragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    GridView gridView;
    String[] categoryName = {"Tools", "Sports", "Gardening", "Photo", "Entertainment", "Clothing", "Electronics", "Books"};
    int[] numberImage = {R.drawable.ic_explore_tab_brush, R.drawable.ic_explore_tab_soccer_ball, R.drawable.ic_explore_tab_garden, R.drawable.ic_explore_tab_photovideo, R.drawable.ic_explore_tab_entertainment, R.drawable.ic_explore_tab_clothing, R.drawable.ic_explore_tab_electronics, R.drawable.ic_explore_tab_books};


        // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ExploreTab() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ExploreTab.
     */
    // TODO: Rename and change types and number of parameters
    public static ExploreTab newInstance(String param1, String param2) {
        ExploreTab fragment = new ExploreTab();
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
        return inflater.inflate(R.layout.fragment_explore_tab, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        gridView = view.findViewById(R.id.grid_view);
        MainAdapter adapter = new MainAdapter(getActivity(), categoryName, numberImage);
        gridView.setAdapter(adapter);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(getContext(), "You Clicked" + categoryName[+position],Toast.LENGTH_SHORT).show();
            }
        });
    }
}