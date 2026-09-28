package att;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.Font;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

public class RecorrenciaGUI extends JFrame {

    private static final int INDICE_FORMA_DIVISIVA = 1;

    private final JComboBox<String> seletorForma;
    private final JLabel rotuloFatorReducao;
    private final JTextField campoQuantidadeChamadas;
    private final JTextField campoFatorReducao;
    private final JTextField campoCoeficienteN;
    private final JTextField campoGrauN;
    private final JTextField campoConstanteCusto;
    private final JTextField campoTamanhoCasoBase;
    private final JTextField campoValorCasoBase;
    private final JTextField campoN;
    private final JTextArea areaResultado;

    public RecorrenciaGUI() {
        super("Resolvedor de Relacoes de Recorrencia");

        seletorForma = new JComboBox<>(new String[] {
            "Subtrativa: T(n) = a*T(n - c) + (p*n^d + q)",
            "Divisiva:   T(n) = a*T(n / b) + (p*n^d + q)"
        });

        rotuloFatorReducao = new JLabel();
        campoQuantidadeChamadas = new JTextField();
        campoFatorReducao = new JTextField();
        campoCoeficienteN = new JTextField();
        campoGrauN = new JTextField("1");
        campoConstanteCusto = new JTextField();
        campoTamanhoCasoBase = new JTextField("1");
        campoValorCasoBase = new JTextField("1");
        campoN = new JTextField();

        areaResultado = new JTextArea(24, 60);
        areaResultado.setEditable(false);
        areaResultado.setFont(new Font("Monospaced", Font.PLAIN, 13));

        JButton botaoResolver = new JButton("Resolver");
        JButton botaoLimpar = new JButton("Limpar");

        JPanel painelEntradas = new JPanel(new GridLayout(10, 2, 8, 6));
        painelEntradas.add(new JLabel("Forma:"));
        painelEntradas.add(seletorForma);
        painelEntradas.add(new JLabel("a (chamadas recursivas, >= 1):"));
        painelEntradas.add(campoQuantidadeChamadas);
        painelEntradas.add(rotuloFatorReducao);
        painelEntradas.add(campoFatorReducao);
        painelEntradas.add(new JLabel("p (numero na frente do n, >= 0):"));
        painelEntradas.add(campoCoeficienteN);
        painelEntradas.add(new JLabel("d (expoente do n, 1 a " + Recorrencia.GRAU_MAXIMO + "):"));
        painelEntradas.add(campoGrauN);
        painelEntradas.add(new JLabel("q (numero sozinho, >= 0):"));
        painelEntradas.add(campoConstanteCusto);
        painelEntradas.add(new JLabel("tamanho do caso base (ex.: 1):"));
        painelEntradas.add(campoTamanhoCasoBase);
        painelEntradas.add(new JLabel("valor de T no caso base (ex.: 1):"));
        painelEntradas.add(campoValorCasoBase);
        painelEntradas.add(new JLabel("n para calcular T(n) (1 a " + Recorrencia.N_MAXIMO + "):"));
        painelEntradas.add(campoN);
        painelEntradas.add(botaoResolver);
        painelEntradas.add(botaoLimpar);

        setLayout(new BorderLayout(10, 10));
        add(painelEntradas, BorderLayout.NORTH);
        add(new JScrollPane(areaResultado), BorderLayout.CENTER);

        seletorForma.addActionListener(evento -> atualizarRotuloFatorReducao());
        botaoResolver.addActionListener(evento -> resolverEntradasInformadas());
        botaoLimpar.addActionListener(evento -> areaResultado.setText(""));

        atualizarRotuloFatorReducao();

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        pack();
        setLocationRelativeTo(null);
    }

    private boolean formaDivisivaSelecionada() {
        return seletorForma.getSelectedIndex() == INDICE_FORMA_DIVISIVA;
    }

    private void atualizarRotuloFatorReducao() {
        if (formaDivisivaSelecionada()) {
            rotuloFatorReducao.setText("b (fator de divisao, >= 2):");
        } else {
            rotuloFatorReducao.setText("c (reducao de n por chamada, >= 1):");
        }
    }

    private long converterNumero(String texto, long minimo) {
        return converterNumero(texto, minimo, Long.MAX_VALUE);
    }

    private long converterNumero(String texto, long minimo, long maximo) {
        long valor = Long.parseLong(texto.trim());
        if (valor < minimo || valor > maximo) {
            throw new NumberFormatException("Valor fora do intervalo " + minimo + " a " + maximo);
        }
        return valor;
    }

    private void resolverEntradasInformadas() {
        try {
            boolean ehDivisiva = formaDivisivaSelecionada();
            int forma = ehDivisiva ? Recorrencia.FORMA_DIVISIVA : Recorrencia.FORMA_SUBTRATIVA;
            long quantidadeChamadas = converterNumero(campoQuantidadeChamadas.getText(), 1);
            long fatorReducao = converterNumero(campoFatorReducao.getText(), ehDivisiva ? 2 : 1);
            long coeficienteN = converterNumero(campoCoeficienteN.getText(), 0);
            long grauN = converterNumero(campoGrauN.getText(), 1, Recorrencia.GRAU_MAXIMO);
            long constanteCusto = converterNumero(campoConstanteCusto.getText(), 0);
            long tamanhoCasoBase = converterNumero(campoTamanhoCasoBase.getText(), 1);
            long valorCasoBase = converterNumero(campoValorCasoBase.getText(), 0);
            long n = converterNumero(campoN.getText(), 1, Recorrencia.N_MAXIMO);
            areaResultado.setText(Recorrencia.resolverRecorrencia(forma, quantidadeChamadas, fatorReducao, coeficienteN, grauN, constanteCusto,
                    tamanhoCasoBase, valorCasoBase, n));
            areaResultado.setCaretPosition(0);
        } catch (NumberFormatException erro) {
            JOptionPane.showMessageDialog(this, "Entrada invalida. Preencha os campos com numeros inteiros validos (d entre 1 e " + Recorrencia.GRAU_MAXIMO + ", n entre 1 e " + Recorrencia.N_MAXIMO + ").", "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new RecorrenciaGUI().setVisible(true));
    }

}
