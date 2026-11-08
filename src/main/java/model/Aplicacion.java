package model;

import java.util.ArrayList;
import java.util.List;

public class Aplicacion {

    // ATRIBUTOS
    private List<Usuario> usuarios;
    private List<Administrador> administradores;
    private List<Cancion> canciones;

    // CONSTRUCTOR COMPLETO
    public Aplicacion(List<Usuario> usuarios,
                      List<Administrador> administradores,
                      List<Cancion> canciones) {

        this.usuarios = usuarios;
        this.administradores = administradores;
        this.canciones = canciones;
    }

    // GETTERS Y SETTERS
    public List<Usuario> getUsuarios() {
        return usuarios;
    }

    public void setUsuarios(List<Usuario> usuarios) {
        this.usuarios = usuarios;
    }

    public List<Administrador> getAdministradores() {
        return administradores;
    }

    public void setAdministradores(List<Administrador> administradores) {
        this.administradores = administradores;
    }

    public List<Cancion> getCanciones() {
        return canciones;
    }

    public void setCanciones(List<Cancion> canciones) {
        this.canciones = canciones;
    }

    // MÉTODO PARA INICIAR SESIÓN
    public Cuenta iniciarSesion(String nombreUsuario, String contrasenia) {

        if (nombreUsuario == null || contrasenia == null) {
            throw new IllegalArgumentException(
                    "Debe ingresar el usuario y la contraseña"
            );
        }

        // Buscar entre los administradores
        for (Administrador administrador : administradores) {

            if (administrador.autenticar(nombreUsuario, contrasenia)) {
                return administrador;
            }
        }

        // Buscar entre los usuarios
        for (Usuario usuario : usuarios) {

            if (usuario.autenticar(nombreUsuario, contrasenia)) {
                return usuario;
            }
        }

        throw new IllegalStateException(
                "Nombre de usuario o contraseña incorrectos"
        );
    }

    // MÉTODO PARA BUSCAR UNA CANCIÓN POR ID
    public Cancion buscarCancionPorId(int id) {

        for (Cancion cancion : canciones) {

            if (cancion.getId() == id) {
                return cancion;
            }
        }

        throw new IllegalArgumentException(
                "No existe una canción con el ID indicado"
        );
    }

    // MÉTODO PARA BUSCAR CANCIONES POR NOMBRE
    public List<Cancion> buscarCancionesPorNombre(String texto) {
        if (texto == null || texto.trim().isEmpty()) {
            throw new IllegalArgumentException("Debe indicar el texto a buscar");
        }
        List<Cancion> resultados = new ArrayList<>();
        for (Cancion cancion : canciones) {
            if (cancion.getNombre().toLowerCase().contains(texto.toLowerCase())) {
                resultados.add(cancion);
            }
        }
        return resultados;
    }

    // MÉTODO PARA BUSCAR CANCIONES POR GÉNERO
    public List<Cancion> buscarCancionesPorGenero(String texto) {
        if (texto == null || texto.trim().isEmpty()) {
            throw new IllegalArgumentException("Debe indicar el texto a buscar");
        }
        List<Cancion> resultados = new ArrayList<>();
        for (Cancion cancion : canciones) {
            if (cancion.getGenero() != null && cancion.getGenero().toLowerCase().contains(texto.toLowerCase())) {
                resultados.add(cancion);
            }
        }
        return resultados;
    }

    // MÉTODO PARA BUSCAR CANCIONES POR ARTISTA
    public List<Cancion> buscarCancionesPorArtista(String texto) {
        if (texto == null || texto.trim().isEmpty()) {
            throw new IllegalArgumentException("Debe indicar el texto a buscar");
        }
        List<Cancion> resultados = new ArrayList<>();
        for (Cancion cancion : canciones) {
            if (cancion.getArtista().toLowerCase().contains(texto.toLowerCase())) {
                resultados.add(cancion);
            }
        }
        return resultados;
    }

