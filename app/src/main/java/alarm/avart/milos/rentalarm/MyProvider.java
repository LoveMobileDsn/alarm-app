package alarm.avart.milos.rentalarm;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.text.TextUtils;

import java.util.HashMap;

/**
 * Created by Milos-PC on 8/2/2016.
 */
public class MyProvider extends android.content.ContentProvider {

    static final String PROVIDER_NAME = "alarm.avart.milos.rentalarm.MyProvider";
    static final String URL = "content://" + PROVIDER_NAME + "/cte";
    static final Uri CONTENT_URI = Uri.parse(URL);

    //------schema-----
    static final String _ID = "_id";
    static final String PAYNAME = "pay_name";
    static final String PAYDATE = "pay_day";
    static final String PAYAMOUNT = "pay_amount";
    static final String PAYMETHOD = "pay_method";
    static final String PAYCONTRACTSTARTDAY = "pay_contract_startday";
    static final String PAYCONTRACTENDDAY = "pay_contract_endday";
    static final String PAYCOUNT = "pay_count";



    static final int ALARM_LIST = 1;
    static final int ALARM_LIST_ID = 2;

    private static final UriMatcher uriMatcher;
    private static HashMap<String, String> values;
    static
    {
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(PROVIDER_NAME, "cte", ALARM_LIST);
        uriMatcher.addURI(PROVIDER_NAME, "cte/#", ALARM_LIST_ID);
    }

    SQLiteDatabase alarmDB;

    //---------------database name---------------------
    static final String DATABASE_NAME = "alarmDB";
    //------table name-------
    static final String DATABASE_TABLE = "alarm_list";
    static final int DATABASE_VERSION = 1;

    //--------------------tableCreat--------------------
    static final String DATABASE_CREATE = "create table " + DATABASE_TABLE
            + " (_id integer primary key autoincrement, "
            + "pay_name text, pay_day text, "
            + "pay_amount text, pay_method text, pay_contract_startday text, pay_contract_endday text, pay_count text);";

    public static String[] colums_contacts = {_ID, PAYNAME, PAYDATE, PAYAMOUNT, PAYMETHOD, PAYCONTRACTSTARTDAY, PAYCONTRACTENDDAY, PAYCOUNT};

    private static class DatabaseHelper extends SQLiteOpenHelper {
        DatabaseHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }


        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(DATABASE_CREATE);
        }


        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("DROP TABLE IF EXISTS titles");
            onCreate(db);

        }
    }

    @Override
    public int delete(Uri url, String selection, String[] selectionArgs) {
        int count = 0;
        switch (uriMatcher.match(url)) {
            case ALARM_LIST:
                count = alarmDB.delete(DATABASE_TABLE, selection, selectionArgs);
                break;
            case ALARM_LIST_ID:
                String id = url.getPathSegments().get(1);
                count = alarmDB.delete(DATABASE_TABLE, _ID + " = " + id
                        + (!TextUtils.isEmpty(selection) ? " AND (" + selection + ')' : ""), selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("Unknown URI " + url);		}
        getContext().getContentResolver().notifyChange(url, null);

        return count;
    }

    @Override
    public String getType(Uri uri) {
        switch (uriMatcher.match(uri)) {
            // ---get all books---
            case ALARM_LIST:
                return "vnd.android.cursor.item/vndcom.example.peoples.contacts";
            // ---get a particular book---
            case ALARM_LIST_ID:
                return "vnd.android.cursor.item/vndcom.example.peoples.contacts ";
            default:
                throw new IllegalArgumentException("Unsupported URI: " + uri);
        }
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        long rowID = alarmDB.insert(DATABASE_TABLE, "", values);
        // ---if added successfully---
        if (rowID > 0) {
            Uri _uri = ContentUris.withAppendedId(CONTENT_URI, rowID);
            getContext().getContentResolver().notifyChange(_uri, null);
            return _uri;

        }
        throw new SQLException("Failed to insert row into " + uri);

    }

    @Override
    public boolean onCreate() {
        Context context = getContext();
        DatabaseHelper dbHelper = new DatabaseHelper(context);
        alarmDB = dbHelper.getWritableDatabase();
        return (alarmDB == null) ? false : true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {
        SQLiteQueryBuilder sqlBuilder = new SQLiteQueryBuilder();
        sqlBuilder.setTables(DATABASE_TABLE);
        if (uriMatcher.match(uri) == ALARM_LIST_ID)
            // ---if getting a particular book---
            sqlBuilder.appendWhere(_ID + " = " + uri.getPathSegments().get(1));
        if (sortOrder == null || sortOrder == "")
            sortOrder = PAYNAME;

        Cursor c = sqlBuilder.query(alarmDB, projection, selection,
                selectionArgs, null, null, sortOrder);
        // ---register to watch a content URI for changes---

        c.setNotificationUri(getContext().getContentResolver(), uri);
        return c;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {
        int count = 0;
        switch (uriMatcher.match(uri)) {
            case ALARM_LIST:
                count = alarmDB.update(DATABASE_TABLE, values, selection,
                        selectionArgs);
                break;
            case ALARM_LIST_ID:
                count = alarmDB.update(
                        DATABASE_TABLE,
                        values,
                        _ID
                                + " = "
                                + uri.getPathSegments().get(1)
                                + (!TextUtils.isEmpty(selection) ? " AND ("
                                + selection + ')' : ""), selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return count;
    }

    public Cursor fetchAllCountries() {

        Cursor mCursor = alarmDB.query(DATABASE_TABLE, new String[] {PAYNAME,
                        PAYDATE, PAYAMOUNT, PAYMETHOD, PAYCONTRACTSTARTDAY, PAYCONTRACTENDDAY, PAYCOUNT},
                null, null, null, null, null);

        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;
    }




}
