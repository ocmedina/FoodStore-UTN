package integrado.prog2.service;

import integrado.prog2.entities.Usuario;
import integrado.prog2.enums.Rol;
import integrado.prog2.exception.ValidacionException;
import integrado.prog2.repository.UsuarioRepository;

import java.sql.SQLException;
import java.util.List;

public class UsuarioService {

    private final UsuarioRepository usuarioRepository;

    public UsuarioService() {
        this.usuarioRepository = new UsuarioRepository();
    }

    public Usuario crearUsuario(String nombre, String apellido, String mail, String celular, String contrasena, Rol rol) throws ValidacionException {
        if (nombre == null || nombre.trim().isEmpty()) {
            throw new ValidacionException("El nombre no puede estar vacío.");
        }
        if (mail == null || mail.trim().isEmpty()) {
            throw new ValidacionException("El mail no puede estar vacío.");
        }
        try {
            if (usuarioRepository.existeMail(mail)) {
                throw new ValidacionException("Ya existe un usuario con el mail: " + mail);
            }
            Usuario nuevo = new Usuario(nombre, apellido, mail, celular, contrasena, rol);
            return usuarioRepository.guardar(nuevo);
        } catch (SQLException e) {
            throw new ValidacionException("Error de base de datos: " + e.getMessage());
        }
    }

    public List<Usuario> obtenerUsuariosActivos() throws ValidacionException {
        try {
            return usuarioRepository.findAllActivos();
        } catch (SQLException e) {
            throw new ValidacionException("Error de base de datos: " + e.getMessage());
        }
    }

    public Usuario buscarPorId(Long id) throws ValidacionException {
        try {
            Usuario u = usuarioRepository.findById(id);
            if (u == null) {
                throw new ValidacionException("No se encontró ningún usuario activo con el ID: " + id);
            }
            return u;
        } catch (SQLException e) {
            throw new ValidacionException("Error de base de datos: " + e.getMessage());
        }
    }

    public void editarUsuario(Long id, String nombre, String apellido, String mail, String celular, String contrasena, Rol rol) throws ValidacionException {
        Usuario usuario = buscarPorId(id);
        try {
            if (nombre != null && !nombre.trim().isEmpty()) usuario.setNombre(nombre);
            if (apellido != null && !apellido.trim().isEmpty()) usuario.setApellido(apellido);
            if (mail != null && !mail.trim().isEmpty()) {
                if (!usuario.getMail().equalsIgnoreCase(mail)) {
                    if (usuarioRepository.existeMailExcluyendoId(mail, id)) {
                        throw new ValidacionException("Ya existe otro usuario con el mail: " + mail);
                    }
                }
                usuario.setMail(mail);
            }
            if (celular != null && !celular.trim().isEmpty()) usuario.setCelular(celular);
            if (contrasena != null && !contrasena.trim().isEmpty()) usuario.setContraseña(contrasena);
            if (rol != null) usuario.setRol(rol);
            usuarioRepository.actualizar(usuario);
        } catch (SQLException e) {
            throw new ValidacionException("Error de base de datos: " + e.getMessage());
        }
    }

    public void eliminarUsuario(Long id) throws ValidacionException {
        buscarPorId(id);
        try {
            usuarioRepository.eliminar(id);
        } catch (SQLException e) {
            throw new ValidacionException("Error de base de datos: " + e.getMessage());
        }
    }
}