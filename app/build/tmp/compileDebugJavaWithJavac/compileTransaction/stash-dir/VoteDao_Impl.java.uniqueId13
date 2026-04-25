package com.example.offlinevotingapp.db;

import android.database.Cursor;
import androidx.annotation.NonNull;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import com.example.offlinevotingapp.models.VoteEntity;
import java.lang.Class;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@SuppressWarnings({"unchecked", "deprecation"})
public final class VoteDao_Impl implements VoteDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<VoteEntity> __insertionAdapterOfVoteEntity;

  public VoteDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfVoteEntity = new EntityInsertionAdapter<VoteEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR ABORT INTO `votes` (`voteId`,`pollId`,`optionId`,`deviceId`,`userName`) VALUES (nullif(?, 0),?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          final VoteEntity entity) {
        statement.bindLong(1, entity.getVoteId());
        if (entity.getPollId() == null) {
          statement.bindNull(2);
        } else {
          statement.bindString(2, entity.getPollId());
        }
        if (entity.getOptionId() == null) {
          statement.bindNull(3);
        } else {
          statement.bindString(3, entity.getOptionId());
        }
        if (entity.getDeviceId() == null) {
          statement.bindNull(4);
        } else {
          statement.bindString(4, entity.getDeviceId());
        }
        if (entity.getUserName() == null) {
          statement.bindNull(5);
        } else {
          statement.bindString(5, entity.getUserName());
        }
      }
    };
  }

  @Override
  public void insertVote(final VoteEntity vote) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      __insertionAdapterOfVoteEntity.insert(vote);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public boolean checkVoteExists(final String pollId, final String deviceId) {
    final String _sql = "SELECT EXISTS(SELECT 1 FROM votes WHERE pollId = ? AND deviceId = ?)";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 2);
    int _argIndex = 1;
    if (pollId == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, pollId);
    }
    _argIndex = 2;
    if (deviceId == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, deviceId);
    }
    __db.assertNotSuspendingTransaction();
    final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
    try {
      final boolean _result;
      if (_cursor.moveToFirst()) {
        final int _tmp;
        _tmp = _cursor.getInt(0);
        _result = _tmp != 0;
      } else {
        _result = false;
      }
      return _result;
    } finally {
      _cursor.close();
      _statement.release();
    }
  }

  @Override
  public List<VoteEntity> getVotesByPoll(final String pollId) {
    final String _sql = "SELECT * FROM votes WHERE pollId = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    if (pollId == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, pollId);
    }
    __db.assertNotSuspendingTransaction();
    final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
    try {
      final int _cursorIndexOfVoteId = CursorUtil.getColumnIndexOrThrow(_cursor, "voteId");
      final int _cursorIndexOfPollId = CursorUtil.getColumnIndexOrThrow(_cursor, "pollId");
      final int _cursorIndexOfOptionId = CursorUtil.getColumnIndexOrThrow(_cursor, "optionId");
      final int _cursorIndexOfDeviceId = CursorUtil.getColumnIndexOrThrow(_cursor, "deviceId");
      final int _cursorIndexOfUserName = CursorUtil.getColumnIndexOrThrow(_cursor, "userName");
      final List<VoteEntity> _result = new ArrayList<VoteEntity>(_cursor.getCount());
      while (_cursor.moveToNext()) {
        final VoteEntity _item;
        final String _tmpPollId;
        if (_cursor.isNull(_cursorIndexOfPollId)) {
          _tmpPollId = null;
        } else {
          _tmpPollId = _cursor.getString(_cursorIndexOfPollId);
        }
        final String _tmpOptionId;
        if (_cursor.isNull(_cursorIndexOfOptionId)) {
          _tmpOptionId = null;
        } else {
          _tmpOptionId = _cursor.getString(_cursorIndexOfOptionId);
        }
        final String _tmpDeviceId;
        if (_cursor.isNull(_cursorIndexOfDeviceId)) {
          _tmpDeviceId = null;
        } else {
          _tmpDeviceId = _cursor.getString(_cursorIndexOfDeviceId);
        }
        final String _tmpUserName;
        if (_cursor.isNull(_cursorIndexOfUserName)) {
          _tmpUserName = null;
        } else {
          _tmpUserName = _cursor.getString(_cursorIndexOfUserName);
        }
        _item = new VoteEntity(_tmpPollId,_tmpOptionId,_tmpDeviceId,_tmpUserName);
        final int _tmpVoteId;
        _tmpVoteId = _cursor.getInt(_cursorIndexOfVoteId);
        _item.setVoteId(_tmpVoteId);
        _result.add(_item);
      }
      return _result;
    } finally {
      _cursor.close();
      _statement.release();
    }
  }

  @NonNull
  public static List<Class<?>> getRequiredConverters() {
    return Collections.emptyList();
  }
}
