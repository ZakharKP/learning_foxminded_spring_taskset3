-- Drop tables if they already exist
DROP TABLE IF EXISTS lectures_groups CASCADE;
DROP TABLE IF EXISTS lectures CASCADE;
DROP TABLE IF EXISTS courses CASCADE;
DROP TABLE IF EXISTS students CASCADE;
DROP TABLE IF EXISTS groups CASCADE;
DROP TABLE IF EXISTS teachers CASCADE;
DROP TABLE IF EXISTS logins CASCADE;
DROP TABLE IF EXISTS roles CASCADE;
DROP TABLE IF EXISTS users_roles CASCADE;
DROP TABLE IF EXISTS images CASCADE;

DROP TYPE IF EXISTS role CASCADE;


-- Create tables

CREATE TYPE role AS ENUM ('ROLE_STUDENT', 'ROLE_TEACHER', 'ROLE_ADMIN');

CREATE TABLE groups (
    group_id SERIAL PRIMARY KEY,
    group_name VARCHAR(255) NOT NULL
);

CREATE TABLE logins (
    user_name VARCHAR(255) PRIMARY KEY,
    password VARCHAR(255),
    role role
);

CREATE TABLE students (
    student_id SERIAL PRIMARY KEY,
    group_id INT,
    student_first_name VARCHAR(255),
    student_last_name VARCHAR(255),
    user_name VARCHAR(255),
    FOREIGN KEY (user_name) REFERENCES logins(user_name) ON DELETE CASCADE,
    FOREIGN KEY (group_id) REFERENCES groups(group_id) ON DELETE SET NULL
);

CREATE TABLE teachers (
    teacher_id SERIAL PRIMARY KEY,
    teacher_first_name VARCHAR(255),
    teacher_last_name VARCHAR(255), 
    user_name VARCHAR(255),
    FOREIGN KEY (user_name) REFERENCES logins(user_name) ON DELETE CASCADE
);

CREATE TABLE courses (
    course_id SERIAL PRIMARY KEY,
    teacher_id INT,
    course_name VARCHAR(255),
    course_description VARCHAR(255), 
    course_intro_text TEXT, 
    FOREIGN KEY (teacher_id) REFERENCES teachers(teacher_id) ON DELETE SET NULL
);

CREATE TABLE lectures (
    lecture_id SERIAL PRIMARY KEY,
    course_id INT,
    start_time TIMESTAMP NOT NULL,
    FOREIGN KEY (course_id) REFERENCES courses(course_id) ON DELETE CASCADE
);

CREATE TABLE lectures_groups (
    lecture_id INT,
    group_id INT,
    PRIMARY KEY (lecture_id, group_id),
    FOREIGN KEY (lecture_id) REFERENCES lectures(lecture_id) ON DELETE CASCADE,
    FOREIGN KEY (group_id) REFERENCES groups(group_id) ON DELETE CASCADE
);

CREATE TABLE images (
    image_id SERIAL PRIMARY KEY,
    image_data BYTEA NOT NULL,
    image_media_type VARCHAR(255),
    image_file_name VARCHAR(255),
    course_id INT,
    FOREIGN KEY (course_id) REFERENCES courses(course_id) ON DELETE CASCADE
);


