create table usuario(
	persona int not null,
	rol varchar( 15 ) not null,
	alcance varchar(1) not null,
	login varchar(24) not null,
	passwd varchar(50) not null,
	red_salud int not null,
	municipio int not null,
	centro_salud int not null,
	activo boolean not null default 't',
	primary key(persona)
);

insert into usuario(persona,rol,alcance,login,passwd,red_salud,municipio,centro_salud,activo) values(1,'administrador','a','juan','juan',1,1,1,'t');