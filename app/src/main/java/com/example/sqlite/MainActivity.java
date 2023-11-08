package com.example.sqlite;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    EditText ID, Usuario, AreaUsuario;
    ListView Lista;

    //HOLAHOLAHOLAHOLAHOLAHOLA
    //chaochaochaochao
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ID = findViewById(R.id.txtID);
        Usuario = findViewById(R.id.txtNombreUsuario);
        AreaUsuario = findViewById(R.id.txtAreaUsuario);
        Lista = findViewById(R.id.ListaUsuarios);
        CargaUsuarios();
    }

    public void RegistrarUsuario(View view) {   //Registrar usuario

        AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper(this, "Produccion",
                null, 1);
        SQLiteDatabase basedatos = admin.getWritableDatabase();

        String IDUsuario = ID.getText().toString();  //tomar texto del ID ingresado por el usuario
        String NombreUsuario = Usuario.getText().toString(); //tomar texto del Usuario ingresado por el usuario
        String areaUsuario = AreaUsuario.getText().toString(); //tomar texto del Area del usuario ingresado por el usuario


        if (!IDUsuario.isEmpty() && !NombreUsuario.isEmpty() && !areaUsuario.isEmpty()) { //si el id, nombre y area no estan vacios

            Cursor fila = basedatos.rawQuery("Select * from Usuarios where ID_Usuario = "  // hacer una consulta a la base de datos
                    + IDUsuario, null); // selecciona todos los datos de la tabla usuario donde posean ID_Usuario como llave

            if (fila.getCount() > 0) {
                Toast.makeText(this, "Esta ID ya se encuentra registrada",
                        Toast.LENGTH_SHORT).show();

            } else {

                ContentValues DatosUsuario = new ContentValues();
                DatosUsuario.put("ID_Usuario", IDUsuario);
                DatosUsuario.put("NombreUsuario", NombreUsuario);
                DatosUsuario.put("AreaUsuario", areaUsuario);
                basedatos.insert("Usuarios", null, DatosUsuario);

                basedatos.close();
                ID.setText("");
                Usuario.setText("");
                AreaUsuario.setText("");
                CargaUsuarios();
                Toast.makeText(this, "Se ha registrado el usuario correctamente",
                        Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "No pueden haber campos vacios",
                    Toast.LENGTH_SHORT).show();
        }
    }

    public void BuscarUsuario(View view){
        AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper(this, "Produccion",
                null, 1);

        SQLiteDatabase baseDatos = admin.getWritableDatabase();
        String IDUsuario = ID.getText().toString();

        if(!IDUsuario.isEmpty()){
            Cursor fila = baseDatos.rawQuery("Select NombreUsuario, AreaUsuario from Usuarios " +
                    "where ID_Usuario = " + IDUsuario, null);

            if (fila.moveToFirst()){
                Usuario.setText(fila.getString(0));
                AreaUsuario.setText(fila.getString(1));
                baseDatos.close();

            } else {
                Toast.makeText(this, "La ID no se encuentra registrada",
                        Toast.LENGTH_SHORT).show();
            }
        }else{
            Toast.makeText(this, "El campo ID no puede estar vacio",
                    Toast.LENGTH_SHORT).show();
        }
    }

    public void EliminarUsuario(View view){

        AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper(this,"Produccion",
                null, 1);

        SQLiteDatabase baseDatos = admin.getWritableDatabase();
        String IDUsuario = ID.getText().toString();

        if(!IDUsuario.isEmpty()){

            int Eliminar = baseDatos.delete("Usuarios", "ID_Usuario = " +
                    IDUsuario, null);

            if(Eliminar == 1){  //Eliminar: 0
                Toast.makeText(this, "El usuario se elimino correctamente",
                        Toast.LENGTH_SHORT).show();
                CargaUsuarios();
            } else {
                Toast.makeText(this, "No se encontro el ID ingresado por el usuario",
                        Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "El campo no puede estar vacio",
                    Toast.LENGTH_SHORT).show();
        }
    }

    public void ModificarUsuario (View view){

        AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper(this,"Produccion",
                null, 1);

        SQLiteDatabase baseDatos = admin.getWritableDatabase();
        String IDUsuario = ID.getText().toString();
        String NombreUsuario = Usuario.getText().toString();
        String areaUsuario = AreaUsuario.getText().toString();

        if (!IDUsuario.isEmpty() && !NombreUsuario.isEmpty() && !areaUsuario.isEmpty()){

            ContentValues DatosUsuario = new ContentValues();
            DatosUsuario.put("NombreUsuario", NombreUsuario);
            DatosUsuario.put("AreaUsuario", areaUsuario);

            int Modificar = baseDatos.update("Usuarios", DatosUsuario, "ID_Usuario =" +
                    IDUsuario, null);

            if(Modificar == 1){  //Modificar: 0

                baseDatos.close();
                ID.setText("");
                Usuario.setText("");
                AreaUsuario.setText("");
                CargaUsuarios();

                Toast.makeText(this, "El usuario se modifico correctamente",
                        Toast.LENGTH_SHORT).show();

            } else {
                Toast.makeText(this, "No se encontro el ID ingresado por el usuario",
                        Toast.LENGTH_SHORT).show();

            }
        }else {
            Toast.makeText(this, "Los campos no pueden estar vacios",
                    Toast.LENGTH_SHORT).show();
        }
    }

    public void CargaUsuarios(){
        AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper(this,"Produccion",
                    null, 1);
        SQLiteDatabase baseDatos = admin.getWritableDatabase();
        Cursor fila = baseDatos.rawQuery("Select * from Usuarios", null);
        ArrayList<String> ListaUsuarios = new ArrayList<>();
        if(fila.moveToFirst()){
            do {
                String ID_Usuario2 = fila.getString(0);
                String NombreUsuario2 = fila.getString(1);
                String AreaUsuario2 = fila.getString(2);
                String UserInfo = "- ID usuario: " + ID_Usuario2 +
                        " - Nombre usuario: " + NombreUsuario2 +
                        " - Area usuario: " + AreaUsuario2;
                ListaUsuarios.add(UserInfo);
            }while(fila.moveToNext());
        }
        baseDatos.close();
        ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, ListaUsuarios);
        Lista.setAdapter(adapter);
    }

}