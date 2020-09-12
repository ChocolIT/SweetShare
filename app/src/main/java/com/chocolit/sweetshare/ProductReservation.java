package com.chocolit.sweetshare;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.auth.User;
import com.google.firestore.v1.StructuredQuery;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import java.sql.Time;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class ProductReservation extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {

    private Button startDateButton, endDateButton, rentButton;
    private TextView selectStartDate, phoneNumberTextView, endDateWarning;
    private DatePickerDialog startDatePicker, endDatePicker;
    private CheckBox termsAgreement, rememberPreferences;
    private EditText phoneNumberField, addressField;
    private ImageView backButton;

    Calendar startDate = Calendar.getInstance();
    Calendar endDate = Calendar.getInstance();

    private String productID;

    private  ArrayList<String> disabledDatesStringList;

    private boolean startDateSelected = false, endDateSelected = false;
    private boolean textCompleted = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_reservation);

        FrameLayout loadingOverlay = findViewById(R.id.loadingOverlay);

        productID = getIntent().getExtras().getString(ProductConstants.ID);
        String productOwner = getIntent().getExtras().getString(UserConstants.USER_ID);
        disabledDatesStringList = getIntent().getExtras().getStringArrayList(ProductConstants.DISABLED_DATES_LIST);

        phoneNumberField = findViewById(R.id.phoneNumberInputField);
        addressField = findViewById(R.id.addressInputField);
        rentButton = findViewById(R.id.rentButton);

        phoneNumberField.addTextChangedListener(textWatcher);
        addressField.addTextChangedListener(textWatcher);

        termsAgreement = findViewById(R.id.agreeToTermsCheckbox);

        startDateButton = findViewById(R.id.startDateButton);
        endDateButton = findViewById(R.id.endDateButton);
        endDateWarning = findViewById(R.id.endDateWarning);
        phoneNumberTextView = findViewById(R.id.phoneNumberTextView);
        backButton = findViewById(R.id.backButton);

        FirebaseFirestore fStore = FirebaseFirestore.getInstance();
        FirebaseAuth fAuth = FirebaseAuth.getInstance();

        startDateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Date currentDate = new Date();
                Calendar now = Calendar.getInstance();

                startDatePicker = DatePickerDialog.newInstance(
                        ProductReservation.this,
                        now.get(Calendar.YEAR), // Initial year selection
                        now.get(Calendar.MONTH), // Initial month selection
                        now.get(Calendar.DAY_OF_MONTH) // Inital day selection
                );
                now.add(Calendar.DAY_OF_MONTH, 1);
                startDatePicker.setMinDate(now);
                now.setTime(currentDate);
                now.add(Calendar.DAY_OF_MONTH, 60);
                startDatePicker.setMaxDate(now);

                if(disabledDatesStringList != null) {
                    Calendar[] disabledDays = new Calendar[disabledDatesStringList.size()];
                    for (int i = 0; i < disabledDatesStringList.size(); i++) {
                        disabledDays[i] = Calendar.getInstance();
                        Date date = new Date(Long.parseLong(disabledDatesStringList.get(i)));
                        disabledDays[i].setTime(date);
                    }
                    startDatePicker.setDisabledDays(disabledDays);
                }
                startDatePicker.show(getSupportFragmentManager(), "Datepickerdialog");
            }
        });

        endDateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Date currentDate = startDate.getTime();
                Calendar now = Calendar.getInstance();
                now.setTime(currentDate);

                endDatePicker = DatePickerDialog.newInstance(
                        ProductReservation.this,
                        now.get(Calendar.YEAR),
                        now.get(Calendar.MONTH),
                        now.get(Calendar.DAY_OF_MONTH)
                );
                now.add(Calendar.DAY_OF_MONTH, 1);
                endDatePicker.setMinDate(now);
                now.setTime(currentDate);
                now.add(Calendar.DAY_OF_MONTH, 14);
                endDatePicker.setMaxDate(now);

                if(disabledDatesStringList != null) {
                    Calendar[] disabledDays = new Calendar[disabledDatesStringList.size()];
                    for (int i = 0; i < disabledDatesStringList.size(); i++) {
                        disabledDays[i] = Calendar.getInstance();
                        Date date = new Date(Long.parseLong(disabledDatesStringList.get(i)));
                        disabledDays[i].setTime(date);
                    }
                    endDatePicker.setDisabledDays(disabledDays);
                }
                endDatePicker.show(getSupportFragmentManager(), "Datepickerdialog");
            }
        });

        termsAgreement.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (textCompleted && startDateSelected && endDateSelected && b) {
                    rentButton.setEnabled(true);
                }
            }
        });

        rentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Adding occupied dates to product's document.

                loadingOverlay.setVisibility(View.VISIBLE);

                FirebaseFirestore fStore = FirebaseFirestore.getInstance();
                DocumentReference productDoc = fStore.collection("products").document(productID);
                Calendar iterateFromDate = startDate;

                for (Date date  = iterateFromDate.getTime(); iterateFromDate.before(endDate); iterateFromDate.add(Calendar.DATE, 1), date = iterateFromDate.getTime()) {
                    productDoc.update(ProductConstants.DISABLED_DATES_LIST, FieldValue.arrayUnion(String.valueOf(new Timestamp(date.getTime()).getTime())));
                }
                productDoc.update(ProductConstants.DISABLED_DATES_LIST, FieldValue.arrayUnion(String.valueOf(new Timestamp(endDate.getTime().getTime()).getTime())));

                // Creating an 'order' document

                Date date = new Date();
                String orderID = fAuth.getCurrentUser().getUid() + new Timestamp(date.getTime()).getTime();
                DocumentReference orderDoc = fStore.collection("orders").document(orderID);

                Map<String, Object> orderData = new HashMap<>();
                orderData.put(OrderConstants.ID, orderID);
                orderData.put(OrderConstants.START_DATE, new Timestamp(startDate.getTime().getTime()).getTime());
                orderData.put(OrderConstants.END_DATE, new Timestamp(endDate.getTime().getTime()).getTime());
                orderData.put(OrderConstants.PRODUCT_OWNER, productOwner);
                orderData.put(OrderConstants.PRODUCT_RENTER, fAuth.getCurrentUser().getUid());

                orderDoc.set(orderData);
                loadingOverlay.setVisibility(View.GONE);

                Intent intent = new Intent(ProductReservation.this, OrderCompleted.class);
                intent.putExtra(OrderConstants.START_DATE, startDate.get(Calendar.DAY_OF_MONTH) + "-" + startDate.get(Calendar.MONTH) + "-" + startDate.get(Calendar.YEAR));
                intent.putExtra(OrderConstants.END_DATE, endDate.get(Calendar.DAY_OF_MONTH) + "-" + endDate.get(Calendar.MONTH) + "-" + endDate.get(Calendar.YEAR));
                intent.putExtra(UserConstants.USER_FULL_NAME, getIntent().getExtras().getString(UserConstants.USER_FULL_NAME));
                intent.putExtra(ProductConstants.PRODUCT_OWNER_PHONE_NUMBER, getIntent().getExtras().getString(ProductConstants.PRODUCT_OWNER_PHONE_NUMBER));

                startActivity(intent);
                finish();
            }
        });

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        ServicesHelper.setInputFieldActivityStatus(phoneNumberField, findViewById(R.id.focusBarPhoneNumberField));
        ServicesHelper.setInputFieldActivityStatus(addressField, findViewById(R.id.focusBarAddressField));
    }

    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {

        if (!startDateSelected) {
            endDateButton.setEnabled(true);

            startDate.set(Calendar.YEAR, year);
            startDate.set(Calendar.MONTH, monthOfYear);
            startDate.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            startDateSelected = true;
        }
        else if (startDateSelected && !endDateSelected) {
            endDate.set(Calendar.YEAR, year);
            endDate.set(Calendar.MONTH, monthOfYear);
            endDate.set(Calendar.DAY_OF_MONTH, dayOfMonth);

            boolean validInterval = true;
            for (String ts : disabledDatesStringList) {
                Calendar currentDay = Calendar.getInstance();
                currentDay.setTimeInMillis(Long.parseLong(ts));
                if (currentDay.after(startDate) && currentDay.before(endDate)) {
                    validInterval = false;
                    startDateSelected = false;
                    break;
                }
            }

            if (validInterval) {
                endDateWarning.setVisibility(View.GONE);

                endDateSelected = true;

                if (textCompleted && termsAgreement.isChecked()) {
                    rentButton.setEnabled(true);
                }
            }
            else {
                ConstraintLayout.LayoutParams layoutParams = (ConstraintLayout.LayoutParams) findViewById(R.id.phoneNumberTextView).getLayoutParams();
                float factor = phoneNumberTextView.getContext().getResources().getDisplayMetrics().density;
                layoutParams.setMargins(0, (int)(35 * factor),0, 0);
                phoneNumberTextView.setLayoutParams(layoutParams);
                endDateWarning.setVisibility(View.VISIBLE);
            }
        }
    }

    TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            if (!phoneNumberField.getText().toString().trim().isEmpty() && !addressField.getText().toString().trim().isEmpty()) {
                textCompleted = true;
            }
            else if (phoneNumberField.getText().toString().trim().isEmpty() || addressField.getText().toString().trim().isEmpty()) {
                textCompleted = false;
            }

            if (textCompleted && startDateSelected && endDateSelected && termsAgreement.isChecked()) {
                rentButton.setEnabled(true);
            }
        }

        @Override
        public void afterTextChanged(Editable editable) {

        }
    };
}