    // MÉTODOS PARA REGISTRAR OBJETOS
    public void registrarUsuario(Usuario usuario) {
        usuarios.add(usuario);
    }

    public void registrarAdministrador(Administrador administrador) {
        administradores.add(administrador);
    }

    public void registrarCancion(Cancion cancion) {
        canciones.add(cancion);
    }

    // MÉTODOS PARA MOSTRAR LAS LISTAS
    public void mostrarUsuarios() {

        System.out.println("USUARIOS REGISTRADOS");

        for (int i = 0; i < usuarios.size(); i++) {
            System.out.println(usuarios.get(i));
            System.out.println();
        }
    }

    public void mostrarAdministradores() {

        System.out.println("ADMINISTRADORES REGISTRADOS");

        for (int i = 0; i < administradores.size(); i++) {
            System.out.println(administradores.get(i));
            System.out.println();
        }
    }

    public void mostrarCanciones() {

        System.out.println("CANCIONES REGISTRADAS");

        for (int i = 0; i < canciones.size(); i++) {
            System.out.println(canciones.get(i));
            System.out.println();
        }
    }

    // MÉTODO TOP 3 DE CANCIONES MEJOR CALIFICADAS
    public List<Cancion> top3MejorCalificadas() {
        List<Cancion> pendientes = new ArrayList<>(canciones); // copia, para no tocar el catálogo
        List<Cancion> top = new ArrayList<>();

        while (top.size() < 3 && !pendientes.isEmpty()) {
            Cancion mejor = pendientes.get(0);
            for (Cancion cancion : pendientes) {
                if (cancion.getCalificacion() > mejor.getCalificacion()) {
                    mejor = cancion;
                }
            }
            top.add(mejor);
            pendientes.remove(mejor); // la aparto para que no se repita
        }
        return top;
    }

    private int contarCompras(Cancion cancion) {
        int total = 0;
        for (Usuario usuario : usuarios) {
            if (usuario.tieneCancion(cancion)) {
                total++;
            }
        }
        return total;
    }

    private int contarEnListas(Cancion cancion) {
        int total = 0;
        for (Usuario usuario : usuarios) { // nivel 1: cada usuario
            for (ListaReproduccion lista : usuario.getListasReproduccion()) { // nivel 2: cada lista suya
                for (Cancion existente : lista.getCanciones()) { // nivel 3: cada canción de esa lista
                    if (existente.equals(cancion)) { // ¿es la que busco?
                        total++;
                    }
                }
            }
        }
        return total;
    }

    // MÉTODO TOP 3 DE CANCIONES MÁS COMPRADAS
    public List<Cancion> top3MasCompradas() {
        List<Cancion> pendientes = new ArrayList<>(canciones);
        List<Cancion> top = new ArrayList<>();

        while (top.size() < 3 && !pendientes.isEmpty()) {
            Cancion mejor = pendientes.get(0);
            for (Cancion cancion : pendientes) {
                if (contarCompras(cancion) > contarCompras(mejor)) {
                    mejor = cancion;
                }
            }
            top.add(mejor);
            pendientes.remove(mejor);
        }
        return top;
    }

    // MÉTODO TOP 3 DE CANCIONES MÁS INCLUIDAS EN LISTAS
    public List<Cancion> top3MasIncluidasEnListas() {
        List<Cancion> pendientes = new ArrayList<>(canciones);
        List<Cancion> top = new ArrayList<>();

        while (top.size() < 3 && !pendientes.isEmpty()) {
            Cancion mejor = pendientes.get(0);
            for (Cancion cancion : pendientes) {
                if (contarEnListas(cancion) > contarEnListas(mejor)) {
                    mejor = cancion;
                }
            }
            top.add(mejor);
            pendientes.remove(mejor);
        }
        return top;
    }



    // toString()
    public String toString() {
        return "Aplicación:"
                + "\nUsuarios: " + usuarios
                + "\nAdministradores: " + administradores
                + "\nCanciones: " + canciones;
    }
}