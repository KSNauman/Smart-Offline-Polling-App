package com.example.offlinevotingapp.repository;

import android.content.Context;

import com.example.offlinevotingapp.db.AppDatabase;
import com.example.offlinevotingapp.db.MyVoteDao;
import com.example.offlinevotingapp.db.OptionDao;
import com.example.offlinevotingapp.db.PollDao;
import com.example.offlinevotingapp.db.VoteDao;
import com.example.offlinevotingapp.models.MyVoteEntity;
import com.example.offlinevotingapp.models.OptionEntity;
import com.example.offlinevotingapp.models.PollEntity;
import com.example.offlinevotingapp.models.VoteEntity;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class VotingRepository {

    private final PollDao pollDao;
    private final OptionDao optionDao;
    private final VoteDao voteDao;
    private final MyVoteDao myVoteDao;
    private final ExecutorService executorService;

    public VotingRepository(Context context) {
        AppDatabase db = AppDatabase.getInstance(context);
        pollDao = db.pollDao();
        optionDao = db.optionDao();
        voteDao = db.voteDao();
        myVoteDao = db.myVoteDao();
        executorService = Executors.newSingleThreadExecutor();
    }

    public void createPoll(PollEntity poll, List<OptionEntity> options, Runnable callback) {
        executorService.execute(() -> {
            pollDao.insertPoll(poll);
            optionDao.insertOptions(options);
            if (callback != null) callback.run();
        });
    }

    public void getAllPolls(Callback<List<PollEntity>> callback) {
        executorService.execute(() -> {
            List<PollEntity> polls = pollDao.getAllPolls();
            callback.onResult(polls);
        });
    }

    public void getOptionsByPoll(String pollId, Callback<List<OptionEntity>> callback) {
        executorService.execute(() -> {
            List<OptionEntity> options = optionDao.getOptionsByPoll(pollId);
            callback.onResult(options);
        });
    }

    public void castVote(VoteEntity vote, Runnable onSuccess, Runnable onDuplicate) {
        executorService.execute(() -> {
            if (voteDao.checkVoteExists(vote.getPollId(), vote.getDeviceId())) {
                if (onDuplicate != null) onDuplicate.run();
            } else {
                voteDao.insertVote(vote);
                optionDao.incrementVoteCount(vote.getOptionId());
                if (onSuccess != null) onSuccess.run();
            }
        });
    }

    public void deletePoll(String pollId, Runnable callback) {
        executorService.execute(() -> {
            pollDao.deletePoll(pollId);
            myVoteDao.deleteMyVote(pollId);
            if (callback != null) callback.run();
        });
    }

    // My Votes
    public void saveMyVote(MyVoteEntity vote) {
        executorService.execute(() -> myVoteDao.insertMyVote(vote));
    }

    public void getMyVote(String pollId, Callback<MyVoteEntity> callback) {
        executorService.execute(() -> {
            MyVoteEntity vote = myVoteDao.getMyVote(pollId);
            callback.onResult(vote);
        });
    }

    public interface Callback<T> {
        void onResult(T result);
    }
}
