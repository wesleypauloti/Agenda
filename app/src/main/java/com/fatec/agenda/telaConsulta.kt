package com.fatec.agenda

import android.database.Cursor
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity

class telaConsulta : AppCompatActivity() {

    private lateinit var nome: EditText
    private lateinit var telefone: EditText
    private lateinit var edit_id: EditText
    private lateinit var btn_anterior: Button
    private lateinit var btn_proximo: Button
    private lateinit var btn_voltar: Button
    private lateinit var btn_atualizar: Button
    private lateinit var btn_remover: Button
    private lateinit var dbHelper: DBHelper
    private var cursor: Cursor? = null
    private var posicaoAtual: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tela_consulta)

        dbHelper = DBHelper(this)

        nome = findViewById<EditText>(R.id.edit_nome)
        telefone = findViewById<EditText>(R.id.edit_fone)
        edit_id = findViewById<EditText>(R.id.edit_id)
        btn_anterior = findViewById<Button>(R.id.btn_anterior)
        btn_proximo = findViewById<Button>(R.id.btn_proximo)
        btn_voltar = findViewById<Button>(R.id.btn_voltar)
        btn_atualizar = findViewById<Button>(R.id.btn_atualizar)
        btn_remover = findViewById<Button>(R.id.btn_remover)

        cursor = dbHelper.obterTodosContatos()

        btn_proximo.setOnClickListener {
            moveToNext()
        }

        btn_anterior.setOnClickListener {
            moveToPrevious()
        }

        btn_voltar.setOnClickListener {
            finish()
        }
        moveToNext()

        btn_atualizar.setOnClickListener {
            val id = cursor!!.getLong(cursor!!.getColumnIndexOrThrow(DBHelper.COL_ID))
            val novoNome = nome.text.toString()
            val novoTelefone = telefone.text.toString()

            if (dbHelper.updateContato(id, novoNome, novoTelefone)) {
                Toast.makeText(this, "Contato atualizado com sucesso", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Falha ao atualizar o contato", Toast.LENGTH_SHORT).show()
            }
        }

        btn_remover.setOnClickListener {
            AlertDialog.Builder(this)
                .setTitle("Confirmação")
                .setMessage("Tem certeza de que deseja remover este contato?")
                .setPositiveButton("Sim") { _, _ ->
                    val id = cursor!!.getLong(cursor!!.getColumnIndexOrThrow(DBHelper.COL_ID)) // Substitua isso pela lógica real para obter o ID do contato atual
                    if (dbHelper.removerContato(id)) {
                        Toast.makeText(this, "Contato $id removido com sucesso", Toast.LENGTH_SHORT).show()
                        moveToNext()
                    } else {
                        Toast.makeText(this, "Falha ao remover o contato", Toast.LENGTH_SHORT).show()
                    }
                }
                .setNegativeButton("Não") { dialog, _ ->
                    dialog.dismiss()
                }
                .show()
        }
    }

    private fun moveToNext() {
        if (cursor != null && cursor!!.moveToNext()) {
            atualizarCampos()
        } else {
            msg("Não há mais registros a frente")
        }
    }

    private fun moveToPrevious() {
        if (cursor != null && cursor!!.moveToPrevious()) {
            atualizarCampos()
        } else {
            msg("Não há registros antes deste")
        }
    }

    private fun msg(mensagem: String) {
        Toast.makeText(this, mensagem, Toast.LENGTH_SHORT).show()
    }

    private fun atualizarCampos() {
        posicaoAtual = cursor!!.position
        val idContato = cursor!!.getLong(cursor!!.getColumnIndexOrThrow(DBHelper.COL_ID))
        val nomeContato = cursor!!.getString(cursor!!.getColumnIndexOrThrow(DBHelper.COL_NOME))
        val telefoneContato = cursor!!.getString(cursor!!.getColumnIndexOrThrow(DBHelper.COL_TELEFONE))

        nome.setText(nomeContato)
        telefone.setText(telefoneContato)
        edit_id.setText(idContato.toString())
    }

    override fun onDestroy() {
        super.onDestroy()
        cursor?.close()
        dbHelper.close()
    }
}