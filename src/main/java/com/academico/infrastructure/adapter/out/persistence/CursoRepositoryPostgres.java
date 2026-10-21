package com.academico.infrastructure.adapter.out.persistence;

import com.academico.domain.model.Curso;
import com.academico.domain.repository.CursoRepository;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CursoRepositoryPostgres implements CursoRepository {
    private final String url;
    private final String usuario;
    private final String password;

    public CursoRepositoryPostgres(){
        this(
                System.getenv().getOrDefault(
                        "DB_URL",
                        "jdbc:postgresql://localhost:5432/sistema_gestion_academica"
                ),
                System.getenv().getOrDefault("DB_USER", "postgres"),
                System.getenv("DB_PASSWORD")
        );
    }
    public CursoRepositoryPostgres(String url, String usuario, String password){
        this.url = url;
        this.usuario=usuario;
        this.password=password;
    }
    private Connection conectar()throws SQLException{
        if(password==null || password.isBlank()){
            throw new IllegalStateException(
                    "Configura la variable de entorno DB_PASSWORD"
            );
        }
        return DriverManager.getConnection(url, usuario,password);

    }
    @Override
    public void guardar (Curso curso){
        String sql = "INSERT INTO cursos (id, nombre, creditos) VALUES(?, ?, ?)";
        try(Connection con = conectar();
            PreparedStatement ps = con.prepareStatement(sql)){

            ps.setInt(1, curso.getId());
            ps.setString(2, curso.getNombre());
            ps.setInt(3, curso.getCreditos());
            ps.executeUpdate();
        }catch (SQLException e){
            throw new IllegalStateException("No se pudo registrar el curso", e);
        }
    }
    @Override
    public List<Curso> listar() {
        String sql = "SELECT id, nombre, creditos FROM cursos ORDER BY id";
        List<Curso> cursos = new ArrayList<>();

        try (Connection con = conectar();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                cursos.add(new Curso(
                        rs.getInt("id"),
                        rs.getString("nombre"),
                        rs.getInt("creditos")
                ));
            }
            return cursos;

        } catch (SQLException e) {
            throw new IllegalStateException("No se pudieron listar los cursos.", e);
        }
    }
    @Override
    public Optional<Curso> buscarPorId(int id) {
        String sql = "SELECT id, nombre, creditos FROM cursos WHERE id = ?";

        try (Connection con = conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(new Curso(
                            rs.getInt("id"),
                            rs.getString("nombre"),
                            rs.getInt("creditos")
                    ));
                }
                return Optional.empty();
            }

        } catch (SQLException e) {
            throw new IllegalStateException("No se pudo buscar el curso.", e);
        }
    }

    @Override
    public boolean actualizar(Curso curso) {
        String sql = "UPDATE cursos SET nombre = ?, creditos = ? WHERE id = ?";

        try (Connection con = conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, curso.getNombre());
            ps.setInt(2, curso.getCreditos());
            ps.setInt(3, curso.getId());

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            throw new IllegalStateException("No se pudo actualizar el curso.", e);
        }
    }

    @Override
    public boolean eliminar(int id) {
        String sql = "DELETE FROM cursos WHERE id = ?";

        try (Connection con = conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id);
            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            throw new IllegalStateException("No se pudo eliminar el curso.", e);
        }
    }


}
