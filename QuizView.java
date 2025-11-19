import java.util.List;
import java.util.Scanner;

/**
 * 🖥️ CLASSE DE VISÃO (Interface do Usuário)
 */
public class QuizView {

    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_GREEN = "\u001B[32m";
    public static final String ANSI_YELLOW = "\u001B[33m";
    public static final String ANSI_CYAN = "\u001B[36m";

    private final Scanner scanner;

    public QuizView() {
        this.scanner = new Scanner(System.in);
    }

    public String escolherQuestionario() {
        System.out.println(ANSI_CYAN + "\nQual matéria você gostaria de estudar?" + ANSI_RESET);
        // Adicione seus arquivos JSON aqui
        String[][] opcoes = {
            {"1", "Java (POO)", "/perguntas_3b2_poo_java.json"},
            {"2", "Estatística", "/perguntas_3b2_estatistica.json"},
            {"3", "Gestão de Inovação", "/perguntas_3b2_inovacao.json"}
        };

        for (String[] opcao : opcoes) {
            System.out.println(ANSI_YELLOW + opcao[0] + ANSI_RESET + " - " + opcao[1]);
        }

        while (true) {
            System.out.print("\nDigite o número da sua escolha: ");
            String escolha = scanner.nextLine();
            for (String[] opcao : opcoes) {
                if (opcao[0].equals(escolha)) {
                    return opcao[2];
                }
            }
            System.out.println(ANSI_RED + "Opção inválida." + ANSI_RESET);
        }
    }

    public void mostrarBoasVindas(int total) {
        System.out.println("✅ Banco carregado com " + total + " perguntas.");
    }

