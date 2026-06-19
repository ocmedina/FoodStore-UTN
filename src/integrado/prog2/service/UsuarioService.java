package integrado.prog2.service;

import integrado.prog2.entities.Usuario;
import integrado.prog2.enums.Rol;
import integrado.prog2.exception.ValidacionException;

import java.util.ArrayList;
import java.util.List;

public class UsuarioService {
    private List<Usuario> usuarios;
    private Long contadorId;

    public UsuarioService() {
        this.usuarios = new ArrayList<>();
        this.contadorId = 1L; // Auto-incremental simulado
    }

    // HU-USR-02: Crear usuario
    public Usuario crearUsuario(String nombre, String apellido, String mail, String celular, String contraseña, Rol rol) throws ValidacionException {
        // Validaciones de negocio
        if (mail == null || mail.trim().isEmpty()) {
            throw new ValidacionException("El mail es obligatorio.");
        }

        // Regla: Mail único
        for (Usuario u : usuarios) {
            if (!u.isEliminado() && u.getMail().equalsIgnoreCase(mail)) {
                throw new ValidacionException("Ya existe un usuario activo con el mail: " + mail);
            }
        }

        Usuario nuevoUsuario = new Usuario(nombre, apellido, mail, celular, contraseña, rol);
        nuevoUsuario.setId(contadorId++);
        usuarios.add(nuevoUsuario);
        return nuevoUsuario;
    }

    // HU-USR-01: Listar usuarios
    public List<Usuario> obtenerUsuariosActivos() {
        List<Usuario> activos = new ArrayList<>();
        for (Usuario u : usuarios) {
            if (!u.isEliminado()) {
                activos.add(u);
            }
        }
        return activos;
    }

    public Usuario buscarPorId(Long id) throws ValidacionException {
        for (Usuario u : usuarios) {
            if (u.getId().equals(id) && !u.isEliminado()) {
                return u;
            }
        }
        throw new ValidacionException("No se encontró ningún usuario con el ID: " + id);
    }

    // HU-USR-03: Editar usuario
    public void editarUsuario(Long id, String nombre, String apellido, String mail, String celular, String contraseña, Rol rol) throws ValidacionException {
        Usuario usuario = buscarPorId(id);

        if (mail != null && !mail.trim().isEmpty() && !usuario.getMail().equalsIgnoreCase(mail)) {
            // Si intenta cambiar el mail, volvemos a validar que el nuevo no esté usado [cite: 350]
            for (Usuario u : usuarios) {
                if (!u.isEliminado() && u.getMail().equalsIgnoreCase(mail)) {
                    throw new ValidacionException("Ya existe otro usuario usando el mail: " + mail);
                }
            }
            usuario.setMail(mail);
        }

        if (nombre != null && !nombre.trim().isEmpty()) usuario.setNombre(nombre);
        if (apellido != null && !apellido.trim().isEmpty()) usuario.setApellido(apellido);
        if (celular != null && !celular.trim().isEmpty()) usuario.setCelular(celular);
        if (contraseña != null && !contraseña.trim().isEmpty()) usuario.setContraseña(contraseña);
        if (rol != null) usuario.setRol(rol);
    }

    // HU-USR-04: Eliminar usuario
    public void eliminarUsuario(Long id) throws ValidacionException {
        Usuario usuario = buscarPorId(id);
        usuario.setEliminado(true); // Baja lógica
    }
}