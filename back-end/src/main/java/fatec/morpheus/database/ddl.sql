create database morpheus;
use morpheus;

create table Source(
	src_cod int auto_increment primary key,
	src_name char(30) unique,
    src_type enum('1','2'),
	src_address char(100) unique,
	src_registry_date timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP 
);

create table Tag(
	tag_cod int auto_increment primary key,
    tag_name char(20) unique
);

create table Source_tag(
	src_tag_cod int auto_increment,
	src_cod int,
	tag_cod int,
    primary key (src_tag_cod),
	foreign key (src_cod) REFERENCES Source(src_cod),
	foreign key (tag_cod) REFERENCES Tag(tag_cod)
);

create table News(
	new_cod int auto_increment,
	new_title char(70),
    new_content text,
	new_registry_date timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
    primary key (new_cod)
);

create table Synonymous(
	tag_cod int,
	syn_group int,
    primary key (tag_cod,syn_group),
	foreign key (tag_cod) REFERENCES Tag(tag_cod)
);

create table News_tag(
	new_cod int,
	src_tag_cod int,
	foreign key (new_cod) REFERENCES News(new_cod),
	foreign key (src_tag_cod) REFERENCES Source_tag(src_tag_cod)
);

create table Texto(
    texto_cod int auto_increment,
    texto_description varchar(255),
    primary key (texto_cod)
);

create table Synonymous(
    texto_cod int,
    syn_group int,
    primary key (texto_cod, syn_group),
    foreign key (texto_cod) REFERENCES Texto(texto_cod) ON DELETE CASCADE,
	foreign key (syn_group) REFERENCES texto(texto_cod) ON DELETE CASCADE
);