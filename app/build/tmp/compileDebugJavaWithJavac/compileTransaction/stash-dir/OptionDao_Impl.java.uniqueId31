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
import com.example.offlinevotingapp.models.OptionEntity;
import java.lang.Class;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@SuppressWarnings({"unchecked", "deprecation"})
public final class OptionDao_Impl implements OptionDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<OptionEntity> __insertionAdapterOfOptionEntity;

  private final SharedSQLiteStatement __preparedStmtOfIncrementVoteCount;

  public OptionDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfOptionEntity = new EntityInsertionAdapter<OptionEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `options` (`optionId`,`pollId`,`text`,`voteCount`) VALUES (?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          final OptionEntity entity) {
        if (entity.getOptionId() == null) {
          statement.bindNull(1);
        } else {
          statement.bindString(1, entity.getOptionId());
        }
        if (entity.getPollId() == null) {
          statement.bindNull(2);
        } else {
          statement.bindString(2, entity.getPollId());
        }
        if (entity.getText() == null) {
          statement.bindNull(3);
        } else {
          statement.bindString(3, entity.getText());
        }
        statement.bindLong(4, entity.getVoteCount());
      }
    };
    this.__preparedStmtOfIncrementVoteCount = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "UPDATE options SET voteCount = voteCount + 1 WHERE optionId = ?";
        return _query;
      }
    };
  }

  @Override
  public void insertOptions(final List<OptionEntity> options) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      __insertionAdapterOfOptionEntity.insert(options);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public void incrementVoteCount(final String optionId) {
    __db.assertNotSuspendingTransaction();
    final SupportSQLiteStatement _stmt = __preparedStmtOfIncrementVoteCount.acquire();
    int _argIndex = 1;
    if (optionId == null) {
      _stmt.bindNull(_argIndex);
    } else {
      _stmt.bindString(_argIndex, optionId);
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
      __preparedStmtOfIncrementVoteCount.release(_stmt);
    }
  }

  @Override
  public List<OptionEntity> getOptionsByPoll(final String pollId) {
    final String _sql = "SELECT * FROM options WHERE pollId = ?";
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
      final int _cursorIndexOfOptionId = CursorUtil.getColumnIndexOrThrow(_cursor, "optionId");
      final int _cursorIndexOfPollId = CursorUtil.getColumnIndexOrThrow(_cursor, "pollId");
      final int _cursorIndexOfText = CursorUtil.getColumnIndexOrThrow(_cursor, "text");
      final int _cursorIndexOfVoteCount = CursorUtil.getColumnIndexOrThrow(_cursor, "voteCount");
      final List<OptionEntity> _result = new ArrayList<OptionEntity>(_cursor.getCount());
      while (_cursor.moveToNext()) {
        final OptionEntity _item;
        final String _tmpOptionId;
        if (_cursor.isNull(_cursorIndexOfOptionId)) {
          _tmpOptionId = null;
        } else {
          _tmpOptionId = _cursor.getString(_cursorIndexOfOptionId);
        }
        final String _tmpPollId;
        if (_cursor.isNull(_cursorIndexOfPollId)) {
          _tmpPollId = null;
        } else {
          _tmpPollId = _cursor.getString(_cursorIndexOfPollId);
        }
        final String _tmpText;
        if (_cursor.isNull(_cursorIndexOfText)) {
          _tmpText = null;
        } else {
          _tmpText = _cursor.getString(_cursorIndexOfText);
        }
        final int _tmpVoteCount;
        _tmpVoteCount = _cursor.getInt(_cursorIndexOfVoteCount);
        _item = new OptionEntity(_tmpOptionId,_tmpPollId,_tmpText,_tmpVoteCount);
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
