package com.academico;
import com.academico.application.service.CursoService;
import com.academico.domain.repository.CursoRepository;
import com.academico.infrastructure.adapter.out.persistence.CursoRepositoryPostgres;
import com.academico.infrastructure.adapter.out.persistence.EstudianteRepositoryJson;
import com.academico.infrastructure.adapter.out.persistence.EstudianteRepositoryPostgres;
import com.academico.presentation.CursoUI;
import com.academico.presentation.EstudianteUI;

import com.academico.application.service.EstudianteService;
import com.academico.domain.repository.EstudianteRepository;

import  java.util.Scanner;

public class Main {

    public static void main(String[] args) {

        // 1. Adaptador de salida: almacena en JSON
        //EstudianteRepository repository=new EstudianteRepositoryJson();
        EstudianteRepository estudianteRepository=new EstudianteRepositoryPostgres();
        CursoRepository cursoRepository=new CursoRepositoryPostgres();


        //2. Nucleo de la aplicacion: caso de uso
        EstudianteService estudianteService=new EstudianteService(estudianteRepository);
        CursoService cursoService=new CursoService(cursoRepository);

        // presentacion
        EstudianteUI estudianteUI=new EstudianteUI(estudianteService);
        CursoUI cursoUI =new CursoUI(cursoService);


        Scanner sc = new Scanner(System.in);

        int opcion;

        do {

            System.out.println("\n=== SISTEMA DE GESTIÓN ACADÉMICA ===");
            System.out.println("1. Gestionar estudiantes");
            System.out.println("2. Gestionar cursos");
            System.out.println("0. Salir");
            System.out.print("Seleccione una opción: ");

            opcion = sc.nextInt();
            sc.nextLine();
            switch (opcion) {

                case 1-> estudianteUI.mostrarMenu(sc);
                case 2-> cursoUI.mostrarMenu(sc);
                case 0->System.out.println("Sistema finalizado");
                default -> System.out.println("Opción no valida");
            }

        } while (opcion != 0);

        sc.close();
    }
}