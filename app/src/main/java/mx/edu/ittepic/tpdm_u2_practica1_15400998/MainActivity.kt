package mx.edu.ittepic.tpdm_u2_practica1_15400998

import android.content.Intent
import android.database.SQLException
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AlertDialog

class MainActivity : AppCompatActivity() {
    var descripcionLista: EditText ?= null
    var fechacreacionLista : EditText ?= null
    var insertarLista : Button ?= null
    var mostrarTodasListas : Button ?= null
    var abrirTareas : Button ?= null
    var mostrarTodasLi : TextView ?= null
    var bdLista = BaseDatosLista(this,"practica1",null,1)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        descripcionLista = findViewById(R.id.descripcionLista)
        fechacreacionLista = findViewById(R.id.fechacreacionLista)
        insertarLista = findViewById(R.id.insertarLista)
        mostrarTodasListas = findViewById(R.id.mostrarTodasListas)
        abrirTareas = findViewById(R.id.abrirTareas)
        mostrarTodasLi = findViewById(R.id.mostrarTodasLi)
        mostrarTodos()

        abrirTareas?.setOnClickListener(){
            val ventanaTareas = Intent(this,Main2Activity::class.java)
            startActivity(ventanaTareas)
        }
        insertarLista?.setOnClickListener(){
            insertarListas()
        }
        mostrarTodasListas?.setOnClickListener(){
            mostrarTodos()
        }
    }

    fun msj(t: String, m: String){
        AlertDialog.Builder(this)
            .setTitle(t)
            .setMessage(m)
            .setPositiveButton("OK")
            { dialogInterface, i ->}.show()
    }

    fun limpiarCampos(){
        descripcionLista?.setText("")
        fechacreacionLista?.setText("")
    }

    fun validaCampos(): Boolean{
        if(descripcionLista?.text!!.toString().isEmpty()||fechacreacionLista?.text!!.isEmpty()){
            return false
        }else{
            return true
        }
    }

    fun insertarListas(){
        try {
            var trans = bdLista.writableDatabase
            var con = "INSERT INTO LISTA VALUES(NULL,'DESC','FECHACREA')"
            if (validaCampos() == false) {
                msj("Error!", "Existe algun campo vacio (\"Descripción\" y/o \"Fecha de creación\")")
                return
            }

            con = con.replace("DESC", descripcionLista?.text.toString())
            con = con.replace("FECHACREA", fechacreacionLista?.text.toString())
            trans.execSQL(con)
            trans.close()
            msj("Registro exitoso!", "Se inserto correctamente")
            limpiarCampos()
        }
        catch (er: SQLException) {
            msj("Error!","No se pudo insertar el registro, verifique sus datos!")
        }
    }

    fun mostrarTodos(){
        var sel = ""
        try {
            var trans = bdLista.readableDatabase
            var con = "SELECT * FROM LISTA"
            var cur = trans.rawQuery(con,null)
            if(cur != null) {
                if (cur.moveToFirst() == true) {
                    do{
                        sel +="ID: ${cur.getString(0)}\nDescripción: ${cur.getString(1)}\nFecha de creación: ${cur.getString(2)}\n"+
                              "______________________________________________\n"
                    }while (cur.moveToNext())
                    mostrarTodasLi?.setText(sel)
                }else{
                    msj("Advertencia!","No existen listas")
                }
            }
            cur.close()
        }
        catch (er: SQLException){
            msj("Error!","No se encuentran registros en la BD")
        }
    }

}
