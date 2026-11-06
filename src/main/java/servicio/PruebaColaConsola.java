
package servicio;

import model.Cancion;
import model.Usuario;

import java.math.BigDecimal;
import java.time.LocalDate;

public class PruebaColaConsola {

    public static void main(String[] args) {

        System.out.println("===== PRUEBA DE COLA POR CONSOLA =====");

        // Crear un usuario válido
        Usuario usuario = new Usuario(
                "prueba@ejemplo.com",
                "usuarioPrueba",
                "Clave123!",
                "Clave123!",
                "Usuario de Prueba",
                LocalDate.of(1996, 4, 24),
                "Costarricense",
                "123456789",
                ""
        );

        // Crear la primera canción
        Cancion cancion1 = new Cancion(
                "Cancion Uno",
                "Rock",
                "Artista Uno",
                "Compositor Uno",
                LocalDate.of(2024, 1, 1),
                "Album Uno",
                null,
                new BigDecimal("1.50")
        );

        // Crear la segunda canción
        Cancion cancion2 = new Cancion(
                "Cancion Dos",
                "Pop",
                "Artista Dos",
                "Compositor Dos",
                LocalDate.of(2024, 2, 1),
                "Album Dos",
                null,
                new BigDecimal("1.50")
        );

        // Comprar las canciones
        usuario.comprarCancion(cancion1);
        usuario.comprarCancion(cancion2);

        System.out.println("Canciones compradas correctamente.");

        // Crear el reproductor
        ReproductorConsola reproductor = new ReproductorConsola();

        // Agregar las canciones compradas a la cola
        reproductor.agregarACola(usuario, cancion1);
        reproductor.agregarACola(usuario, cancion2);

        System.out.println(reproductor.mostrarCola());

        System.out.println("Canciones pendientes: "
                + reproductor.cantidadPendientes());

        // Reproducir primera canción
        System.out.println(reproductor.reproducirSiguiente());

        // Avanzar manualmente a la segunda canción
        System.out.println(reproductor.reproducirSiguiente());

        // Detener la reproducción
        System.out.println(reproductor.detener());

        System.out.println("Canciones pendientes al finalizar: "
                + reproductor.cantidadPendientes());

        System.out.println("Prueba finalizada.");
    }
}
