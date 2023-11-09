-- Drop tables if they already exist
DROP TABLE IF EXISTS lectures_groups;
DROP TABLE IF EXISTS lectures;
DROP TABLE IF EXISTS courses;
DROP TABLE IF EXISTS students;
DROP TABLE IF EXISTS groups;
DROP TABLE IF EXISTS teachers;

-- Create tables
CREATE TABLE groups (
    group_id SERIAL PRIMARY KEY,
    group_name VARCHAR(255) NOT NULL
);

CREATE TABLE students (
    student_id SERIAL PRIMARY KEY,
    group_id INT,
    student_first_name VARCHAR(255),
    student_last_name VARCHAR(255),
    FOREIGN KEY (group_id) REFERENCES groups(group_id) ON DELETE SET NULL
);

CREATE TABLE teachers (
    teacher_id SERIAL PRIMARY KEY,
    teacher_first_name VARCHAR(255),
    teacher_last_name VARCHAR(255)
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
