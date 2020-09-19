package com.chocolit.sweetshare;

import android.annotation.SuppressLint;
import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.motion.widget.MotionLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class ExploreTab extends Fragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    GridView gridView;
    String[] categoryName = {"Tools", "Sports", "Gardening", "Photo/Video", "Entertainment", "Clothing", "Electronics", "Books"};
    int[] numberImage = {R.drawable.ic_explore_tab_brush, R.drawable.ic_explore_tab_soccer_ball, R.drawable.ic_explore_tab_garden, R.drawable.ic_explore_tab_photovideo, R.drawable.ic_explore_tab_entertainment, R.drawable.ic_explore_tab_clothing, R.drawable.ic_explore_tab_electronics, R.drawable.ic_explore_tab_books};

    int[] stringResIDs = {R.string.explore_tools, R.string.explore_sports, R.string.explore_gardening, R.string.explore_photo, R.string.explore_entartainment, R.string.explore_clothing, R.string.explore_electronics, R.string.explore_books};

    private String mParam1;
    private String mParam2;

    Query query;
    FirestoreRecyclerOptions<ProductsModel> recyclerOptions;

    public ExploreTab() {
        // Required empty public constructor
    }

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
        for (int i = 0; i < categoryName.length; i++) {
            categoryName[i] = getActivity().getString(stringResIDs[i]);
        }
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
               // Toast.makeText(getContext(), "You Clicked" + categoryName[+position],Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getActivity(), Categories.class);
                intent.putExtra(ProductConstants.PRODUCT_CATEGORY, categoryName[+position]);
                startActivity(intent);
            }
        });

        MotionLayout motionLayout = view.findViewById(R.id.parentLayout);
        ImageView icSearch = view.findViewById(R.id.icSearch);
        ImageView icBackArrow = view.findViewById(R.id.icBackArrow);
        RecyclerView resultsRecyclerView = view.findViewById(R.id.resultsRecyclerView);
        EditText searchBar = view.findViewById(R.id.searchBar);

        FirebaseFirestore fStore = FirebaseFirestore.getInstance();
        query = fStore.collection("products");
        recyclerOptions = new FirestoreRecyclerOptions.Builder<ProductsModel>().setQuery(query, ProductsModel.class).build();

        FirestoreRecyclerAdapter recyclerAdapter = new FirestoreRecyclerAdapter<ProductsModel, ExploreTab.ProductsViewHolder>(recyclerOptions) {
            @NonNull
            @Override
            public ExploreTab.ProductsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_items, parent, false);
                return new ExploreTab.ProductsViewHolder(view);
            }

            @Override
            protected void onBindViewHolder(@NonNull ExploreTab.ProductsViewHolder holder, int position, @NonNull final ProductsModel model) {
                holder.list_title.setText(model.getPRODUCT_TITLE());
                holder.list_city.setText(model.getPRODUCT_CITY());
                holder.list_price.setText(model.getPRICE() + " SWEETS");
                String url = model.getPRODUCT_IMG_LIST().get(0);
                Picasso
                        .get()
                        .load(url)
                        .fit()
                        .centerCrop()
                        .into(holder.list_image);

                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(getActivity(), ProductLoadingScreen.class);
                        intent.putExtra(ProductConstants.ID, model.getID());
                        startActivity(intent);
                    }
                });
            }
        };

        resultsRecyclerView.setHasFixedSize(true);
        resultsRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        resultsRecyclerView.setAdapter(recyclerAdapter);
        recyclerAdapter.startListening();

        icSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String searchQuery = searchBar.getText().toString().trim();
                String[] wordsInQuery = searchQuery.split(" ");
                ArrayList<String> keywords= new ArrayList<>();

                keywords.add(" ");

                for (String word : wordsInQuery) {
                    for (int i = 0; i < word.length(); i++) {
                        String currentKeyword = "";
                        for (int j = 0; j <= i; j++) {
                            currentKeyword += word.charAt(j);
                        }
                        keywords.add(currentKeyword);
                    }
                }

                Log.d("TAG", "onClick: " + keywords);

                query = fStore.collection("products").whereArrayContainsAny(ProductConstants.KEYWORDS, keywords);
                recyclerOptions = new FirestoreRecyclerOptions.Builder<ProductsModel>().setQuery(query, ProductsModel.class).build();
                recyclerAdapter.updateOptions(recyclerOptions);
                motionLayout.transitionToEnd();
            }
        });

        icBackArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                searchBar.getText().clear();
                motionLayout.transitionToStart();
            }
        });
    }

    private class ProductsViewHolder extends RecyclerView.ViewHolder{
        private TextView list_title;
        private TextView list_price;
        private TextView list_city;
        private TextView list_description;
        private ImageView list_image;

        public ProductsViewHolder(@NonNull View itemView) {
            super(itemView);

            list_title = itemView.findViewById(R.id.productName);
            list_city = itemView.findViewById(R.id.productCity);
            list_price = itemView.findViewById(R.id.productPrice);
            list_image = itemView.findViewById(R.id.productImage);
        }
    }
}
