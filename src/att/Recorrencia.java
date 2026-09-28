package att;

import java.util.Scanner;

public class Recorrencia {

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        System.out.println("===== RESOLVEDOR DE RELACOES DE RECORRENCIA =====");
        System.out.println("Formas aceitas:");
        System.out.println("  1 - Subtrativa: T(n) = a*T(n - c) + (p*n + q)");
        System.out.println("  2 - Divisiva:   T(n) = a*T(n / b) + (p*n + q)");

        int forma = 0;
        while (true) {
            System.out.print("Escolha a forma (1 ou 2): ");
            if (sc.hasNextInt()) {
                forma = sc.nextInt();
                if (forma == 1 || forma == 2) {
                    break;
                }
                System.out.println("Valor invalido. Digite 1 ou 2.");
            } else {
                System.out.println("Entrada invalida. Digite um numero.");
                sc.next();
            }
        }

        boolean divisivo = (forma == 2);

        long a = 0;
        while (true) {
            System.out.print("a (quantidade de chamadas recursivas, >= 1): ");
            if (sc.hasNextLong()) {
                a = sc.nextLong();
                if (a >= 1) {
                    break;
                }
                System.out.println("Valor invalido. Informe um numero >= 1.");
            } else {
                System.out.println("Entrada invalida. Digite um numero inteiro.");
                sc.next();
            }
        }

        long bc = 0;
        while (true) {
            if (divisivo) {
                System.out.print("b (fator de divisao, >= 2): ");
            } else {
                System.out.print("c (reducao de n por chamada, >= 1): ");
            }
            if (sc.hasNextLong()) {
                bc = sc.nextLong();
                if (divisivo && bc >= 2) {
                    break;
                }
                if (!divisivo && bc >= 1) {
                    break;
                }
                System.out.println("Valor invalido.");
            } else {
                System.out.println("Entrada invalida. Digite um numero inteiro.");
                sc.next();
            }
        }

        long p = -1;
        while (true) {
            System.out.print("p (coeficiente de n no custo f(n)=p*n+q, >= 0): ");
            if (sc.hasNextLong()) {
                p = sc.nextLong();
                if (p >= 0) {
                    break;
                }
                System.out.println("Valor invalido. Informe um numero >= 0.");
            } else {
                System.out.println("Entrada invalida. Digite um numero inteiro.");
                sc.next();
            }
        }

        long q = -1;
        while (true) {
            System.out.print("q (constante do custo, >= 0): ");
            if (sc.hasNextLong()) {
                q = sc.nextLong();
                if (q >= 0) {
                    break;
                }
                System.out.println("Valor invalido. Informe um numero >= 0.");
            } else {
                System.out.println("Entrada invalida. Digite um numero inteiro.");
                sc.next();
            }
        }

        long base = 0;
        while (true) {
            System.out.print("tamanho do caso base (ex.: 1): ");
            if (sc.hasNextLong()) {
                base = sc.nextLong();
                if (base >= 1) {
                    break;
                }
                System.out.println("Valor invalido. Informe um numero >= 1.");
            } else {
                System.out.println("Entrada invalida. Digite um numero inteiro.");
                sc.next();
            }
        }

        System.out.println();

        String custoOriginal;
        {
            StringBuilder cb = new StringBuilder();
            if (p != 0) {
                if (p == 1) {
                    cb.append("n");
                } else {
                    cb.append(p + "n");
                }
            }
            if (q != 0) {
                if (cb.length() > 0) {
                    cb.append(" + " + q);
                } else {
                    cb.append(q);
                }
            }
            if (cb.length() == 0) {
                cb.append("0");
            }
            custoOriginal = cb.toString();
        }

        System.out.println("Recorrencia informada:");
        if (divisivo) {
            String coefA = (a == 1) ? "" : a + " ";
            System.out.println("  T(n) = " + coefA + "T(n/" + bc + ") + " + custoOriginal);
        } else {
            String coefA = (a == 1) ? "" : a + " ";
            System.out.println("  T(n) = " + coefA + "T(n - " + bc + ") + " + custoOriginal);
        }
        System.out.println();

        System.out.println("Leitura dos termos:");
        System.out.println("  T(n)  : tempo para resolver o problema de tamanho n.");
        if (divisivo) {
            String coefA = (a == 1) ? "" : a + " x ";
            System.out.println("  " + coefA + "T(n/" + bc + ") : chamada(s) recursiva(s) com o tamanho dividido por " + bc + ".");
        } else {
            String coefA = (a == 1) ? "" : a + " x ";
            System.out.println("  " + coefA + "T(n - " + bc + ") : chamada(s) recursiva(s) com o tamanho reduzido em " + bc + ".");
        }
        System.out.println("  " + custoOriginal + " : custo de trabalho fora da recursao (funcao f(n)).");
        System.out.println();

