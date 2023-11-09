-- Drop tables if they already exist
DROP TABLE IF EXISTS lectures_groups CASCADE;
DROP TABLE IF EXISTS lectures CASCADE;
DROP TABLE IF EXISTS courses CASCADE;
DROP TABLE IF EXISTS students CASCADE;
DROP TABLE IF EXISTS groups CASCADE;
DROP TABLE IF EXISTS teachers CASCADE;
DROP TABLE IF EXISTS logins CASCADE;
DROP TABLE IF EXISTS roles CASCADE;

-- Create tables
CREATE TABLE groups (
    group_id SERIAL PRIMARY KEY,
    group_name VARCHAR(255) NOT NULL
);

CREATE TABLE logins (
    user_name VARCHAR(255) PRIMARY KEY,
    password VARCHAR(255)
);

CREATE TABLE roles (
    role_name VARCHAR(255) PRIMARY KEY
);

CREATE TABLE users_roles (
	role_name VARCHAR(255),    
	user_name VARCHAR(255),    
    PRIMARY KEY (role_name, user_name),
    FOREIGN KEY (role_name) REFERENCES roles(role_name) ON DELETE CASCADE,
    FOREIGN KEY (user_name) REFERENCES logins(user_name) ON DELETE CASCADE    
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


