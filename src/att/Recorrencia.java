package att;

import java.util.Scanner;

public class Recorrencia {

    static long gcd(long a, long b) {
        a = Math.abs(a);
        b = Math.abs(b);
        while (b != 0) {
            long t = b;
            b = a % b;
            a = t;
        }
        return a == 0 ? 1 : a;
    }

    static long[] ratReduce(long num, long den) {
        if (den < 0) {
            num = -num;
            den = -den;
        }
        long g = gcd(num, den);
        return new long[] { num / g, den / g };
    }

    static long[] ratAdd(long[] x, long[] y) {
        return ratReduce(x[0] * y[1] + y[0] * x[1], x[1] * y[1]);
    }

    static long powL(long base, int exp) {
        long r = 1;
        for (int i = 0; i < exp; i++) {
            r *= base;
        }
        return r;
    }

    static String fmtCoefN(long[] r) {
        if (r[0] == 0) {
            return "";
        }
        if (r[1] == 1) {
            if (r[0] == 1) {
                return "n";
            }
            if (r[0] == -1) {
                return "-n";
            }
            return r[0] + "n";
        }
        return "(" + r[0] + "/" + r[1] + ")n";
    }

    static String fmtCost(long[] coefN, long constTerm) {
        String cn = fmtCoefN(coefN);
        StringBuilder sb = new StringBuilder();
        if (!cn.isEmpty()) {
            sb.append(cn);
        }
        if (constTerm != 0) {
            if (sb.length() > 0) {
                sb.append(constTerm > 0 ? " + " + constTerm : " - " + (-constTerm));
            } else {
                sb.append(constTerm);
            }
        }
        if (sb.length() == 0) {
            sb.append("0");
        }
        return sb.toString();
    }

    static String custoOriginal(long p, long q) {
        return fmtCost(new long[] { p, 1 }, q);
    }

    static String recursivoDivisivo(long a, int k, long b) {
        String coef = (powL(a, k) == 1) ? "" : powL(a, k) + " ";
        return coef + "T(n/" + powL(b, k) + ")";
    }

    static String recursivoSubtrativo(long a, int k, long c) {
        String coef = (powL(a, k) == 1) ? "" : powL(a, k) + " ";
        long dec = (long) k * c;
        return coef + "T(n - " + dec + ")";
    }

    static long[] custoCoefNDivisivo(long a, long b, long p, int k) {
        long[] soma = new long[] { 0, 1 };
        for (int i = 0; i < k; i++) {
            soma = ratAdd(soma, new long[] { powL(a, i), powL(b, i) });
        }
        return ratReduce(p * soma[0], soma[1]);
    }

    static long custoConstDivisivo(long a, long q, int k) {
        long soma = 0;
        for (int i = 0; i < k; i++) {
            soma += powL(a, i);
        }
        return q * soma;
    }

    static long[] custoCoefNSubtrativo(long a, long p, int k) {
        long soma = 0;
        for (int i = 0; i < k; i++) {
            soma += powL(a, i);
        }
        return new long[] { p * soma, 1 };
    }

    static long custoConstSubtrativo(long a, long c, long p, long q, int k) {
        long somaA = 0;
        long somaIA = 0;
        for (int i = 0; i < k; i++) {
            somaA += powL(a, i);
            somaIA += (long) i * powL(a, i);
        }
        return q * somaA - p * c * somaIA;
    }

    static String complexidadeDivisivo(long a, long b, long p) {
        int d = (p != 0) ? 1 : 0;
        long bd = powL(b, d);
        if (a == bd) {
            return (d == 0) ? "O(log n)" : "O(n log n)";
        }
        if (a > bd) {
            double ex = Math.log(a) / Math.log(b);
            long arred = Math.round(ex);
            if (Math.abs(ex - arred) < 1e-9) {
                return "O(n^" + arred + ")";
            }
            return "O(n^" + String.format(java.util.Locale.US, "%.3f", ex) + ")  (expoente = log base " + b + " de " + a + ")";
        }
        return (d == 1) ? "O(n)" : "O(1)";
    }

    static String complexidadeSubtrativo(long a, long c, long p) {
        if (a == 1) {
            return (p != 0) ? "O(n^2)" : "O(n)";
        }
        return (c == 1) ? "O(" + a + "^n)" : "O(" + a + "^(n/" + c + "))";
    }

