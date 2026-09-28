package att;

public class Fracao {

    private long numerador;
    private long denominador;

    public Fracao(long numerador, long denominador) {
        if (denominador < 0) {
            numerador = -numerador;
            denominador = -denominador;
        }
        this.numerador = numerador;
        this.denominador = denominador;
        simplificar();
    }

    public long getNumerador() {
        return numerador;
    }

    public long getDenominador() {
        return denominador;
    }

    public Fracao somar(Fracao outra) {
        long novoNumerador = this.numerador * outra.denominador + outra.numerador * this.denominador;
        long novoDenominador = this.denominador * outra.denominador;
        return new Fracao(novoNumerador, novoDenominador);
    }

    public Fracao multiplicarPorInteiro(long valor) {
        return new Fracao(this.numerador * valor, this.denominador);
    }

    private void simplificar() {
        long a = Math.abs(numerador);
        long b = Math.abs(denominador);
        while (b != 0) {
            long temporario = b;
            b = a % b;
            a = temporario;
        }
        long divisor = (a == 0) ? 1 : a;
        numerador /= divisor;
        denominador /= divisor;
    }

}
