package com.chocolit.sweetshare;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.PagerSnapHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SnapHelper;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.models.SlideModel;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProductPage extends AppCompatActivity {

    private ImageSlider imageSlider;
    private FirebaseFirestore fStore;
    private RecyclerView reviewsRecyclerView;
    private FirestoreRecyclerAdapter adapter;
    boolean isFavorite;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_page);
        Bundle loadedProductData = getIntent().getExtras();

        fStore = FirebaseFirestore.getInstance();

        final TextView productTitleText = findViewById(R.id.productTitleText);
        final TextView productCityText = findViewById(R.id.productCityText);
        final TextView productReviewsNo = findViewById(R.id.productReviewsNo);
        final TextView productPrice = findViewById(R.id.productPrice);
        final TextView productDescription = findViewById(R.id.descriptionText);
        final ImageView favoriteButton = findViewById(R.id.icFavoriteButton);

        ImageView backButton = findViewById(R.id.backButton);

        reviewsRecyclerView = findViewById(R.id.reviewsRecyclerView);
        imageSlider = findViewById(R.id.imageSlider);

        productTitleText.setText(loadedProductData.getString(ProductConstants.PRODUCT_TITLE));
        productCityText.setText(loadedProductData.getString(ProductConstants.PRODUCT_CITY));
        productReviewsNo.setText(loadedProductData.getString(ProductConstants.REVIEWS_NO));
        productPrice.setText(loadedProductData.getString(ProductConstants.PRICE));
        productDescription.setText(loadedProductData.getString(ProductConstants.PRODUCT_DESCRIPTION));

        setProductStarRating(loadedProductData.getLong(ProductConstants.PRODUCT_RATING), getApplicationContext());

        isFavorite = loadedProductData.getBoolean(ProductConstants.IS_FAVORITE);
        if (isFavorite) {
            favoriteButton.setImageDrawable(getResources().getDrawable(R.drawable.ic_round_favorite));
        }
        else {
            favoriteButton.setImageDrawable(getResources().getDrawable(R.drawable.ic_round_favorite_border));
        }

        final List<SlideModel> slideModels = new ArrayList<>();
        for (int i = 0; i < loadedProductData.getStringArrayList(ProductConstants.PRODUCT_IMG_LIST).size(); i++) {
            Log.d("TAG", "onCreate: " + loadedProductData.getStringArrayList(ProductConstants.PRODUCT_IMG_LIST).get(i));
            slideModels.add(new SlideModel(loadedProductData.getStringArrayList(ProductConstants.PRODUCT_IMG_LIST).get(i)));
        }

        final String productID = loadedProductData.getString(ProductConstants.ID);
        final DocumentReference productDoc = fStore.collection("products").document(productID);
        final DocumentReference userDoc = fStore.collection("users").document(FirebaseAuth.getInstance().getCurrentUser().getUid());

        Query query = fStore.collection("reviews").whereEqualTo(ProductConstants.REVIEW_ID, productID);
        FirestoreRecyclerOptions<ReviewsModel> options = new FirestoreRecyclerOptions.Builder<ReviewsModel>()
                .setQuery(query, ReviewsModel.class)
                .build();

        imageSlider.setImageList(slideModels, true);

        adapter = new FirestoreRecyclerAdapter<ReviewsModel, ProductsViewHolder>(options) {
            @NonNull
            @Override
            public ProductsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_list_item, parent, false);
                return new ProductsViewHolder(view);
            }

            @Override
            protected void onBindViewHolder(@NonNull ProductsViewHolder holder, int position, @NonNull ReviewsModel model) {
                holder.reviewerName.setText(model.getREVIEWER_NAME());
                holder.reviewContent.setText(model.getREVIEW_CONTENT());

                for (int i = 1; i <= 5; i++) {
                    int drawableId = ServicesHelper.getStarIconFill(model.getRATING(), i);
                    Drawable drawable = ContextCompat.getDrawable(getApplicationContext(), drawableId);
                    drawable.setColorFilter(ContextCompat.getColor(getApplicationContext(), R.color.reviewStarYellow), android.graphics.PorterDuff.Mode.SRC_IN);
                    holder.starsList[i - 1].setBackground(drawable);
                }
            }
        };

        reviewsRecyclerView.setHasFixedSize(true);
        reviewsRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        reviewsRecyclerView.setAdapter(adapter);

        SnapHelper helper = new PagerSnapHelper();
        helper.attachToRecyclerView(reviewsRecyclerView);


        favoriteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!isFavorite) {
                    favoriteButton.setImageDrawable(getResources().getDrawable(R.drawable.ic_round_favorite));
                    isFavorite = true;

                    Map<String, Object> addProductId = new HashMap<>();
                    addProductId.put(UserConstants.USER_FAVORITES, FieldValue.arrayUnion(productID));
                    userDoc.update(addProductId);
                }
                else {
                    favoriteButton.setImageDrawable(getResources().getDrawable(R.drawable.ic_round_favorite_border));
                    isFavorite = false;

                    Map<String, Object> addProductId = new HashMap<>();
                    addProductId.put(UserConstants.USER_FAVORITES, FieldValue.arrayRemove(productID));
                    userDoc.update(addProductId);
                }
            }
        });

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void setProductStarRating(Long productRating, Context currentContext) {
        int[] viewList = {R.id.ic_review_star1, R.id.ic_review_star2, R.id.ic_review_star3, R.id.ic_review_star4, R.id.ic_review_star5};
        for (int i = 1; i <= 5; i++) {
            int drawableId = ServicesHelper.getStarIconFill(productRating, i);
            Drawable drawable = ContextCompat.getDrawable(currentContext, drawableId);
            ImageView currentStar = findViewById(viewList[i - 1]);
            drawable.setColorFilter(ContextCompat.getColor(currentContext, R.color.reviewStarYellow), android.graphics.PorterDuff.Mode.SRC_IN);
            currentStar.setBackground(drawable);
        }
    }

    private class ProductsViewHolder  extends  RecyclerView.ViewHolder{
        private TextView reviewerName, reviewContent;
        int[] viewList = {R.id.ic_review_star1, R.id.ic_review_star2, R.id.ic_review_star3, R.id.ic_review_star4, R.id.ic_review_star5};
        ImageView[] starsList = new ImageView[5];

        public ProductsViewHolder(@NonNull View itemView) {
            super(itemView);
            reviewerName = itemView.findViewById(R.id.reviewerName);
            reviewContent = itemView.findViewById(R.id.reviewContent);

            starsList[0] = itemView.findViewById(viewList[0]);
            starsList[1] = itemView.findViewById(viewList[1]);
            starsList[2] = itemView.findViewById(viewList[2]);
            starsList[3] = itemView.findViewById(viewList[3]);
            starsList[4] = itemView.findViewById(viewList[4]);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        adapter.stopListening();
    }

    @Override
    protected void onStart() {
        super.onStart();
        adapter.startListening();
    }
}