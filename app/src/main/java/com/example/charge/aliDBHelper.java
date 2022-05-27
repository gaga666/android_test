package com.example.charge;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import androidx.annotation.Nullable;
public class aliDBHelper {
    private static String driver = "com.mysql.jdbc.Driver";
    private static String url = "jdbc:mysql://118.31.57.102/test?characterEncoding = utf-8";
    private static String user = "test";
    private static String password = "test";
    /**
     * 连接数据库
     */
    public static Connection getConn(){
        Connection conn = null;
        try {
            Class.forName(driver);
            conn = (Connection) DriverManager.getConnection(url,user,password);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e){
            e.printStackTrace();
        }
        return conn;
    }
}
