package integrado.prog2.repository;

import integrado.prog2.config.DatabaseConnection;
import integrado.prog2.entities.Usuario;
import integrado.prog2.enums.Rol;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UsuarioRepository {

    public Usuario guardar(Usuario usuario) throws SQLException {
        String sql = "INSERT INTO usuarios (nombre, apellido, mail, celular, contrasena, rol) VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement ps = DatabaseConnection.getConnection().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, usuario.getNombre());
            ps.setString(2, usuario.getApellido());
            ps.setString(3, usuario.getMail());
            ps.setString(4, usuario.getCelular());
            ps.setString(5, usuario.getContraseña());
            ps.setString(6, usuario.getRol().name());
            ps.executeUpdate();
            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) {
                usuario.setId(rs.getLong(1));
            }
        }
        return usuario;
    }

    public List<Usuario> findAllActivos() throws SQLException {
        String sql = "SELECT id, nombre, apellido, mail, celular, contrasena, rol, eliminado, created_at FROM usuarios WHERE eliminado = false ORDER BY id";
        List<Usuario> lista = new ArrayList<>();
        try (PreparedStatement ps = DatabaseConnection.getConnection().prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) lista.add(mapear(rs));
        }
        return lista;
    }

    public Usuario findById(Long id) throws SQLException {
        String sql = "SELECT id, nombre, apellido, mail, celular, contrasena, rol, eliminado, created_at FROM usuarios WHERE id = ? AND eliminado = false";
        try (PreparedStatement ps = DatabaseConnection.getConnection().prepareStatement(sql)) {
            ps.setLong(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return mapear(rs);
        }
        return null;
    }

    public Usuario findByMailCualquiera(String mail) throws SQLException {
        String sql = "SELECT id, nombre, apellido, mail, celular, contrasena, rol, eliminado, created_at FROM usuarios WHERE LOWER(mail) = LOWER(?)";
        try (PreparedStatement ps = DatabaseConnection.getConnection().prepareStatement(sql)) {
            ps.setString(1, mail);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return mapear(rs);
        }
        return null;
    }

    public void eliminar(Long id) throws SQLException {
        String sql = "UPDATE usuarios SET eliminado = true WHERE id = ?";
        try (PreparedStatement ps = DatabaseConnection.getConnection().prepareStatement(sql)) {
            ps.setLong(1, id);
            ps.executeUpdate();
        }
    }

    public boolean existeMail(String mail) throws SQLException {
        String sql = "SELECT COUNT(*) FROM usuarios WHERE LOWER(mail) = LOWER(?) AND eliminado = false";
        try (PreparedStatement ps = DatabaseConnection.getConnection().prepareStatement(sql)) {
            ps.setString(1, mail);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return rs.getInt(1) > 0;
        }
        return false;
    }

    public boolean existeMailExcluyendoId(String mail, Long id) throws SQLException {
        String sql = "SELECT COUNT(*) FROM usuarios WHERE LOWER(mail) = LOWER(?) AND eliminado = false AND id != ?";
        try (PreparedStatement ps = DatabaseConnection.getConnection().prepareStatement(sql)) {
            ps.setString(1, mail);
            ps.setLong(2, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return rs.getInt(1) > 0;
        }
        return false;
    }

    public void actualizar(Usuario usuario) throws SQLException {
        String sql = "UPDATE usuarios SET nombre=?, apellido=?, mail=?, celular=?, contrasena=?, rol=? WHERE id=?";
        try (PreparedStatement ps = DatabaseConnection.getConnection().prepareStatement(sql)) {
            ps.setString(1, usuario.getNombre());
            ps.setString(2, usuario.getApellido());
            ps.setString(3, usuario.getMail());
            ps.setString(4, usuario.getCelular());
            ps.setString(5, usuario.getContraseña());
            ps.setString(6, usuario.getRol().name());
            ps.setLong(7, usuario.getId());
            ps.executeUpdate();
        }
    }

    public void reactivar(Long id, String nombre, String apellido, String celular, String contrasena, Rol rol) throws SQLException {
        String sql = "UPDATE usuarios SET eliminado = false, nombre = ?, apellido = ?, celular = ?, contrasena = ?, rol = ? WHERE id = ?";
        try (PreparedStatement ps = DatabaseConnection.getConnection().prepareStatement(sql)) {
            ps.setString(1, nombre);
            ps.setString(2, apellido);
            ps.setString(3, celular);
            ps.setString(4, contrasena);
            ps.setString(5, rol.name());
            ps.setLong(6, id);
            ps.executeUpdate();
        }
    }

    private Usuario mapear(ResultSet rs) throws SQLException {
        Usuario u = new Usuario();
        u.setId(rs.getLong("id"));
        u.setNombre(rs.getString("nombre"));
        u.setApellido(rs.getString("apellido"));
        u.setMail(rs.getString("mail"));
        u.setCelular(rs.getString("celular"));
        u.setContraseña(rs.getString("contrasena"));
        u.setRol(Rol.valueOf(rs.getString("rol")));
        u.setEliminado(rs.getBoolean("eliminado"));
        u.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
        return u;
    }
}
