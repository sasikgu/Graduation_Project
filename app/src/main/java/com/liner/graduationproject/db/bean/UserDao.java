package com.liner.graduationproject.db.bean;

import android.database.Cursor;
import android.database.sqlite.SQLiteStatement;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.Property;
import org.greenrobot.greendao.internal.DaoConfig;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.database.DatabaseStatement;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table "USER".
*/
public class UserDao extends AbstractDao<User, Long> {

    public static final String TABLENAME = "USER";

    /**
     * Properties of entity User.<br/>
     * Can be used for QueryBuilder and for referencing column names.
     */
    public static class Properties {
        public final static Property Id = new Property(0, Long.class, "id", true, "_id");
        public final static Property UserName = new Property(1, String.class, "userName", false, "USER_NAME");
        public final static Property Password = new Property(2, String.class, "password", false, "PASSWORD");
        public final static Property Age = new Property(3, String.class, "age", false, "AGE");
        public final static Property Sex = new Property(4, String.class, "sex", false, "SEX");
        public final static Property Birthday = new Property(5, String.class, "birthday", false, "BIRTHDAY");
        public final static Property Constellation = new Property(6, String.class, "constellation", false, "CONSTELLATION");
        public final static Property Residence = new Property(7, String.class, "residence", false, "RESIDENCE");
        public final static Property Introduce = new Property(8, String.class, "introduce", false, "INTRODUCE");
    }


    public UserDao(DaoConfig config) {
        super(config);
    }
    
    public UserDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    /** Creates the underlying database table. */
    public static void createTable(Database db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "\"USER\" (" + //
                "\"_id\" INTEGER PRIMARY KEY AUTOINCREMENT ," + // 0: id
                "\"USER_NAME\" TEXT," + // 1: userName
                "\"PASSWORD\" TEXT," + // 2: password
                "\"AGE\" TEXT," + // 3: age
                "\"SEX\" TEXT," + // 4: sex
                "\"BIRTHDAY\" TEXT," + // 5: birthday
                "\"CONSTELLATION\" TEXT," + // 6: constellation
                "\"RESIDENCE\" TEXT," + // 7: residence
                "\"INTRODUCE\" TEXT);"); // 8: introduce
    }

    /** Drops the underlying database table. */
    public static void dropTable(Database db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "\"USER\"";
        db.execSQL(sql);
    }

    @Override
    protected final void bindValues(DatabaseStatement stmt, User entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
 
        String userName = entity.getUserName();
        if (userName != null) {
            stmt.bindString(2, userName);
        }
 
        String password = entity.getPassword();
        if (password != null) {
            stmt.bindString(3, password);
        }
 
        String age = entity.getAge();
        if (age != null) {
            stmt.bindString(4, age);
        }
 
        String sex = entity.getSex();
        if (sex != null) {
            stmt.bindString(5, sex);
        }
 
        String birthday = entity.getBirthday();
        if (birthday != null) {
            stmt.bindString(6, birthday);
        }
 
        String constellation = entity.getConstellation();
        if (constellation != null) {
            stmt.bindString(7, constellation);
        }
 
        String residence = entity.getResidence();
        if (residence != null) {
            stmt.bindString(8, residence);
        }
 
        String introduce = entity.getIntroduce();
        if (introduce != null) {
            stmt.bindString(9, introduce);
        }
    }

    @Override
    protected final void bindValues(SQLiteStatement stmt, User entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
 
        String userName = entity.getUserName();
        if (userName != null) {
            stmt.bindString(2, userName);
        }
 
        String password = entity.getPassword();
        if (password != null) {
            stmt.bindString(3, password);
        }
 
        String age = entity.getAge();
        if (age != null) {
            stmt.bindString(4, age);
        }
 
        String sex = entity.getSex();
        if (sex != null) {
            stmt.bindString(5, sex);
        }
 
        String birthday = entity.getBirthday();
        if (birthday != null) {
            stmt.bindString(6, birthday);
        }
 
        String constellation = entity.getConstellation();
        if (constellation != null) {
            stmt.bindString(7, constellation);
        }
 
        String residence = entity.getResidence();
        if (residence != null) {
            stmt.bindString(8, residence);
        }
 
        String introduce = entity.getIntroduce();
        if (introduce != null) {
            stmt.bindString(9, introduce);
        }
    }

    @Override
    public Long readKey(Cursor cursor, int offset) {
        return cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0);
    }    

    @Override
    public User readEntity(Cursor cursor, int offset) {
        User entity = new User( //
            cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0), // id
            cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1), // userName
            cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2), // password
            cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3), // age
            cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4), // sex
            cursor.isNull(offset + 5) ? null : cursor.getString(offset + 5), // birthday
            cursor.isNull(offset + 6) ? null : cursor.getString(offset + 6), // constellation
            cursor.isNull(offset + 7) ? null : cursor.getString(offset + 7), // residence
            cursor.isNull(offset + 8) ? null : cursor.getString(offset + 8) // introduce
        );
        return entity;
    }
     
    @Override
    public void readEntity(Cursor cursor, User entity, int offset) {
        entity.setId(cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0));
        entity.setUserName(cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1));
        entity.setPassword(cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2));
        entity.setAge(cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3));
        entity.setSex(cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4));
        entity.setBirthday(cursor.isNull(offset + 5) ? null : cursor.getString(offset + 5));
        entity.setConstellation(cursor.isNull(offset + 6) ? null : cursor.getString(offset + 6));
        entity.setResidence(cursor.isNull(offset + 7) ? null : cursor.getString(offset + 7));
        entity.setIntroduce(cursor.isNull(offset + 8) ? null : cursor.getString(offset + 8));
     }
    
    @Override
    protected final Long updateKeyAfterInsert(User entity, long rowId) {
        entity.setId(rowId);
        return rowId;
    }
    
    @Override
    public Long getKey(User entity) {
        if(entity != null) {
            return entity.getId();
        } else {
            return null;
        }
    }

    @Override
    public boolean hasKey(User entity) {
        return entity.getId() != null;
    }

    @Override
    protected final boolean isEntityUpdateable() {
        return true;
    }
    
}
