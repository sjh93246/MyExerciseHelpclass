package com.example.myexercisehelpclass;


import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    EditText editText;
    EditText editText2;
    TextView textView;

    EditText editText3;
    EditText editText4;
    EditText editText5;


    SQLiteDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editText = findViewById(R.id.editText);
        editText2 = findViewById(R.id.editText2);
        textView = findViewById(R.id.textView);

        editText3 = findViewById(R.id.editText3);
        editText4 = findViewById(R.id.editText4);
        editText5 = findViewById(R.id.editText5);

        Button button = findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String databaseName = editText.getText().toString();
                createDatabase(databaseName);
            }
        });

        Button button2 = findViewById(R.id.button2);
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String tableName = editText2.getText().toString();
                createTable(tableName);

            }
        });

        Button button3 = findViewById(R.id.button3);
        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                insertRecord();

            }
        });

        Button button4 = findViewById(R.id.button4);
        button4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                executeQuery();

            }
        });
    }

    public void executeQuery(){
        println("executeQuery가 실행되었네요");
        String tableName = editText2.getText().toString();
        if (tableName == null){
            println("테이블을 입력하세요");
            return;
        }

        String sql = "select _id, name, age, mobile from " + tableName;
        Cursor cursor = database.rawQuery(sql, null);
        int recordCount = cursor.getCount();
        println("레코드 갯수는 : " + recordCount);

        for (int i = 0; i < recordCount; i++){
            cursor.moveToNext();

            int id = cursor.getInt(0);
            String name = cursor.getString(1);
            int age =  cursor.getInt(2);
            String mobile = cursor.getString(3);

            println("레코드 #" + i + " , " + id + " , " + name + " , " + age + " , " + mobile);
        }
        cursor.close();
    }

    public void insertRecord(){
        println("insertRecord() 함수가 호출되었네요");
        if (database == null){
            println("database를 열어주세요");
            return;
        }
        String tableName = editText2.getText().toString();
        if (tableName == null){
            println("생성할 테이블을 입력해 주세요");
            return;
        }
        // String sql = "insert into " + tableName + "(name, age, mobile) values ('john', 20, '010-111-1111' )";
        String name = editText3.getText().toString();
        String age = editText4.getText().toString();
        String mobile = editText5.getText().toString();

        String sql = "insert into " + tableName + "(name, age, mobile) values ('" + name + "', " +  "'" + age + "', " + "'" + mobile + "' )";

        database.execSQL(sql);
        println("레코드 추가함");

    }

    public void createTable(String tableName){
        println("creatTable가 호출됨");

        try {
            if (database == null) {
                println("database를 개설해 주세요");
                return;
            }
            String sql = "create table if not exists " + tableName + "(_id integer PRIMARY KEY autoincrement, name text, age integer, mobile text)";
            database.execSQL(sql);
            println("테이블 생성됨 :" + tableName);
        } catch(Exception e){
            e.printStackTrace();
        }
    }

    public void createDatabase(String databaseName){
        println("createDB가 호출되었네요, 이름은");

        /*try {
            database = openOrCreateDatabase(databaseName, MODE_PRIVATE, null);
            println("database가 생성되었어요" + databaseName);
        } catch(Exception e){
            e.printStackTrace();
        } */

        DatabaseHelper helper = new DatabaseHelper(this, databaseName, null, 3);
        database = helper.getWritableDatabase();
    }


    public void println(String data){
        textView.append(data + "\n");
    }


    class DatabaseHelper extends SQLiteOpenHelper {
        public DatabaseHelper(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
            super(context, name, factory, version);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
           // 위에서 만들어 놓은 메스드를 그대로 활용한다.
            println("creatTable가 호출됨");

            String tableName = "customer";
            try {
                String sql = "create table if not exists " + tableName + "(_id integer PRIMARY KEY autoincrement, name text, age integer, mobile text)";
                db.execSQL(sql);
            } catch(Exception e){
                e.printStackTrace();
            }
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            println("onUpgrade 호출됨" + oldVersion + ", " + newVersion);

            // 구버전의 테이블을 삭제하는 경우
            if (newVersion > 1){
                String tableName = "customer";
                db.execSQL("drop table if exists " + tableName);
                println("테이블 삭제함");

                String sql = "create table if not exists " + tableName + "(_id integer PRIMARY KEY autoincrement, name text, age integer, mobile text)";
                db.execSQL(sql);

                println("테이블 새로 생성됨됨");
            }

       }
    }
}