package hilos_horas;

//Importaciones necesarias
import java.time.LocalTime; //imprime la hora sin fecha
import java.time.format.DateTimeFormatter;//formatea y parsea la hora

public class Main{
    private static volatile boolean ejecucion = true;

    public static void main(String[] args) {
        //parte de codigo para la interfaz
        Interfaz inter= new Interfaz();
        inter.setVisible(true);
        inter.setLocationRelativeTo(null);//para centralizar valores 
        
        
        // Crear los hilos
        Thread hiloHora = new Thread(new MostrarHora());
        Thread hiloTrabajo = new Thread(new MostrarTrabajo());

        // Iniciar los hilos
        hiloHora.start();
        hiloTrabajo.start();

        // Ejecutar durante 1 minuto (60 segundos)
        try {
            Thread.sleep(60000); // 60,000 ms = 1 minuto
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Detener la ejecución
        ejecucion = false;

        // Esperar a que los hilos terminen
        try {
            hiloHora.join();
            hiloTrabajo.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("/nPrograma terminado, paso 1 minuto.");
    }

    // Hilo para mostrar la hora cada 6 segundos
    static class MostrarHora implements Runnable {
        private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");

        @Override
        public void run() {
            while (ejecucion) {
                // Obtener y mostrar la hora actual
                String horaActual = LocalTime.now().format(formatter);
                System.out.println("Hora actual: " + horaActual);
                
                try {
                    // Esperar 6 segundos (5 segundos de trabajo + 1 segundo)
                    Thread.sleep(6000);
                } catch (InterruptedException e) {
                    System.out.println("Hilo de hora interrumpido");
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
                // Esperar 1 segundo después de que se muestre la hora
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    System.out.println("Hilo de trabajo interrumpido");
                    break;
                }
                
                // Mostrar "trabajando" 5 veces seguidas
                for (int i = 0; i < 5 && ejecucion; i++) {
                    System.out.println("Chambeando");
                    try {
                        Thread.sleep(1000); // 1 segundo entre cada "trabajando"
                    } catch (InterruptedException e) {
                        System.out.println("Hilo de trabajo interrumpido");
                        break;
                    }
                }
            }
        }
    }
}