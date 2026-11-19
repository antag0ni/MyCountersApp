package com.example.mycounters;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class CounterAdapter extends RecyclerView.Adapter<CounterAdapter.CounterViewHolder> {

    private final List<Counter> counterList;
    public CounterAdapter(List<Counter> counterList) {
        this.counterList = counterList;
    }

    @NonNull
    @Override
    public CounterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.counter_row, parent, false);
        return new CounterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CounterViewHolder holder, int position) {
        Counter c = counterList.get(position);
        holder.counterName.setText(c.getName());
        holder.counterValue.setText(String.valueOf(c.getValue()));

        holder.btnIncrement.setOnClickListener(v -> {
            c.increment(1);
            holder.counterValue.setText(String.valueOf(c.getValue()));
        });
        holder.btnDecrement.setOnClickListener(v -> {
            c.decrement(1);
            holder.counterValue.setText(String.valueOf(c.getValue()));
        });
        holder.btnMore.setOnClickListener(v -> {
            PopupMenu popup = new PopupMenu(v.getContext(), holder.btnMore);
            popup.getMenu().add(0, 1, 0, "Reset");
            popup.getMenu().add(0, 2, 1, "Delete");

            popup.setOnMenuItemClickListener(item -> {
                int pos = holder.getBindingAdapterPosition();
                if (pos == RecyclerView.NO_POSITION) return false;

                switch (item.getItemId()) {

                    case 1:
                        c.reset();
                        notifyItemChanged(pos);
                        return true;
                    case 2:
                        counterList.remove(c);

                        c.removeFromPrefs();

                        SharedPreferences prefs = v.getContext().getSharedPreferences(MainActivity.PREFS_NAME, Context.MODE_PRIVATE);
                        Set<String> keys = new HashSet<>(prefs.getStringSet(MainActivity.KEYS_SET, new HashSet<>()));
                        keys.remove(c.getName());
                        prefs.edit().putStringSet(MainActivity.KEYS_SET, keys).apply();

                        notifyItemRemoved(pos);
                        return true;
                    default:
                        return false;
                }
            });
            popup.show();
        });

    }

    @Override
    public int getItemCount() {
        return counterList.size();
    }

    static class CounterViewHolder extends RecyclerView.ViewHolder {
        TextView counterName;
        TextView counterValue;
        Button btnIncrement;
        Button btnDecrement;
        ImageButton btnMore;

        public CounterViewHolder(View itemView) {
            super(itemView);
            counterName = itemView.findViewById(R.id.counterName);
            counterValue = itemView.findViewById(R.id.counterValue);
            btnIncrement = itemView.findViewById(R.id.btnIncrement);
            btnDecrement = itemView.findViewById(R.id.btnDecrement);
            btnMore = itemView.findViewById(R.id.btnMore);
        }
    }
}
