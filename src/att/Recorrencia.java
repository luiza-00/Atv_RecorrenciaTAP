package att;

import java.util.Scanner;

public class Recorrencia {

    static final int FORMA_SUBTRATIVA = 1;
    static final int FORMA_DIVISIVA = 2;
    static final long N_MAXIMO = 1000;
    static final long GRAU_MAXIMO = 10;
    static final int TERMOS_DA_SERIE = 60;
    static final double LOGARITMO_DE_DOIS = serieDoLogaritmo(1.0 / 3.0, 0);

    static long multiplicar(long x, long y) {
        if (x != 0 && y > Long.MAX_VALUE / x) {
            throw new ArithmeticException("estouro");
        }
        return x * y;
    }

    static long somar(long x, long y) {
        if (y > Long.MAX_VALUE - x) {
            throw new ArithmeticException("estouro");
        }
        return x + y;
    }

    static long potencia(long base, long expoente) {
        if (expoente == 0) {
            return 1;
        }
        return multiplicar(base, potencia(base, expoente - 1));
    }

    static double potenciaReal(double base, int expoente) {
        if (expoente == 0) {
            return 1.0;
        }
        return base * potenciaReal(base, expoente - 1);
    }

    static double serieDoLogaritmo(double y, int termo) {
        if (termo == TERMOS_DA_SERIE) {
            return 0.0;
        }
        int expoenteImpar = 2 * termo + 1;
        return 2.0 * potenciaReal(y, expoenteImpar) / expoenteImpar + serieDoLogaritmo(y, termo + 1);
    }

    static double logaritmoNatural(double x) {
        if (x > 2.0) {
            return logaritmoNatural(x / 2.0) + LOGARITMO_DE_DOIS;
        }
        return serieDoLogaritmo((x - 1.0) / (x + 1.0), 0);
    }

    static long logaritmoInteiro(long valor, long base) {
        if (valor == 1) {
            return 0;
        }
        if (valor % base != 0) {
            return -1;
        }
        long restante = logaritmoInteiro(valor / base, base);
        return (restante < 0) ? -1 : restante + 1;
    }

    static String duasCasasDecimais(double valor) {
        long centesimos = (long) (valor * 100.0 + 0.5);
        long parteInteira = centesimos / 100;
        long parteDecimal = centesimos % 100;
        return parteInteira + "." + (parteDecimal < 10 ? "0" : "") + parteDecimal;
    }

    static String potenciaDeN(long grau) {
        return (grau == 1) ? "n" : "n^" + grau;
    }

    static long expandir(
            StringBuilder texto,
            boolean ehDivisiva,
            long quantidadeChamadas,
            long fatorReducao,
            long coeficienteN,
            long grauN,
            long constanteCusto,
            long tamanhoCasoBase,
            long valorCasoBase,
            long tamanhoAtual,
            long multiplicador,
            long custoAcumulado) {

        if (tamanhoAtual <= tamanhoCasoBase) {
            long resultado = somar(multiplicar(multiplicador, valorCasoBase), custoAcumulado);
            texto.append("  = ").append(multiplicador).append(" x ").append(valorCasoBase).append(" + ").append(custoAcumulado)
                .append(" = ").append(resultado).append("   (caso base: T(").append(tamanhoAtual).append(") = ").append(valorCasoBase).append(")\n");
            return resultado;
        }

        long tamanhoSubproblema = ehDivisiva ? tamanhoAtual / fatorReducao : tamanhoAtual - fatorReducao;
        long custoDoNivel = somar(multiplicar(coeficienteN, potencia(tamanhoAtual, grauN)), constanteCusto);
        long novoCusto = somar(custoAcumulado, multiplicar(multiplicador, custoDoNivel));
        long novoMultiplicador = multiplicar(multiplicador, quantidadeChamadas);

        texto.append("  = ").append(novoMultiplicador).append(" x T(").append(tamanhoSubproblema).append(") + ").append(novoCusto).append("\n");

        return expandir(texto, ehDivisiva, quantidadeChamadas, fatorReducao, coeficienteN, grauN, constanteCusto,
                tamanhoCasoBase, valorCasoBase, tamanhoSubproblema, novoMultiplicador, novoCusto);
    }

    static boolean chamadasMenoresQuePotencia(long quantidadeChamadas, long divisor, long grau) {
        try {
            return quantidadeChamadas < potencia(divisor, grau);
        } catch (ArithmeticException estouro) {
            return true;
        }
    }

    static String complexidadeDivisiva(long quantidadeChamadas, long divisor, long coeficienteN, long grauN) {
        long grauDoCusto = (coeficienteN != 0) ? grauN : 0;
        if (chamadasMenoresQuePotencia(quantidadeChamadas, divisor, grauDoCusto)) {
            return (grauDoCusto == 0) ? "O(1)" : "O(" + potenciaDeN(grauDoCusto) + ")";
        }
        if (quantidadeChamadas == potencia(divisor, grauDoCusto)) {
            return (grauDoCusto == 0) ? "O(log n)" : "O(" + potenciaDeN(grauDoCusto) + " log n)";
        }
        long expoenteExato = logaritmoInteiro(quantidadeChamadas, divisor);
        if (expoenteExato >= 0) {
            return "O(" + potenciaDeN(expoenteExato) + ")";
        }
        double expoente = logaritmoNatural(quantidadeChamadas) / logaritmoNatural(divisor);
        return "O(n^" + duasCasasDecimais(expoente) + ")   (expoente = log base " + divisor + " de " + quantidadeChamadas + ")";
    }

