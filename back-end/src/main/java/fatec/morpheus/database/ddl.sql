create database morpheus;
use morpheus;

create table Tag(
	tag_cod int auto_increment primary key,
    tag_name char(20) unique
);

create table Source(
	src_cod int auto_increment primary key,
	src_name char(30) unique,
    src_type enum('1','2'),
	src_address char(100) unique,
	src_registry_date timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP 
);

create table News(
	new_cod int auto_increment,
	new_title char(70),
	new_source int,
	new_registry_date timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
    primary key (new_cod),
	foreign key (new_source) REFERENCES Source(src_cod)
);

create table News_tag(
	new_cod int auto_increment,
	tag_cod int,
    primary key (new_cod,tag_cod),
	foreign key (new_cod) REFERENCES News(new_cod),
	foreign key (tag_cod) REFERENCES Tag(tag_cod)
);

create table Synonymous(
	syn_tag_cod int,
	syn_group int,
    primary key (syn_tag_cod,syn_group),
	foreign key (syn_tag_cod) REFERENCES Tag(tag_cod)
);






