create table if not exists cursos (
    id integer primary key,
    nombre varchar(120) not null,
    creditos integer not null check (creditos > 0)
);
create index  if not exists idx_cursos_nombre
    on cursos(nombre);