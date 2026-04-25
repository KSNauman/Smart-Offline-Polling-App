package com.example.offlinevotingapp.db;

import androidx.annotation.NonNull;
import androidx.room.DatabaseConfiguration;
import androidx.room.InvalidationTracker;
import androidx.room.RoomDatabase;
import androidx.room.RoomOpenHelper;
import androidx.room.migration.AutoMigrationSpec;
import androidx.room.migration.Migration;
import androidx.room.util.DBUtil;
import androidx.room.util.TableInfo;
import androidx.sqlite.db.SupportSQLiteDatabase;
import androidx.sqlite.db.SupportSQLiteOpenHelper;
import java.lang.Class;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

@SuppressWarnings({"unchecked", "deprecation"})
public final class AppDatabase_Impl extends AppDatabase {
  private volatile PollDao _pollDao;

  private volatile OptionDao _optionDao;

  private volatile VoteDao _voteDao;

  private volatile MyVoteDao _myVoteDao;

  @Override
  @NonNull
  protected SupportSQLiteOpenHelper createOpenHelper(@NonNull final DatabaseConfiguration config) {
    final SupportSQLiteOpenHelper.Callback _openCallback = new RoomOpenHelper(config, new RoomOpenHelper.Delegate(2) {
      @Override
      public void createAllTables(@NonNull final SupportSQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS `polls` (`pollId` TEXT NOT NULL, `question` TEXT, PRIMARY KEY(`pollId`))");
        db.execSQL("CREATE TABLE IF NOT EXISTS `options` (`optionId` TEXT NOT NULL, `pollId` TEXT, `text` TEXT, `voteCount` INTEGER NOT NULL, PRIMARY KEY(`optionId`), FOREIGN KEY(`pollId`) REFERENCES `polls`(`pollId`) ON UPDATE NO ACTION ON DELETE CASCADE )");
        db.execSQL("CREATE INDEX IF NOT EXISTS `index_options_pollId` ON `options` (`pollId`)");
        db.execSQL("CREATE TABLE IF NOT EXISTS `votes` (`voteId` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `pollId` TEXT, `optionId` TEXT, `deviceId` TEXT, `userName` TEXT)");
        db.execSQL("CREATE TABLE IF NOT EXISTS `my_votes` (`pollId` TEXT NOT NULL, `optionId` TEXT, `optionText` TEXT, PRIMARY KEY(`pollId`))");
        db.execSQL("CREATE TABLE IF NOT EXISTS room_master_table (id INTEGER PRIMARY KEY,identity_hash TEXT)");
        db.execSQL("INSERT OR REPLACE INTO room_master_table (id,identity_hash) VALUES(42, '3aae1e3a3e11bbe368b29ab620eff199')");
      }

      @Override
      public void dropAllTables(@NonNull final SupportSQLiteDatabase db) {
        db.execSQL("DROP TABLE IF EXISTS `polls`");
        db.execSQL("DROP TABLE IF EXISTS `options`");
        db.execSQL("DROP TABLE IF EXISTS `votes`");
        db.execSQL("DROP TABLE IF EXISTS `my_votes`");
        final List<? extends RoomDatabase.Callback> _callbacks = mCallbacks;
        if (_callbacks != null) {
          for (RoomDatabase.Callback _callback : _callbacks) {
            _callback.onDestructiveMigration(db);
          }
        }
      }

      @Override
      public void onCreate(@NonNull final SupportSQLiteDatabase db) {
        final List<? extends RoomDatabase.Callback> _callbacks = mCallbacks;
        if (_callbacks != null) {
          for (RoomDatabase.Callback _callback : _callbacks) {
            _callback.onCreate(db);
          }
        }
      }

      @Override
      public void onOpen(@NonNull final SupportSQLiteDatabase db) {
        mDatabase = db;
        db.execSQL("PRAGMA foreign_keys = ON");
        internalInitInvalidationTracker(db);
        final List<? extends RoomDatabase.Callback> _callbacks = mCallbacks;
        if (_callbacks != null) {
          for (RoomDatabase.Callback _callback : _callbacks) {
            _callback.onOpen(db);
          }
        }
      }

      @Override
      public void onPreMigrate(@NonNull final SupportSQLiteDatabase db) {
        DBUtil.dropFtsSyncTriggers(db);
      }

      @Override
      public void onPostMigrate(@NonNull final SupportSQLiteDatabase db) {
      }

      @Override
      @NonNull
      public RoomOpenHelper.ValidationResult onValidateSchema(
          @NonNull final SupportSQLiteDatabase db) {
        final HashMap<String, TableInfo.Column> _columnsPolls = new HashMap<String, TableInfo.Column>(2);
        _columnsPolls.put("pollId", new TableInfo.Column("pollId", "TEXT", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsPolls.put("question", new TableInfo.Column("question", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysPolls = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesPolls = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoPolls = new TableInfo("polls", _columnsPolls, _foreignKeysPolls, _indicesPolls);
        final TableInfo _existingPolls = TableInfo.read(db, "polls");
        if (!_infoPolls.equals(_existingPolls)) {
          return new RoomOpenHelper.ValidationResult(false, "polls(com.example.offlinevotingapp.models.PollEntity).\n"
                  + " Expected:\n" + _infoPolls + "\n"
                  + " Found:\n" + _existingPolls);
        }
        final HashMap<String, TableInfo.Column> _columnsOptions = new HashMap<String, TableInfo.Column>(4);
        _columnsOptions.put("optionId", new TableInfo.Column("optionId", "TEXT", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsOptions.put("pollId", new TableInfo.Column("pollId", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsOptions.put("text", new TableInfo.Column("text", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsOptions.put("voteCount", new TableInfo.Column("voteCount", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysOptions = new HashSet<TableInfo.ForeignKey>(1);
        _foreignKeysOptions.add(new TableInfo.ForeignKey("polls", "CASCADE", "NO ACTION", Arrays.asList("pollId"), Arrays.asList("pollId")));
        final HashSet<TableInfo.Index> _indicesOptions = new HashSet<TableInfo.Index>(1);
        _indicesOptions.add(new TableInfo.Index("index_options_pollId", false, Arrays.asList("pollId"), Arrays.asList("ASC")));
        final TableInfo _infoOptions = new TableInfo("options", _columnsOptions, _foreignKeysOptions, _indicesOptions);
        final TableInfo _existingOptions = TableInfo.read(db, "options");
        if (!_infoOptions.equals(_existingOptions)) {
          return new RoomOpenHelper.ValidationResult(false, "options(com.example.offlinevotingapp.models.OptionEntity).\n"
                  + " Expected:\n" + _infoOptions + "\n"
                  + " Found:\n" + _existingOptions);
        }
        final HashMap<String, TableInfo.Column> _columnsVotes = new HashMap<String, TableInfo.Column>(5);
        _columnsVotes.put("voteId", new TableInfo.Column("voteId", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsVotes.put("pollId", new TableInfo.Column("pollId", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsVotes.put("optionId", new TableInfo.Column("optionId", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsVotes.put("deviceId", new TableInfo.Column("deviceId", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsVotes.put("userName", new TableInfo.Column("userName", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysVotes = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesVotes = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoVotes = new TableInfo("votes", _columnsVotes, _foreignKeysVotes, _indicesVotes);
        final TableInfo _existingVotes = TableInfo.read(db, "votes");
        if (!_infoVotes.equals(_existingVotes)) {
          return new RoomOpenHelper.ValidationResult(false, "votes(com.example.offlinevotingapp.models.VoteEntity).\n"
                  + " Expected:\n" + _infoVotes + "\n"
                  + " Found:\n" + _existingVotes);
        }
        final HashMap<String, TableInfo.Column> _columnsMyVotes = new HashMap<String, TableInfo.Column>(3);
        _columnsMyVotes.put("pollId", new TableInfo.Column("pollId", "TEXT", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsMyVotes.put("optionId", new TableInfo.Column("optionId", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsMyVotes.put("optionText", new TableInfo.Column("optionText", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysMyVotes = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesMyVotes = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoMyVotes = new TableInfo("my_votes", _columnsMyVotes, _foreignKeysMyVotes, _indicesMyVotes);
        final TableInfo _existingMyVotes = TableInfo.read(db, "my_votes");
        if (!_infoMyVotes.equals(_existingMyVotes)) {
          return new RoomOpenHelper.ValidationResult(false, "my_votes(com.example.offlinevotingapp.models.MyVoteEntity).\n"
                  + " Expected:\n" + _infoMyVotes + "\n"
                  + " Found:\n" + _existingMyVotes);
        }
        return new RoomOpenHelper.ValidationResult(true, null);
      }
    }, "3aae1e3a3e11bbe368b29ab620eff199", "461f0e05d48daa5bcd2c2e5ba4a86e85");
    final SupportSQLiteOpenHelper.Configuration _sqliteConfig = SupportSQLiteOpenHelper.Configuration.builder(config.context).name(config.name).callback(_openCallback).build();
    final SupportSQLiteOpenHelper _helper = config.sqliteOpenHelperFactory.create(_sqliteConfig);
    return _helper;
  }

  @Override
  @NonNull
  protected InvalidationTracker createInvalidationTracker() {
    final HashMap<String, String> _shadowTablesMap = new HashMap<String, String>(0);
    final HashMap<String, Set<String>> _viewTables = new HashMap<String, Set<String>>(0);
    return new InvalidationTracker(this, _shadowTablesMap, _viewTables, "polls","options","votes","my_votes");
  }

  @Override
  public void clearAllTables() {
    super.assertNotMainThread();
    final SupportSQLiteDatabase _db = super.getOpenHelper().getWritableDatabase();
    final boolean _supportsDeferForeignKeys = android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP;
    try {
      if (!_supportsDeferForeignKeys) {
        _db.execSQL("PRAGMA foreign_keys = FALSE");
      }
      super.beginTransaction();
      if (_supportsDeferForeignKeys) {
        _db.execSQL("PRAGMA defer_foreign_keys = TRUE");
      }
      _db.execSQL("DELETE FROM `polls`");
      _db.execSQL("DELETE FROM `options`");
      _db.execSQL("DELETE FROM `votes`");
      _db.execSQL("DELETE FROM `my_votes`");
      super.setTransactionSuccessful();
    } finally {
      super.endTransaction();
      if (!_supportsDeferForeignKeys) {
        _db.execSQL("PRAGMA foreign_keys = TRUE");
      }
      _db.query("PRAGMA wal_checkpoint(FULL)").close();
      if (!_db.inTransaction()) {
        _db.execSQL("VACUUM");
      }
    }
  }

  @Override
  @NonNull
  protected Map<Class<?>, List<Class<?>>> getRequiredTypeConverters() {
    final HashMap<Class<?>, List<Class<?>>> _typeConvertersMap = new HashMap<Class<?>, List<Class<?>>>();
    _typeConvertersMap.put(PollDao.class, PollDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(OptionDao.class, OptionDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(VoteDao.class, VoteDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(MyVoteDao.class, MyVoteDao_Impl.getRequiredConverters());
    return _typeConvertersMap;
  }

  @Override
  @NonNull
  public Set<Class<? extends AutoMigrationSpec>> getRequiredAutoMigrationSpecs() {
    final HashSet<Class<? extends AutoMigrationSpec>> _autoMigrationSpecsSet = new HashSet<Class<? extends AutoMigrationSpec>>();
    return _autoMigrationSpecsSet;
  }

  @Override
  @NonNull
  public List<Migration> getAutoMigrations(
      @NonNull final Map<Class<? extends AutoMigrationSpec>, AutoMigrationSpec> autoMigrationSpecs) {
    final List<Migration> _autoMigrations = new ArrayList<Migration>();
    return _autoMigrations;
  }

  @Override
  public PollDao pollDao() {
    if (_pollDao != null) {
      return _pollDao;
    } else {
      synchronized(this) {
        if(_pollDao == null) {
          _pollDao = new PollDao_Impl(this);
        }
        return _pollDao;
      }
    }
  }

  @Override
  public OptionDao optionDao() {
    if (_optionDao != null) {
      return _optionDao;
    } else {
      synchronized(this) {
        if(_optionDao == null) {
          _optionDao = new OptionDao_Impl(this);
        }
        return _optionDao;
      }
    }
  }

  @Override
  public VoteDao voteDao() {
    if (_voteDao != null) {
      return _voteDao;
    } else {
      synchronized(this) {
        if(_voteDao == null) {
          _voteDao = new VoteDao_Impl(this);
        }
        return _voteDao;
      }
    }
  }

  @Override
  public MyVoteDao myVoteDao() {
    if (_myVoteDao != null) {
      return _myVoteDao;
    } else {
      synchronized(this) {
        if(_myVoteDao == null) {
          _myVoteDao = new MyVoteDao_Impl(this);
        }
        return _myVoteDao;
      }
    }
  }
}
