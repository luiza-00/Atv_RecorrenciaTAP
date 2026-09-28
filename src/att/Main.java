package att;

import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        LeitorEntrada leitor = new LeitorEntrada(scanner);

        System.out.println("===== RESOLVEDOR DE RELACOES DE RECORRENCIA =====");
        System.out.println("Formas aceitas:");
        System.out.println("  1 - Subtrativa: T(n) = a*T(n - c) + (p*n + q)");
        System.out.println("  2 - Divisiva:   T(n) = a*T(n / b) + (p*n + q)");

        int forma = leitor.lerInteiroNoIntervalo("Escolha a forma (1 ou 2): ", 1, 2);
        long a = leitor.lerLongoMinimo("a (quantidade de chamadas recursivas, >= 1): ", 1);

        long fatorTamanho;
        if (forma == Recorrencia.FORMA_DIVISIVA) {
            fatorTamanho = leitor.lerLongoMinimo("b (fator de divisao, >= 2): ", 2);
        } else {
            fatorTamanho = leitor.lerLongoMinimo("c (reducao de n por chamada, >= 1): ", 1);
        }

        long coeficienteN = leitor.lerLongoMinimo("p (coeficiente de n no custo f(n)=p*n+q, >= 0): ", 0);
        long constante = leitor.lerLongoMinimo("q (constante do custo, >= 0): ", 0);
        long casoBase = leitor.lerLongoMinimo("tamanho do caso base (ex.: 1): ", 1);

        Recorrencia recorrencia = new Recorrencia(forma, a, fatorTamanho, coeficienteN, constante, casoBase);
        ResolvedorRecorrencia resolvedor = new ResolvedorRecorrencia(recorrencia);

        System.out.println();
        System.out.print(resolvedor.resolver());

        scanner.close();
    }

}
