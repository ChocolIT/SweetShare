package com.chocolit.sweetshare;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.widget.ImageViewCompat;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.transition.Slide;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.models.SlideModel;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class ProductPage extends AppCompatActivity {

    private ImageSlider imageSlider;
    private FirebaseFirestore fStore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_page);

        final TextView productTitleText = findViewById(R.id.productTitleText);
        final TextView productCityText = findViewById(R.id.productCityText);
        final TextView productReviewsNo = findViewById(R.id.productReviewsNo);
        final TextView productPrice = findViewById(R.id.productPrice);
        final TextView productDescription = findViewById(R.id.descriptionText);

        imageSlider = findViewById(R.id.imageSlider);
        final List<SlideModel> slideModels = new ArrayList<>();

        fStore = FirebaseFirestore.getInstance();
        String docID = "zcorO0qMOlOlCOi3au42jcDhV5A31598709036068";
        DocumentReference productDoc = fStore.collection("products").document(docID);

        productDoc.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                List<String> group = (List<String>) documentSnapshot.get(ProductConstants.PRODUCT_IMG_LIST);
                for (int i = 0; i < group.size(); i++) {
                    slideModels.add(new SlideModel(group.get(i)));
                }
                imageSlider.setImageList(slideModels, true);

                productTitleText.setText(documentSnapshot.getString(ProductConstants.PRODUCT_TITLE));
                productCityText.setText(documentSnapshot.getString(ProductConstants.PRODUCT_CITY));
                productReviewsNo.setText(String.format("%s reviews", documentSnapshot.getLong(ProductConstants.REVIEWS_NO)));
                productPrice.setText(String.format("%s SWEETS/DAY", documentSnapshot.getLong(ProductConstants.PRICE)));
                productDescription.setText(documentSnapshot.getString(ProductConstants.PRODUCT_DESCRIPTION));

                setProductStarRating(documentSnapshot.getLong(ProductConstants.PRODUCT_RATING), getApplicationContext());
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
}