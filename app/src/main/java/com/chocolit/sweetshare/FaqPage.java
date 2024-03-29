package com.chocolit.sweetshare;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.SharedPreferences;
import android.icu.lang.UCharacter;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.florent37.expansionpanel.ExpansionLayout;
import com.github.florent37.expansionpanel.viewgroup.ExpansionLayoutCollection;

import java.util.ArrayList;
import java.util.List;

import com.chocolit.sweetshare.EntityFaqItem;

public class FaqPage extends AppCompatActivity {

    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_faq_page);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        final RecyclerAdapter adapter = new RecyclerAdapter();
        recyclerView.setAdapter(adapter);

        ImageView icBackArrow = findViewById(R.id.icBackArrow);

        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("FAQ_CONTENTS", Context.MODE_PRIVATE);

        //fill with empty objects
        final List<EntityFaqItem> list = new ArrayList<>();
        for (int i = 1; i <= 6; i++) {
            list.add(new EntityFaqItem(sharedPreferences.getString("QUESTION_" + i, "404"), sharedPreferences.getString("ANSWER_" + i, "404")));
            Log.d("TAG", "onCreate: " + sharedPreferences.getString("QUESTION_" + i, "404"));
        }
        adapter.setItems(list);

        icBackArrow.setOnClickListener((view -> {
            finish();
        }));
    }

    public final static class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.RecyclerHolder> {

        private final List<EntityFaqItem> list = new ArrayList<>();

        private final ExpansionLayoutCollection expansionsCollection = new ExpansionLayoutCollection();

        public RecyclerAdapter() {
            expansionsCollection.openOnlyOne(true);
        }

        @Override
        public RecyclerHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return RecyclerHolder.buildFor(parent);
        }

        @Override
        public void onBindViewHolder(RecyclerHolder holder, int position) {
            holder.bind(list.get(position));
            expansionsCollection.add(holder.getExpansionLayout());
            holder.questionText.setText(list.get(position).getQuestion());
            holder.answerText.setText(list.get(position).getAnswer());
        }

        @Override
        public int getItemCount() {
            return list.size();
        }

        public void setItems(List<EntityFaqItem> items) {
            this.list.addAll(items);
            notifyDataSetChanged();
        }

        public final static class RecyclerHolder extends RecyclerView.ViewHolder {

            private static final int LAYOUT = R.layout.expandable_item;

            ExpansionLayout expansionLayout;
            TextView questionText, answerText;

            public static RecyclerHolder buildFor(ViewGroup viewGroup){
                return new RecyclerHolder(LayoutInflater.from(viewGroup.getContext()).inflate(LAYOUT, viewGroup, false));
            }

            public RecyclerHolder(View itemView) {
                super(itemView);
                expansionLayout = itemView.findViewById(R.id.expansionLayout);
                questionText = itemView.findViewById(R.id.questionText);
                answerText = itemView.findViewById(R.id.answerText);
            }

            public void bind(Object object){
                expansionLayout.collapse(false);
            }

            public ExpansionLayout getExpansionLayout() {
                return expansionLayout;
            }
        }
    }
}