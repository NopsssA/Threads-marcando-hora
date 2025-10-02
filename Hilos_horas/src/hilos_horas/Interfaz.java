package hilos_horas;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Interfaz extends JFrame {
    private JLabel label;
    private JButton button;
    private JTextArea areaTexto;
    
    public Interfaz() {
        // Configurar la ventana
        setTitle("Ejecución de Hilos");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 300);
        setLayout(new BorderLayout());
        
        // Establecer color de fondo claro
        getContentPane().setBackground(new Color(240, 240, 240)); // Gris claro
        
        // Crear los TRES elementos principales
        label = new JLabel("Presiona para ejecutar :3", SwingConstants.CENTER);
        button = new JButton("Ejecuta"); // Nombre cambiado a "Ejecuta"
        areaTexto = new JTextArea();
        areaTexto.setEdatable(false);
        
        // Configurar colores para los componentes
        label.setBackground(new Color(250, 250, 250)); // Fondo más claro para el label
        label.setOpaque(true);
        
        button.setBackground(new Color(200, 230, 255)); // Azul claro para el botón
        button.setFocusPainted(false);
        
        areaTexto.setBackground(new Color(255, 255, 255)); // Fondo blanco para el área de texto
        areaTexto.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200))); // Borde suave
        
        // Configurar fuente y estilo
        Font fuenteLabel = new Font("Arial", Font.BOLD, 14);
        label.setFont(fuenteLabel);
        
        Font fuenteBoton = new Font("Arial", Font.PLAIN, 12);
        button.setFont(fuenteBoton);
        
        Font fuenteArea = new Font("Consolas", Font.PLAIN, 12);
        areaTexto.setFont(fuenteArea);
        
        // Configurar el área de texto con scroll
        JScrollPane scrollPane = new JScrollPane(areaTexto);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5)); // Margen interno
        
        // Panel superior con label y botón
        JPanel panelSuperior = new JPanel(new BorderLayout());
        panelSuperior.setBackground(new Color(240, 240, 240)); // Mismo color de fondo
        panelSuperior.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10)); // Margen
        panelSuperior.add(label, BorderLayout.CENTER);
        panelSuperior.add(button, BorderLayout.EAST);
        
        // Agregar componentes a la ventana
        add(panelSuperior, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        
        // Configurar el action listener del botón
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                iniciarEjecucion();
            }
        });
    }
    
    private void iniciarEjecucion() {
        // Deshabilitar el botón mientras se ejecuta
        button.setEnabled(false);
        button.setBackground(new Color(180, 180, 180)); // Gris cuando está deshabilitado
        label.setText("Ejecutando... :3");
        
        // Limpiar el área de texto
        areaTexto.setText("");
        
        // Usar un hilo separado para ejecutar la lógica de Main
        Thread hiloEjecucion = new Thread(new Runnable() {
            @Override
            public void run() {
                // Redirigir la salida estándar al JTextArea
                RedireccionadorSalida redireccionador = new Redireccionador(areaTexto);
                System.setOut(redireccionador);
                
                // Ejecutar la lógica principal
                Main.iniciarEjecucion();
                
                // Restaurar la interfaz cuando termine
                SwingUtilities.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        button.setEnabled(true);
                        button.setBackground(new Color(200, 230, 255)); // Volver al color original
                        label.setText("Presiona para ejecutar :3");
                    }
                });
            }
        });
        hiloEjecucion.start();
    }
    
    // Clase interna para redirigir System.out al JTextArea
    private static class Redireccionador extends java.io.PrintStream {
        private JTextArea textArea;
        
        public Redireccionador(JTextArea textArea) {
            super(System.out);
            this.textArea = textArea;
        }
        
        @Override
        public void println(String x) {
            SwingUtilities.invokeLater(new Runnable() {
                @Override
                public void run() {
                    textArea.append(x + "\n");
                    // Hacer scroll automático al final
                    textArea.setCaretPosition(textArea.getDocument().getLength());
                }
            });
        }
        
        @Override
        public void print(String x) {
            SwingUtilities.invokeLater(new Runnable() {
                @Override
                public void run() {
                    textArea.append(x);
                    // Hacer scroll automático al final
                    textArea.setCaretPosition(textArea.getDocument().getLength());
                }
            });
        }
    }
}