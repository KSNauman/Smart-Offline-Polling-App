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
import com.example.offlinevotingapp.models.MyVoteEntity;
import java.lang.Class;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.Collections;
import java.util.List;

@SuppressWarnings({"unchecked", "deprecation"})
public final class MyVoteDao_Impl implements MyVoteDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<MyVoteEntity> __insertionAdapterOfMyVoteEntity;

  private final SharedSQLiteStatement __preparedStmtOfDeleteMyVote;

  public MyVoteDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfMyVoteEntity = new EntityInsertionAdapter<MyVoteEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `my_votes` (`pollId`,`optionId`,`optionText`) VALUES (?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          final MyVoteEntity entity) {
        if (entity.getPollId() == null) {
          statement.bindNull(1);
        } else {
          statement.bindString(1, entity.getPollId());
        }
        if (entity.getOptionId() == null) {
          statement.bindNull(2);
        } else {
          statement.bindString(2, entity.getOptionId());
        }
        if (entity.getOptionText() == null) {
          statement.bindNull(3);
        } else {
          statement.bindString(3, entity.getOptionText());
        }
      }
    };
    this.__preparedStmtOfDeleteMyVote = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "DELETE FROM my_votes WHERE pollId = ?";
        return _query;
      }
    };
  }

  @Override
  public void insertMyVote(final MyVoteEntity vote) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      __insertionAdapterOfMyVoteEntity.insert(vote);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public void deleteMyVote(final String pollId) {
    __db.assertNotSuspendingTransaction();
    final SupportSQLiteStatement _stmt = __preparedStmtOfDeleteMyVote.acquire();
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
      __preparedStmtOfDeleteMyVote.release(_stmt);
    }
  }

  @Override
  public MyVoteEntity getMyVote(final String pollId) {
    final String _sql = "SELECT * FROM my_votes WHERE pollId = ? LIMIT 1";
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
      final int _cursorIndexOfOptionId = CursorUtil.getColumnIndexOrThrow(_cursor, "optionId");
      final int _cursorIndexOfOptionText = CursorUtil.getColumnIndexOrThrow(_cursor, "optionText");
      final MyVoteEntity _result;
      if (_cursor.moveToFirst()) {
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
        final String _tmpOptionText;
        if (_cursor.isNull(_cursorIndexOfOptionText)) {
          _tmpOptionText = null;
        } else {
          _tmpOptionText = _cursor.getString(_cursorIndexOfOptionText);
        }
        _result = new MyVoteEntity(_tmpPollId,_tmpOptionId,_tmpOptionText);
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
