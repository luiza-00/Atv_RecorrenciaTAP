package att;

import java.util.Scanner;

public class LeitorEntrada {

    private final Scanner scanner;

    public LeitorEntrada(Scanner scanner) {
        this.scanner = scanner;
    }

    public int lerInteiroNoIntervalo(String rotulo, int minimo, int maximo) {
        while (true) {
            System.out.print(rotulo);
            if (scanner.hasNextInt()) {
                int valor = scanner.nextInt();
                if (valor >= minimo && valor <= maximo) {
                    return valor;
                }
                System.out.println("Valor invalido. Informe um numero entre " + minimo + " e " + maximo + ".");
            } else {
                System.out.println("Entrada invalida. Digite um numero inteiro.");
                scanner.next();
            }
        }
    }

    public long lerLongoMinimo(String rotulo, long minimo) {
        while (true) {
            System.out.print(rotulo);
            if (scanner.hasNextLong()) {
                long valor = scanner.nextLong();
                if (valor >= minimo) {
                    return valor;
                }
                System.out.println("Valor invalido. Informe um numero >= " + minimo + ".");
            } else {
                System.out.println("Entrada invalida. Digite um numero inteiro.");
                scanner.next();
            }
        }
    }

}