    static String complexidadeSubtrativa(long quantidadeChamadas, long reducao, long coeficienteN, long grauN) {
        if (quantidadeChamadas == 1) {
            long grauDoCusto = (coeficienteN != 0) ? grauN : 0;
            return "O(" + potenciaDeN(grauDoCusto + 1) + ")";
        }
        return (reducao == 1) ? "O(" + quantidadeChamadas + "^n)" : "O(" + quantidadeChamadas + "^(n/" + reducao + "))";
    }

    static String resolverRecorrencia(
            int forma,
            long quantidadeChamadas,
            long fatorReducao,
            long coeficienteN,
            long grauN,
            long constanteCusto,
            long tamanhoCasoBase,
            long valorCasoBase,
            long n) {

        StringBuilder texto = new StringBuilder();
        boolean ehDivisiva = (forma == FORMA_DIVISIVA);
        String chamadaRecursiva = ehDivisiva ? "T(n/" + fatorReducao + ")" : "T(n - " + fatorReducao + ")";

        texto.append("Recorrencia: T(n) = ").append(quantidadeChamadas).append(" x ").append(chamadaRecursiva)
            .append(" + ").append(coeficienteN).append(potenciaDeN(grauN)).append(" + ").append(constanteCusto).append("\n");
        texto.append("Caso base:   T(n) = ").append(valorCasoBase).append(" para n <= ").append(tamanhoCasoBase).append("\n\n");

        texto.append("Expansao (metodo da substituicao):\n");
        texto.append("T(").append(n).append(")\n");
        try {
            long resultado = expandir(texto, ehDivisiva, quantidadeChamadas, fatorReducao, coeficienteN, grauN, constanteCusto,
                    tamanhoCasoBase, valorCasoBase, n, 1, 0);
            texto.append("\nResultado: T(").append(n).append(") = ").append(resultado).append("\n\n");
        } catch (ArithmeticException estouro) {
            texto.append("\nO valor de T(").append(n).append(") passou do maior numero suportado (").append(Long.MAX_VALUE)
                .append("). Tente um n menor.\n\n");
        }

        texto.append("Forma geral apos k expansoes:\n");
        if (ehDivisiva) {
            texto.append("  T(n) = a^k x T(n/b^k) + soma dos custos de cada nivel\n");
            texto.append("  caso base quando n/b^k = ").append(tamanhoCasoBase).append("  =>  k = log base ").append(fatorReducao).append(" de (n/").append(tamanhoCasoBase).append(")\n\n");
        } else {
            texto.append("  T(n) = a^k x T(n - k*c) + soma dos custos de cada nivel\n");
            texto.append("  caso base quando n - k*c = ").append(tamanhoCasoBase).append("  =>  k = (n - ").append(tamanhoCasoBase).append(")/").append(fatorReducao).append("\n\n");
        }

        String complexidade = ehDivisiva
                ? complexidadeDivisiva(quantidadeChamadas, fatorReducao, coeficienteN, grauN)
                : complexidadeSubtrativa(quantidadeChamadas, fatorReducao, coeficienteN, grauN);
        texto.append("Complexidade: ").append(complexidade).append("\n");
        return texto.toString();
    }

    static long lerNumero(Scanner entrada, String rotulo, long minimo, long maximo) {
        while (true) {
            System.out.print(rotulo);
            if (entrada.hasNextLong()) {
                long valor = entrada.nextLong();
                if (valor >= minimo && valor <= maximo) {
                    return valor;
                }
                System.out.println("Valor invalido. Informe um numero entre " + minimo + " e " + maximo + ".");
            } else {
                System.out.println("Entrada invalida. Digite um numero inteiro.");
                entrada.next();
            }
        }
    }

    public static void main(String[] args) {
        Scanner entrada = new Scanner(System.in);
        System.out.println("===== RESOLVEDOR DE RELACOES DE RECORRENCIA =====");
        System.out.println("  " + FORMA_SUBTRATIVA + " - Subtrativa: T(n) = a*T(n - c) + (p*n^d + q)");
        System.out.println("  " + FORMA_DIVISIVA + " - Divisiva:   T(n) = a*T(n / b) + (p*n^d + q)");
        int forma = (int) lerNumero(entrada, "Escolha a forma (1 ou 2): ", FORMA_SUBTRATIVA, FORMA_DIVISIVA);
        long quantidadeChamadas = lerNumero(entrada, "a (quantidade de chamadas recursivas): ", 1, Long.MAX_VALUE);
        long fatorReducao = (forma == FORMA_DIVISIVA)
                ? lerNumero(entrada, "b (fator de divisao, >= 2): ", 2, Long.MAX_VALUE)
                : lerNumero(entrada, "c (reducao de n por chamada, >= 1): ", 1, Long.MAX_VALUE);
        long coeficienteN = lerNumero(entrada, "p (numero na frente do n em f(n) = p*n^d + q): ", 0, Long.MAX_VALUE);
        long grauN = lerNumero(entrada, "d (expoente do n em f(n) = p*n^d + q, 1 a " + GRAU_MAXIMO + "): ", 1, GRAU_MAXIMO);
        long constanteCusto = lerNumero(entrada, "q (numero sozinho em f(n) = p*n^d + q): ", 0, Long.MAX_VALUE);
        long tamanhoCasoBase = lerNumero(entrada, "tamanho do caso base (ex.: 1): ", 1, Long.MAX_VALUE);
        long valorCasoBase = lerNumero(entrada, "valor de T no caso base (ex.: 1): ", 0, Long.MAX_VALUE);
        long n = lerNumero(entrada, "n para calcular T(n) (1 a " + N_MAXIMO + "): ", 1, N_MAXIMO);

        System.out.println();
        System.out.print(resolverRecorrencia(forma, quantidadeChamadas, fatorReducao, coeficienteN, grauN, constanteCusto, tamanhoCasoBase, valorCasoBase, n));
        entrada.close();
    }

}
