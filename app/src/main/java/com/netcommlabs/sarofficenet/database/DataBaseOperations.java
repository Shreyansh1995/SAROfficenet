package com.netcommlabs.sarofficenet.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.netcommlabs.sarofficenet.model.AttendanceModelOffline;
import com.netcommlabs.sarofficenet.utils.LogUtils;

import java.util.ArrayList;

import static com.netcommlabs.sarofficenet.database.DataBaseTable.TableInfo.ID;
import static com.netcommlabs.sarofficenet.database.DataBaseTable.TableInfo.ID2;
import static com.netcommlabs.sarofficenet.database.DataBaseTable.TableInfo.JSON_REQUEST;
import static com.netcommlabs.sarofficenet.database.DataBaseTable.TableInfo.RESPONSE;
import static com.netcommlabs.sarofficenet.database.DataBaseTable.TableInfo.TYPE;
import static com.netcommlabs.sarofficenet.database.DataBaseTable.TableInfo.Table_NAME;
import static com.netcommlabs.sarofficenet.database.DataBaseTable.TableInfo.Table_NAME2;

public class DataBaseOperations extends SQLiteOpenHelper {
    SQLiteDatabase SQ;
    public static final int database_version = 1;

    public String CREATE_QUERY = "CREATE TABLE " + Table_NAME + "(" + ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + DataBaseTable.TableInfo.DATE + " TEXT," + DataBaseTable.TableInfo.TIME + " TEXT," + RESPONSE + " TEXT,"
            + TYPE + " TEXT );";

    public String CREATE_QUERY2 = "CREATE TABLE " + Table_NAME2 + "(" + ID2 + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + DataBaseTable.TableInfo.MODULE + " TEXT," + JSON_REQUEST + " TEXT );";


    public DataBaseOperations(Context context) {
        super(context, DataBaseTable.TableInfo.DATABASE_NAME, null, database_version);

    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_QUERY);
        db.execSQL(CREATE_QUERY2);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS " + Table_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + Table_NAME2);
        onCreate(db);

    }

    public void insertAttendance(DataBaseOperations dop, String date, String time, String request, String type) {
        SQ = dop.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(DataBaseTable.TableInfo.DATE, date);
        cv.put(DataBaseTable.TableInfo.TIME, time);
        cv.put(RESPONSE, request);
        cv.put(TYPE, type);
        long k = SQ.insert(Table_NAME, null, cv);
        LogUtils.showLog("column insert", "One row successfully");
    }

    public void insertRequest(DataBaseOperations dop, String module, String request) {
        SQ = dop.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(DataBaseTable.TableInfo.MODULE, module);
        cv.put(DataBaseTable.TableInfo.JSON_REQUEST, request);
        long k = SQ.insert(Table_NAME2, null, cv);
        LogUtils.showLog("column insert", "TWO row successfully");
    }

    public ArrayList<AttendanceModelOffline> getdata() {
        ArrayList<AttendanceModelOffline> data = new ArrayList<>();
        try {
            SQLiteDatabase db = this.getWritableDatabase();
            Cursor cursor = db.rawQuery("select * from " + Table_NAME2 + " ;", null);
            StringBuffer stringBuffer = new StringBuffer();
            AttendanceModelOffline attendanceModelOffline = null;
            while (cursor.moveToNext()) {
                attendanceModelOffline = new AttendanceModelOffline();
                String Module = cursor.getString(cursor.getColumnIndexOrThrow("module"));
                String Request = cursor.getString(cursor.getColumnIndexOrThrow("json_request"));
                String ID = cursor.getString(cursor.getColumnIndexOrThrow("id2"));
                attendanceModelOffline.setModule(Module);
                attendanceModelOffline.setRequest(Request);
                attendanceModelOffline.setID(ID);
                stringBuffer.append(attendanceModelOffline);
                data.add(attendanceModelOffline);
            }
        } catch (Exception ex) {
            LogUtils.showLog("Oreoexception", ex.toString());
        }
        return data;
    }

    public void DeleteRow(DataBaseOperations dop,String Id) {
        SQ = getWritableDatabase();
        SQ.delete(DataBaseTable.TableInfo.Table_NAME2, ID2 + " =?", new String[]{Id});
        SQ.delete(DataBaseTable.TableInfo.Table_NAME, ID + " =?", new String[]{Id});
    }

}
