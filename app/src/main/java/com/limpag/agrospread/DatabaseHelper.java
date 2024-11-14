package com.limpag.agrospread;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "agrospread.db";
    private static final int DATABASE_VERSION = 1;

    private static final String TABLE_PRODUCTS = "products";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_NAME = "name";
    private static final String COLUMN_PRICE = "price";
    private static final String COLUMN_TYPE = "type";
    private static final String COLUMN_DESCRIPTION = "description";
    private static final String COLUMN_IMAGE_URI = "image_uri";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_PRODUCTS_TABLE = "CREATE TABLE " + TABLE_PRODUCTS + "("
                + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COLUMN_NAME + " TEXT,"
                + COLUMN_PRICE + " TEXT,"
                + COLUMN_TYPE + " TEXT,"
                + COLUMN_DESCRIPTION + " TEXT,"
                + COLUMN_IMAGE_URI + " TEXT" + ")";
        db.execSQL(CREATE_PRODUCTS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PRODUCTS);
        onCreate(db);
    }

    // Add a product
    public void addProduct(String name, String price, String type, String desc, String imageUri) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME, name);
        values.put(COLUMN_PRICE, price);
        values.put(COLUMN_TYPE, type);
        values.put(COLUMN_DESCRIPTION, desc);
        values.put(COLUMN_IMAGE_URI, imageUri);
        db.insert(TABLE_PRODUCTS, null, values);
        db.close();
    }

    // Fetch all products
    // DatabaseHelper class
    public List<Product> getAllProducts() {
        List<Product> products = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + TABLE_PRODUCTS;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                // Ensure the column exists using getColumnIndex
                int nameIndex = cursor.getColumnIndex(COLUMN_NAME);
                int priceIndex = cursor.getColumnIndex(COLUMN_PRICE);
                int typeIndex = cursor.getColumnIndex(COLUMN_TYPE);
                int descriptionIndex = cursor.getColumnIndex(COLUMN_DESCRIPTION);
                int imageUriIndex = cursor.getColumnIndex(COLUMN_IMAGE_URI);

                // Check if columns are found
                if (nameIndex != -1 && priceIndex != -1 && typeIndex != -1 && descriptionIndex != -1 && imageUriIndex != -1) {
                    Product product = new Product(
                            cursor.getString(nameIndex),
                            cursor.getString(priceIndex),
                            cursor.getString(typeIndex),
                            cursor.getString(descriptionIndex),
                            cursor.getString(imageUriIndex)
                    );
                    products.add(product);
                } else {
                    // Log an error or throw an exception if required
                    // This happens if the column is missing or misnamed
                }
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return products;
    }
}
