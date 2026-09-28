package att;

public class Formatador {

    public static String coeficienteN(Fracao coeficiente) {
        long numerador = coeficiente.getNumerador();
        long denominador = coeficiente.getDenominador();
        if (numerador == 0) {
            return "";
        }
        if (denominador == 1) {
            if (numerador == 1) {
                return "n";
            }
            if (numerador == -1) {
                return "-n";
            }
            return numerador + "n";
        }
        return "(" + numerador + "/" + denominador + ")n";
    }

    public static String custo(Fracao coeficienteN, long constante) {
        StringBuilder texto = new StringBuilder();
        String parteN = coeficienteN(coeficienteN);
        if (!parteN.isEmpty()) {
            texto.append(parteN);
        }
        if (constante != 0) {
            if (texto.length() > 0) {
                texto.append(constante > 0 ? " + " + constante : " - " + (-constante));
            } else {
                texto.append(constante);
            }
        }
        if (texto.length() == 0) {
            texto.append("0");
        }
        return texto.toString();
    }

    public static String custoOriginal(long coeficienteN, long constante) {
        return custo(new Fracao(coeficienteN, 1), constante);
    }

    public static String coeficienteA(long a) {
        return (a == 1) ? "" : a + " ";
    }

}
