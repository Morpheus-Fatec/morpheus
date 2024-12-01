CREATE DATABASE morpheus;
USE morpheus;

CREATE TABLE Source (
    src_cod INT AUTO_INCREMENT PRIMARY KEY,
    src_name CHAR(30) UNIQUE NOT NULL,
    src_type ENUM('1', '2') NOT NULL,
    src_address CHAR(150) UNIQUE NOT NULL,
    src_registry_date TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE Map_source (
    map_id INT AUTO_INCREMENT PRIMARY KEY,
    src_cod INT NOT NULL,
    map_author CHAR(100),
    map_body TEXT NOT NULL,
    map_title CHAR(100) NOT NULL,
    map_date CHAR(100) NOT NULL,
    FOREIGN KEY (src_cod) REFERENCES Source(src_cod) ON DELETE CASCADE
);

CREATE TABLE Tag (
    tag_cod INT AUTO_INCREMENT PRIMARY KEY,
    tag_name CHAR(255) UNIQUE NOT NULL
);

CREATE TABLE Source_tag (
    src_tag_cod INT AUTO_INCREMENT PRIMARY KEY,
    src_cod INT NOT NULL,
    tag_cod INT NOT NULL,
    FOREIGN KEY (src_cod) REFERENCES Source(src_cod) ON DELETE CASCADE,
    FOREIGN KEY (tag_cod) REFERENCES Tag(tag_cod) ON DELETE CASCADE
);

CREATE TABLE News_author (
    new_aut_id INT AUTO_INCREMENT PRIMARY KEY,
    new_aut_name VARCHAR(500) UNIQUE NOT NULL
);

CREATE TABLE News (
    new_cod INT AUTO_INCREMENT PRIMARY KEY,
    new_title CHAR(70) NOT NULL,
    new_content TEXT,
    new_registry_date TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    new_aut_cod INT,
    new_src_cod INT,
    new_address LONGTEXT,
    FOREIGN KEY (new_aut_cod) REFERENCES News_author(new_aut_id) ON DELETE SET NULL,
    FOREIGN KEY (new_src_cod) REFERENCES Source(src_cod) ON DELETE CASCADE
);

CREATE TABLE News_tag (
    new_cod INT NOT NULL,
    src_tag_cod INT NOT NULL,
    PRIMARY KEY (new_cod, src_tag_cod),
    FOREIGN KEY (new_cod) REFERENCES News(new_cod) ON DELETE CASCADE,
    FOREIGN KEY (src_tag_cod) REFERENCES Source_tag(src_tag_cod) ON DELETE CASCADE
);

CREATE TABLE Text (
    text_cod INT AUTO_INCREMENT PRIMARY KEY,
    text_description VARCHAR(255) UNIQUE NOT NULL
);

CREATE TABLE Synonymous (
    text_cod INT NOT NULL,
    syn_group INT NOT NULL,
    PRIMARY KEY (text_cod, syn_group),
    FOREIGN KEY (text_cod) REFERENCES Text(text_cod) ON DELETE CASCADE,
    FOREIGN KEY (syn_group) REFERENCES Text(text_cod) ON DELETE CASCADE
);

CREATE TABLE Api (
    api_cod INT AUTO_INCREMENT PRIMARY KEY,
    api_name VARCHAR(30) NOT NULL,
    api_url VARCHAR(500) UNIQUE NOT NULL
);

CREATE TABLE Data_collected_api (
    dat_coll_api_cod INT AUTO_INCREMENT PRIMARY KEY,
    api_cod INT NOT NULL,
    dat_coll_api_registry_date TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    dat_coll_api_content LONGTEXT NOT NULL,
    FOREIGN KEY (api_cod) REFERENCES Api(api_cod) ON DELETE CASCADE
);

CREATE TABLE Tagrelfont (
    rel_id INT AUTO_INCREMENT PRIMARY KEY,
    api_cod INT NOT NULL,
    tag_cod INT NOT NULL,
    FOREIGN KEY (api_cod) REFERENCES Api(api_cod) ON DELETE CASCADE,
    FOREIGN KEY (tag_cod) REFERENCES Tag(tag_cod) ON DELETE CASCADE
);
