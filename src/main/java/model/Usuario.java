package model;

import java.time.LocalDate;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Arrays;
import java.util.Collections;

public class Usuario extends Cuenta {

    // ATRIBUTOS
    private static final List<String> NACIONALIDADES_DISPONIBLES = Collections.unmodifiableList(Arrays.asList("Costarricense", "Panameña", "Panameño", "Nicaragüense", "Guatemalteca","Guatemalteco", "Salvadoreña","Salvadoreño", "Hondureña", "Hondureño"));
    private String nombreCompleto;
    private LocalDate fechaNacimiento;
    private String nacionalidad;
    private String cedula;
    private String avatar = "recursos/imagenes/avatar-predeterminado.png";
    private BigDecimal saldo = new BigDecimal("4.99");
    private List<Cancion> cancionesCompradas = new ArrayList<>();
    private List<ListaReproduccion> listasReproduccion = new ArrayList<>();

    // CONSTRUCTOR
    public Usuario(String correo, String usuario, String clave, String confirmacion, String nombre, LocalDate nacimiento, String nacionalidad, String cedula, String avatar) {
        super(correo, usuario, clave);
        validarContrasenia(clave, confirmacion);
        this.nombreCompleto = requerido(nombre, "nombre completo");
        setFechaNacimientoValidada(nacimiento);
        this.nacionalidad = validarNacionalidad(nacionalidad);
        this.cedula = requerido(cedula, "cédula");
        setAvatar(avatar);
    }

    // GETTERS Y SETTERS
    public static List<String> getNacionalidadesDisponibles() {
        return NACIONALIDADES_DISPONIBLES;
    }

    public String getNombreCompleto() {
        return nombreCompleto;
    }

    public void setNombreCompleto(String nombreCompleto) {
        this.nombreCompleto = requerido(nombreCompleto, "nombre completo");
    }

    public LocalDate getFechaNacimiento() {
        return fechaNacimiento;
    }

    public void setFechaNacimiento(LocalDate fechaNacimiento) {
        setFechaNacimientoValidada(fechaNacimiento);
    }

    public String getNacionalidad() {
        return nacionalidad;
    }

    public void setNacionalidad(String nacionalidad) {
        this.nacionalidad = validarNacionalidad(nacionalidad);
    }

    public String getCedula() {
        return cedula;
    }

    public void setCedula(String cedula) {
        this.cedula = requerido(cedula, "cédula");
    }

    public BigDecimal getSaldo() {
        return saldo;
    }

    public void setSaldo(BigDecimal saldo) {
        validarDinero(saldo);
        this.saldo = saldo;
    }

    public List<Cancion> getCancionesCompradas() {
        return java.util.Collections.unmodifiableList(new ArrayList<>(cancionesCompradas));
    }

    // ListaReproduccion
    public List<ListaReproduccion> getListasReproduccion(){
        return java.util.Collections.unmodifiableList(new ArrayList<>(listasReproduccion));
    }

    // TOSTRING
    public String toString() {
        return "Usuario:" +
                "\nCorreo: " + getCorreoElectronico() +
                "\nNombre de usuario: " + getNombreUsuario() +
                "\nNombre completo: " + nombreCompleto +
                "\nFecha de nacimiento: " + fechaNacimiento +
                "\nNacionalidad: " + nacionalidad +
                "\nCédula: " + cedula +
                "\nSaldo: $" + saldo;
    }

    // MÉTODOS
    private static String validarNacionalidad(String valor) {
        String limpio = requerido(valor, "nacionalidad");
        for (String nacionalidad : NACIONALIDADES_DISPONIBLES) {
            if (nacionalidad.equalsIgnoreCase(limpio)) {
                return nacionalidad;
            }
        }
        throw new IllegalArgumentException("Nacionalidad fuera de los territorios disponibles");
    }

    public boolean esMayorDeEdad() {
        if (fechaNacimiento == null) {
            return false;
        }
        LocalDate fechaLimite = LocalDate.now().minusYears(18);
        if (fechaNacimiento.isAfter(fechaLimite)) {
            return false;
        }
        return true;
    }