        System.out.println("Expandindo a relacao de recorrencia (metodo da substituicao):");
        for (int k = 1; k <= 4; k++) {
            long ak = 1;
            for (int e = 0; e < k; e++) {
                ak = ak * a;
            }

            String recursivo;
            long cnNum;
            long cnDen;
            long constT;

            if (divisivo) {
                long bk = 1;
                for (int e = 0; e < k; e++) {
                    bk = bk * bc;
                }
                String coef = (ak == 1) ? "" : ak + " ";
                recursivo = coef + "T(n/" + bk + ")";

                long somaNum = 0;
                long somaDen = 1;
                for (int i = 0; i < k; i++) {
                    long ai = 1;
                    for (int e = 0; e < i; e++) {
                        ai = ai * a;
                    }
                    long bi = 1;
                    for (int e = 0; e < i; e++) {
                        bi = bi * bc;
                    }
                    long novoNum = somaNum * bi + ai * somaDen;
                    long novoDen = somaDen * bi;
                    long x = Math.abs(novoNum);
                    long y = Math.abs(novoDen);
                    while (y != 0) {
                        long t = y;
                        y = x % y;
                        x = t;
                    }
                    long g = (x == 0) ? 1 : x;
                    somaNum = novoNum / g;
                    somaDen = novoDen / g;
                }
                cnNum = p * somaNum;
                cnDen = somaDen;
                long x = Math.abs(cnNum);
                long y = Math.abs(cnDen);
                while (y != 0) {
                    long t = y;
                    y = x % y;
                    x = t;
                }
                long g = (x == 0) ? 1 : x;
                cnNum = cnNum / g;
                cnDen = cnDen / g;

                long somaA = 0;
                for (int i = 0; i < k; i++) {
                    long ai = 1;
                    for (int e = 0; e < i; e++) {
                        ai = ai * a;
                    }
                    somaA = somaA + ai;
                }
                constT = q * somaA;
            } else {
                String coef = (ak == 1) ? "" : ak + " ";
                long dec = (long) k * bc;
                recursivo = coef + "T(n - " + dec + ")";

                long somaA = 0;
                for (int i = 0; i < k; i++) {
                    long ai = 1;
                    for (int e = 0; e < i; e++) {
                        ai = ai * a;
                    }
                    somaA = somaA + ai;
                }
                cnNum = p * somaA;
                cnDen = 1;

                long somaIA = 0;
                for (int i = 0; i < k; i++) {
                    long ai = 1;
                    for (int e = 0; e < i; e++) {
                        ai = ai * a;
                    }
                    somaIA = somaIA + (long) i * ai;
                }
                constT = q * somaA - p * bc * somaIA;
            }

            StringBuilder cb = new StringBuilder();
            if (cnNum != 0) {
                if (cnDen == 1) {
                    if (cnNum == 1) {
                        cb.append("n");
                    } else if (cnNum == -1) {
                        cb.append("-n");
                    } else {
                        cb.append(cnNum + "n");
                    }
                } else {
                    cb.append("(" + cnNum + "/" + cnDen + ")n");
                }
            }
            if (constT != 0) {
                if (cb.length() > 0) {
                    cb.append(constT > 0 ? " + " + constT : " - " + (-constT));
                } else {
                    cb.append(constT);
                }
            }
            if (cb.length() == 0) {
                cb.append("0");
            }
            String custo = cb.toString();

            if (custo.equals("0")) {
                System.out.println("  k=" + k + ":  T(n) = " + recursivo);
            } else {
                System.out.println("  k=" + k + ":  T(n) = " + recursivo + " + " + custo);
            }
        }
        System.out.println();

        System.out.println("Forma geral (apos k expansoes):");
        if (divisivo) {
            System.out.println("  T(n) = a^k T(n/b^k) + SOMA(i=0..k-1) a^i f(n/b^i)");
            System.out.println("  com a=" + a + ", b=" + bc + ", f(n)=" + custoOriginal);
            System.out.println();
            System.out.println("Chegando ao caso base (tamanho " + base + "):");
            if (base == 1) {
                System.out.println("  n / b^k = 1  =>  b^k = n  =>  k = log base " + bc + " de n");
            } else {
                System.out.println("  n / b^k = " + base + "  =>  b^k = n/" + base + "  =>  k = log base " + bc + " de n/" + base);
            }
        } else {
            System.out.println("  T(n) = a^k T(n - k*c) + SOMA(i=0..k-1) a^i f(n - i*c)");
            System.out.println("  com a=" + a + ", c=" + bc + ", f(n)=" + custoOriginal);
            System.out.println();
            System.out.println("Chegando ao caso base (tamanho " + base + "):");
            System.out.println("  n - k*c = " + base + "  =>  k = (n - " + base + ")/" + bc);
        }
        System.out.println();

        System.out.println("Complexidade da recorrencia:");
        String bigO;
        if (divisivo) {
            int d = (p != 0) ? 1 : 0;
            long bd = 1;
            for (int e = 0; e < d; e++) {
                bd = bd * bc;
            }
            if (a == bd) {
                bigO = (d == 0) ? "O(log n)" : "O(n log n)";
            } else if (a > bd) {
                double ex = Math.log(a) / Math.log(bc);
                long arred = Math.round(ex);
                if (Math.abs(ex - arred) < 0.000000001) {
                    bigO = "O(n^" + arred + ")";
                } else {
                    bigO = "O(n^" + String.format(java.util.Locale.US, "%.3f", ex) + ")";
                }
            } else {
                bigO = (d == 1) ? "O(n)" : "O(1)";
            }
        } else {
            if (a == 1) {
                bigO = (p != 0) ? "O(n^2)" : "O(n)";
            } else if (bc == 1) {
                bigO = "O(" + a + "^n)";
            } else {
                bigO = "O(" + a + "^(n/" + bc + "))";
            }
        }
        System.out.println("  " + bigO);

        sc.close();
    }

}
