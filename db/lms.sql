CREATE TABLE books (
                       id BIGINT PRIMARY KEY AUTO_INCREMENT,
                       title VARCHAR(255) NOT NULL,
                       author VARCHAR(255) NOT NULL,
                       publisher VARCHAR(255) NOT NULL,
                       year INT NOT NULL,
                       isbn VARCHAR(13) NOT NULL UNIQUE,
                       available BOOLEAN NOT NULL,
                       category VARCHAR(255) NOT NULL,
                       quantity INT NOT NULL,
                       description TEXT,
                       user_id BIGINT,
                       FOREIGN KEY (user_id) REFERENCES users(id)
);