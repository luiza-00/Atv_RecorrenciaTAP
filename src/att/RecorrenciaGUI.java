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

    private final JComboBox<String> forma;
    private final JLabel rotuloBc;
    private final JTextField campoA;
    private final JTextField campoBc;
    private final JTextField campoP;
    private final JTextField campoQ;
    private final JTextField campoBase;
    private final JTextArea saida;

    public RecorrenciaGUI() {
        super("Resolvedor de Relacoes de Recorrencia");

        forma = new JComboBox<>(new String[] {
            "Subtrativa: T(n) = a*T(n - c) + (p*n + q)",
            "Divisiva:   T(n) = a*T(n / b) + (p*n + q)"
        });

        rotuloBc = new JLabel();
        campoA = new JTextField();
        campoBc = new JTextField();
        campoP = new JTextField();
        campoQ = new JTextField();
        campoBase = new JTextField("1");

        saida = new JTextArea(24, 60);
        saida.setEditable(false);
        saida.setFont(new Font("Monospaced", Font.PLAIN, 13));

        JButton resolver = new JButton("Resolver");
        JButton limpar = new JButton("Limpar");

        JPanel entradas = new JPanel(new GridLayout(7, 2, 8, 6));
        entradas.add(new JLabel("Forma:"));
        entradas.add(forma);
        entradas.add(new JLabel("a (chamadas recursivas, >= 1):"));
        entradas.add(campoA);
        entradas.add(rotuloBc);
        entradas.add(campoBc);
        entradas.add(new JLabel("p (coef. de n no custo, >= 0):"));
        entradas.add(campoP);
        entradas.add(new JLabel("q (constante do custo, >= 0):"));
        entradas.add(campoQ);
        entradas.add(new JLabel("tamanho do caso base (ex.: 1):"));
        entradas.add(campoBase);
        entradas.add(resolver);
        entradas.add(limpar);

        setLayout(new BorderLayout(10, 10));
        add(entradas, BorderLayout.NORTH);
        add(new JScrollPane(saida), BorderLayout.CENTER);

        forma.addActionListener(e -> atualizarRotulo());
        resolver.addActionListener(e -> executar());
        limpar.addActionListener(e -> saida.setText(""));

        atualizarRotulo();

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        pack();
        setLocationRelativeTo(null);
    }

    private void atualizarRotulo() {
        if (forma.getSelectedIndex() == 1) {
            rotuloBc.setText("b (fator de divisao, >= 2):");
        } else {
            rotuloBc.setText("c (reducao de n por chamada, >= 1):");
        }
    }

    private long lerLongo(String texto, long minimo) {
        long valor = Long.parseLong(texto.trim());
        if (valor < minimo) {
            throw new NumberFormatException("Valor menor que o minimo " + minimo);
        }
        return valor;
    }

    private void executar() {
        try {
            int f = (forma.getSelectedIndex() == 1) ? 2 : 1;
            long a = lerLongo(campoA.getText(), 1);
            long bc = lerLongo(campoBc.getText(), f == 2 ? 2 : 1);
            long p = lerLongo(campoP.getText(), 0);
            long q = lerLongo(campoQ.getText(), 0);
            long base = lerLongo(campoBase.getText(), 1);
            saida.setText(Recorrencia.resolver(f, a, bc, p, q, base));
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Entrada invalida. Preencha os campos com numeros inteiros validos.", "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new RecorrenciaGUI().setVisible(true));
    }

}
