create database morpheus;
use morpheus;

create table Source(
	src_cod int auto_increment primary key,
	src_name char(30) unique,
    src_type enum('1','2'),
	src_address char(150) unique,
	src_registry_date timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE Map_source (
	map_id INT AUTO_INCREMENT PRIMARY KEY,
    src_cod INT,
    map_author CHAR(100),
    map_body TEXT,
    map_title CHAR(100),
	map_date char(100),
    FOREIGN KEY (src_cod) REFERENCES Source(src_cod) ON DELETE CASCADE
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
	new_aut_cod int,
    new_src_cod int,
	new_address LONGTEXT,
    FOREIGN KEY (new_aut_cod) REFERENCES News_author(new_aut_id),
    FOREIGN KEY (new_src_cod) REFERENCES Source(src_cod),
    primary key (new_cod)
);

create table News_tag(
	new_cod int,
	src_tag_cod int,
	foreign key (new_cod) REFERENCES News(new_cod),
	foreign key (src_tag_cod) REFERENCES Source_tag(src_tag_cod)
);

CREATE TABLE News_author (
    new_aut_id INT AUTO_INCREMENT PRIMARY KEY,  
    new_aut_name VARCHAR(500)
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