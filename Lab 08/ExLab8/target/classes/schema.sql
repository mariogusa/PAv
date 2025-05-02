CREATE TABLE continents (
  id    INT PRIMARY KEY AUTO_INCREMENT,
  name  VARCHAR(100) NOT NULL
);

CREATE TABLE countries (
  id           INT PRIMARY KEY AUTO_INCREMENT,
  name         VARCHAR(100) NOT NULL,
  code         VARCHAR(10),
  continent_id INT,
  FOREIGN KEY (continent_id) REFERENCES continents(id)
);