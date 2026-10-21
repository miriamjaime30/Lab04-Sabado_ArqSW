package com.academico.infrastructure.adapter.out.persistence;

import com.academico.domain.model.Curso;
import com.academico.domain.repository.CursoRepository;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;


import java.io.FileWriter;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CursoRepositoryJson implements CursoRepository {

    private final String archivo = "data/cursos.json";
    private final Gson gson = new Gson();

    @Override
    public void guardar(Curso curso){
        List<Curso> cursos=listar();
        cursos.add(curso);
        escribir(cursos);

    }

    @Override
    public List<Curso> listar() {

        try (Reader reader = new FileReader(archivo)) {

            Type tipo = new TypeToken<List<Curso>>() {}.getType();

            List<Curso> cursos = gson.fromJson(reader, tipo);

            return cursos != null ? cursos : new ArrayList<>();

        } catch (IOException e) {
            return new ArrayList<>();
        }
    }

    @Override
    public Optional<Curso>buscarPorId(int id){
        return listar().stream()
                .filter(c-> c.getId()==id)
                .findFirst();
    }

    @Override
    public boolean actualizar(Curso curso){
        List<Curso>cursos = listar();

        for(Curso c : cursos){
            if (c.getId()== curso.getId()){
                c.setNombre(curso.getNombre());
                c.setCreditos(curso.getCreditos());
                escribir(cursos);
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean eliminar (int id){
        List<Curso>cursos = listar();
        boolean eliminado = cursos.removeIf(c->c.getId()==id);
        if (eliminado){
            escribir(cursos);
        }
        return eliminado;
    }
    private void escribir(List<Curso>cursos){
        try(Writer writer = new FileWriter(archivo)){
            gson.toJson(cursos, writer);
        }catch (IOException e){
            System.out.println("Error al guardar cursos");
        }
    }
}