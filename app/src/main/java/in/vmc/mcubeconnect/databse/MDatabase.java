package in.vmc.mcubeconnect.databse;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;

import java.util.ArrayList;

import in.vmc.mcubeconnect.model.VisitData;

/**
 * Created by mukesh on 14/3/16.
 */
public class MDatabase {
    private SiteHelper mHelper;
    private SQLiteDatabase mDatabase;

    public MDatabase(Context context) {
        mHelper = new SiteHelper(context);
        mDatabase = mHelper.getWritableDatabase();
    }

    public void insertAllSites(ArrayList<VisitData> listMovies, boolean clearPrevious) {
        if (clearPrevious) {
            deleteMovies();
        }
        String sql = "INSERT INTO " + SiteHelper.ALL_SITES + " VALUES (?,?,?,?,?,?,?,?,?,?);";
        SQLiteStatement statement = mDatabase.compileStatement(sql);
        mDatabase.beginTransaction();
        for (int i = 0; i < listMovies.size(); i++) {
            VisitData visitData = listMovies.get(i);
            statement.clearBindings();
            statement.bindString(2, visitData.getSitename());
            statement.bindString(3, visitData.getSiteid());
            statement.bindString(4, visitData.getSiteicon());
            statement.bindString(5, visitData.getNumber());
            statement.bindString(6, visitData.getOffer());
            statement.bindString(7, visitData.getBid());
            statement.bindString(8, visitData.getOffer_desc());
            statement.bindString(9, visitData.getSitedesc());
            statement.bindString(10, visitData.isLike() ? "1" : "0");
            statement.execute();
        }
        //set the transaction as successful and end the transaction
        //    L.m("inserting entries " + listMovies.size() + new Date(System.currentTimeMillis()));
        mDatabase.setTransactionSuccessful();
        mDatabase.endTransaction();
    }


    public void deleteMovies() {
        mDatabase.delete(SiteHelper.ALL_SITES, null, null);
    }


    public ArrayList<VisitData> getAllSites() {
        ArrayList<VisitData> listMovies = new ArrayList<>();

        String[] columns = {SiteHelper.COLUMN_UID,
                SiteHelper.COLUMN_SITENAME,
                SiteHelper.COLUMN_SITE_ID,
                SiteHelper.COLUMN_SITEICON,
                SiteHelper.COLUMN_NUMBER,
                SiteHelper.COLUMN_OFFER,
                SiteHelper.COLUMN_BID,
                SiteHelper.COLUMN_OFFER_DESC,
                SiteHelper.COLUMN_LIKE,
                SiteHelper.COLUMN_SITE_DESC,

        };
        Cursor cursor = mDatabase.query(SiteHelper.ALL_SITES, columns, null, null, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            // L.m("loading entries " + cursor.getCount() + new Date(System.currentTimeMillis()));
            do {

                VisitData visitData = new VisitData();
                visitData.setSitename(cursor.getString(cursor.getColumnIndex(SiteHelper.COLUMN_SITENAME)));
                visitData.setSiteid(cursor.getString(cursor.getColumnIndex(SiteHelper.COLUMN_SITE_ID)));
                visitData.setSiteicon(cursor.getString(cursor.getColumnIndex(SiteHelper.COLUMN_SITEICON)));
                visitData.setNumber(cursor.getString(cursor.getColumnIndex(SiteHelper.COLUMN_NUMBER)));
                visitData.setOffer(cursor.getString(cursor.getColumnIndex(SiteHelper.COLUMN_OFFER)));
                visitData.setBid(cursor.getString(cursor.getColumnIndex(SiteHelper.COLUMN_BID)));
                visitData.setOffer_desc(cursor.getString(cursor.getColumnIndex(SiteHelper.COLUMN_OFFER_DESC)));
                visitData.setSitedesc(cursor.getString(cursor.getColumnIndex(SiteHelper.COLUMN_SITE_DESC)));
                visitData.setLike(cursor.getString(cursor.getColumnIndex(SiteHelper.COLUMN_LIKE)).equals("1") ? true : false);

                listMovies.add(visitData);
            }
            while (cursor.moveToNext());
        }
        return listMovies;
    }


    private static class SiteHelper extends SQLiteOpenHelper {
        public static final String ALL_SITES = "all_sites";
        public static final String COLUMN_UID = "_id";
        public static final String COLUMN_SITENAME = "sitename";
        public static final String COLUMN_SITE_ID = "siteid";
        public static final String COLUMN_SITEICON = "siteicon";
        public static final String COLUMN_NUMBER = "number";
        public static final String COLUMN_OFFER = "offer";
        public static final String COLUMN_BID = "bid";
        public static final String COLUMN_OFFER_DESC = "offerdesc";
        public static final String COLUMN_LIKE = "like";
        public static final String COLUMN_SITE_DESC = "sitedesc";

        private static final String CREATE_TABLE_ALL_SITES = "CREATE TABLE " + ALL_SITES + " (" +
                COLUMN_UID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                COLUMN_SITENAME + " TEXT," +
                COLUMN_SITE_ID + " TEXT," +
                COLUMN_SITEICON + " TEXT," +
                COLUMN_NUMBER + " TEXT," +
                COLUMN_OFFER + " TEXT," +
                COLUMN_BID + " TEXT," +
                COLUMN_OFFER_DESC + " TEXT," +
                COLUMN_SITE_DESC + " TEXT," +
                COLUMN_LIKE + " TEXT" +
                ");";

        private static final String DB_NAME = "allsites_db";
        private static final int DB_VERSION = 4;

        private Context mContext;

        public SiteHelper(Context context) {
            super(context, DB_NAME, null, DB_VERSION);
            mContext = context;
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            try {
                db.execSQL(CREATE_TABLE_ALL_SITES);
                // L.m("create table box office executed");
            } catch (SQLiteException exception) {
                // L.t(mContext, exception + "");
            }
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            try {
                // L.m("upgrade table box office executed");

                db.execSQL(" DROP TABLE " + ALL_SITES + " IF EXISTS;");
                onCreate(db);
            } catch (SQLiteException exception) {
                //  L.t(mContext, exception + "");
            }
        }

    }
}