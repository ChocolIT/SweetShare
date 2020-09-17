package com.chocolit.sweetshare;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.squareup.picasso.Picasso;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class SearchPage extends AppCompatActivity {

    private RecyclerView products_list;
    private FirebaseFirestore firebaseFirestore;
    private FirestoreRecyclerAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_page);

        EditText editText = (EditText) findViewById(R.id.searchText);
        String value = editText.getText().toString().trim();
        products_list = findViewById(R.id.category_list);
        firebaseFirestore = FirebaseFirestore.getInstance();

        Query query = firebaseFirestore.collection("products").whereGreaterThanOrEqualTo(ProductConstants.PRODUCT_TITLE, "b").whereLessThanOrEqualTo(ProductConstants.PRODUCT_TITLE,"b\uf8ff");

        FirestoreRecyclerOptions<ProductsModel> options = new FirestoreRecyclerOptions.Builder<ProductsModel>().setQuery(query, ProductsModel.class).build();
        adapter = new FirestoreRecyclerAdapter<ProductsModel, SearchPage.ProductsViewHolder>(options) {
            @NonNull
            @Override
            public SearchPage.ProductsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_items, parent, false);
                return new SearchPage.ProductsViewHolder(view);
            }

            @SuppressLint("SetTextI18n")
            @Override
            protected void onBindViewHolder(@NonNull SearchPage.ProductsViewHolder holder, int position, @NonNull final ProductsModel model) {
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
                        Intent intent = new Intent(getApplicationContext(), ProductLoadingScreen.class);
                        intent.putExtra(ProductConstants.ID, model.getID());
                        startActivity(intent);
//                        Log.d("TAG", "onClick: " + model.getID());
                    }
                });
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
        private ImageView list_image;

        public ProductsViewHolder(@NonNull View itemView) {
            super(itemView);

            list_title = itemView.findViewById(R.id.productName);
            list_city = itemView.findViewById(R.id.productCity);
            list_price = itemView.findViewById(R.id.productPrice);
            list_image = itemView.findViewById(R.id.productImage);
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