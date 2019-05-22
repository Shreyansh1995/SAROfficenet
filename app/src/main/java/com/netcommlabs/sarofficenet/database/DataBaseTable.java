package com.netcommlabs.sarofficenet.database;

import android.provider.BaseColumns;

public class DataBaseTable {
    public static abstract class TableInfo implements BaseColumns {
        public final static String DATABASE_NAME = "officenet_db";

        public final static String Table_NAME = "punch_in_out";
        public final static String ID = "id";
        public final static String DATE = "date";
        public final static String TIME = "time";
        public final static String RESPONSE = "request";
        public final static String TYPE = "type";

        public final static String Table_NAME2 = "offline_attendace";
        public final static String ID2 = "id2";
        public final static String MODULE = "module";
        public final static String JSON_REQUEST = "json_request";


    }
}
