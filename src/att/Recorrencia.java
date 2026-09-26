package att;

import java.util.Scanner;

public class Recorrencia {

    static void expandir(int a, int num, int den, int k, int limite) {

        if (k > limite) {
            return;
        }

        int quantidade = (int) Math.pow(a, k);
        int novoNum = (int) Math.pow(num, k);
        int novoDen = (int) Math.pow(den, k);

        System.out.println("T(n) = " + quantidade + "T(" + novoNum + "n/" + novoDen + ") + " +k + "cn");

        expandir(a, num, den, k + 1, limite);
    }

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);

        System.out.print("qtd de subproblemas: ");
        int a = sc.nextInt();

        System.out.print("numerador (tam. problema): ");
        int num = sc.nextInt();

        System.out.print("denominador (tam. problema): ");
        int den = sc.nextInt();

        System.out.print("expansoes que deseja: ");
        int limite = sc.nextInt();

        System.out.println();
        System.out.println("T(n) = " + a + "T(" + num + "n/" + den + ") + cn");
        System.out.println();

        expandir(a, num, den, 1, limite);

        sc.close();
    }
    
}