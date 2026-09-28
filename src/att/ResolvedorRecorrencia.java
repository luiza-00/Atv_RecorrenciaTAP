package att;

public class ResolvedorRecorrencia {

    private static final int PASSOS_EXPANSAO = 4;

    private final Recorrencia recorrencia;

    public ResolvedorRecorrencia(Recorrencia recorrencia) {
        this.recorrencia = recorrencia;
    }

    public String resolver() {
        StringBuilder texto = new StringBuilder();
        texto.append(montarCabecalho());
        texto.append("\n");
        texto.append(montarLeituraTermos());
        texto.append("\n");
        texto.append(montarExpansao());
        texto.append("\n");
        texto.append(montarFormaGeral());
        texto.append("\n");
        texto.append(montarComplexidade());
        return texto.toString();
    }

    private String montarCabecalho() {
        long a = recorrencia.getA();
        long fator = recorrencia.getFatorTamanho();
        String custo = Formatador.custoOriginal(recorrencia.getCoeficienteN(), recorrencia.getConstante());
        String coefA = Formatador.coeficienteA(a);
        StringBuilder texto = new StringBuilder();
        texto.append("Recorrencia informada:\n");
        if (recorrencia.ehDivisiva()) {
            texto.append("  T(n) = ").append(coefA).append("T(n/").append(fator).append(") + ").append(custo).append("\n");
        } else {
            texto.append("  T(n) = ").append(coefA).append("T(n - ").append(fator).append(") + ").append(custo).append("\n");
        }
        return texto.toString();
    }

    private String montarLeituraTermos() {
        long a = recorrencia.getA();
        long fator = recorrencia.getFatorTamanho();
        String custo = Formatador.custoOriginal(recorrencia.getCoeficienteN(), recorrencia.getConstante());
        String coefA = (a == 1) ? "" : a + " x ";
        StringBuilder texto = new StringBuilder();
        texto.append("Leitura dos termos:\n");
        texto.append("  T(n)  : tempo para resolver o problema de tamanho n.\n");
        if (recorrencia.ehDivisiva()) {
            texto.append("  ").append(coefA).append("T(n/").append(fator).append(") : chamada(s) recursiva(s) com o tamanho dividido por ").append(fator).append(".\n");
        } else {
            texto.append("  ").append(coefA).append("T(n - ").append(fator).append(") : chamada(s) recursiva(s) com o tamanho reduzido em ").append(fator).append(".\n");
        }
        texto.append("  ").append(custo).append(" : custo de trabalho fora da recursao (funcao f(n)).\n");
        return texto.toString();
    }

    private String montarExpansao() {
        StringBuilder texto = new StringBuilder();
        texto.append("Expandindo a relacao de recorrencia (metodo da substituicao):\n");
        for (int k = 1; k <= PASSOS_EXPANSAO; k++) {
            String recursivo = recorrencia.ehDivisiva() ? termoRecursivoDivisivo(k) : termoRecursivoSubtrativo(k);
            Fracao coeficienteN = recorrencia.ehDivisiva() ? custoCoeficienteNDivisivo(k) : custoCoeficienteNSubtrativo(k);
            long constante = recorrencia.ehDivisiva() ? custoConstanteDivisivo(k) : custoConstanteSubtrativo(k);
            String custo = Formatador.custo(coeficienteN, constante);
            texto.append("  k=").append(k).append(":  T(n) = ").append(recursivo);
            if (!custo.equals("0")) {
                texto.append(" + ").append(custo);
            }
            texto.append("\n");
        }
        return texto.toString();
    }

    private String termoRecursivoDivisivo(int k) {
        long ak = Matematica.potencia(recorrencia.getA(), k);
        long bk = Matematica.potencia(recorrencia.getFatorTamanho(), k);
        String coef = (ak == 1) ? "" : ak + " ";
        return coef + "T(n/" + bk + ")";
    }

    private String termoRecursivoSubtrativo(int k) {
        long ak = Matematica.potencia(recorrencia.getA(), k);
        long decremento = (long) k * recorrencia.getFatorTamanho();
        String coef = (ak == 1) ? "" : ak + " ";
        return coef + "T(n - " + decremento + ")";
    }

    private Fracao custoCoeficienteNDivisivo(int k) {
        long a = recorrencia.getA();
        long b = recorrencia.getFatorTamanho();
        Fracao soma = new Fracao(0, 1);
        for (int i = 0; i < k; i++) {
            Fracao termo = new Fracao(Matematica.potencia(a, i), Matematica.potencia(b, i));
            soma = soma.somar(termo);
        }
        return soma.multiplicarPorInteiro(recorrencia.getCoeficienteN());
    }

