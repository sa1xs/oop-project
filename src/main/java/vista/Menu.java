package vista;

import model.Aplicacion;
import model.Cuenta;
import model.Usuario;
import model.ListaReproduccion;
import model.Cancion;
import model.Administrador;
import servicio.ReproductorConsola;
import java.util.Scanner;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;

public class Menu {

    // ATRIBUTO PARA LEER DATOS DEL TECLADO
    private final Scanner scanner = new Scanner(System.in);

    // REFERENCIA A LA APLICACIÓN
    private final Aplicacion aplicacion;

    // CUENTA QUE TIENE LA SESIÓN ACTIVA
    private Cuenta cuentaActual;

    // CONSTRUCTOR
    public Menu(Aplicacion aplicacion) {
        this.aplicacion = aplicacion;
    }

    // MÉTODO PARA INICIAR SESIÓN
    public void iniciarSesion() {

        System.out.println("\n===== INICIO DE SESIÓN =====");

        System.out.print("Nombre de usuario: ");
        String nombreUsuario = scanner.nextLine().trim();

        System.out.print("Contraseña: ");
        String contrasenia = scanner.nextLine();

        // LIMPIAR CUALQUIER SESIÓN ANTERIOR
        cuentaActual = null;

        try {
            Cuenta cuenta = aplicacion.iniciarSesion(nombreUsuario, contrasenia);

            // GUARDAR LA CUENTA AUTENTICADA
            cuentaActual = cuenta;
            System.out.println("Inicio de sesión exitoso.");
            System.out.println("Usuario: " + cuenta.getNombreUsuario());
            System.out.println("Rol: " + cuenta.getRol());

        } catch (IllegalArgumentException | IllegalStateException e) {
            System.out.println("Error: " + e.getMessage());
        }

        // ABRIR EL MENÚ SEGÚN EL TIPO DE CUENTA
        if (cuentaActual instanceof Usuario) {
            ejecutarMenuUsuario();
        } else if (cuentaActual instanceof model.Administrador) {
            ejecutarMenuAdministrador();
        }
    }


    // MÉTODO PARA REGISTRAR EL ADMINISTRADOR INICIAL
    public void registrarAdministradorInicial() {

        // SI YA EXISTE UN ADMINISTRADOR, NO PERMITIR OTRO REGISTRO INICIAL
        if (!aplicacion.getAdministradores().isEmpty()) {
            return;
        }

        System.out.println("\n===== REGISTRO DEL ADMINISTRADOR INICIAL =====");

        // EL REGISTRO ES OBLIGATORIO HASTA CREAR UN ADMINISTRADOR VÁLIDO
        while (aplicacion.getAdministradores().isEmpty()) {

            try {
                System.out.print("Correo electrónico: ");
                String correo = scanner.nextLine().trim();

                System.out.print("Nombre de usuario: ");
                String nombreUsuario = scanner.nextLine().trim();

                // EVITAR NOMBRES DE USUARIO REPETIDOS
                for (Usuario usuario : aplicacion.getUsuarios()) {
                    if (usuario.getNombreUsuario().equalsIgnoreCase(nombreUsuario)) {
                        throw new IllegalArgumentException(
                                "El nombre de usuario ya está registrado.");
                    }
                }

                System.out.print("Contraseña: ");
                String contrasenia = scanner.nextLine();

                System.out.print("Confirmar contraseña: ");
                String confirmacion = scanner.nextLine();

                Administrador nuevoAdministrador = new Administrador(
                        correo,
                        nombreUsuario,
                        contrasenia,
                        confirmacion
                );

                aplicacion.registrarAdministrador(nuevoAdministrador);

                System.out.println("Administrador registrado correctamente.");
                System.out.println("Nombre de usuario: "
                        + nuevoAdministrador.getNombreUsuario());

            } catch (IllegalArgumentException | IllegalStateException e) {
                System.out.println("Error: " + e.getMessage());
                System.out.println("Intente registrar el administrador nuevamente.");
            }
        }
    }