    public String recargarSaldo(BigDecimal monto) {
        if (monto == null) {
            throw new IllegalArgumentException("Debe incluir monto");
        }
        if (monto.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("El monto debe ser mayor que 0");
        }
        validarDinero(monto);
        this.saldo = this.saldo.add(monto);
        return "El saldo fue agregado correctamente";
    }

    // GETTERS Y SETTERS
    @Override
    public String getRol() {
        return "USUARIO";
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String valor) {
        avatar = valor == null || valor.trim().isEmpty()
                ? "recursos/imagenes/avatar.png"
                : valor.trim();
    }

    // MÉTODOS
    private void setFechaNacimientoValidada(LocalDate fecha) {
        if (fecha == null || fecha.isAfter(LocalDate.now().minusYears(18)))
            throw new IllegalArgumentException("Debe ser mayor de edad");
        fechaNacimiento = fecha;
    }

    private static void validarDinero(BigDecimal monto) {
        if (monto == null) {
            throw new IllegalArgumentException("Debe indicar un monto");
        }

        if (monto.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("El monto no puede ser negativo");
        }

        if (monto.stripTrailingZeros().scale() > 2) {
            throw new IllegalArgumentException("Use un máximo de dos decimales");
        }
    }

    public boolean tieneCancion(Cancion cancion) {
        if (cancion == null)
            throw new IllegalArgumentException("Canción requerida");
        for (Cancion comprada : cancionesCompradas)
            if (comprada.equals(cancion))
                return true;
        return false;
    }

    public void comprarCancion(Cancion cancion) {
        if (tieneCancion(cancion))
            throw new IllegalStateException("La canción ya fue comprada");
        BigDecimal precio = cancion.getPrecio();
        validarDinero(precio);
        if (saldo.compareTo(precio) < 0)
            throw new IllegalStateException("Saldo insuficiente");
        cancionesCompradas.add(cancion);
        saldo = saldo.subtract(precio);
    }

    public void calificarCancion(Cancion cancion, double nota) {
        if (!tieneCancion(cancion)) {
            throw new IllegalStateException("Primero debe comprar la canción");
        }

        cancion.calificar(nota);
    }

    // Crear lista de reproducción
    public ListaReproduccion crearListaReproduccion(String nombre){
        ListaReproduccion lista = new ListaReproduccion(nombre);
        listasReproduccion.add(lista);
        return lista;
    }

    // Agregar canción a la lista
    public void agregarCancionALista(ListaReproduccion lista, Cancion cancion){
        boolean esMia = false; // La lista es de este usuario?
        for (ListaReproduccion propia : listasReproduccion){
            if (propia == lista){ // Compara para saber que es exactamente el mismo objeto
                esMia = true;
            }
        }
        if (!esMia){
            throw new IllegalStateException("La lista no pertenece a este usuario");
        }

        if (!tieneCancion(cancion)){ // si ya compró la canción
            throw new IllegalStateException("Primero debe comprar la canción");
        }
        lista.agregarCancion(cancion); // agregar canción
    }

    // Buscar listas de reproducción por Nombre
    public List<ListaReproduccion> buscarListasPorNombre(String texto) {
        if (texto == null || texto.trim().isEmpty()) {
            throw new IllegalArgumentException("Debe indicar el texto a buscar");
        }
        List<ListaReproduccion> resultados = new ArrayList<>();
        for (ListaReproduccion lista : listasReproduccion) {
            if (lista.getNombre().toLowerCase().contains(texto.toLowerCase())) {
                resultados.add(lista);
            }
        }
        return resultados;
    }

    // Quitar canción de una lista
    public void quitarCancionDeLista(ListaReproduccion lista, Cancion cancion){
        if (cancion == null){
            throw new IllegalArgumentException("Debe indicar la canción");
        }
        boolean esMia = false; // ¿la lista es de este usuario?
        for (ListaReproduccion propia : listasReproduccion){
            if (propia == lista){
                esMia = true;
            }
        }
        if (!esMia){
            throw new IllegalStateException("La lista no pertenece a este usuario");
        }
        lista.quitarCancion(cancion);
    }
}
