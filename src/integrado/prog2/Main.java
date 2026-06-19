package integrado.prog2;

import integrado.prog2.entities.Categoria;
import integrado.prog2.entities.Producto;
import integrado.prog2.exception.ValidacionException;
import integrado.prog2.service.CategoriaService;
import integrado.prog2.service.ProductoService;

import java.util.List;
import java.util.Scanner;

public class Main {
    // Instanciamos el Scanner y los Servicios de forma estática para poder usarlos en todos los métodos
    private static final Scanner scanner = new Scanner(System.in);
    private static final CategoriaService categoriaService = new CategoriaService();
    // Inyectamos el categoriaService dentro del productoService
    private static final ProductoService productoService = new ProductoService(categoriaService);

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
                // Usamos Integer.parseInt(scanner.nextLine()) para evitar el bug del "Enter" fantasma del Scanner
                int opcion = Integer.parseInt(scanner.nextLine());

                switch (opcion) {
                    case 1:
                        menuCategorias();
                        break;
                    case 2:
                        menuProductos();
                        break;
                    case 3:
                        System.out.println("En desarrollo..."); // Acá irá menuUsuarios()
                        break;
                    case 4:
                        System.out.println("En desarrollo..."); // Acá irá menuPedidos()
                        break;
                    case 0:
                        System.out.println("Saliendo del sistema...");
                        salir = true;
                        break;
                    default:
                        System.out.println("Error: Opción fuera de rango. Intente nuevamente.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Error: Debe ingresar un número válido.");
            }
        }
        scanner.close();
    }

    // --- MENÚ DE CATEGORÍAS ---
    private static void menuCategorias() {
        boolean volver = false;
        while (!volver) {
            System.out.println("\n--- GESTIÓN DE CATEGORÍAS ---");
            System.out.println("1. Listar");
            System.out.println("2. Crear");
            System.out.println("3. Editar");
            System.out.println("4. Eliminar");
            System.out.println("0. Volver al menú principal");
            System.out.print("Seleccione: ");

            try {
                int opcion = Integer.parseInt(scanner.nextLine());
                switch (opcion) {
                    case 1:
                        listarCategorias();
                        break;
                    case 2:
                        crearCategoria();
                        break;
                    case 3:
                        editarCategoria();
                        break;
                    case 4:
                        eliminarCategoria();
                        break;
                    case 0:
                        volver = true;
                        break;
                    default:
                        System.out.println("Error: Opción inválida.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Error: Debe ingresar un número válido.");
            }
        }
    }

    private static void listarCategorias() {
        System.out.println("\n-- Lista de Categorías --");
        List<Categoria> categorias = categoriaService.obtenerCategoriasActivas();
        if (categorias.isEmpty()) {
            System.out.println("No hay categorías cargadas.");
        } else {
            for (Categoria cat : categorias) {
                System.out.println(cat.toString());
            }
        }
    }

    private static void crearCategoria() {
        System.out.print("Ingrese nombre de la categoría: ");
        String nombre = scanner.nextLine();
        System.out.print("Ingrese descripción: ");
        String descripcion = scanner.nextLine();

        try {
            Categoria nueva = categoriaService.crearCategoria(nombre, descripcion);
            System.out.println("✅ Categoría creada con éxito. ID asignado: " + nueva.getId());
        } catch (ValidacionException e) {
            System.out.println("❌ Error al crear: " + e.getMessage());
        }
    }

    private static void editarCategoria() {
        listarCategorias();
        System.out.print("\nIngrese el ID de la categoría a editar: ");
        try {
            Long id = Long.parseLong(scanner.nextLine());
            System.out.print("Ingrese nuevo nombre (deje vacío para no modificar): ");
            String nombre = scanner.nextLine();
            System.out.print("Ingrese nueva descripción (deje vacío para no modificar): ");
            String descripcion = scanner.nextLine();

            categoriaService.editarCategoria(id, nombre, descripcion);
            System.out.println("✅ Categoría actualizada con éxito.");
        } catch (NumberFormatException e) {
            System.out.println("❌ Error: El ID debe ser un número.");
        } catch (ValidacionException e) {
            System.out.println("❌ Error al editar: " + e.getMessage());
        }
    }

    private static void eliminarCategoria() {
        listarCategorias();
        System.out.print("\nIngrese el ID de la categoría a eliminar: ");
        try {
            Long id = Long.parseLong(scanner.nextLine());
            System.out.print("¿Está seguro que desea eliminarla? (S/N): ");
            String confirmacion = scanner.nextLine();

            if (confirmacion.equalsIgnoreCase("S")) {
                categoriaService.eliminarCategoria(id);
                System.out.println("✅ Categoría eliminada con éxito.");
            } else {
                System.out.println("Operación cancelada.");
            }
        } catch (NumberFormatException e) {
            System.out.println("❌ Error: El ID debe ser un número.");
        } catch (ValidacionException e) {
            System.out.println("❌ Error al eliminar: " + e.getMessage());
        }
    }

    // --- MENÚ DE PRODUCTOS ---
    private static void menuProductos() {
        boolean volver = false;
        while (!volver) {
            System.out.println("\n--- GESTIÓN DE PRODUCTOS ---");
            System.out.println("1. Listar");
            System.out.println("2. Crear");
            System.out.println("3. Editar");
            System.out.println("4. Eliminar");
            System.out.println("0. Volver al menú principal");
            System.out.print("Seleccione: ");

            try {
                int opcion = Integer.parseInt(scanner.nextLine());
                switch (opcion) {
                    case 1:
                        listarProductos();
                        break;
                    case 2:
                        crearProducto();
                        break;
                    case 3:
                        // editarProducto(); // Te lo dejo como desafío para que lo implementes igual que editarCategoria
                        System.out.println("En desarrollo...");
                        break;
                    case 4:
                        eliminarProducto();
                        break;
                    case 0:
                        volver = true;
                        break;
                    default:
                        System.out.println("Error: Opción inválida.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Error: Debe ingresar un número válido.");
            }
        }
    }

    private static void listarProductos() {
        System.out.println("\n-- Lista de Productos --");
        List<Producto> productos = productoService.obtenerProductosActivos();
        if (productos.isEmpty()) {
            System.out.println("No hay productos cargados.");
        } else {
            for (Producto prod : productos) {
                System.out.println(prod.toString());
            }
        }
    }

    private static void crearProducto() {
        try {
            System.out.print("Ingrese nombre del producto: ");
            String nombre = scanner.nextLine();
            System.out.print("Ingrese descripción: ");
            String descripcion = scanner.nextLine();

            System.out.print("Ingrese precio: ");
            Double precio = Double.parseDouble(scanner.nextLine());

            System.out.print("Ingrese stock inicial: ");
            int stock = Integer.parseInt(scanner.nextLine());

            System.out.print("Ingrese URL o nombre de imagen: ");
            String imagen = scanner.nextLine();

            System.out.print("¿Está disponible? (true/false): ");
            Boolean disponible = Boolean.parseBoolean(scanner.nextLine());

            listarCategorias(); // Listamos para facilitar la prueba
            System.out.print("Ingrese el ID de la categoría a la que pertenece: ");
            Long idCategoria = Long.parseLong(scanner.nextLine());

            Producto nuevo = productoService.crearProducto(nombre, descripcion, precio, stock, imagen, disponible, idCategoria);
            System.out.println("✅ Producto creado con éxito. ID asignado: " + nuevo.getId());

        } catch (NumberFormatException e) {
            System.out.println("❌ Error: Formato de número inválido en precio, stock o ID.");
        } catch (ValidacionException e) {
            System.out.println("❌ Error al crear: " + e.getMessage());
        }
    }

    private static void eliminarProducto() {
        listarProductos();
        System.out.print("\nIngrese el ID del producto a eliminar: ");
        try {
            Long id = Long.parseLong(scanner.nextLine());
            System.out.print("¿Está seguro que desea eliminarlo? (S/N): ");
            String confirmacion = scanner.nextLine();

            if (confirmacion.equalsIgnoreCase("S")) {
                productoService.eliminarProducto(id);
                System.out.println("✅ Producto eliminado con éxito.");
            } else {
                System.out.println("Operación cancelada.");
            }
        } catch (NumberFormatException e) {
            System.out.println("❌ Error: El ID debe ser un número.");
        } catch (ValidacionException e) {
            System.out.println("❌ Error al eliminar: " + e.getMessage());
        }
    }
}