    // MÉTODO PARA REGISTRAR UN NUEVO USUARIO
    public void registrarUsuario() {

        System.out.println("\n===== REGISTRO DE USUARIO =====");

        try {
            System.out.print("Correo electrónico: ");
            String correo = scanner.nextLine().trim();

            System.out.print("Nombre de usuario: ");
            String nombreUsuario = scanner.nextLine().trim();

            // EVITAR NOMBRES DE USUARIO REPETIDOS
            for (Cuenta cuenta : aplicacion.getUsuarios()) {
                if (cuenta.getNombreUsuario().equalsIgnoreCase(nombreUsuario)) {
                    throw new IllegalArgumentException(
                            "El nombre de usuario ya está registrado.");
                }
            }

            for (Cuenta cuenta : aplicacion.getAdministradores()) {
                if (cuenta.getNombreUsuario().equalsIgnoreCase(nombreUsuario)) {
                    throw new IllegalArgumentException(
                            "El nombre de usuario ya está registrado.");
                }
            }

            System.out.print("Contraseña: ");
            String contrasenia = scanner.nextLine();

            System.out.print("Confirmar contraseña: ");
            String confirmacion = scanner.nextLine();

            System.out.print("Nombre completo: ");
            String nombreCompleto = scanner.nextLine().trim();

            System.out.print("Fecha de nacimiento (AAAA-MM-DD): ");
            LocalDate fechaNacimiento =
                    LocalDate.parse(scanner.nextLine().trim());

            System.out.println("Nacionalidades disponibles: "
                    + Usuario.getNacionalidadesDisponibles());

            System.out.print("Nacionalidad: ");
            String nacionalidad = scanner.nextLine().trim();

            System.out.print("Cédula: ");
            String cedula = scanner.nextLine().trim();

            System.out.print("Ruta del avatar (Enter para predeterminado): ");
            String avatar = scanner.nextLine().trim();

            Usuario nuevoUsuario = new Usuario(
                    correo,
                    nombreUsuario,
                    contrasenia,
                    confirmacion,
                    nombreCompleto,
                    fechaNacimiento,
                    nacionalidad,
                    cedula,
                    avatar
            );

            aplicacion.registrarUsuario(nuevoUsuario);

            System.out.println("Usuario registrado correctamente.");
            System.out.println("Nombre de usuario: "
                    + nuevoUsuario.getNombreUsuario());

        } catch (DateTimeParseException e) {
            System.out.println(
                    "Error: La fecha debe tener el formato AAAA-MM-DD.");

        } catch (IllegalArgumentException | IllegalStateException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }


    // MÉTODO PARA MOSTRAR EL MENÚ DEL USUARIO
    public void mostrarMenuUsuario() {

        System.out.println("\n===== MENÚ DE USUARIO =====");
        System.out.println("1. Consultar catálogo de canciones");
        System.out.println("2. Consultar mis listas de reproducción");
        System.out.println("3. Comprar canción");
        System.out.println("4. Recargar saldo");
        System.out.println("5. Calificar canción");
        System.out.println("6. Crear lista de reproducción");
        System.out.println("7. Agregar canción a una lista");
        System.out.println("8. Reproducir muestra gratuita");
        System.out.println("9. Agregar canción a la cola");
        System.out.println("10. Consultar cola de reproducción");
        System.out.println("11. Reproducir siguiente canción");
        System.out.println("12. Detener reproducción");
        System.out.println("0. Cerrar sesión");


    }

    // MÉTODO PARA EJECUTAR EL MENÚ DEL USUARIO
    public void ejecutarMenuUsuario() {

        if (!(cuentaActual instanceof Usuario usuario)) {
            System.out.println("Debe iniciar sesión como usuario.");
            return;
        }

        ReproductorConsola reproductor = new ReproductorConsola();

        String opcion;

        do {
            mostrarMenuUsuario();
            opcion = leerOpcion();

            switch (opcion) {

                case "1":
                    System.out.println("\n===== CATÁLOGO DE CANCIONES =====");
                    aplicacion.mostrarCanciones();
                    break;

                case "2":
                    System.out.println("\n===== MIS LISTAS DE REPRODUCCIÓN =====");

                    if (usuario.getListasReproduccion().isEmpty()) {
                        System.out.println("No tienes listas de reproducción.");
                    } else {
                        for (ListaReproduccion lista : usuario.getListasReproduccion()) {
                            System.out.println(lista);
                        }
                    }
                    break;

                case "3":
                    System.out.println("\n===== COMPRAR CANCIÓN =====");

                    System.out.print("Ingrese el ID de la canción: ");

                    try {
                        int idCancion = Integer.parseInt(scanner.nextLine().trim());

                        Cancion cancion = aplicacion.buscarCancionPorId(idCancion);

                        usuario.comprarCancion(cancion);

                        System.out.println("Compra realizada correctamente.");
                        System.out.println("Canción: " + cancion.getNombre());
                        System.out.println("Saldo disponible: $" + usuario.getSaldo());

                    } catch (NumberFormatException e) {
                        System.out.println("Error: El ID debe ser un número entero.");

                    } catch (IllegalArgumentException | IllegalStateException e) {
                        System.out.println("Error: " + e.getMessage());
                    }

                    break;


                case "4":
                    System.out.println("\n===== RECARGAR SALDO =====");

                    System.out.println("Saldo actual: $" + usuario.getSaldo());
                    System.out.print("Ingrese el monto que desea recargar: ");

                    try {
                        String montoIngresado = scanner.nextLine().trim();

                        BigDecimal monto = new BigDecimal(montoIngresado);

                        String resultado = usuario.recargarSaldo(monto);

                        System.out.println(resultado);
                        System.out.println("Nuevo saldo disponible: $" + usuario.getSaldo());

                    } catch (NumberFormatException e) {
                        System.out.println("Error: Debe ingresar un monto numérico válido.");

                    } catch (IllegalArgumentException | IllegalStateException e) {
                        System.out.println("Error: " + e.getMessage());
                    }

                    break;


                case "5":
                    System.out.println("\n===== CALIFICAR CANCIÓN =====");

                    System.out.print("Ingrese el ID de la canción: ");

                    try {
                        int idCancion = Integer.parseInt(scanner.nextLine().trim());

                        Cancion cancion = aplicacion.buscarCancionPorId(idCancion);

                        System.out.print("Ingrese la calificación (0.0 a 5.0): ");
                        double nota = Double.parseDouble(scanner.nextLine().trim());

                        if (!Double.isFinite(nota)) {
                            throw new IllegalArgumentException(
                                    "La calificación debe ser un número válido.");
                        }

                        usuario.calificarCancion(cancion, nota);

                        System.out.println("Calificación registrada correctamente.");
                        System.out.println("Canción: " + cancion.getNombre());
                        System.out.println("Calificación promedio: "
                                + cancion.getCalificacion());

                    } catch (NumberFormatException e) {
                        System.out.println("Error: Debe ingresar un número válido.");

                    } catch (IllegalArgumentException | IllegalStateException e) {
                        System.out.println("Error: " + e.getMessage());
                    }

                    break;


                case "6":
                    System.out.println("\n===== CREAR LISTA DE REPRODUCCIÓN =====");

                    System.out.print("Ingrese el nombre de la lista: ");

                    try {
                        String nombreLista = scanner.nextLine().trim();

                        ListaReproduccion nuevaLista =
                                usuario.crearListaReproduccion(nombreLista);

                        System.out.println("Lista de reproducción creada correctamente.");
                        System.out.println("Nombre: " + nuevaLista.getNombre());

                    } catch (IllegalArgumentException | IllegalStateException e) {
                        System.out.println("Error: " + e.getMessage());
                    }

                    break;


                case "7":
                    System.out.println("\n===== AGREGAR CANCIÓN A UNA LISTA =====");

                    if (usuario.getListasReproduccion().isEmpty()) {
                        System.out.println("Primero debe crear una lista de reproducción.");
                        break;
                    }

                    System.out.println("Seleccione una lista:");

                    for (int i = 0; i < usuario.getListasReproduccion().size(); i++) {
                        ListaReproduccion lista = usuario.getListasReproduccion().get(i);

                        System.out.println((i + 1) + ". " + lista.getNombre());
                    }

                    try {
                        System.out.print("Número de la lista: ");
                        int numeroLista = Integer.parseInt(scanner.nextLine().trim());

                        if (numeroLista < 1 ||
                                numeroLista > usuario.getListasReproduccion().size()) {
                            throw new IllegalArgumentException(
                                    "La lista seleccionada no existe.");
                        }

                        ListaReproduccion listaSeleccionada =
                                usuario.getListasReproduccion().get(numeroLista - 1);

                        System.out.print("Ingrese el ID de la canción: ");
                        int idCancion = Integer.parseInt(scanner.nextLine().trim());

                        Cancion cancion = aplicacion.buscarCancionPorId(idCancion);

                        usuario.agregarCancionALista(listaSeleccionada, cancion);

                        System.out.println("Canción agregada correctamente.");
                        System.out.println("Lista: " + listaSeleccionada.getNombre());
                        System.out.println("Canción: " + cancion.getNombre());

                    } catch (NumberFormatException e) {
                        System.out.println("Error: Debe ingresar un número entero válido.");

                    } catch (IllegalArgumentException | IllegalStateException e) {
                        System.out.println("Error: " + e.getMessage());
                    }

                    break;


                case "8":
                    System.out.println("\n===== MUESTRA GRATUITA =====");
                    System.out.print("Ingrese el ID de la canción: ");

                    try {
                        int idCancion = Integer.parseInt(
                                scanner.nextLine().trim()
                        );

                        Cancion cancion = aplicacion.buscarCancionPorId(idCancion);

                        System.out.println(
                                reproductor.reproducirMuestra(cancion)
                        );

                    } catch (NumberFormatException e) {
                        System.out.println(
                                "Error: El ID debe ser un número entero."
                        );

                    } catch (IllegalArgumentException | IllegalStateException e) {
                        System.out.println("Error: " + e.getMessage());
                    }

                    break;


                case "9":
                    System.out.println("\n===== AGREGAR CANCION A LA COLA =====");
                    System.out.print("Ingrese el ID de la cancion: ");

                    try {
                        int idCancion = Integer.parseInt(
                                scanner.nextLine().trim()
                        );

                        Cancion cancion = aplicacion.buscarCancionPorId(idCancion);

                        reproductor.agregarACola(usuario, cancion);

                        System.out.println("Cancion agregada correctamente a la cola.");
                        System.out.println("Cancion: " + cancion.getNombre());
                        System.out.println("Canciones pendientes: "
                                + reproductor.cantidadPendientes());

                    } catch (NumberFormatException e) {
                        System.out.println(
                                "Error: El ID debe ser un numero entero."
                        );

                    } catch (IllegalArgumentException | IllegalStateException e) {
                        System.out.println("Error: " + e.getMessage());
                    }

                    break;


                case "10":
                    System.out.println("\n===== CONSULTAR COLA DE REPRODUCCIÓN =====");

                    System.out.println(reproductor.mostrarCola());

                    System.out.println("Canciones pendientes: "
                            + reproductor.cantidadPendientes());

                    break;


                case "11":
                    System.out.println("\n===== REPRODUCIR SIGUIENTE CANCIÓN =====");

                    System.out.println(
                            reproductor.reproducirSiguiente()
                    );

                    System.out.println("Canciones pendientes: "
                            + reproductor.cantidadPendientes());

                    break;


                case "12":
                    System.out.println("\n===== DETENER REPRODUCCIÓN =====");

                    System.out.println(
                            reproductor.detener()
                    );

                    break;


                case "0":
                    cuentaActual = null;
                    System.out.println("Sesión cerrada.");
                    break;

                default:
                    System.out.println("Opción inválida. Intente nuevamente.");
                    break;
            }

        } while (!opcion.equals("0"));
    }

    // MÉTODO PARA MOSTRAR EL MENÚ DEL ADMINISTRADOR
    public void mostrarMenuAdministrador() {

        System.out.println("\n===== MENÚ DE ADMINISTRADOR =====");
        System.out.println("1. Consultar usuarios registrados");
        System.out.println("2. Consultar catálogo de canciones");
        System.out.println("0. Cerrar sesión");

    }

    // MÉTODO PARA EJECUTAR EL MENÚ DEL ADMINISTRADOR
    public void ejecutarMenuAdministrador() {

        if (!(cuentaActual instanceof model.Administrador)) {
            System.out.println("Debe iniciar sesión como administrador.");
            return;
        }

        String opcion;

        do {
            mostrarMenuAdministrador();
            opcion = leerOpcion();

            switch (opcion) {

                case "1":
                    System.out.println("\n===== USUARIOS REGISTRADOS =====");
                    aplicacion.mostrarUsuarios();
                    break;

                case "2":
                    System.out.println("\n===== CATÁLOGO DE CANCIONES =====");
                    aplicacion.mostrarCanciones();
                    break;

                case "0":
                    cuentaActual = null;
                    System.out.println("Sesión cerrada.");
                    break;

                default:
                    System.out.println("Opción inválida. Intente nuevamente.");
                    break;
            }

        } while (!opcion.equals("0"));
    }

    // MÉTODO PARA MOSTRAR EL MENÚ PRINCIPAL
    public void mostrarMenuPrincipal() {

        System.out.println("\n===== APLICACIÓN MUSICAL =====");
        System.out.println("1. Iniciar sesión");
        System.out.println("2. Consultar catálogo de canciones");
        System.out.println("3. Registrarse como usuario");
        System.out.println("0. Salir");

    }

    // MÉTODO PARA LEER LA OPCIÓN SELECCIONADA
    public String leerOpcion() {

        System.out.print("Seleccione una opción: ");
        return scanner.nextLine().trim();

    }
    // MÉTODO PARA EJECUTAR EL MENÚ PRINCIPAL
    public void ejecutar() {

        // REGISTRAR EL PRIMER ADMINISTRADOR SI NO EXISTE
        registrarAdministradorInicial();

        String opcion;

        do {
            mostrarMenuPrincipal();
            opcion = leerOpcion();

            switch (opcion) {

                case "1":
                    iniciarSesion();
                    break;

                case "2":
                    System.out.println("\n===== CATÁLOGO DE CANCIONES =====");
                    aplicacion.mostrarCanciones();
                    break;

                case "3":
                    registrarUsuario();
                    break;

                case "0":
                    System.out.println("Saliendo de la aplicación...");
                    break;

                default:
                    System.out.println("Opción inválida. Intente nuevamente.");
                    break;
            }

        } while (!opcion.equals("0"));
    }
}


