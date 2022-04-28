package com.cristianomoraes.libri_retorfit;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

public class EditarLivro extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar_livro);

        /**RECEBE O DADO DE CÓDIGO DE KIVRO ENVIADO A PARTIR DA ADCTIVITY FEEDLIVRO**/
        int cod_livro = getIntent().getExtras().getInt("cod_livro");
        Log.d("Teste", String.valueOf(cod_livro));
    }
}