package com.example.offlinevotingapp.adapters;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.offlinevotingapp.databinding.ItemPollBinding;
import com.example.offlinevotingapp.models.PollEntity;

import java.util.ArrayList;
import java.util.List;

public class PollAdapter extends RecyclerView.Adapter<PollAdapter.PollViewHolder> {

    private List<PollEntity> polls = new ArrayList<>();
    private final OnPollClickListener listener;

    public interface OnPollClickListener {
        void onPollClick(PollEntity poll);
        void onDeleteClick(PollEntity poll);
        void onShareClick(PollEntity poll);
    }

    public PollAdapter(OnPollClickListener listener) {
        this.listener = listener;
    }

    public void setPolls(List<PollEntity> polls) {
        this.polls = polls;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public PollViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemPollBinding binding = ItemPollBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new PollViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull PollViewHolder holder, int position) {
        PollEntity poll = polls.get(position);
        holder.binding.tvPollQuestion.setText(poll.getQuestion());
        holder.itemView.setOnClickListener(v -> listener.onPollClick(poll));
        holder.binding.btnDeletePoll.setOnClickListener(v -> listener.onDeleteClick(poll));
        holder.binding.btnSharePoll.setOnClickListener(v -> listener.onShareClick(poll));
    }

    @Override
    public int getItemCount() {
        return polls.size();
    }

    static class PollViewHolder extends RecyclerView.ViewHolder {
        final ItemPollBinding binding;

        PollViewHolder(ItemPollBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
