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

    static long t(int n, int nivel, StringBuilder sb) {
        sb.append(recuo(nivel)).append("T(").append(n).append(")\n");
        if (n <= 0) {
            sb.append(recuo(nivel)).append("-> caso base: T(0) = 0\n");
            return 0;
        }
        long resultado = t(n - 1, nivel + 1, sb) + n;
        sb.append(recuo(nivel)).append("-> T(").append(n - 1).append(") + ").append(n).append(" = ").append(resultado).append("\n");
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
        System.out.println("===== RECORRENCIA T(n) = T(n-1) + n =====");
        int n = lerInteiro(sc, "Informe n (>= 0): ", 0);
        StringBuilder sb = new StringBuilder();
        long r = t(n, 0, sb);
        System.out.println();
        System.out.println("Rastro das chamadas:");
        System.out.print(sb);
        System.out.println();
        System.out.println("Resultado: T(" + n + ") = " + r);
        sc.close();
    }

}
