package com.cristianomoraes.libri_retorfit.remote;

import com.cristianomoraes.libri_retorfit.model.Livro;
import com.cristianomoraes.libri_retorfit.model.Usuario;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Body;

public interface RouterInterface {

    /** MAPEAMENTO DAS ROTA DE USUÁRIOS **/
    @POST("/usuario/CadastrarUsuario")
    Call<Usuario> addUsuario(@Body Usuario usuario);

    /** MAPEAMENTO DAS ROTA DE LIVROS**/
    @POST("/livro/cadastrarLivro")
    Call<Livro> addlivro(@Body Livro Livro);

    @GET("livro/listarLivro")
    Call<List<Livro>> getLivro();
}
