package att;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.Font;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

public class RecorrenciaGUI extends JFrame {

    private final JTextField campo;
    private final JTextArea saida;

    public RecorrenciaGUI() {
        super("Fatorial Recursivo");

        campo = new JTextField();

        saida = new JTextArea(20, 40);
        saida.setEditable(false);
        saida.setFont(new Font("Monospaced", Font.PLAIN, 13));

        JButton executar = new JButton("Executar");
        JButton limpar = new JButton("Limpar");

        JPanel entradas = new JPanel(new GridLayout(2, 2, 8, 8));
        entradas.add(new JLabel("n (>= 0):"));
        entradas.add(campo);
        entradas.add(executar);
        entradas.add(limpar);

        setLayout(new BorderLayout(10, 10));
        add(entradas, BorderLayout.NORTH);
        add(new JScrollPane(saida), BorderLayout.CENTER);

        executar.addActionListener(e -> executarFatorial());
        limpar.addActionListener(e -> saida.setText(""));

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        pack();
        setLocationRelativeTo(null);
    }

    private int lerInteiro(String texto, int minimo) {
        int valor = Integer.parseInt(texto.trim());
        if (valor < minimo) {
            throw new NumberFormatException("Valor deve ser >= " + minimo);
        }
        return valor;
    }

    private void executarFatorial() {
        StringBuilder sb = new StringBuilder();
        try {
            int n = lerInteiro(campo.getText(), 0);
            long r = Recorrencia.fatorial(n, 0, sb);
            saida.setText("Fatorial de " + n + "\n\nRastro das chamadas:\n" + sb + "\nResultado: " + n + "! = " + r + "\n");
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Entrada invalida. Informe um numero inteiro >= 0.", "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new RecorrenciaGUI().setVisible(true));
    }

}
