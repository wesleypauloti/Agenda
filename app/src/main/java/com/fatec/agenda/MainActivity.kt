package com.fatec.agenda

import android.content.ContentValues
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity


class MainActivity : AppCompatActivity() {

    private lateinit var nome: EditText
    private lateinit var telefone: EditText
    private lateinit var btn_fechar: Button
    private lateinit var btn_gravar: Button
    private lateinit var btn_consultar: Button
    private lateinit var dbHelper: DBHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        dbHelper = DBHelper(this)

        btn_gravar = findViewById(R.id.btn_gravar)

        btn_gravar.setOnClickListener {
            val nomeText = nome.text.toString()
            val telefoneText = telefone.text.toString()

            if (nomeText.isNotBlank() && telefoneText.isNotBlank()) {
                inserirContato(nomeText, telefoneText)
                Toast.makeText(this, "Dados gravados com sucesso!", Toast.LENGTH_SHORT).show()
            }
        }

        nome = findViewById<EditText>(R.id.edit_nome)
        telefone = findViewById<EditText>(R.id.edit_fone)
        btn_fechar = findViewById<Button>(R.id.btn_fechar)
        btn_gravar = findViewById<Button>(R.id.btn_gravar)
        btn_consultar = findViewById<Button>(R.id.btn_consultar)

        btn_consultar.setOnClickListener {
            var proxima_tela = Intent(baseContext, telaConsulta::class.java)
            startActivity(proxima_tela)
        }

        btn_fechar.setOnClickListener {
            finish()
        }
    }

    private fun inserirContato(nome: String, telefone: String) {
        val db = dbHelper.writableDatabase

        val values = ContentValues().apply {
            put(DBHelper.COL_NOME, nome)
            put(DBHelper.COL_TELEFONE, telefone)
        }

        db.insert(DBHelper.TABLE_NAME, null, values)
    }
}