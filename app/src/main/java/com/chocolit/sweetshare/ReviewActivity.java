package com.chocolit.sweetshare;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

public class ReviewActivity extends AppCompatActivity {
    private RecyclerView review_list;
    private FirebaseFirestore firebaseFirestore;
    private FirestoreRecyclerAdapter adapter;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_review);
        review_list = findViewById(R.id.category_list);
        firebaseFirestore = FirebaseFirestore.getInstance();

        Query query = firebaseFirestore.collection("reviews");
        FirestoreRecyclerOptions<ReviewsModel> options = new FirestoreRecyclerOptions.Builder<ReviewsModel>().setQuery(query, ReviewsModel.class).build();

        ImageView backButton = findViewById(R.id.backArrowButton);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        adapter = new FirestoreRecyclerAdapter<ReviewsModel, ProductsViewHolder>(options) {
            @NonNull
            @Override
            public ProductsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_items, parent, false);
                return new ProductsViewHolder(view);
            }

            @SuppressLint("SetTextI18n")
            @Override
            protected void onBindViewHolder(@NonNull ProductsViewHolder holder, int position, @NonNull final ReviewsModel model) {

                holder.list_name.setText(model.getREVIEWER_NAME());
                holder.list_content.setText(model.getREVIEW_CONTENT());
                holder.list_rating.setText(model.getRATING() + "");


            }
        };

        review_list.setHasFixedSize(true);
        review_list.setLayoutManager(new LinearLayoutManager(this));
        review_list.setAdapter(adapter);
    }

    private static class ProductsViewHolder extends RecyclerView.ViewHolder{
        private TextView list_content;
        private TextView list_name;
        private TextView list_rating;

        public ProductsViewHolder(@NonNull View itemView) {
            super(itemView);

            list_name = itemView.findViewById(R.id.productName);
            list_content = itemView.findViewById(R.id.productCity);
            list_rating = itemView.findViewById(R.id.productPrice);
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