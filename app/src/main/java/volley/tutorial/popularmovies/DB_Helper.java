package volley.tutorial.popularmovies;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by OPEYEMI OLORUNLEKE on 5/30/2017.
 */

public class DB_Helper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "movies.db";
    private static final int DATABASE_VERSION = 1;


    public DB_Helper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        final String SQL_MOVIE_CREATE_TABLE =
                "CREATE TABLE "
                +MovieContract.MovieEntry.TABLE_NAME
                +" ("
                + MovieContract.MovieEntry._ID
                +" INTEGER PRIMARY KEY AUTOINCREMENT, "
                + MovieContract.MovieEntry.COLUMN_TITLE+ " TEXT NOT NULL, "
                + MovieContract.MovieEntry.COLUMN_MOVIE_ID+" INTEGER NOT NULL, "
                + MovieContract.MovieEntry.COLUMN_OVERVIEW+ " TEXT NOT NULL, "
                + MovieContract.MovieEntry.COLUMN_POSTER_PATH+ " TEXT NOT NULL, "
                + MovieContract.MovieEntry.COLUMN_RELEASE_DATE+ " TEXT NOT NULL, "
                + MovieContract.MovieEntry.COLUMN_VOTE_AVERAGE+ " TEXT NOT NULL, "
                + MovieContract.MovieEntry.COLUMN_VOTE_COUNT+ " INTEGER NOT NULL);";

        db.execSQL(SQL_MOVIE_CREATE_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+MovieContract.MovieEntry.TABLE_NAME);
        onCreate(db);
    }
}