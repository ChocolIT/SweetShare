package com.chocolit.sweetshare;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.denzcoskun.imageslider.interfaces.ItemClickListener;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Favorites extends AppCompatActivity {

    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;
    List<EntityProduct> myDataset = new ArrayList<>();
    static Context context;
    static ArrayList<String> productIds;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorites);
        context = getApplicationContext();
        ImageView icBackArrow = findViewById(R.id.backArrowButton);
        icBackArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        Bundle bundle = getIntent().getExtras();
        final ArrayList<String> productNames = bundle.getStringArrayList(ProductConstants.PRODUCT_TITLE);
        final ArrayList<String> productCities = bundle.getStringArrayList(ProductConstants.PRODUCT_CITY);
        final ArrayList<String> productPrices = bundle.getStringArrayList(ProductConstants.PRICE);
        final ArrayList<String> productImgs = bundle.getStringArrayList(ProductConstants.PRODUCT_IMG_LIST);
        productIds = bundle.getStringArrayList(ProductConstants.ID);

        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);

        recyclerView.setHasFixedSize(true);

        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        mAdapter = new MyRecyclerViewAdapter(getApplicationContext(), myDataset);
        recyclerView.setAdapter(mAdapter);
        for(int i = 0; i < productNames.size(); i++){
            myDataset.add(new EntityProduct(productNames.get(i), productCities.get(i), productImgs.get(i), productPrices.get(i)));
        }
        Log.d("TAG", "onCreate: " + myDataset.size());

    }
    public class MyRecyclerViewAdapter extends RecyclerView.Adapter<MyRecyclerViewAdapter.ViewHolder> {

        private List<EntityProduct> mData;
        private LayoutInflater mInflater;
        private ItemClickListener mClickListener;

        // data is passed into the constructor
        MyRecyclerViewAdapter(Context context, List<EntityProduct> data) {
            this.mInflater = LayoutInflater.from(context);
            this.mData = data;
        }

        // inflates the row layout from xml when needed
        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = mInflater.inflate(R.layout.list_items, parent, false);
            return new ViewHolder(view);
        }

        // binds the data to the TextView in each row
        @Override
        public void onBindViewHolder(ViewHolder holder,final int position) {

            holder.nameTextView.setText(mData.get(position).getName());
            holder.priceTextView.setText(mData.get(position).getPrice());
            holder.cityTextView.setText(mData.get(position).getCity());
            Picasso
                    .get()
                    .load(mData.get(position).getImg_url())
                    .fit()
                    .centerCrop()
                    .into(holder.productImage);

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(Favorites.this, ProductLoadingScreen.class);
                    intent.putExtra(ProductConstants.ID, productIds.get(position));
                    startActivity(intent);
                }
            });
        }

        // total number of rows
        @Override
        public int getItemCount() {
            return mData.size();
        }


        // stores and recycles views as they are scrolled off screen
        public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
            TextView nameTextView;
            TextView priceTextView;
            TextView cityTextView;
            ImageView productImage;

            ViewHolder(View itemView) {
                super(itemView);
                nameTextView = itemView.findViewById(R.id.productName);
                priceTextView = itemView.findViewById(R.id.productPrice);
                cityTextView = itemView.findViewById(R.id.productCity);
                productImage = itemView.findViewById(R.id.productImage);
                itemView.setOnClickListener(this);
            }

            @Override
            public void onClick(View view) {
            }
        }

        // convenience method for getting data at click position
        EntityProduct getItem(int id) {
            return mData.get(id);
        }

        // allows clicks events to be caught
        void setClickListener(ItemClickListener itemClickListener) {
            this.mClickListener = itemClickListener;
        }

    }
}