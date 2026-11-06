
package servicio;

import model.Cancion;
import model.Usuario;

import java.util.ArrayDeque;
import java.util.Queue;

public class ReproductorConsola {

    private final Queue<Cancion> cola = new ArrayDeque<>();
    private Cancion cancionActual;

    public void agregarACola(Usuario usuario, Cancion cancion) {

        if (usuario == null || cancion == null) {
            throw new IllegalArgumentException(
                    "Debe indicar un usuario y una canción."
            );
        }

        if (!usuario.tieneCancion(cancion)) {
            throw new IllegalStateException(
                    "Primero debe comprar la canción."
            );
        }

        cola.offer(cancion);
    }

    public String reproducirSiguiente() {

        if (cola.isEmpty()) {
            return "No hay canciones pendientes en la cola.";
        }

        String mensaje = "";

        if (cancionActual != null) {
            mensaje = cancionActual.detener() + "\n";
        }

        cancionActual = cola.poll();

        return mensaje + cancionActual.reproducir();
    }

    public String detener() {

        if (cancionActual == null) {
            return "No hay ninguna canción reproduciéndose.";
        }

        String mensaje = cancionActual.detener();
        cancionActual = null;

        return mensaje;
    }

    public String reproducirMuestra(Cancion cancion) {

        if (cancion == null) {
            throw new IllegalArgumentException(
                    "Debe indicar una canción."
            );
        }

        return "===== MUESTRA GRATUITA ====="
                + "\nCanción: " + cancion.getNombre()
                + "\nArtista: " + cancion.getArtista()
                + "\nDuración de la muestra: 30 segundos (simulada)"
                + "\n" + cancion.reproducir()
                + "\n" + cancion.detener();
    }

    public String mostrarCola() {

        if (cola.isEmpty()) {
            return "No hay canciones pendientes en la cola.";
        }

        StringBuilder resultado = new StringBuilder(
                "===== COLA DE REPRODUCCIÓN =====\n"
        );

        int numero = 1;

        for (Cancion cancion : cola) {
            resultado.append(numero)
                    .append(". ")
                    .append(cancion.getNombre())
                    .append(" - ")
                    .append(cancion.getArtista())
                    .append("\n");

            numero++;
        }

        return resultado.toString();
    }

    public int cantidadPendientes() {
        return cola.size();
    }
}
