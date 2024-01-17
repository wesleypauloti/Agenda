// DBHelper.kt
package com.fatec.agenda

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.database.Cursor

class DBHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        const val DATABASE_NAME = "AgendaDB"
        const val TABLE_NAME = "Contatos"
        const val COL_ID = "_id"
        const val COL_NOME = "nome"
        const val COL_TELEFONE = "telefone"
        const val DATABASE_VERSION = 1
    }

    override fun onCreate(db: SQLiteDatabase?) {
        val createTableQuery = """
            CREATE TABLE $TABLE_NAME (
                $COL_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $COL_NOME TEXT,
                $COL_TELEFONE TEXT
            )
        """.trimIndent()

        db?.execSQL(createTableQuery)
    }

    fun obterTodosContatos(): Cursor {
        val db = readableDatabase
        val query = "SELECT * FROM $TABLE_NAME"
        return db.rawQuery(query, null)
    }

    fun updateContato(id: Long, novoNome: String, novoTelefone: String): Boolean {
        val values = ContentValues().apply {
            put(COL_NOME, novoNome)
            put(COL_TELEFONE, novoTelefone)
        }

        val whereClause = "$COL_ID = ?"
        val whereArgs = arrayOf(id.toString())

        val linhasAfetadas = writableDatabase.update(TABLE_NAME, values, whereClause, whereArgs)

        return linhasAfetadas > 0
    }

    fun removerContato(id: Long): Boolean {
        val db = this.writableDatabase
        val whereClause = "$COL_ID = ?"
        val whereArgs = arrayOf(id.toString())

        // Excluindo o registro
        val resultado = db.delete(TABLE_NAME, whereClause, whereArgs) > 0

        // Fechando o banco de dados
        db.close()

        return resultado
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        // Caso você precise atualizar o esquema do banco de dados no futuro
    }
}
