package att;

public class Recorrencia {

    public static final int FORMA_SUBTRATIVA = 1;
    public static final int FORMA_DIVISIVA = 2;

    private final int forma;
    private final long a;
    private final long fatorTamanho;
    private final long coeficienteN;
    private final long constante;
    private final long casoBase;

    public Recorrencia(int forma, long a, long fatorTamanho, long coeficienteN, long constante, long casoBase) {
        this.forma = forma;
        this.a = a;
        this.fatorTamanho = fatorTamanho;
        this.coeficienteN = coeficienteN;
        this.constante = constante;
        this.casoBase = casoBase;
    }

    public boolean ehDivisiva() {
        return forma == FORMA_DIVISIVA;
    }

    public int getForma() {
        return forma;
    }

    public long getA() {
        return a;
    }

    public long getFatorTamanho() {
        return fatorTamanho;
    }

    public long getCoeficienteN() {
        return coeficienteN;
    }

    public long getConstante() {
        return constante;
    }

    public long getCasoBase() {
        return casoBase;
    }

}
