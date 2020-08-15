package com.chocolit.sweetshare;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

public class TabPagerAdapter extends FragmentStateAdapter {
    public TabPagerAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 0:
                return new ProfileTab();
            case 1:
                return new ExploreTab();
            case 2:
                return new MessagesTab();
            default:
                return null;
        }
    }

    @Override
    public int getItemCount() {
        return 3;
    }
}
