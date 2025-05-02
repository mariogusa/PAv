DROP TABLE IF EXISTS countries;

CREATE TABLE countries (
    id INT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(100) NOT NULL,
    code VARCHAR(10) NOT NULL,
    continent_id INT,
    FOREIGN KEY (continent_id) REFERENCES continents(id)
);