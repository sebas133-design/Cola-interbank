import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

// Clase que representa cada transacción individual (El bloque de información)
class NodoTransferencia {
    String datosOperacion;
    NodoTransferencia siguiente;

    public NodoTransferencia(String datosOperacion) {
        this.datosOperacion = datosOperacion;
        this.siguiente = null;
    }
}

// Estructura de la cola dinámica (El motor nuevo para Interbank)
class ColaDinamica {
    NodoTransferencia nodoFrente; // Apunta al primero de la fila
    NodoTransferencia nodoFinal;  // Apunta al último de la fila

    public ColaDinamica() {
        nodoFrente = null;
        nodoFinal = null;
    }

    // Método de complejidad O(1) para que no colapse el sistema
    public void encolar(String datos) {
        NodoTransferencia nuevoNodo = new NodoTransferencia(datos);
        if (nodoFinal == null) {
            nodoFrente = nuevoNodo;
            nodoFinal = nuevoNodo;
        } else {
            nodoFinal.siguiente = nuevoNodo;
            nodoFinal = nuevoNodo;
        }
    }

    // Método de complejidad O(1) para procesar la transacción
    public String desencolar() {
        if (nodoFrente == null) {
            return "Error: No hay transferencias en la cola.";
        }
        String datoProcesado = nodoFrente.datosOperacion;
        nodoFrente = nodoFrente.siguiente;
        
        // Si al sacar el elemento la cola se queda vacía
        if (nodoFrente == null) {
            nodoFinal = null;
        }
        return datoProcesado;
    }
}

// Interfaz Gráfica para el usuario
public class ModuloBancarioGUI extends JFrame {
    private ColaDinamica colaInterbank;
    private JTextArea areaPantalla;
    private JTextField campoMonto;
    private int contadorTx = 1000; // Para simular números de operación

    public ModuloBancarioGUI() {
        colaInterbank = new ColaDinamica();
        
        // Configuración de la ventana
        setTitle("Interbank - Módulo Dinámico de Transferencias");
        setSize(450, 350);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        areaPantalla = new JTextArea();
        areaPantalla.setEditable(false);
        add(new JScrollPane(areaPantalla), BorderLayout.CENTER);

        JPanel panelAbajo = new JPanel();
        campoMonto = new JTextField(10);
        JButton btnEncolar = new JButton("Recibir Transferencia");
        JButton btnProcesar = new JButton("Procesar Siguiente");

        panelAbajo.add(new JLabel("Monto S/:"));
        panelAbajo.add(campoMonto);
        panelAbajo.add(btnEncolar);
        panelAbajo.add(btnProcesar);
        add(panelAbajo, BorderLayout.SOUTH);

        // Acciones de los botones
        btnEncolar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String monto = campoMonto.getText();
                if (!monto.isEmpty()) {
                    contadorTx++;
                    String operacion = "TX-" + contadorTx + " | Monto: S/ " + monto;
                    colaInterbank.encolar(operacion);
                    areaPantalla.append("-> Ingresó: " + operacion + "\n");
                    campoMonto.setText("");
                }
            }
        });

        btnProcesar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String resultado = colaInterbank.desencolar();
                if (resultado.startsWith("Error")) {
                    areaPantalla.append(resultado + "\n");
                } else {
                    areaPantalla.append("<- Procesado con éxito: " + resultado + "\n");
                }
            }
        });
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new ModuloBancarioGUI().setVisible(true);
            }
        });
    }
}