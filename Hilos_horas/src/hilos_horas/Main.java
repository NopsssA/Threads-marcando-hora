package hilos_horas;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class Main {
    private static volatile boolean ejecucion = true;
    private static Interfaz interfaz;

    public static void setInterfaz(Interfaz inter) {
        interfaz = inter;
    }

    public static void iniciarProceso() {
        // Reiniciar la bandera de ejecución
        ejecucion = true;
        
        // Crear los hilos
        Thread hiloHora = new Thread(new MostrarHora());
        Thread hiloTrabajo = new Thread(new MostrarTrabajo());

        // Iniciar los hilos
        hiloHora.start();
        hiloTrabajo.start();

        // Timer para detener después de 1 minuto
        new Thread(new Runnable() {
            public void run() {
                try {
                    Thread.sleep(60000);
                    ejecucion = false;
                    
                    hiloHora.join();
                    hiloTrabajo.join();
                    
                    enviarTexto("\nPrograma terminado, paso 1 minuto.");
                } catch (InterruptedException e) {
                    enviarTexto("Error: " + e.getMessage());
                }
            }
        }).start();
    }

    private static void enviarTexto(String texto) {
        if (interfaz != null) {
            javax.swing.SwingUtilities.invokeLater(new Runnable() {
                public void run() {
                    interfaz.agregarTexto(texto);
                }
            });
        }
    }

    // Hilo para mostrar la hora cada 6 segundos
    static class MostrarHora implements Runnable {
        private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");

        @Override
        public void run() {
            while (ejecucion) {
                String horaActual = LocalTime.now().format(formatter);
                enviarTexto("Hora actual: " + horaActual);
                
                try {
                    Thread.sleep(6000);
                } catch (InterruptedException e) {
                    enviarTexto("Hilo de hora interrumpido");
                    break;
                }
            }
        }
    }

    // Hilo para mostrar "trabajando" 5 veces seguidas
    static class MostrarTrabajo implements Runnable {
        @Override
        public void run() {
            while (ejecucion) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    enviarTexto("Hilo de trabajo interrumpido");
                    break;
                }
                
                for (int i = 0; i < 5 && ejecucion; i++) {
                    enviarTexto("Chambeando");
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        enviarTexto("Hilo de trabajo interrumpido");
                        break;
                    }
                }
            }
        }
    }
    public static void main(String[] args) {
    java.awt.EventQueue.invokeLater(new Runnable() {
        public void run() {
            new Interfaz().setVisible(true);
        }
    });
}
}