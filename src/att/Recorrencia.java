package att;

import java.util.Scanner;

public class Recorrencia {

    static String recuo(int nivel) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < nivel; i++) {
            sb.append("|   ");
        }
        return sb.toString();
    }

    static long fatorial(int n, int nivel, StringBuilder sb) {
        sb.append(recuo(nivel)).append("fatorial(").append(n).append(")\n");
        if (n <= 1) {
            sb.append(recuo(nivel)).append("-> caso base: retorna 1\n");
            return 1;
        }
        long resultado = n * fatorial(n - 1, nivel + 1, sb);
        sb.append(recuo(nivel)).append("-> ").append(n).append(" * fatorial(").append(n - 1).append(") = ").append(resultado).append("\n");
        return resultado;
    }

    static int lerInteiro(Scanner sc, String rotulo, int minimo) {
        while (true) {
            System.out.print(rotulo);
            if (sc.hasNextInt()) {
                int valor = sc.nextInt();
                if (valor >= minimo) {
                    return valor;
                }
                System.out.println("Valor invalido. Informe um numero >= " + minimo + ".");
            } else {
                System.out.println("Entrada invalida. Digite um numero inteiro.");
                sc.next();
            }
        }
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        System.out.println("===== FATORIAL RECURSIVO =====");
        int n = lerInteiro(sc, "Informe n (>= 0): ", 0);
        StringBuilder sb = new StringBuilder();
        long r = fatorial(n, 0, sb);
        System.out.println();
        System.out.println("Rastro das chamadas:");
        System.out.print(sb);
        System.out.println();
        System.out.println("Resultado: " + n + "! = " + r);
        sc.close();
    }

}
