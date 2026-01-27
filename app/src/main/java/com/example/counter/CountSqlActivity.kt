package com.example.counter

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteDatabase.CONFLICT_REPLACE
import android.database.sqlite.SQLiteOpenHelper
import android.os.Bundle
import android.provider.BaseColumns._ID
import androidx.appcompat.app.AppCompatActivity
import com.example.counter.databinding.CountActivityBinding
import com.example.counter.utils.checkAndShowIntro


class CountSqlActivity : AppCompatActivity() {

    companion object {
        private const val DB_NAME = "my_db_name"
        private const val DB_VERSION = 1
        private const val KEY_COUNT = "KEY_COUNT"
    }

    private val dbHelper by lazy { DbHelper(this) }

    private var count = 0
    private lateinit var binding: CountActivityBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        checkAndShowIntro()

        binding = CountActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (savedInstanceState != null) {
            count = savedInstanceState.getInt(KEY_COUNT)
        } else {
            count = getCounterData()
        }

        binding.message.text = "$count"

        binding.buttonMinus.setOnClickListener { updateCount(count - 1) }
        binding.buttonPlus.setOnClickListener { updateCount(count + 1) }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt(KEY_COUNT, count)
    }

    private fun updateCount(newCount: Int) {
        count = newCount
        binding.message.text = "$count"
        saveCounterData(count)
    }

    private fun getCounterData(): Int {
        var count = 0
        val db = dbHelper.readableDatabase
        val cursor = db.rawQuery("SELECT * FROM ${DbHelper.TABLE_NAME} WHERE $_ID = ?", arrayOf("1"))

        if (cursor.moveToFirst()) {
            val id = cursor.getInt(cursor.getColumnIndexOrThrow(_ID))
            val name = cursor.getString(cursor.getColumnIndexOrThrow(DbHelper.COLUMN_NAME))
            count = cursor.getInt(cursor.getColumnIndexOrThrow(DbHelper.COLUMN_VALUE))
        }

        cursor.close()
        db.close()

        return count
    }

    private fun saveCounterData(value: Int) {
        val values = ContentValues()
        values.put(_ID, 1)
        values.put(DbHelper.COLUMN_NAME, "count_x")
        values.put(DbHelper.COLUMN_VALUE, value)

        dbHelper.writableDatabase
            .insertWithOnConflict(DbHelper.TABLE_NAME, null, values, CONFLICT_REPLACE)
    }

    class DbHelper(context: Context) : SQLiteOpenHelper(context, DB_NAME, null, DB_VERSION) {

        companion object {
            const val TABLE_NAME = "count"
            const val COLUMN_NAME = "name"
            const val COLUMN_VALUE = "value"
        }

        private val CREATE_TABLE = """CREATE TABLE IF NOT EXISTS $TABLE_NAME
            |($_ID INTEGER PRIMARY KEY AUTOINCREMENT,
            |$COLUMN_NAME TEXT,
            |$COLUMN_VALUE INTERGER)""".trimMargin()

        override fun onCreate(db: SQLiteDatabase) {
            db.execSQL(CREATE_TABLE)
        }

        override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
            db.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
            onCreate(db)
        }
    }
}
