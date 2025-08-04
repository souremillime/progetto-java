import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;
import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

public class test {

    static class ElementoCheckbox {
        String testo;
        boolean selezionato;

        ElementoCheckbox(String testo) {
            this.testo = testo;
            this.selezionato = false;
        }

        @Override
        public String toString() {
            return testo;
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            String[] elementi = {"Rosso", "Verde", "Blu", "Giallo", "Arancione", "Viola", "Nero", "Bianco"};

            List<ElementoCheckbox> listaCompleta = new ArrayList<>();
            for (String e : elementi) {
                listaCompleta.add(new ElementoCheckbox(e));
            }

            DefaultListModel<ElementoCheckbox> modello = new DefaultListModel<>();
            listaCompleta.forEach(modello::addElement);

            JList<ElementoCheckbox> lista = new JList<>(modello);
            lista.setCellRenderer(new CheckboxListRenderer());
            lista.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

            // Toggle checkbox al click
            lista.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    int index = lista.locationToIndex(e.getPoint());
                    if (index >= 0) {
                        ElementoCheckbox item = modello.getElementAt(index);
                        item.selezionato = !item.selezionato;
                        lista.repaint(lista.getCellBounds(index, index));
                    }
                }
            });

            JTextField campoRicerca = new JTextField(15);

            JPanel panel = new JPanel(new BorderLayout(5,5));
            panel.add(campoRicerca, BorderLayout.NORTH);
            panel.add(new JScrollPane(lista), BorderLayout.CENTER);

            // Filtro
            campoRicerca.getDocument().addDocumentListener(new DocumentListener() {
                private void aggiornaFiltro() {
                    String filtro = campoRicerca.getText().toLowerCase();
                    modello.clear();
                    for (ElementoCheckbox elem : listaCompleta) {
                        if (elem.testo.toLowerCase().contains(filtro)) {
                            modello.addElement(elem);
                        }
                    }
                }

                @Override public void insertUpdate(DocumentEvent e) { aggiornaFiltro(); }
                @Override public void removeUpdate(DocumentEvent e) { aggiornaFiltro(); }
                @Override public void changedUpdate(DocumentEvent e) { aggiornaFiltro(); }
            });

            int result = JOptionPane.showConfirmDialog(
                    null,
                    panel,
                    "Seleziona uno o più colori",
                    JOptionPane.OK_CANCEL_OPTION,
                    JOptionPane.QUESTION_MESSAGE);

            if (result == JOptionPane.OK_OPTION) {
                List<String> selezionati = new ArrayList<>();
                for (int i = 0; i < modello.getSize(); i++) {
                    ElementoCheckbox item = modello.getElementAt(i);
                    if (item.selezionato) {
                        selezionati.add(item.testo);
                    }
                }
                if (!selezionati.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Hai scelto: " + selezionati);
                } else {
                    JOptionPane.showMessageDialog(null, "Nessuna selezione effettuata.");
                }
            } else {
                JOptionPane.showMessageDialog(null, "Dialog cancellato.");
            }
        });
    }

    // Renderer per mostrare checkbox in JList
    static class CheckboxListRenderer extends JCheckBox implements ListCellRenderer<ElementoCheckbox> {

        @Override
        public Component getListCellRendererComponent(JList<? extends ElementoCheckbox> list, ElementoCheckbox value, int index,
                                                      boolean isSelected, boolean cellHasFocus) {
            setEnabled(list.isEnabled());
            setSelected(value.selezionato);
            setFont(list.getFont());
            setBackground(isSelected ? list.getSelectionBackground() : list.getBackground());
            setForeground(isSelected ? list.getSelectionForeground() : list.getForeground());
            setText(value.testo);
            return this;
        }
    }
}
