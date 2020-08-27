package com.chocolit.sweetshare;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import static android.app.Activity.RESULT_OK;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AddProductTab#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AddProductTab extends Fragment {

    private View chooseImgView, publishButton;
    private EditText productNameField, productDescriptionField, cityField, phoneNumberField, priceInputField;
    private TextView userDisplayNameField, userEmailField;
    private FirebaseAuth fAuth;
    private FirebaseFirestore fStore;
    private FrameLayout loadingOverlay;
    private Spinner categoriesSpinner;

    final ArrayList<String> uriList = new ArrayList<>();
    private Map<String, Object> productData = new HashMap<>();

    private ArrayList<Uri> imgList = new ArrayList<Uri>();

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    public AddProductTab() {

    }

    public static AddProductTab newInstance(String param1, String param2) {
        AddProductTab fragment = new AddProductTab();
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
        return inflater.inflate(R.layout.fragment_add_product_tab, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        super.onCreate(savedInstanceState);

        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();

        SharedPreferences sharedPref = getActivity().getSharedPreferences(UserConstants.USER_FETCHED_DATA_SHARED_PREFERENCES_FILE, Context.MODE_PRIVATE);
        SharedPreferences productCategoriesSharedPref = getActivity().getSharedPreferences(ProductConstants.PRODUCT_CATEGORIES, Context.MODE_PRIVATE);

        Set<String> pcSet = productCategoriesSharedPref.getStringSet(ProductConstants.PRODUCT_CATEGORIES, new HashSet<String>());
        String[] productCategories = pcSet.toArray(new String[pcSet.size()]);

        userDisplayNameField = view.findViewById(R.id.ContactNameDisplay);
        userEmailField = view.findViewById(R.id.EmailAdressInputField);
        userDisplayNameField.setText(sharedPref.getString(UserConstants.USER_FULL_NAME, "Default"));
        userEmailField.setText(sharedPref.getString(UserConstants.USER_EMAIL, "Default"));

        chooseImgView = view.findViewById(R.id.addPhotosBackground);
        publishButton = view.findViewById(R.id.PublishButtonBackground);
        categoriesSpinner = view.findViewById(R.id.categoriesSpinner);

        loadingOverlay = view.findViewById(R.id.loadingOverlay);

        productNameField = view.findViewById(R.id.TitleInputField);
        productDescriptionField = view.findViewById(R.id.DescriptionInputField);
        cityField = view.findViewById(R.id.CityInputField);
        phoneNumberField = view.findViewById(R.id.PhoneNumberInputField);
        priceInputField = view.findViewById(R.id.priceInputField);

        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_dropdown_item, productCategories);
        spinnerAdapter.setDropDownViewResource(R.layout.simple_spinner_dropdown_item);
        categoriesSpinner.setAdapter(spinnerAdapter);

        chooseImgView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                startActivityForResult(intent, RequestCodes.GALLERY_REQUEST_CODE);
            }
        });

        categoriesSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int pos, long l) {
                TextView selectedText = (TextView) adapterView.getChildAt(0);
                if (selectedText != null) {
                    selectedText.setTextColor(Color.BLACK);
                    selectedText.setTextSize(18);
                }
                productData.put(ProductConstants.PRODUCT_CATEGORY, adapterView.getItemAtPosition(pos));
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                TextView errorText = (TextView)categoriesSpinner.getSelectedView();
                errorText.setError("");
                errorText.setTextColor(Color.RED);
                errorText.setText("A category must be selected");
            }
        });

        publishButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String productTitle = productNameField.getText().toString();
                String productDescription = productDescriptionField.getText().toString();
                String city = cityField.getText().toString();
                String phoneNumber = phoneNumberField.getText().toString();
                long price = 0;

                try {
                    price = Long.parseLong(priceInputField.getText().toString());
                }
                catch (Exception e) {
                    priceInputField.setError("Price must be a number");
                }

                if (productTitle.isEmpty()){
                    productNameField.setError("This field can't be empty");
                    return;
                }
                if (productDescription.isEmpty()) {
                    productDescriptionField.setError("This field can't be empty");
                    return;
                }
                if (city.isEmpty()) {
                    cityField.setError("This field can't be empty");
                    return;
                }
                if (price == 0) {
                    priceInputField.setError("Invalid input");
                    return;
                }

                productData.put(ProductConstants.PRODUCT_TITLE, productTitle);
                productData.put(ProductConstants.PRODUCT_DESCRIPTION, productDescription);
                productData.put(ProductConstants.PRODUCT_CITY, city);
                productData.put(ProductConstants.PRODUCT_OWNER_PHONE_NUMBER, phoneNumber);
                productData.put(UserConstants.USER_ID, fAuth.getCurrentUser().getUid());
                productData.put(ProductConstants.PRICE, price);
                productData.put(ProductConstants.REVIEWS_NO, 0);

                loadingOverlay.setVisibility(View.VISIBLE);

                Date date = new Date();
                Timestamp ts = new Timestamp(date.getTime());
                final String productId = fAuth.getCurrentUser().getUid() + ts.getTime();
                StorageReference imageFolder = FirebaseStorage.getInstance().getReference().child("productImg/" + productId);
                Log.d("TAG", "onClick: " + imageFolder);

                for (int i = 0; i < imgList.size(); i++) {
                    Uri currentImg = imgList.get(i);

                    final StorageReference imageName = imageFolder.child("" + i);
                    imageName.putFile(currentImg).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            Log.d("TAG", "onSuccess: Uploaded files");

                            imageName.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    uriList.add(uri.toString());

                                    if (uriList.size() == imgList.size()) {
                                        productData.put(ProductConstants.PRODUCT_IMG_LIST, uriList);
                                        final DocumentReference documentReference = fStore.collection("products").document(productId);
                                        Log.d("TAG", "onSuccess: uploading to db");
                                        documentReference.set(productData);

                                        // Adding the product id to its corresponding user
                                        DocumentReference userReference = fStore.collection("users").document(fAuth.getCurrentUser().getUid());
                                        Map<String, Object> addProductId = new HashMap<>();
                                        addProductId.put(UserConstants.USER_OWNED_PRODUCTS_LIST, FieldValue.arrayUnion(productId));
                                        userReference.update(addProductId);

                                        loadingOverlay.setVisibility(View.GONE);

                                        Toast.makeText(getContext(), "Product created successfully.", Toast.LENGTH_SHORT);
                                    }

                                }
                            });
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.d("TAG", "onFailure: " + e);
                        }
                    });
                }
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RequestCodes.GALLERY_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                Log.d("TAG", "onActivityResult: DA");
                if (data.getClipData() != null) {
                    int countClipData = data.getClipData().getItemCount();

                    for (int i = 0; i < countClipData; i++) {
                        Uri imgUri = data.getClipData().getItemAt(i).getUri();
                        imgList.add(imgUri);
                    }
                }
            } else {

            }
        }
    }
}