    private long custoConstanteDivisivo(int k) {
        long a = recorrencia.getA();
        long soma = 0;
        for (int i = 0; i < k; i++) {
            soma += Matematica.potencia(a, i);
        }
        return recorrencia.getConstante() * soma;
    }

    private Fracao custoCoeficienteNSubtrativo(int k) {
        long a = recorrencia.getA();
        long soma = 0;
        for (int i = 0; i < k; i++) {
            soma += Matematica.potencia(a, i);
        }
        return new Fracao(recorrencia.getCoeficienteN() * soma, 1);
    }

    private long custoConstanteSubtrativo(int k) {
        long a = recorrencia.getA();
        long c = recorrencia.getFatorTamanho();
        long somaPotencias = 0;
        long somaPonderada = 0;
        for (int i = 0; i < k; i++) {
            long ai = Matematica.potencia(a, i);
            somaPotencias += ai;
            somaPonderada += (long) i * ai;
        }
        return recorrencia.getConstante() * somaPotencias - recorrencia.getCoeficienteN() * c * somaPonderada;
    }

    private String montarFormaGeral() {
        long a = recorrencia.getA();
        long fator = recorrencia.getFatorTamanho();
        long base = recorrencia.getCasoBase();
        String custo = Formatador.custoOriginal(recorrencia.getCoeficienteN(), recorrencia.getConstante());
        StringBuilder texto = new StringBuilder();
        texto.append("Forma geral (apos k expansoes):\n");
        if (recorrencia.ehDivisiva()) {
            texto.append("  T(n) = a^k T(n/b^k) + SOMA(i=0..k-1) a^i f(n/b^i)\n");
            texto.append("  com a=").append(a).append(", b=").append(fator).append(", f(n)=").append(custo).append("\n\n");
            texto.append("Chegando ao caso base (tamanho ").append(base).append("):\n");
            if (base == 1) {
                texto.append("  n / b^k = 1  =>  b^k = n  =>  k = log base ").append(fator).append(" de n\n");
            } else {
                texto.append("  n / b^k = ").append(base).append("  =>  b^k = n/").append(base).append("  =>  k = log base ").append(fator).append(" de n/").append(base).append("\n");
            }
        } else {
            texto.append("  T(n) = a^k T(n - k*c) + SOMA(i=0..k-1) a^i f(n - i*c)\n");
            texto.append("  com a=").append(a).append(", c=").append(fator).append(", f(n)=").append(custo).append("\n\n");
            texto.append("Chegando ao caso base (tamanho ").append(base).append("):\n");
            texto.append("  n - k*c = ").append(base).append("  =>  k = (n - ").append(base).append(")/").append(fator).append("\n");
        }
        return texto.toString();
    }

    private String montarComplexidade() {
        StringBuilder texto = new StringBuilder();
        texto.append("Complexidade da recorrencia:\n");
        texto.append("  ").append(calcularComplexidade()).append("\n");
        return texto.toString();
    }

    private String calcularComplexidade() {
        if (recorrencia.ehDivisiva()) {
            return complexidadeDivisiva();
        }
        return complexidadeSubtrativa();
    }

    private String complexidadeDivisiva() {
        long a = recorrencia.getA();
        long b = recorrencia.getFatorTamanho();
        int grau = (recorrencia.getCoeficienteN() != 0) ? 1 : 0;
        long bElevadoGrau = Matematica.potencia(b, grau);
        if (a == bElevadoGrau) {
            return (grau == 0) ? "O(log n)" : "O(n log n)";
        }
        if (a > bElevadoGrau) {
            double expoente = Math.log(a) / Math.log(b);
            long arredondado = Math.round(expoente);
            if (Math.abs(expoente - arredondado) < 0.000000001) {
                return "O(n^" + arredondado + ")";
            }
            return "O(n^" + String.format(java.util.Locale.US, "%.3f", expoente) + ")";
        }
        return (grau == 1) ? "O(n)" : "O(1)";
    }

    private String complexidadeSubtrativa() {
        long a = recorrencia.getA();
        long c = recorrencia.getFatorTamanho();
        if (a == 1) {
            return (recorrencia.getCoeficienteN() != 0) ? "O(n^2)" : "O(n)";
        }
        return (c == 1) ? "O(" + a + "^n)" : "O(" + a + "^(n/" + c + "))";
    }

}
