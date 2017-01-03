package com.wandell.rich.reactblocks.Helpers;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.Nullable;
import android.util.Log;

import static com.wandell.rich.reactblocks.State.TAG;

public class DBOpenHelper extends SQLiteOpenHelper {

    private static final int LOWEST_VERSION = 12;
    private static final int DATABASE_VERSION = LOWEST_VERSION;
    private static final String DATABASE_NAME = "data.db";
    private static final String CREATE_TABLE_PLAYER = "CREATE TABLE `player` ( `id` INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, `name` TEXT NOT NULL DEFAULT 'Player 1', `g_play_id` TEXT DEFAULT '' )";
    private static final String CREATE_TABLE_SCORE = "CREATE TABLE `score` ( `p_id` INTEGER NOT NULL, `points` INTEGER NOT NULL, foreign key (p_id) REFERENCES player(id) )";

    private Context context;

    public DBOpenHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_PLAYER);
        db.execSQL(CREATE_TABLE_SCORE);
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.d(TAG, "DBOpenHelper.onUpgrade");
        Log.d(TAG, "upgrading database - old: " + Integer.toString(oldVersion) +
                " new: " + Integer.toString(newVersion));

        if(oldVersion < LOWEST_VERSION){
            dropAndRecreate(db);
        }
        logAllTables(db);
    }

    private void dropAndRecreate(SQLiteDatabase db){
        Log.d(TAG, "DBOpenHelper.dropAndRecreate");
        db.execSQL("drop table if exists player;");
        db.execSQL("drop table if exists score;");
        onCreate(db);
    }

    private void logAllTables(SQLiteDatabase db){
        try (Cursor cursor = db.query("SQLITE_MASTER", new String[]{
                "type", "name"
        }, null, null, null, null, null)) {
            while(cursor.moveToNext()) {
                String type = cursor.getString(0);
                String name = cursor.getString(1);
                Log.d("richd", "Existing table: " + name);
                cursor.moveToNext();
            }
        } catch (Exception ignored) {}
    }

    private DevicePlayer getDefaultDevicePlayer(){
        Log.d(TAG, "DBOpenHelper.getDefaultDevicePlayer");
        DBOpenHelper dbOpenHelper = new DBOpenHelper(context);
        SQLiteDatabase db = dbOpenHelper.getReadableDatabase();
        Cursor c = db.rawQuery("select id, name, g_play_id from player limit 1;", null);
        if (c.moveToFirst()) {
            int id = c.getInt(0);
            String name = c.getString(1);
            String g_play_id = c.getString(2);
            c.close();
            db.close();
            return new DevicePlayer(id, name, g_play_id);
        }
        db.rawQuery("insert into player (`id`, `name`) values (null, '');", null);
        c.close();
        c = db.rawQuery("select last_insert_rowid();", null);
        if(c.moveToFirst()){
            int id = c.getInt(0);
            if(id == 0){
                db.rawQuery("delete from player where id = 0;", null);
                db.rawQuery("insert into player (`id`, `name`) values (null, '');", null);
                id = 1;
            }
            c.close();
            db.close();
            return new DevicePlayer(id, "", "");
        }
        c.close();
        db.close();
        return null;
    }

    private DevicePlayer getDevicePlayerById(int id){
        Log.d(TAG, "DBOpenHelper.getDevicePlayerById");
        DBOpenHelper dbOpenHelper = new DBOpenHelper(context);
        SQLiteDatabase db = dbOpenHelper.getReadableDatabase();
        Cursor c = db.rawQuery("select id, name, g_play_id from player where id = ?;",
                new String[]{Integer.toString(id)});
        if(c.moveToFirst()){
            String name = c.getString(1);
            String g_play_id = c.getString(2);
            c.close();
            db.close();
            return new DevicePlayer(id, name, g_play_id);
        }
        c.close();
        db.close();
        return null;
    }

    private DevicePlayer getDevicePlayerByGID(String g_play_id){
        Log.d(TAG, "DBOpenHelper.getDevicePlayerByGID");
        DBOpenHelper dbOpenHelper = new DBOpenHelper(context);
        SQLiteDatabase db = dbOpenHelper.getReadableDatabase();
        Cursor c = db.rawQuery("select id, name, g_play_id from player where g_play_id = ?;",
                new String[]{g_play_id});
        if(c.moveToFirst()){
            int id = c.getInt(0);
            String name = c.getString(1);
            c.close();
            db.close();
            return new DevicePlayer(id, name, g_play_id);
        }
        c.close();
        db.close();
        return null;
    }

    public DevicePlayer getDevicePlayer(int id, @Nullable String g_id){
        Log.d(TAG, "DBOpenHelper.getDevicePlayer");
        try {
            if (id == 0 && g_id == null) {
                return getDefaultDevicePlayer();
            } else if (g_id == null) {
                return getDevicePlayerById(id);
            }
            return getDevicePlayerByGID(g_id);
        }catch(Exception e){
            e.printStackTrace();
        }
        return null;
    }

    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }

    public class DevicePlayer{
        private final int id;
        private final String name;
        private String gPlayId;

        DevicePlayer(int id, String name, String gPlayId){
            this.id = id;
            this.name = name;
            this.gPlayId = gPlayId;
        }

        public String getName() {
            return name;
        }

        public int getId() {
            return id;
        }

        public String getgPlayId() {
            return gPlayId;
        }
    }
}
