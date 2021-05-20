package com.example.digicus.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import com.example.digicus.model.Details;

import androidx.annotation.Nullable;

public class DbHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "DiGicus.db";
    private static final int VERSION_NUMBER = 1;

    private static final String TABLE_FINANCE = "Finance";
    private static final String FINANCE_ID = "financeId";
    private static final String FINANCE_TITLE = "financeTitle";
    private static final String FINANCE_CURRENTBALANCE = "financeCurrentBalance";

    private static final String TABLE_DETAILS = "Details";
    private static final String DETAILS_ID = "detailsId";
    private static final String DETAILS_DETAILS = "detailsDetails";
    private static final String DETAILS_BALANCE = "detailsBalance";
    private static final String DETAILS_CREATED_AT = "detailsCreatedAt";
    private static final String DETAILS_TYPE = "detailsType";
    private static final String DETAILS_OPERAND1 = "detailsOperand1";
    private static final String DETAILS_OPERAND2 = "detailsOperand2";
    private static final String DETAILS_OPCODE = "detailsOpcode";

    private static final String CREATE_TABLE_FINANCE = "CREATE TABLE "+TABLE_FINANCE+
            "("+FINANCE_ID+" INTEGER PRIMARY KEY AUTOINCREMENT,"+FINANCE_TITLE+
            " VARCHAR(255),"+FINANCE_CURRENTBALANCE+" REAL);";
    private static final String CREATE_TABLE_DETAILS = "CREATE TABLE "+TABLE_DETAILS+
            "("+DETAILS_ID+" INTEGER PRIMARY KEY AUTOINCREMENT,"+DETAILS_DETAILS+
            " VARCHAR(255),"+DETAILS_TYPE+" VARCHAR(255),"+DETAILS_OPERAND1+" REAL,"+DETAILS_OPERAND2+
            " REAL,"+DETAILS_OPCODE+" VARCHAR(255),"+DETAILS_BALANCE+" REAL,"+DETAILS_CREATED_AT+
            " VARCHAR(255),"+FINANCE_ID+" INTEGER );";


    private static final String DROP_TABLE_FINANCE = "DROP TABLE IF EXISTS "+TABLE_FINANCE;
    private static final String DROP_TABLE_DETAILS = "DROP TABLE IF EXISTS "+TABLE_DETAILS;


    private static final String QUERY_FETCH_ALLFINANCE = "SELECT * FROM "+TABLE_FINANCE;

    private final Context context;


    public DbHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, VERSION_NUMBER);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        try {
            db.execSQL(CREATE_TABLE_FINANCE);
            db.execSQL(CREATE_TABLE_DETAILS);
        }catch (Exception e)
        {
            Toast.makeText(context,"Exception : "+e, Toast.LENGTH_LONG).show();
        }

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        try {
            db.execSQL(DROP_TABLE_DETAILS);
            db.execSQL(DROP_TABLE_FINANCE);
            onCreate(db);
        }catch (Exception e)
        {
            Toast.makeText(context,"Exception : "+e,Toast.LENGTH_LONG).show();
        }
    }



    public Cursor FetchAllFinance(){
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        return sqLiteDatabase.rawQuery(QUERY_FETCH_ALLFINANCE,null);
    }


    public boolean CheckTitle(String title){
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery(QUERY_FETCH_ALLFINANCE,null);
        boolean result = false;
        if(cursor.getCount()>0){
            while (cursor.moveToNext()){
                String databaseTitle = cursor.getString(1);
                if(title.equals(databaseTitle)){
                    result = true;
                    break;
                }
            }
        }
        cursor.close();
        return result;
    }


    public long InsertFinance(String title) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(FINANCE_TITLE,title);
        contentValues.put(FINANCE_CURRENTBALANCE,0.0);

        return sqLiteDatabase.insert(TABLE_FINANCE,null,contentValues);
    }


    public long InsertDetails(String details,String balancetype,double op1,double op2,
                              String opcode,double balance, String createdat,long financeid) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(DETAILS_DETAILS,details);
        contentValues.put(DETAILS_TYPE,balancetype);
        contentValues.put(DETAILS_OPERAND1,op1);
        contentValues.put(DETAILS_OPERAND2,op2);
        contentValues.put(DETAILS_OPCODE,opcode);
        contentValues.put(DETAILS_BALANCE,balance);
        contentValues.put(DETAILS_CREATED_AT,createdat);
        contentValues.put(FINANCE_ID,financeid);

        return sqLiteDatabase.insert(TABLE_DETAILS,null,contentValues);
    }



    public long FetchFinanceIdByTitle(String title){
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery(QUERY_FETCH_ALLFINANCE,null);
        long result = 0;
        if(cursor.getCount()>0){
            while (cursor.moveToNext()){
                String databaseTitle = cursor.getString(1);
                if(title.equals(databaseTitle)){
                    result = Long.parseLong(cursor.getString(0));
                    break;
                }
            }
        }
        cursor.close();
        return result;
    }

    public String FetchFinanceTitleByFinanceID(long id){
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery(QUERY_FETCH_ALLFINANCE,null);
        String result = null;
        if(cursor.getCount()>0){
            while (cursor.moveToNext()){
                long databaseid = Long.parseLong(cursor.getString(0));
                if(databaseid==id){
                    result = cursor.getString(1);
                    break;
                }
            }
        }
        cursor.close();
        return result;
    }



    public Cursor FetchAllDetailsByFinanceId(long id){
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        String Id = String.valueOf(id);
        String[] params = new String[]{ Id };

        return sqLiteDatabase.query(TABLE_DETAILS,null,
                FINANCE_ID+" = ?",params,
                null,null,null);
    }

    public double FetchBalanceByFinanceId(long id){
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();

        Cursor cursor = sqLiteDatabase.rawQuery(QUERY_FETCH_ALLFINANCE,null);
        double result = 0;
        if(cursor.getCount()>0){
            while (cursor.moveToNext()){
                long financeid = Long.parseLong(cursor.getString(0));
                if(id==financeid){
                    result = Double.parseDouble(cursor.getString(2));
                    break;
                }
            }
        }
        cursor.close();
        return result;
    }

    public long FetchFinanceIdByDetailsId(long id){
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        String Id = String.valueOf(id);
        String[] params = new String[]{ Id };

        Cursor cursor = sqLiteDatabase.query(TABLE_DETAILS,null,
                DETAILS_ID+" = ?",params,
                null,null,null);
        long financeid = Long.parseLong(cursor.getString(8));
        cursor.close();
        return financeid;
    }


    public Cursor FetchDetailByDetailId(long id){
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        String Id = String.valueOf(id);
        String[] params = new String[]{ Id };

        return sqLiteDatabase.query(TABLE_DETAILS,null,
                DETAILS_ID+" = ?",params,
                null,null,null);
    }


    public boolean UpdateFinanceBalanceById(long id,double balance){
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        String Id = String.valueOf(id);
        ContentValues contentValues = new ContentValues();
        contentValues.put(FINANCE_CURRENTBALANCE,balance);

        long result = sqLiteDatabase.update(TABLE_FINANCE,contentValues,FINANCE_ID+" = ?",new String[] {Id});
        return result > 0;
    }


    public boolean UpdateFinanceTitleById(long id,String title){
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        String Id = String.valueOf(id);
        ContentValues contentValues = new ContentValues();
        contentValues.put(FINANCE_TITLE,title);

        long result = sqLiteDatabase.update(TABLE_FINANCE,contentValues,FINANCE_ID+" = ?",new String[] {Id});
        return result > 0;
    }

    public boolean UpdateDetailsById(long id,String details,String detailstype,double op1,double op2,
                                     String opcode,double balance){
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        String Id = String.valueOf(id);
        ContentValues contentValues = new ContentValues();
        contentValues.put(DETAILS_DETAILS,details);
        contentValues.put(DETAILS_TYPE,detailstype);
        contentValues.put(DETAILS_OPERAND1,op1);
        contentValues.put(DETAILS_OPERAND2,op2);
        contentValues.put(DETAILS_OPCODE,opcode);
        contentValues.put(DETAILS_BALANCE,balance);

        long result = sqLiteDatabase.update(TABLE_DETAILS,contentValues,DETAILS_ID+" = ?",new String[] {Id});
        return result > 0;
    }



    public int DeleteDetailsById(long id){
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        String Id = String.valueOf(id);
        String[] params = new String[]{ Id };

        Cursor cursor = sqLiteDatabase.query(TABLE_DETAILS,null,
                DETAILS_ID+" = ?",params,
                null,null,null);

        if(cursor.getCount()>0){
            while (cursor.moveToNext()){
                double detailsbalance = Double.parseDouble(cursor.getString(6));
                String detailstype = cursor.getString(2);
                long financeid = Long.parseLong(cursor.getString(8));
                if(detailstype.equals("Cash In")){
                    double currentBalance = FetchBalanceByFinanceId(financeid);
                    currentBalance-=detailsbalance;
                    UpdateFinanceBalanceById(financeid,currentBalance);
                }else{
                    double currentBalance = FetchBalanceByFinanceId(financeid);
                    currentBalance+=detailsbalance;
                    UpdateFinanceBalanceById(financeid,currentBalance);
                }
            }
        }
        cursor.close();

        return sqLiteDatabase.delete(TABLE_DETAILS,DETAILS_ID+" = ?",new String[] {Id});
    }

    public boolean DeleteAllDetailsByFinanceId(long id){
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        String Id = String.valueOf(id);
        String[] params = new String[]{ Id };
        boolean flag = false;

        Cursor cursor = sqLiteDatabase.query(TABLE_DETAILS,null,
                FINANCE_ID+" = ?",params,
                null,null,null);

        if(cursor.getCount()>0){
            while (cursor.moveToNext()){
                long detailsid = Long.parseLong(cursor.getString(0));
                sqLiteDatabase.delete(TABLE_DETAILS,DETAILS_ID+" = ?",new String[] {String.valueOf(detailsid)});
                flag = true;
            }
        }
        cursor.close();
        return flag;
    }

    public int DeleteFinanceById(long id){
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        String Id = String.valueOf(id);
        String[] params = new String[]{ Id };

        Cursor cursor = sqLiteDatabase.query(TABLE_DETAILS,null,
                FINANCE_ID+" = ?",params,
                null,null,null);

        if(cursor.getCount()>0){
            while (cursor.moveToNext()){
                long detailsid = Long.parseLong(cursor.getString(0));
                sqLiteDatabase.delete(TABLE_DETAILS,DETAILS_ID+" = ?",new String[] {String.valueOf(detailsid)});
            }
        }

        cursor.close();
        return sqLiteDatabase.delete(TABLE_FINANCE,FINANCE_ID+" = ?",new String[] {Id});
    }
}