    static String resolver(int forma, long a, long bc, long p, long q, long base) {
        StringBuilder sb = new StringBuilder();
        boolean divisivo = (forma == 2);

        sb.append("Recorrencia informada:\n");
        if (divisivo) {
            String coefA = (a == 1) ? "" : a + " ";
            sb.append("  T(n) = ").append(coefA).append("T(n/").append(bc).append(") + ").append(custoOriginal(p, q)).append("\n");
        } else {
            String coefA = (a == 1) ? "" : a + " ";
            sb.append("  T(n) = ").append(coefA).append("T(n - ").append(bc).append(") + ").append(custoOriginal(p, q)).append("\n");
        }
        sb.append("\n");

        sb.append("Leitura dos termos:\n");
        sb.append("  T(n)  : tempo para resolver o problema de tamanho n.\n");
        if (divisivo) {
            sb.append("  ").append(a == 1 ? "" : a + " x ").append("T(n/").append(bc).append(") : chamada(s) recursiva(s) com o tamanho dividido por ").append(bc).append(".\n");
        } else {
            sb.append("  ").append(a == 1 ? "" : a + " x ").append("T(n - ").append(bc).append(") : chamada(s) recursiva(s) com o tamanho reduzido em ").append(bc).append(".\n");
        }
        sb.append("  ").append(custoOriginal(p, q)).append(" : custo de trabalho fora da recursao (funcao f(n)).\n\n");

        sb.append("Expandindo a relacao de recorrencia (metodo da substituicao):\n");
        int passos = 4;
        for (int k = 1; k <= passos; k++) {
            String rec;
            long[] coefN;
            long constT;
            if (divisivo) {
                rec = recursivoDivisivo(a, k, bc);
                coefN = custoCoefNDivisivo(a, bc, p, k);
                constT = custoConstDivisivo(a, q, k);
            } else {
                rec = recursivoSubtrativo(a, k, bc);
                coefN = custoCoefNSubtrativo(a, p, k);
                constT = custoConstSubtrativo(a, bc, p, q, k);
            }
            sb.append("  k=").append(k).append(":  T(n) = ").append(rec);
            String custo = fmtCost(coefN, constT);
            if (!custo.equals("0")) {
                sb.append(" + ").append(custo);
            }
            sb.append("\n");
        }
        sb.append("\n");

        sb.append("Forma geral (apos k expansoes):\n");
        if (divisivo) {
            sb.append("  T(n) = a^k T(n/b^k) + SOMA(i=0..k-1) a^i f(n/b^i)\n");
            sb.append("  com a=").append(a).append(", b=").append(bc).append(", f(n)=").append(custoOriginal(p, q)).append("\n\n");
            sb.append("Chegando ao caso base (tamanho ").append(base).append("):\n");
            sb.append("  n / b^k = ").append(base).append("  =>  b^k = n").append(base == 1 ? "" : "/" + base).append("  =>  k = log base ").append(bc).append(" de n").append(base == 1 ? "" : "/" + base).append("\n\n");
        } else {
            sb.append("  T(n) = a^k T(n - k*c) + SOMA(i=0..k-1) a^i f(n - i*c)\n");
            sb.append("  com a=").append(a).append(", c=").append(bc).append(", f(n)=").append(custoOriginal(p, q)).append("\n\n");
            sb.append("Chegando ao caso base (tamanho ").append(base).append("):\n");
            sb.append("  n - k*c = ").append(base).append("  =>  k = (n - ").append(base).append(")/").append(bc).append("\n\n");
        }

        sb.append("Complexidade da recorrencia:\n");
        sb.append("  ").append(divisivo ? complexidadeDivisivo(a, bc, p) : complexidadeSubtrativo(a, bc, p)).append("\n");
        return sb.toString();
    }

    static int lerInteiro(Scanner sc, String rotulo, int minimo, int maximo) {
        while (true) {
            System.out.print(rotulo);
            if (sc.hasNextInt()) {
                int valor = sc.nextInt();
                if (valor >= minimo && valor <= maximo) {
                    return valor;
                }
                System.out.println("Valor invalido. Informe um numero entre " + minimo + " e " + maximo + ".");
            } else {
                System.out.println("Entrada invalida. Digite um numero inteiro.");
                sc.next();
            }
        }
    }

    static long lerLongo(Scanner sc, String rotulo, long minimo) {
        while (true) {
            System.out.print(rotulo);
            if (sc.hasNextLong()) {
                long valor = sc.nextLong();
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
        System.out.println("===== RESOLVEDOR DE RELACOES DE RECORRENCIA =====");
        System.out.println("Formas aceitas:");
        System.out.println("  1 - Subtrativa: T(n) = a*T(n - c) + (p*n + q)");
        System.out.println("  2 - Divisiva:   T(n) = a*T(n / b) + (p*n + q)");
        int forma = lerInteiro(sc, "Escolha a forma (1 ou 2): ", 1, 2);
        long a = lerLongo(sc, "a (quantidade de chamadas recursivas, >= 1): ", 1);
        long bc;
        if (forma == 2) {
            bc = lerLongo(sc, "b (fator de divisao, >= 2): ", 2);
        } else {
            bc = lerLongo(sc, "c (reducao de n por chamada, >= 1): ", 1);
        }
        long p = lerLongo(sc, "p (coeficiente de n no custo f(n)=p*n+q, >= 0): ", 0);
        long q = lerLongo(sc, "q (constante do custo, >= 0): ", 0);
        long base = lerLongo(sc, "tamanho do caso base (ex.: 1): ", 1);

        System.out.println();
        System.out.print(resolver(forma, a, bc, p, q, base));
        sc.close();
    }

}
