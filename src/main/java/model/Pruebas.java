package model;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;

public class Pruebas {

    public static void main(String[] args) {

        // 1. Construcción de la aplicación
        Aplicacion aplicacion = new Aplicacion(
                new ArrayList<>(),
                new ArrayList<>(),
                new ArrayList<>()
        );

        // 2. Construcción de usuarios
        Usuario usuario1 = new Usuario(
                "juan@gmail.com",
                "juan123",
                "Clave123!",
                "Clave123!",
                "Juan Pérez",
                LocalDate.of(2000, 5, 10),
                "Costarricense",
                "123456789",
                null
        );

        Usuario usuario2 = new Usuario(
                "maria@gmail.com",
                "maria456",
                "Maria123!",
                "Maria123!",
                "María Rodríguez",
                LocalDate.of(1998, 8, 20),
                "Costarricense",
                "987654321",
                null
        );

        // 3. Construcción de administrador
        Administrador administrador1 = new Administrador(
                "admin@gmail.com",
                "admin1",
                "Admin123!",
                "Admin123!"
        );

        // 4. Construcción de canción
        Cancion cancion1 = new Cancion(
                "Bohemian Rhapsody",
                "Rock",
                "Queen",
                "Freddie Mercury",
                LocalDate.of(1975, 10, 31),
                "A Night at the Opera",
                "bohemian_rhapsody.jpg",
                new BigDecimal("1.99")
        );

        // PRUEBA 1: Registrar objetos en las listas
        aplicacion.registrarUsuario(usuario1);
        aplicacion.registrarUsuario(usuario2);
        aplicacion.registrarAdministrador(administrador1);
        aplicacion.registrarCancion(cancion1);

        // PRUEBA DE BÚSQUEDA DE CANCIÓN POR ID
        System.out.println("\n===== PRUEBA DE BÚSQUEDA POR ID =====");

        Cancion cancionEncontrada = aplicacion.buscarCancionPorId(cancion1.getId());

        System.out.println("Canción encontrada: " + cancionEncontrada.getNombre());
        System.out.println("ID: " + cancionEncontrada.getId());

// PRUEBA CON ID INEXISTENTE
        try {
            aplicacion.buscarCancionPorId(99);
        } catch (IllegalArgumentException e) {
            System.out.println("Error esperado: " + e.getMessage());
        }

        // PRUEBA DE INICIO DE SESIÓN
        System.out.println("\n===== PRUEBA DE INICIO DE SESIÓN =====");

        Cuenta sesionUsuario = aplicacion.iniciarSesion("juan123", "Clave123!");
        System.out.println("Usuario autenticado: " + sesionUsuario.getNombreUsuario());
        System.out.println("Rol: " + sesionUsuario.getRol());

        Cuenta sesionAdministrador = aplicacion.iniciarSesion("admin1", "Admin123!");
        System.out.println("Administrador autenticado: " + sesionAdministrador.getNombreUsuario());
        System.out.println("Rol: " + sesionAdministrador.getRol());

        // PRUEBA DE INICIO DE SESIÓN INCORRECTO
        System.out.println("\n===== PRUEBA DE CONTRASEÑA INCORRECTA =====");

        try {
            aplicacion.iniciarSesion("juan123", "ClaveIncorrecta");
        } catch (IllegalStateException e) {
            System.out.println("Error esperado: " + e.getMessage());
        }

        // Mostrar las listas
        System.out.println("===== USUARIOS =====");
        aplicacion.mostrarUsuarios();

        System.out.println("\n===== ADMINISTRADORES =====");
        aplicacion.mostrarAdministradores();

        System.out.println("\n===== CANCIONES =====");
        aplicacion.mostrarCanciones();

        // PRUEBA 2: Calificar una canción
        System.out.println("\n===== PRUEBA DE CALIFICACIÓN =====");
        System.out.println(cancion1.calificar(4.5));
        System.out.println(cancion1);

        // Mostrar aplicación
        System.out.println("\n===== APLICACIÓN =====");
        System.out.println(aplicacion);

        // PRUEBA 3: Reproducir
        System.out.println("\n===== PRUEBA DE REPRODUCCIÓN =====");
        System.out.println(cancion1.reproducir());
        System.out.println(cancion1.detener());

        // PRUEBA 4: Comprar canciones y calificar
        System.out.println("\n===== PRUEBA DE COMPRA Y CALIFICACIÓN =====");
        Cancion cancion2 = new Cancion(
                "Imagine",
                "Pop",
                "John Lennon",
                "John Lennon",
                LocalDate.of(1971, 9, 9),
                null,   // sin álbum
                null,   // carátula predeterminada
                new BigDecimal("0.99")
        );
        aplicacion.registrarCancion(cancion2);

        usuario1.comprarCancion(cancion1);
        usuario1.comprarCancion(cancion2);
        usuario1.calificarCancion(cancion1, 4.0);
        usuario1.calificarCancion(cancion2, 3.0);
        System.out.println("Saldo de " + usuario1.getNombreUsuario() + ": $" + usuario1.getSaldo());

        try {
            usuario2.calificarCancion(cancion1, 5.0); // usuario2 no la ha comprado
        } catch (IllegalStateException e) {
            System.out.println("Error esperado: " + e.getMessage());
        }

        try {
            usuario1.calificarCancion(cancion1, 9.0); // nota fuera de rango
        } catch (IllegalArgumentException e) {
            System.out.println("Error esperado: " + e.getMessage());
        }

        // PRUEBA 5: Listas de reproducción
        System.out.println("\n===== PRUEBA DE LISTAS DE REPRODUCCIÓN =====");
        ListaReproduccion lista1 = usuario1.crearListaReproduccion("Mis favoritas");
        usuario1.agregarCancionALista(lista1, cancion1);
        usuario1.agregarCancionALista(lista1, cancion2);
        System.out.println(lista1);

        try {
            usuario1.agregarCancionALista(lista1, cancion1); // repetida
        } catch (IllegalStateException e) {
            System.out.println("Error esperado: " + e.getMessage());
        }

        try {
            usuario2.agregarCancionALista(lista1, cancion1); // lista de otro usuario
        } catch (IllegalStateException e) {
            System.out.println("Error esperado: " + e.getMessage());
        }

        // PRUEBA 6: Polimorfismo - canción y lista tratadas como Reproducible
        System.out.println("\n===== PRUEBA DE POLIMORFISMO =====");
        ArrayList<Reproducible> cola = new ArrayList<>();
        cola.add(cancion1);
        cola.add(lista1);

        for (Reproducible item : cola) {
            System.out.println(item.reproducir());
        }

        // PRUEBA 7: Búsquedas
        System.out.println("\n===== PRUEBA DE BÚSQUEDAS =====");
        System.out.println("Por nombre 'bohem': " + aplicacion.buscarCancionesPorNombre("bohem").size());
        System.out.println("Por género 'pop': " + aplicacion.buscarCancionesPorGenero("pop").size());
        System.out.println("Por artista 'queen': " + aplicacion.buscarCancionesPorArtista("queen").size());
        System.out.println("Listas de usuario1 con 'fav': " + usuario1.buscarListasPorNombre("fav").size());
        try {
            aplicacion.buscarCancionesPorNombre("  ");
        } catch (IllegalArgumentException e) {
            System.out.println("Error esperado: " + e.getMessage());
        }

        // PRUEBA 8: Top 3
        System.out.println("\n===== PRUEBA DE TOP 3 =====");
        System.out.println("Mejor calificadas: ");
        for (Cancion c : aplicacion.top3MejorCalificadas()) {
            System.out.println(" - " + c.getNombre());
        }
        System.out.println("Más compradas: ");
        for (Cancion c : aplicacion.top3MasCompradas()) {
            System.out.println(" - " + c.getNombre());
        }
        System.out.println("Más incluidas en listas: ");
        for (Cancion c : aplicacion.top3MasIncluidasEnListas()) {
            System.out.println(" - " + c.getNombre());
        }

        // PRUEBA 9: Quitar canción de una lista
        System.out.println("\n===== PRUEBA DE QUITAR CANCIÓN =====");
        usuario1.quitarCancionDeLista(lista1, cancion2);
        System.out.println(lista1);
        try {
            usuario2.quitarCancionDeLista(lista1, cancion1); // lista de otro usuario
        } catch (IllegalStateException e) {
            System.out.println("Error esperado: " + e.getMessage());
        }
    }
}