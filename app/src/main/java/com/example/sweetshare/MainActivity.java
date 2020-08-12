package com.example.sweetshare;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {
    //GridViewExplore
    GridView gridView;
    //GridViewExplore
    String[] categoryName = {"Tools", "Sports", "Gardening", "Photo", "Entertainment", "Clothing", "Electronics", "Books"};

    int[] numberImage = {R.drawable.tools, R.drawable.sports, R.drawable.gardening, R.drawable.photovideo, R.drawable.entertainment, R.drawable.clothing, R.drawable.electronics, R.drawable.books};

    public void logout(View view) {
        FirebaseAuth.getInstance().signOut();
        startActivity(new Intent(getApplicationContext(), Login.class));
        finish();
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ViewPager2 viewPager2 = findViewById(R.id.viewPager);
        viewPager2.setAdapter(new TabPagerAdapter(this));

        TabLayout tabLayout = findViewById(R.id.tabLayout);
        TabLayoutMediator tabLayoutMediator = new TabLayoutMediator(
                tabLayout, viewPager2, new TabLayoutMediator.TabConfigurationStrategy() {
            @Override
            public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
                switch (position) {
                    case 0:
                        tab.setText("Profile");
                        tab.setIcon(R.drawable.profile_icon);
                        break;
                    case 1:
                        tab.setText("Explore");
                        tab.setIcon(R.drawable.explore_icon);
                        break;
                    case 2:
                        tab.setText("Messages");
                        tab.setIcon(R.drawable.messages_icon);
                        break;
                }

            }
        }
        );
        tabLayoutMediator.attach();
        viewPager2.setCurrentItem(1, false);


        //GridViewExplore
        gridView = findViewById(R.id.grid_view);
        MainAdapter adapter = new MainAdapter(MainActivity.this,categoryName, numberImage);
        gridView.setAdapter(adapter);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(getApplicationContext(), "You Clicked" + categoryName[+position],Toast.LENGTH_SHORT).show();
            }
        });
    }
}