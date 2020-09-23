package com.chocolit.sweetshare;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.squareup.picasso.Picasso;

import java.util.Calendar;

public class UserOrders extends AppCompatActivity {

    private FirestoreRecyclerAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_orders);

        FirebaseFirestore fStore = FirebaseFirestore.getInstance();
        RecyclerView recyclerView = findViewById(R.id.userOrdersRecyclerView);

        String uID = FirebaseAuth.getInstance().getCurrentUser().getUid();
        Query query = fStore.collection("orders").whereEqualTo(OrderConstants.PRODUCT_RENTER, uID);
        FirestoreRecyclerOptions<UserOrdersModel> options = new FirestoreRecyclerOptions.Builder<UserOrdersModel>().setQuery(query, UserOrdersModel.class).build();

        adapter = new FirestoreRecyclerAdapter<UserOrdersModel, UserOrders.ProductsViewHolder>(options) {
            @NonNull
            @Override
            public UserOrders.ProductsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_orders_list_item, parent, false);
                return new UserOrders.ProductsViewHolder(view);
            }

            @SuppressLint("SetTextI18n")
            @Override
            protected void onBindViewHolder(@NonNull UserOrders.ProductsViewHolder holder, int position, @NonNull UserOrdersModel model) {
                Calendar startDateCalendar = Calendar.getInstance(), endDateCalendar = Calendar.getInstance();

                startDateCalendar.setTimeInMillis(model.getSTART_DATE());
                endDateCalendar.setTimeInMillis(model.getEND_DATE());

                String startDate = startDateCalendar.get(Calendar.DAY_OF_MONTH ) + "-" + startDateCalendar.get(Calendar.MONTH) + "-" + startDateCalendar.get(Calendar.YEAR);
                String endDate = endDateCalendar.get(Calendar.DAY_OF_MONTH ) + "-" + endDateCalendar.get(Calendar.MONTH) + "-" + endDateCalendar.get(Calendar.YEAR);

                holder.list_title.setText(model.getPRODUCT_TITLE());
                holder.list_interval.setText(startDate+ "\n" + endDate);
                holder.list_contactPhoneNumber.setText(model.getCONTACT_PHONE_NUMBER());
                String url = model.getFIRST_IMAGE();
                Picasso.get().load(url).into(holder.list_image);

                Calendar now = Calendar.getInstance();

                Drawable unwrappedDrawable = AppCompatResources.getDrawable(getApplicationContext(), R.drawable.ic_clock);
                Drawable wrappedDrawable = DrawableCompat.wrap(unwrappedDrawable);

                if (now.before(startDateCalendar)) {
                    holder.list_orderStatus.setText("Pending");
                    holder.list_orderStatus.setTextColor(Color.YELLOW);
                    DrawableCompat.setTint(wrappedDrawable, Color.YELLOW);
                }
                else if (now.after(startDateCalendar) && now.before(endDateCalendar)) {
                    holder.list_orderStatus.setText("Running");
                    holder.list_orderStatus.setTextColor(Color.GREEN);
                    DrawableCompat.setTint(wrappedDrawable, Color.GREEN);
                }
                else {
                    holder.list_orderStatus.setText("Completed");
                    holder.list_orderStatus.setTextColor(Color.RED);
                    DrawableCompat.setTint(wrappedDrawable, Color.RED);
                }
                holder.icClockStatus.setBackground(wrappedDrawable);
            }
        };

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        findViewById(R.id.backArrowButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private class ProductsViewHolder extends RecyclerView.ViewHolder{
        private TextView list_title;
        private TextView list_interval;
        private TextView list_orderStatus;
        private TextView list_contactPhoneNumber;
        private ImageView list_image, icClockStatus;

        public ProductsViewHolder(@NonNull View itemView) {
            super(itemView);

            list_title = itemView.findViewById(R.id.productTitle);
            list_interval = itemView.findViewById(R.id.orderInterval);
            list_image = itemView.findViewById(R.id.orderImage);
            list_orderStatus = itemView.findViewById(R.id.orderStatus);
            icClockStatus = itemView.findViewById(R.id.icClock);
            list_contactPhoneNumber = itemView.findViewById(R.id.contactPhoneNumber);

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