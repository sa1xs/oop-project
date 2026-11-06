
package servicio;

import model.Cancion;

import java.math.BigDecimal;
import java.time.LocalDate;

public class PruebaReproductorConsola {

    public static void main(String[] args) {

        Cancion cancion = new Cancion(
                "Cancion de prueba",
                "Rock",
                "Artista de prueba",
                "Compositor de prueba",
                LocalDate.of(2024, 1, 1),
                "Album de prueba",
                null,
                new BigDecimal("1.99")
        );

        ReproductorConsola reproductor =
                new ReproductorConsola();

        System.out.println(
                reproductor.reproducirMuestra(cancion)
        );
    }
}
