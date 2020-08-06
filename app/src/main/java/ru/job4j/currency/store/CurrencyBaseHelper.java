package ru.job4j.currency.store;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import ru.job4j.currency.Currency;
import ru.job4j.currency.Rates;

public class CurrencyBaseHelper extends SQLiteOpenHelper {
    public static final String DB = "tourist_marks.db";
    public static final int VERSION = 1;
    private Currency currency;
    private static CurrencyBaseHelper INST;

    public CurrencyBaseHelper(@Nullable Context context) {
        super(context, DB, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(
                "create table " + CurrencyDbSchema.CurrencyTable.NAME + " (" +
                        "id integer primary key autoincrement, " +
                        CurrencyDbSchema.CurrencyTable.Cols.BASE + " text, " +
                        CurrencyDbSchema.CurrencyTable.Cols.DATE + " text, " +
                        CurrencyDbSchema.CurrencyTable.Cols.USD + " real, " +
                        CurrencyDbSchema.CurrencyTable.Cols.EUR + " real, " +
                        CurrencyDbSchema.CurrencyTable.Cols.AUD + " real, " +
                        CurrencyDbSchema.CurrencyTable.Cols.GDP + " real " +
                        ")"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

    public static CurrencyBaseHelper getInstance(Context context) {
        if (INST == null){
            INST = new CurrencyBaseHelper(context);
            INST.updateCurrency();
        }
        return INST;
    }

    private void updateCurrency() {
        Cursor cursor = this.getWritableDatabase().query(
                CurrencyDbSchema.CurrencyTable.NAME,
                null,
                null, null,
                null, null, null
        );
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            currency = new Currency(
                    cursor.getString(cursor.getColumnIndex(CurrencyDbSchema.CurrencyTable.Cols.BASE)),
                    cursor.getString(cursor.getColumnIndex(CurrencyDbSchema.CurrencyTable.Cols.DATE)),
                    new Rates(cursor.getDouble(cursor.getColumnIndex(CurrencyDbSchema.CurrencyTable.Cols.USD)),
                            cursor.getDouble(cursor.getColumnIndex(CurrencyDbSchema.CurrencyTable.Cols.EUR)),
                            cursor.getDouble(cursor.getColumnIndex(CurrencyDbSchema.CurrencyTable.Cols.AUD)),
                            cursor.getDouble(cursor.getColumnIndex(CurrencyDbSchema.CurrencyTable.Cols.GDP))
                            )
            );
            cursor.moveToNext();
        }
        cursor.close();
    }

    public Currency getCurrency() {
        return this.currency;
    }

    public void addCurrency(Currency currency) {
        this.getWritableDatabase().delete(CurrencyDbSchema.CurrencyTable.NAME, null, null);
        this.currency = currency;
        ContentValues value = new ContentValues();
        value.put(CurrencyDbSchema.CurrencyTable.Cols.BASE, currency.getBase());
        value.put(CurrencyDbSchema.CurrencyTable.Cols.DATE, currency.getDate());
        value.put(CurrencyDbSchema.CurrencyTable.Cols.USD, currency.getRates().getUsd());
        value.put(CurrencyDbSchema.CurrencyTable.Cols.EUR, currency.getRates().getEur());
        value.put(CurrencyDbSchema.CurrencyTable.Cols.AUD, currency.getRates().getAud());
        value.put(CurrencyDbSchema.CurrencyTable.Cols.GDP, currency.getRates().getGbp());
        this.getWritableDatabase().insert(CurrencyDbSchema.CurrencyTable.NAME, null, value);
    }
}
