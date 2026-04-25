package com.example.offlinevotingapp.db;

import android.database.Cursor;
import androidx.annotation.NonNull;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.room.SharedSQLiteStatement;
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import com.example.offlinevotingapp.models.PollEntity;
import java.lang.Class;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@SuppressWarnings({"unchecked", "deprecation"})
public final class PollDao_Impl implements PollDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<PollEntity> __insertionAdapterOfPollEntity;

  private final SharedSQLiteStatement __preparedStmtOfDeletePoll;

  public PollDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfPollEntity = new EntityInsertionAdapter<PollEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `polls` (`pollId`,`question`) VALUES (?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          final PollEntity entity) {
        if (entity.getPollId() == null) {
          statement.bindNull(1);
        } else {
          statement.bindString(1, entity.getPollId());
        }
        if (entity.getQuestion() == null) {
          statement.bindNull(2);
        } else {
          statement.bindString(2, entity.getQuestion());
        }
      }
    };
    this.__preparedStmtOfDeletePoll = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "DELETE FROM polls WHERE pollId = ?";
        return _query;
      }
    };
  }

  @Override
  public void insertPoll(final PollEntity poll) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      __insertionAdapterOfPollEntity.insert(poll);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public void deletePoll(final String pollId) {
    __db.assertNotSuspendingTransaction();
    final SupportSQLiteStatement _stmt = __preparedStmtOfDeletePoll.acquire();
    int _argIndex = 1;
    if (pollId == null) {
      _stmt.bindNull(_argIndex);
    } else {
      _stmt.bindString(_argIndex, pollId);
    }
    try {
      __db.beginTransaction();
      try {
        _stmt.executeUpdateDelete();
        __db.setTransactionSuccessful();
      } finally {
        __db.endTransaction();
      }
    } finally {
      __preparedStmtOfDeletePoll.release(_stmt);
    }
  }

  @Override
  public List<PollEntity> getAllPolls() {
    final String _sql = "SELECT * FROM polls";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    __db.assertNotSuspendingTransaction();
    final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
    try {
      final int _cursorIndexOfPollId = CursorUtil.getColumnIndexOrThrow(_cursor, "pollId");
      final int _cursorIndexOfQuestion = CursorUtil.getColumnIndexOrThrow(_cursor, "question");
      final List<PollEntity> _result = new ArrayList<PollEntity>(_cursor.getCount());
      while (_cursor.moveToNext()) {
        final PollEntity _item;
        final String _tmpPollId;
        if (_cursor.isNull(_cursorIndexOfPollId)) {
          _tmpPollId = null;
        } else {
          _tmpPollId = _cursor.getString(_cursorIndexOfPollId);
        }
        final String _tmpQuestion;
        if (_cursor.isNull(_cursorIndexOfQuestion)) {
          _tmpQuestion = null;
        } else {
          _tmpQuestion = _cursor.getString(_cursorIndexOfQuestion);
        }
        _item = new PollEntity(_tmpPollId,_tmpQuestion);
        _result.add(_item);
      }
      return _result;
    } finally {
      _cursor.close();
      _statement.release();
    }
  }

  @Override
  public PollEntity getPollById(final String pollId) {
    final String _sql = "SELECT * FROM polls WHERE pollId = ?";
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
      final int _cursorIndexOfPollId = CursorUtil.getColumnIndexOrThrow(_cursor, "pollId");
      final int _cursorIndexOfQuestion = CursorUtil.getColumnIndexOrThrow(_cursor, "question");
      final PollEntity _result;
      if (_cursor.moveToFirst()) {
        final String _tmpPollId;
        if (_cursor.isNull(_cursorIndexOfPollId)) {
          _tmpPollId = null;
        } else {
          _tmpPollId = _cursor.getString(_cursorIndexOfPollId);
        }
        final String _tmpQuestion;
        if (_cursor.isNull(_cursorIndexOfQuestion)) {
          _tmpQuestion = null;
        } else {
          _tmpQuestion = _cursor.getString(_cursorIndexOfQuestion);
        }
        _result = new PollEntity(_tmpPollId,_tmpQuestion);
      } else {
        _result = null;
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
