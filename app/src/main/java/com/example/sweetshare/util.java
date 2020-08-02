package com.example.sweetshare;

import android.view.View;

public class util {
        public static void setInputFieldActivityStatus(View inputField, final View activityBar) {
            /** Adds onFocusChangeListener to a View and displays/hides its activity bar based on it.
             * param: [View] The view we want to add the listener to.
             * param: [View] The activity bar corresponding to view.
             */
            inputField.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View view, boolean b) {
                    if (view.hasFocus()) {
                        activityBar.setVisibility(View.VISIBLE);
                    }
                    else {
                        activityBar.setVisibility(View.INVISIBLE);
                    }
                }
            });
        }
}
