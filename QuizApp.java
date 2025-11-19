import java.io.IOException;
import java.util.List;

/**
 * 🚀 CLASSE PRINCIPAL (Controlador)
 */
public class QuizApp {

    private final QuizService quizService;
    private final QuizView quizView;

    public QuizApp() {
        this.quizView = new QuizView();
        
        // 1. Escolha do arquivo
        String arquivoEscolhido = quizView.escolherQuestionario();

        // 2. Inicialização do Serviço
        QuizService service = null;
        try {
            service = new QuizService(arquivoEscolhido);
            quizView.mostrarBoasVindas(service.getTotalPerguntasDisponiveis());
        } catch (IOException e) {
            quizView.mostrarErroFatal("Falha ao carregar arquivo: " + e.getMessage());
        }
        this.quizService = service;
    }

    public static void main(String[] args) {
        QuizApp app = new QuizApp();
        if (app.quizService != null) {
            app.run();
        }
    }

    /**
     * Loop principal que gerencia o Menu (Quiz vs Pesquisa)
     */
    public void run() {
        boolean rodando = true;

        while (rodando) {
            // Mostra menu: 1.Quiz, 2.Pesquisa, 0.Sair
            int opcao = quizView.mostrarMenuPrincipal();

            switch (opcao) {
                case 1:
                    iniciarQuiz(); // Modo Jogo
                    break;
                case 2:
                    iniciarModoPesquisa(); // Modo Estudo/Busca
                    break;
                case 0:
                    System.out.println("Encerrando... Bons estudos!");
                    rodando = false;
                    break;
                default:
                    System.out.println("Opção inválida.");
            }
        }
        quizView.fechar();
    }

    // --- LÓGICA DO MODO QUIZ (JOGO) ---
    private void iniciarQuiz() {
        int maxPerguntas = quizService.getTotalPerguntasDisponiveis();
        int numPerguntas = quizView.pedirQuantidade(maxPerguntas);
        
        List<Pergunta> perguntas = quizService.getPerguntasAleatorias(numPerguntas);
        quizView.mostrarInicioQuiz(numPerguntas);

        int pontuacao = 0;

        for (int i = 0; i < perguntas.size(); i++) {
            Pergunta p = perguntas.get(i);
            quizView.mostrarPergunta(p, i + 1, numPerguntas);
            int indiceResposta = quizView.pedirResposta(p.opcoes.size());
            
            boolean correto = quizService.verificarResposta(p, indiceResposta);
            char letraCorreta = (char) ('a' + p.indiceRespostaCorreta);
            
            if (correto) {
                pontuacao++;
                quizView.mostrarFeedback(true, p.comentario, letraCorreta);
            } else {
                quizView.mostrarFeedback(false, null, letraCorreta);
            }
        }
        quizView.mostrarResultadoFinal(pontuacao, numPerguntas);
    }

    // --- LÓGICA DO MODO PESQUISA (ESTUDO) ---
    private void iniciarModoPesquisa() {
        // Loop externo: Mantém o usuário no modo de pesquisa até ele querer sair
        while (true) {
            String termo = quizView.pedirTermoDeBusca();

            // Se o usuário der ENTER vazio, sai do modo pesquisa e volta ao Menu Principal
            if (termo.trim().isEmpty()) {
                return; 
            }

            List<Pergunta> resultados = quizService.pesquisarPorPalavra(termo);
            
            // Loop interno: Navega pelos resultados daquela pesquisa específica
            while (true) {
                int indiceEscolhido = quizView.escolherResultadoDaBusca(resultados);
                
                if (indiceEscolhido == -1) {
                    break; // Sai dos resultados e pede uma NOVA palavra-chave
                }
                
                // Mostra a pergunta escolhida
                Pergunta p = resultados.get(indiceEscolhido);
                quizView.mostrarPerguntaComResposta(p);
                // Após ver a pergunta, o loop roda de novo mostrando a lista da MESMA pesquisa
            }
        }
    }
}