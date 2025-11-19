/**
 * 💿 CLASSE MODELO (POJO / Data Class)
 * Representa uma única pergunta.
 * Os nomes das variáveis DEVEM ser idênticos às chaves do JSON.
 */

import java.util.List;

public class Pergunta {
    // Estas variáveis são preenchidas pelo GSON
    public String textoDaPergunta;
    public List<String> opcoes;
    public int indiceRespostaCorreta;
    public String comentario;
    /**
     * Construtor vazio. 
     * Boa prática para bibliotecas de serialização/desserialização como Gson.
     */
    public Pergunta() {
    }
}