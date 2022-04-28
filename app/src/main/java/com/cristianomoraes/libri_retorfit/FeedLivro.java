package com.cristianomoraes.libri_retorfit;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.cristianomoraes.libri_retorfit.model.Item;
import com.cristianomoraes.libri_retorfit.model.Livro;
import com.cristianomoraes.libri_retorfit.remote.APIUtil;
import com.cristianomoraes.libri_retorfit.remote.RouterInterface;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FeedLivro extends AppCompatActivity {

    RouterInterface routerInterface;
    List<Livro> list = new ArrayList<Livro>();
    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed_livro);

        routerInterface = APIUtil.getUsuarioInterface();
        recyclerView = findViewById(R.id.recyclerView);

        /** Recebe os dados de livros vindos da APIREST **/
        Call<List<Livro>> call = routerInterface.getLivro();

        call.enqueue(new Callback<List<Livro>>() {
            @Override
            public void onResponse(Call<List<Livro>> call, Response<List<Livro>> response) {

                List<Item> itens = new ArrayList<>();
                list =  response.body();

                // Percorre o list até o final
                for(int i = 0; i < list.size(); i++){
                    itens.add(new Item(0, list.get(i)));
                }

                recyclerView.setAdapter(new LivroAdapter(itens));
            }

            @Override
            public void onFailure(Call<List<Livro>> call, Throwable t) {
                Log.d("API-ERRO", t.getMessage());
            }
        });

    }
    /** CLASSE DE ADAPTER DO RECYCLERVIEW **/
    private class LivroAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
        // Recebe todos os itens
        List<Item> itens;

        public LivroAdapter(List<Item> itens) {
            this.itens = itens;
        }// FIM DO METODO CONSTRUTOR DE LIVROADAPTER

        @NonNull
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            /**Infla a estrutura xml e os dados referentes a livro**/
            if(viewType == 0){
            return new LivroAdapter.LivroViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_container_livro, parent, false));
            }
            return null;
        }



        @Override
        public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
            /** Recupera os dados de livro **/
                if(getItemViewType(position) == 0){
                    Livro livro = (Livro) itens.get(position).getObject();
                    ((LivroAdapter.LivroViewHolder) holder).setLivroData(livro);

                }

                // if(getItemViewType(position) == 0){}

                //  if(getItemViewType(position) == 0){}

        }

        @Override
        public int getItemCount() {
            return itens.size();
        }

        /** RECUPERA O TIPO DO OBJETO DE ITEM **/
        public int getItemViewType(int position){
            return itens.get(position).getType();
        }

        /** CLASSE DE VIEWHOLDER DA RECYCLERVIEW **/
        // classe filha herda a classe pai
        class LivroViewHolder extends RecyclerView.ViewHolder {

            private TextView txtTitulo, txtDescricao;
            private int cod_livro;

            public LivroViewHolder(@NonNull View itemView) {
                super(itemView);

                txtTitulo = itemView.findViewById(R.id.txtCardTituloLivro);
                txtDescricao = itemView.findViewById(R.id.txtCardDescricaoLivro);

                /** TRATAMENTO DE CLIQUE PARA A ALTERAÇÃO E EXCLUSÃO DE LIVRO **/
                itemView.setOnClickListener(view -> {
                    /**
                     PARAMETRO:
                        1 - Contexto onde será exibido

                     CHAMA DOIS MÉTODOS:
                     setMessage -> Configura a mensagem da instrução para o usuario
                     setPositiveButton
                        Parametros:
                                1 - Titulo (type: string)
                                2 - Função que trata a ação do botão
                     setNegativeButton
                                 1 - Titulo (type: string)
                                 2 - Função que trata a ação do botão
                      **/
                    AlertDialog.Builder alertDialog = new AlertDialog.Builder(FeedLivro.this)
                            .setMessage("Escola a ação que deseja")
                            .setPositiveButton("Alterar", (dialog1, witch) ->{
                                Intent intent = new Intent(FeedLivro.this, EditarLivro.class);
                                startActivity(intent);
                            })
                            .setNegativeButton("Excluir", (dialog1, witch) ->{
                                routerInterface = APIUtil.getUsuarioInterface();

                                Call<Livro> call = routerInterface.deleteLivro(cod_livro);
                                call.enqueue(new Callback<Livro>() {
                                    @Override
                                    public void onResponse(Call<Livro> call, Response<Livro> response) {
                                        Toast.makeText(FeedLivro.this,
                                                "O livro selecionado foi excluido! ACÃO IRREVERSIVEL", Toast.LENGTH_SHORT).show();
                                            startActivity(new Intent(FeedLivro.this, FeedLivro.class));

                                    }

                                    @Override
                                    public void onFailure(Call<Livro> call, Throwable t) {

                                    }
                                });
                            });

                        alertDialog.show();
                });



            }// FIM DO MÉTODO CONSTRUTOR DE LIVROVIEWHOLDER

            public void setLivroData(Livro livro){
                txtTitulo.setText(livro.getTitulo());
                txtDescricao.setText(livro.getDescricao());
                cod_livro = livro.getCod_livro();
            }


        }// FIM DA CLASSE DE LIVROVIEWHOLDER
    }// FIM DA CLASSE DO LIVROADAPTER
}