    /**
     * --- NOVO MENU PRINCIPAL ---
     */
    public int mostrarMenuPrincipal() {
        System.out.println(ANSI_CYAN + "\n================ MENU ================" + ANSI_RESET);
        System.out.println("1 - 📝 Iniciar Quiz Aleatório");
        System.out.println("2 - 🔍 Pesquisar por Palavra-chave");
        System.out.println("0 - 🚪 Sair");
        System.out.print("Escolha uma opção: ");
        
        try {
            return Integer.parseInt(scanner.nextLine());
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    public String pedirTermoDeBusca() {
        System.out.print(ANSI_YELLOW + "\nDigite a palavra ou frase para pesquisar: " + ANSI_RESET);
        return scanner.nextLine();
    }

    public int escolherResultadoDaBusca(List<Pergunta> resultados) {
        if (resultados.isEmpty()) {
            System.out.println(ANSI_RED + ">> Nenhuma pergunta encontrada com esse termo." + ANSI_RESET);
            return -1;
        }

        System.out.println(ANSI_GREEN + "\nEncontramos " + resultados.size() + " perguntas:" + ANSI_RESET);
        
        for (int i = 0; i < resultados.size(); i++) {
            Pergunta p = resultados.get(i);
            // Mostra apenas o título ou os primeiros 60 caracteres
            String resumo = p.textoDaPergunta.split(" ___ ")[0];
            if (resumo.length() > 60) resumo = resumo.substring(0, 60) + "...";
            
            System.out.println(ANSI_YELLOW + (i + 1) + ANSI_RESET + ". " + resumo);
        }
        System.out.println("0. Voltar ao menu");

        while (true) {
            System.out.print("\nDigite o número da pergunta para ver detalhes (0 para voltar): ");
            try {
                int escolha = Integer.parseInt(scanner.nextLine());
                if (escolha == 0) return -1;
                if (escolha > 0 && escolha <= resultados.size()) {
                    return escolha - 1; // Retorna índice de array (0-based)
                }
            } catch (NumberFormatException e) {
                // ignora
            }
            System.out.println("Número inválido.");
        }
    }

    // Mostra a pergunta já com a resposta certa (Modo Estudo)
    public void mostrarPerguntaComResposta(Pergunta p) {
        System.out.println(ANSI_CYAN + "\n--- Detalhes da Pergunta (Modo Estudo) ---" + ANSI_RESET);
        // Substitui o separador por quebra de linha para ficar bonito
        System.out.println(p.textoDaPergunta.replace(" ___ ", "\n"));
        
        for (int i = 0; i < p.opcoes.size(); i++) {
            if (i == p.indiceRespostaCorreta) {
                System.out.println(ANSI_GREEN + " (X) " + p.opcoes.get(i) + "  ✅ CORRETA" + ANSI_RESET);
            } else {
                System.out.println(" ( ) " + p.opcoes.get(i));
            }
        }
        System.out.println(ANSI_YELLOW + "\nComentário: " + p.comentario + ANSI_RESET);
        System.out.println("------------------------------------------------");
        System.out.println("Pressione ENTER para continuar...");
        scanner.nextLine();
    }

    // --- MÉTODOS DO QUIZ ORIGINAL ---

    public void mostrarInicioQuiz(int num) {
        System.out.println(ANSI_GREEN + "\n--- Início do Quiz ---" + ANSI_RESET);
        System.out.println("Respondendo a " + num + " perguntas aleatórias...\n");
    }

    public int pedirQuantidade(int maxDisponivel) {
        int numeroEscolhido = 0;
        while (numeroEscolhido <= 0 || numeroEscolhido > maxDisponivel) {
            System.out.println(ANSI_CYAN + "\nVocê tem " + maxDisponivel + " perguntas disponíveis." + ANSI_RESET);
            System.out.print("Quantas perguntas você gostaria de responder? ");
            try {
                numeroEscolhido = Integer.parseInt(scanner.nextLine());
                if (numeroEscolhido <= 0) System.out.println(ANSI_RED + "Digite > 0." + ANSI_RESET);
                else if (numeroEscolhido > maxDisponivel) {
                    System.out.println("Usando o máximo: " + maxDisponivel);
                    numeroEscolhido = maxDisponivel;
                }
            } catch (Exception e) {
                System.out.println("Entrada inválida.");
            }
        }
        return numeroEscolhido;
    }

    public void mostrarPergunta(Pergunta p, int numAtual, int numTotal) {
        String texto = p.textoDaPergunta.replace(" ___ ", "\n");
        System.out.println(ANSI_YELLOW + "\nPergunta " + numAtual + " de " + numTotal + ANSI_RESET);
        System.out.println(ANSI_CYAN + texto + ANSI_RESET);
        for (String opcao : p.opcoes) {
            System.out.println("  ( ) " + opcao);
        }
    }

    public int pedirResposta(int numOpcoes) {
        while (true) {
            System.out.print("\nSua resposta (a, b, c...): ");
            String entrada = scanner.nextLine().toLowerCase();
            if (entrada.length() == 1) {
                int indice = entrada.charAt(0) - 'a';
                if (indice >= 0 && indice < numOpcoes) return indice;
            }
            System.out.println(ANSI_RED + "Inválido." + ANSI_RESET);
        }
    }

    public void mostrarFeedback(boolean correto, String comentario, char letraCorreta) {
        if (correto) {
            System.out.println(ANSI_GREEN + ">> Resposta Correta! <<" + ANSI_RESET);
            System.out.println("Comentário: " + comentario);
        } else {
            System.out.println(ANSI_RED + ">> Incorreta. <<" + ANSI_RESET);
            System.out.println("Correta era: " + ANSI_GREEN + letraCorreta + ANSI_RESET);
        }
    }

    public void mostrarResultadoFinal(int pontuacao, int total) {
        System.out.println("\n--- Fim do Quiz ---");
        System.out.println("Acertos: " + pontuacao + " / " + total);
    }

    public void mostrarErroFatal(String msg) {
        System.out.println(ANSI_RED + "ERRO FATAL: " + msg + ANSI_RESET);
    }

    public void fechar() {
        if (scanner != null) scanner.close();
    }
}