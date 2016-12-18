create schema fortunes;
grant all on fortunes.* to 'javadbapp';
use fortunes;
create table fortune (
    id  INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    saying longtext not null
);
