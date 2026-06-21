package integrado.prog2;

import integrado.prog2.config.DatabaseConnection;
import integrado.prog2.entities.Categoria;
import integrado.prog2.entities.Producto;
import integrado.prog2.entities.Usuario;
import integrado.prog2.entities.Pedido;
import integrado.prog2.enums.Estado;
import integrado.prog2.enums.FormaPago;
import integrado.prog2.enums.Rol;
import integrado.prog2.exception.ValidacionException;
import integrado.prog2.service.CategoriaService;
import integrado.prog2.service.ProductoService;
import integrado.prog2.service.UsuarioService;
import integrado.prog2.service.PedidoService;

import java.util.List;
import java.util.Scanner;

public class Main {
    private static final Scanner scanner = new Scanner(System.in);

    private static final CategoriaService categoriaService = new CategoriaService();
    private static final ProductoService  productoService  = new ProductoService(categoriaService);
    private static final UsuarioService   usuarioService   = new UsuarioService();
    private static final PedidoService    pedidoService    = new PedidoService(usuarioService, productoService);

    public static void main(String[] args) {
        boolean salir = false;

        while (!salir) {
            System.out.println("\n=== SISTEMA DE PEDIDOS (FOOD STORE) ===");
            System.out.println("1. Categorías");
            System.out.println("2. Productos");
            System.out.println("3. Usuarios");
            System.out.println("4. Pedidos");
            System.out.println("0. Salir");
            System.out.print("Seleccione: ");

            try {
                int opcion = Integer.parseInt(scanner.nextLine());
                switch (opcion) {
                    case 1: menuCategorias(); break;
                    case 2: menuProductos();  break;
                    case 3: menuUsuarios();   break;
                    case 4: menuPedidos();    break;
                    case 0: salir = true;     break;
                    default: System.out.println("Opción fuera de rango.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Error: Ingrese un número válido.");
            }
        }
        scanner.close();
        DatabaseConnection.closeConnection();
    }

    // --- MENÚ CATEGORÍAS ---
    private static void menuCategorias() {
        boolean volver = false;
        while (!volver) {
            System.out.println("\n--- GESTIÓN DE CATEGORÍAS ---");
            System.out.println("1. Listar");
            System.out.println("2. Crear");
            System.out.println("3. Editar");
            System.out.println("4. Eliminar");
            System.out.println("0. Volver");
            System.out.print("Seleccione: ");

            try {
                int opcion = Integer.parseInt(scanner.nextLine());
                switch (opcion) {
                    case 1: listarCategorias();  break;
                    case 2: crearCategoria();    break;
                    case 3: editarCategoria();   break;
                    case 4: eliminarCategoria(); break;
                    case 0: volver = true;       break;
                    default: System.out.println("Opción inválida.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Error numérico.");
            }
        }
    }

    private static void listarCategorias() {
        try {
            List<Categoria> categorias = categoriaService.obtenerCategoriasActivas();
            if (categorias.isEmpty()) System.out.println("No hay categorías.");
            else categorias.forEach(System.out::println);
        } catch (ValidacionException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private static void crearCategoria() {
        System.out.print("Nombre: ");
        String nombre = scanner.nextLine();
        System.out.print("Descripción: ");
        String descripcion = scanner.nextLine();
        try {
            Categoria nueva = categoriaService.crearCategoria(nombre, descripcion);
            System.out.println("Categoría ID " + nueva.getId() + " creada.");
        } catch (ValidacionException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private static void editarCategoria() {
        listarCategorias();
        try {
            System.out.print("ID a editar: ");
            Long id = Long.parseLong(scanner.nextLine());
            System.out.print("Nuevo nombre (Enter para omitir): ");
            String nombre = scanner.nextLine();
            System.out.print("Nueva descripción (Enter para omitir): ");
            String descripcion = scanner.nextLine();
            categoriaService.editarCategoria(id, nombre, descripcion);
            System.out.println("Actualizada.");
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private static void eliminarCategoria() {
        listarCategorias();
        try {
            System.out.print("ID a eliminar: ");
            Long id = Long.parseLong(scanner.nextLine());
            categoriaService.eliminarCategoria(id);
            System.out.println("Eliminada.");
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    // --- MENÚ PRODUCTOS ---
    private static void menuProductos() {
        boolean volver = false;
        while (!volver) {
            System.out.println("\n--- GESTIÓN DE PRODUCTOS ---");
            System.out.println("1. Listar");
            System.out.println("2. Crear");
            System.out.println("3. Editar");
            System.out.println("4. Eliminar");
            System.out.println("0. Volver");
            System.out.print("Seleccione: ");

            try {
                int opcion = Integer.parseInt(scanner.nextLine());
                switch (opcion) {
                    case 1: listarProductos();  break;
                    case 2: crearProducto();    break;
                    case 3: editarProducto();   break;
                    case 4: eliminarProducto(); break;
                    case 0: volver = true;      break;
                    default: System.out.println("Opción inválida.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Error numérico.");
            }
        }
    }

    private static void listarProductos() {
        try {
            List<Producto> productos = productoService.obtenerProductosActivos();
            if (productos.isEmpty()) System.out.println("No hay productos.");
            else productos.forEach(System.out::println);
        } catch (ValidacionException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private static void crearProducto() {
        try {
            System.out.print("Nombre: ");      String nombre = scanner.nextLine();
            System.out.print("Descripción: "); String desc   = scanner.nextLine();
            System.out.print("Precio: ");      Double precio = Double.parseDouble(scanner.nextLine());
            System.out.print("Stock: ");       int stock     = Integer.parseInt(scanner.nextLine());
            System.out.print("Imagen URL: ");  String img    = scanner.nextLine();
            System.out.print("Disponible (true/false): "); Boolean disp = Boolean.parseBoolean(scanner.nextLine());
            listarCategorias();
            System.out.print("ID Categoría: "); Long idCat = Long.parseLong(scanner.nextLine());

            Producto p = productoService.crearProducto(nombre, desc, precio, stock, img, disp, idCat);
            System.out.println("Producto ID " + p.getId() + " creado.");
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private static void editarProducto() {
        listarProductos();
        try {
            System.out.print("ID a editar: ");
            Long id = Long.parseLong(scanner.nextLine());
            System.out.print("Nuevo nombre (Enter para omitir): ");
            String nombre = scanner.nextLine();
            System.out.print("Nueva descripción (Enter para omitir): ");
            String desc = scanner.nextLine();
            System.out.print("Nuevo precio (Enter para omitir): ");
            String precioStr = scanner.nextLine();
            Double precio = precioStr.isEmpty() ? null : Double.parseDouble(precioStr);
            System.out.print("Nuevo stock (Enter para omitir): ");
            String stockStr = scanner.nextLine();
            Integer stock = stockStr.isEmpty() ? null : Integer.parseInt(stockStr);
            System.out.print("Nueva imagen URL (Enter para omitir): ");
            String img = scanner.nextLine();
            System.out.print("Disponible true/false (Enter para omitir): ");
            String dispStr = scanner.nextLine();
            Boolean disp = dispStr.isEmpty() ? null : Boolean.parseBoolean(dispStr);
            System.out.print("Nuevo ID Categoría (Enter para omitir): ");
            String catStr = scanner.nextLine();
            Long idCat = catStr.isEmpty() ? null : Long.parseLong(catStr);

            productoService.editarProducto(id,
                nombre.isEmpty() ? null : nombre,
                desc.isEmpty() ? null : desc,
                precio, stock,
                img.isEmpty() ? null : img,
                disp, idCat);
            System.out.println("Producto actualizado.");
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private static void eliminarProducto() {
        listarProductos();
        try {
            System.out.print("ID a eliminar: ");
            Long id = Long.parseLong(scanner.nextLine());
            productoService.eliminarProducto(id);
            System.out.println("Eliminado.");
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    // --- MENÚ USUARIOS ---
    private static void menuUsuarios() {
        boolean volver = false;
        while (!volver) {
            System.out.println("\n--- GESTIÓN DE USUARIOS ---");
            System.out.println("1. Listar");
            System.out.println("2. Crear");
            System.out.println("3. Editar");
            System.out.println("4. Eliminar");
            System.out.println("0. Volver");
            System.out.print("Seleccione: ");

            try {
                int opcion = Integer.parseInt(scanner.nextLine());
                switch (opcion) {
                    case 1: listarUsuarios();  break;
                    case 2: crearUsuario();    break;
                    case 3: editarUsuario();   break;
                    case 4: eliminarUsuario(); break;
                    case 0: volver = true;     break;
                    default: System.out.println("Opción inválida.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Error numérico.");
            }
        }
    }

    private static void listarUsuarios() {
        try {
            List<Usuario> usuarios = usuarioService.obtenerUsuariosActivos();
            if (usuarios.isEmpty()) System.out.println("No hay usuarios.");
            else usuarios.forEach(System.out::println);
        } catch (ValidacionException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private static void crearUsuario() {
        try {
            System.out.print("Nombre: ");     String nom  = scanner.nextLine();
            System.out.print("Apellido: ");   String ape  = scanner.nextLine();
            System.out.print("Mail: ");       String mail = scanner.nextLine();
            System.out.print("Celular: ");    String cel  = scanner.nextLine();
            System.out.print("Contraseña: "); String pass = scanner.nextLine();
            System.out.print("Rol (1: ADMIN, 2: USUARIO): ");
            Rol rol = scanner.nextLine().equals("1") ? Rol.ADMIN : Rol.USUARIO;

            Usuario u = usuarioService.crearUsuario(nom, ape, mail, cel, pass, rol);
            System.out.println("Usuario ID " + u.getId() + " creado.");
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private static void editarUsuario() {
        listarUsuarios();
        try {
            System.out.print("ID a editar: ");
            Long id = Long.parseLong(scanner.nextLine());
            System.out.print("Nuevo nombre (Enter para omitir): ");
            String nombre = scanner.nextLine();
            System.out.print("Nuevo apellido (Enter para omitir): ");
            String apellido = scanner.nextLine();
            System.out.print("Nuevo mail (Enter para omitir): ");
            String mail = scanner.nextLine();
            System.out.print("Nuevo celular (Enter para omitir): ");
            String celular = scanner.nextLine();
            System.out.print("Nueva contraseña (Enter para omitir): ");
            String pass = scanner.nextLine();
            System.out.print("Nuevo Rol - 1: ADMIN, 2: USUARIO (Enter para omitir): ");
            String rolStr = scanner.nextLine();
            Rol rol = null;
            if (rolStr.equals("1")) rol = Rol.ADMIN;
            else if (rolStr.equals("2")) rol = Rol.USUARIO;

            usuarioService.editarUsuario(id,
                nombre.isEmpty() ? null : nombre,
                apellido.isEmpty() ? null : apellido,
                mail.isEmpty() ? null : mail,
                celular.isEmpty() ? null : celular,
                pass.isEmpty() ? null : pass,
                rol);
            System.out.println("Usuario actualizado.");
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private static void eliminarUsuario() {
        listarUsuarios();
        try {
            System.out.print("ID a eliminar: ");
            Long id = Long.parseLong(scanner.nextLine());
            usuarioService.eliminarUsuario(id);
            System.out.println("Eliminado.");
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    // --- MENÚ PEDIDOS ---
    private static void menuPedidos() {
        boolean volver = false;
        while (!volver) {
            System.out.println("\n--- GESTIÓN DE PEDIDOS ---");
            System.out.println("1. Listar");
            System.out.println("2. Crear Pedido");
            System.out.println("3. Actualizar Estado");
            System.out.println("4. Eliminar");
            System.out.println("0. Volver");
            System.out.print("Seleccione: ");

            try {
                int opcion = Integer.parseInt(scanner.nextLine());
                switch (opcion) {
                    case 1: listarPedidos();    break;
                    case 2: crearPedido();      break;
                    case 3: actualizarPedido(); break;
                    case 4: eliminarPedido();   break;
                    case 0: volver = true;      break;
                }
            } catch (NumberFormatException e) {
                System.out.println("Error numérico.");
            }
        }
    }

    private static void listarPedidos() {
        try {
            List<Pedido> pedidos = pedidoService.obtenerPedidosActivos();
            if (pedidos.isEmpty()) System.out.println("No hay pedidos.");
            else pedidos.forEach(System.out::println);
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private static void crearPedido() {
        try {
            listarUsuarios();
            System.out.print("ID Usuario: ");
            Long idUser = Long.parseLong(scanner.nextLine());

            System.out.print("Forma Pago (1: EFECTIVO, 2: TARJETA, 3: TRANSFERENCIA): ");
            String pagoStr = scanner.nextLine();
            FormaPago fp = pagoStr.equals("2") ? FormaPago.TARJETA :
                           (pagoStr.equals("3") ? FormaPago.TRANSFERENCIA : FormaPago.EFECTIVO);

            Pedido borrador = pedidoService.iniciarPedido(idUser, fp);

            boolean agregando = true;
            while (agregando) {
                listarProductos();
                System.out.print("ID Producto a agregar: ");
                Long idProd = Long.parseLong(scanner.nextLine());
                System.out.print("Cantidad: ");
                int cant = Integer.parseInt(scanner.nextLine());

                pedidoService.agregarDetalleAPedido(borrador, idProd, cant);
                System.out.println("Producto agregado. Subtotal actual: $" + borrador.getTotal());

                System.out.print("¿Agregar otro producto? (S/N): ");
                agregando = scanner.nextLine().equalsIgnoreCase("S");
            }

            pedidoService.confirmarYGuardarPedido(borrador);
            System.out.println("Pedido ID " + borrador.getId() + " guardado. Total: $" + borrador.getTotal());

        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private static void actualizarPedido() {
        listarPedidos();
        try {
            System.out.print("ID Pedido: ");
            Long id = Long.parseLong(scanner.nextLine());
            System.out.print("Nuevo Estado (1: PENDIENTE, 2: CONFIRMADO, 3: TERMINADO, 4: CANCELADO): ");
            int est = Integer.parseInt(scanner.nextLine());
            Estado estado = switch (est) {
                case 2 -> Estado.CONFIRMADO;
                case 3 -> Estado.TERMINADO;
                case 4 -> Estado.CANCELADO;
                default -> Estado.PENDIENTE;
            };
            pedidoService.actualizarEstadoYPago(id, estado, null);
            System.out.println("Estado actualizado.");
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private static void eliminarPedido() {
        listarPedidos();
        try {
            System.out.print("ID a eliminar: ");
            Long id = Long.parseLong(scanner.nextLine());
            pedidoService.eliminarPedido(id);
            System.out.println("Eliminado.");
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
}