import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 🧠 CLASSE DE SERVIÇO (Lógica de Negócio)
 * Responsável por carregar, gerenciar e validar os dados do quiz.
 */
public class QuizService {

    private final List<Pergunta> bancoDePerguntas;

    public QuizService(String caminhoRecurso) throws IOException {
        this.bancoDePerguntas = carregarPerguntas(caminhoRecurso);
    }

    public int getTotalPerguntasDisponiveis() {
        return this.bancoDePerguntas.size();
    }

    public List<Pergunta> getPerguntasAleatorias(int quantidade) {
        List<Pergunta> copiaPerguntas = new ArrayList<>(this.bancoDePerguntas);
        Collections.shuffle(copiaPerguntas);
        int tamanhoReal = Math.min(quantidade, copiaPerguntas.size());
        return copiaPerguntas.subList(0, tamanhoReal);
    }

    public boolean verificarResposta(Pergunta pergunta, int indiceResposta) {
        return pergunta.indiceRespostaCorreta == indiceResposta;
    }

    /**
     * --- NOVO MÉTODO DE PESQUISA ---
     * Procura termo no enunciado e nas opções.
     */
    public List<Pergunta> pesquisarPorPalavra(String termo) {
        List<Pergunta> resultados = new ArrayList<>();
        if (termo == null || termo.trim().isEmpty()) {
            return resultados;
        }

        String termoBusca = termo.toLowerCase(); // Ignora maiúsculas/minúsculas

        for (Pergunta p : this.bancoDePerguntas) {
            boolean achouNoTitulo = p.textoDaPergunta.toLowerCase().contains(termoBusca);
            boolean achouNasOpcoes = false;
            
            for (String opcao : p.opcoes) {
                if (opcao.toLowerCase().contains(termoBusca)) {
                    achouNasOpcoes = true;
                    break;
                }
            }

            if (achouNoTitulo || achouNasOpcoes) {
                resultados.add(p);
            }
        }
        return resultados;
    }

    private List<Pergunta> carregarPerguntas(String caminhoRecurso) throws IOException {
        InputStream is = QuizService.class.getResourceAsStream(caminhoRecurso);
        if (is == null) {
            throw new FileNotFoundException("Recurso JSON não encontrado: " + caminhoRecurso);
        }

        try (Reader reader = new InputStreamReader(is, StandardCharsets.UTF_8)) {
            Gson gson = new Gson();
            Type tipoListaPerguntas = new TypeToken<ArrayList<Pergunta>>() {}.getType();
            List<Pergunta> perguntas = gson.fromJson(reader, tipoListaPerguntas);

            if (perguntas == null || perguntas.isEmpty()) {
                throw new IOException("JSON vazio ou mal formatado.");
            }
            return perguntas;
        }
    }
}