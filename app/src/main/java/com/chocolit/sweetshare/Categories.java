package com.chocolit.sweetshare;

import android.annotation.SuppressLint;
import android.os.Bundle;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class Categories extends AppCompatActivity {

    private RecyclerView products_list;
    private FirebaseFirestore firebaseFirestore;
    private FirestoreRecyclerAdapter adapter;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_categories);
        products_list = findViewById(R.id.category_list);
        firebaseFirestore = FirebaseFirestore.getInstance();
        String clickCategory = getIntent().getExtras().getString(ProductConstants.PRODUCT_CATEGORY);
        Query query = firebaseFirestore.collection("products").whereEqualTo(ProductConstants.PRODUCT_CATEGORY, clickCategory);


        FirestoreRecyclerOptions<ProductsModel> options = new FirestoreRecyclerOptions.Builder<ProductsModel>().setQuery(query, ProductsModel.class).build();

        adapter = new FirestoreRecyclerAdapter<ProductsModel, ProductsViewHolder>(options) {
            @NonNull
            @Override
            public ProductsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_items, parent, false);
                return new ProductsViewHolder(view);
            }

            @SuppressLint("SetTextI18n")
            @Override
            protected void onBindViewHolder(@NonNull ProductsViewHolder holder, int position, @NonNull ProductsModel model) {
                holder.list_title.setText(model.getPRODUCT_TITLE());
                holder.list_city.setText(model.getPRODUCT_CITY());
                holder.list_price.setText(model.getPRODUCT_PRICE() + "");
            }
        };

        products_list.setHasFixedSize(true);
        products_list.setLayoutManager(new LinearLayoutManager(this));
        products_list.setAdapter(adapter);

    }

    private class ProductsViewHolder extends RecyclerView.ViewHolder{

        private TextView list_title;
        private TextView list_price;
        private TextView list_city;

        public ProductsViewHolder(@NonNull View itemView) {
            super(itemView);

            list_title = itemView.findViewById(R.id.list_name);
            list_city = itemView.findViewById(R.id.list_city);
            list_price = itemView.findViewById(R.id.list_price);
        }

    }
    @Override
    protected void onStop(){
        super.onStop();
        adapter.stopListening();
    }

    @Override
    protected void onStart() {
        super.onStart();
        adapter.startListening();
    }
}