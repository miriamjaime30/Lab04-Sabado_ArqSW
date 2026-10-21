package com.academico.application.service;

import com.academico.application.port.in.CursoUseCase;
import com.academico.domain.model.Curso;
import com.academico.domain.repository.CursoRepository;
import java.util.List;

public class CursoService implements CursoUseCase {

    private final CursoRepository repository;

    public CursoService(CursoRepository repository) {
        this.repository = repository;
    }

    @Override
    public void registrar(Curso curso) {
        repository.guardar(curso);
    }

    @Override
    public List<Curso> listar() {
        return repository.listar();
    }

    public boolean actualizar(Curso curso) {
        return repository.actualizar(curso);
    }

    public boolean eliminar(int id) {
        return repository.eliminar(id);
    }
}