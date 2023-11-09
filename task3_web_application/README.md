# Task 3: Web Application

This repository contains my solutions for the Web Application task set of the [Foxminded](https://foxminded.ua/) Java Spring Development course. The tasks in this set focus on develop University Schedule web application.

## Introduction
The primary objective of this project is to create a web-based platform that allows students and teachers to access and manage their class timetables efficiently. Users will have the ability to view their schedules for the day or month, making the scheduling process more streamlined and convenient.

<br>
<details>
<summary>Task List</summary>
<br>
<div style="margin-left: 20px;">
<details>
  <summary> Task 3.1 Planning: Decompose university </summary>

## Important: 

In the next series of tasks you're going to develop Univesity Schedule web application, make sure to give repo meaningful name (ex. university-cms)
## Assignment:

Analyze and decompose University (create UML class diagram for application).
You should make a research and describe university structure. The main feature of the application is Class Timetable for students and teachers. Students or teachers can get their timetable for a day or for a month.

Add png image to the separate GitLab project.

Add text description of main user stories using markup language Example:

Teacher can view own schedule flow:

Given user is logged on as Teacher

- User can see and navigate to `My Schedule` menu
- User should see own Teacher schedule according with selected date/range filter
</details>
<br>
<details>
  <summary> Task 3.2 Bootstrap project </summary>

## Assignment
Create new Spring Boot project using Initializer with dependencies:
Spring Web (Build web, including RESTful, applications using Spring MVC. Uses Apache Tomcat as the default embedded container.)
Spring Data JPA (Persist data in SQL stores with Java Persistence API using Spring Data and Hibernate.)
Thymeleaf (A modern server-side Java template engine for both web and standalone environments. Allows HTML to be correctly displayed in browsers and as static prototypes.)
Flyway Migration (Version control for your database so you can migrate from any version (incl. an empty database) to the latest version of the schema.)
H2 Database or PostgreSQL Driver of your choice
Create model and schema initializing sql migration script according with your UML diagrama

Create JPA repositories and service layer with base CRUD operations

## Important:

From now on you should cover all your code (repository, service) with test in case you add any logic like custom query or multiple repository/service calls in one method

Example:

```
@Repository
public interface GroupRepository extends JpaRepository<Group, Long> {
        
        // should not be covered with test 
	Optional<Group> findByGroupName(String name) throws SQLException;

        // sould be covered with test
	@Query(value = "SELECT gr.* "
			+ "FROM Groups gr inner join (SELECT COUNT(student_id) as studCount, group_id as group_id_counter FROM Students "
			+ "group by group_id " + ") as counter on group_id = group_id_counter "
			+ "WHERE studCount <= :stdCount", nativeQuery = true)
	List<Group> findWithEquelOrLessStudents(@Param("stdCount") int count) throws SQLException;
}
```

```

@Service
public class StudentService {

        // should not be covered with tests
        @Transactional
	public void deleteById(Long id) throws SQLException {
		studentRepository.delete(studentOpt.get());
	}
  
       // should be covered with test
       @Transactional
	public Student addCourse(Long studentId, Long courseId) throws SQLException {
	
		var student = studentRepository.findById(studentId);
		var course = courseRepository.findById(courseId);

		if (course.isPresent() && student.isPresent()) {
			Optional<Course> courseInStudent = student.get().getCourses().stream()
					.filter(c -> c.getId().equals(courseId)).findFirst();
			if (courseInStudent.isEmpty()) {
				student.get().getCourses().add(course.get());
				studentRepository.save(student.get());
				return student.get();
			}
		}

		throw new Somexception("Could not add student to course");
	}

}

```
</details>
<br>
<details>
<summary> Task 3.3 Create basic UI </summary>

## Assignment:
Add Bootstrap js/css support to your project (webjars recommended)

Add basic data generation or migration script to populate your db with sample data

Create welcome page and controller with menu with main entities from your model

## Important use thymeleaf templates and reusable fragments

Create pages with tables to list content from DB for each Entity and link those pages from main menu

Cover controllers with Spring MVC tests
</details>
<br>
<details>
  <summary> Task 3.4 Adding Security </summary>
  
## Assignment

1.  Review your user/roles model, and ask your mentor for clarifications regarding your security model. For example, you can add ADMIN, STUDENT, TEACHER, and STUFF roles.
    
2.  Use form security for user authentication.
    
3.  Create an admin panel for assigning a new user's role and create services that help the admin manage users.
    
4.  add required changes with login/logout functionality and logged-in user information to UI
    

## Read:  
[https://www.baeldung.com/spring-security-login](https://www.baeldung.com/spring-security-login "https://www.baeldung.com/spring-security-login")  
[https://www.baeldung.com/spring-security-method-security](https://www.baeldung.com/spring-security-method-security "https://www.baeldung.com/spring-security-method-security")  
[https://www.thymeleaf.org/doc/articles/springsecurity.html](https://www.thymeleaf.org/doc/articles/springsecurity.html)  
[https://docs.spring.io/spring-security/site/docs/4.2.x/reference/html/test-method.html](https://docs.spring.io/spring-security/site/docs/4.2.x/reference/html/test-method.html "https://docs.spring.io/spring-security/site/docs/4.2.x/reference/html/test-method.html")  
  
Security configuration example:  
  
`@Bean   SecurityFilterChain config(HttpSecurity httpSecurity) throws Exception {    return httpSecurity.authorizeHttpRequests() .requestMatchers("/css/**", "/webjars/**").permitAll() // public matcher first    .requestMatchers("/foo").hasRole("FOO") // single role    .requestMatchers("/bar", "/foo-bar").hasAnyRole("FOO", "FOO_BAR") // multiple roles    .anyRequest().authenticated() // other requests need to have any role    .and().formLogin() .and().build();    }`  

## Example:

    User administration flow
    
    Given User `A` logged in with Admin role
    - User 'A' should be able to navigate to admin panel
    - User without admin role should not have access to user admin panel
    - User 'A' should be able to list all registered users on user admin page
    - User 'A' should be able to set required role for each registered user... etc
  </details>


<br>
  <details>
  <summary> Task 3.5 Implement Course view + edit feature </summary>
  
## Assignment
1.  Using your flows descriptions from task 3.1 create list of flows to implement, call it features, consult with Mentor if required.

##Example:

User administration flow

Given User 'A' logged in with Admin role
- User 'A' should be able to create/read/update/delete courses.
Given User 'B' logged in with Student or Teacher role
- User 'B' should be able to list all courses (read access).
Given User 'C' logged in with Stuff rolef
- User 'C' should be able to create/read/update all courses
- User 'C' should be able to assign/reassign teacher to a course
- User 'C' should be able to assign/reassign groups to a course.
- ... etc

2.  Consider feature implementation as subtask(made in new branch and merged int main/master on completion)

For each feature, implement UI pages(usually list, create, edit, delete, etc), controller/controller methods, service/service methods, repository methods.

Controller tests are mandatory, add other components tests if required

  </details>
<br>
  <details>
  <summary> Task 3.6 Implement Groups view + edit feature</summary>
  
##Assignment
Using your flows descriptions from task 3.1 create list of flows to implement, call it features, consult with Mentor if required.
Example:

User administration flow

```
 Given User `A` logged in with Admin role
	- User 'A' can Create/Read/Update/Delete group information
Given User `B` logged in with Student or Teacher role.
	- User 'A' should be able to list all groups information (read access).
 Given User `C` logged in with Stuff role
	- User 'A' should be able to Create/Read/Update group information.
... etc
```

Consider feature implementation as subtask(made in new branch and merged int main/master on completion)
For each feature, implement UI pages(usually list, create, edit, delete, etc), controller/controller methods, service/service methods, repository methods.

Controller tests are mandatory, add other components tests if required
  </details>
  

  <details>
  <summary> Task 3.7 Implement Students view + edit feature
   </summary>
   
##Assignment
Using your flows descriptions from task 3.1 create list of flows to implement, call it features, consult with Mentor if required.

###Example:

```
Given User `A` logged in with Admin, or Stuff role.
- User 'A' can assign/ reassign Students to Group
Given User `B` logged in with Admin, Stuff, Student, or Teacher role.
- User 'B' should be able to list all students in a group (read access).
... etc

```
Consider feature implementation as subtask(made in new branch and merged into main/master on completion)
For each feature, implement UI pages(usually list, create, edit, delete, etc.), controller/controller methods, service/service methods, repository methods.

Controller tests are mandatory, add other components tests if required.

  </details>
<br>
  <details>
  <summary> Task 3.8 Implement Teachers view + edit features</summary>
  
##Assignment
Using your flows descriptions from task 3.1 create list of flows to implement, call it features, consult with Mentor if required.

###Example:

```
Given User `B` logged in with Teacher role.
- User 'B' should be able to list all its courses.
... etc
```

Consider feature implementation as subtask(made in new branch and merged into main/master on completion)
For each feature, implement UI pages(usually list, create, edit, delete, etc.), controller/controller methods, service/service methods, repository methods.

Controller tests are mandatory, add other components tests if required.Assignment

  </details>
<br>
  <details>
  <summary> Task 3.9 Implement Schedule view + edit features</summary>
  
##Assignment
Using your flows descriptions from task 3.1 create list of flows to implement, call it features, consult with Mentor if required.

###Example:

```
Given User `A` logged in with Admin or Stuff role.
- User 'A' should be able to create/read/update/delete new schedule.
Given User 'B' logged in with Student or Teacher role.
- User 'B' should be able to list all its schedules.
... etc
```

Consider feature implementation as subtask(made in new branch and merged into main/master on completion)
For each feature, implement UI pages(usually list, create, edit, delete, etc.), controller/controller methods, service/service methods, repository methods.

Controller tests are mandatory, add other components tests if required.

  </details>
<br>

<details>
<summary>Task 3.10 Finallize app functionality</summary>

##Assignment

1.  Using your flows descriptions from `task 3.1` create list of flows to implement, call it `features`, consult with Mentor if required.

Example:

    Given Anonymous User `D`
</details>

</div>
</details>
<br>
<details>
  <summary>Technologies Used</summary>
  <br>

*   **Java:** The primary programming language used for the project.
*   **Spring Boot:** The project is built using the Spring Boot framework, which simplifies the development of production-ready applications.
*   **Spring Boot Starters:**
    *   `spring-boot-starter-data-jpa:` Provides support for data access using Java Persistence API (JPA) and Hibernate.
    *   `spring-boot-starter-thymeleaf:` Integrates the Thymeleaf template engine for server-side rendering.
    *   `spring-boot-starter-web:` Includes everything needed to build a web application.
    *   `spring-boot-starter-security:` Provides security features and integration for user authentication and authorization.
*   **Flyway:** A database migration tool used for version control of your database schema.
*   **PostgreSQL Driver:** The PostgreSQL database driver used for database connections.
*   **Testcontainers:** Provides testing support for running dependencies such as databases in containers during testing.
*   **Lombok:** A library that reduces boilerplate code by generating getters, setters, and other common methods at compile time.
*   **Apache Log4j:** A logging framework used for logging and managing application logs.
*   **Webjars:** Web libraries (e.g., Bootstrap, jQuery, FullCalendar, Bootstrap Datepicker, Bootstrap Select, Popper.js, DataTables, Summernote, Dropzone) packaged as JARs for easier integration into your project.
*   **Thymeleaf-Extras-SpringSecurity5:** Thymeleaf extension for Spring Security integration.

</details>

<br>
<details>
  <summary>How to try:</summary>
  <br>


To try this project, you'll need to have [Docker](https://www.docker.com/get-started/) and [Git](https://git-scm.com/book/en/v2/Getting-Started-Installing-Git) installed. Follow the instructions below:

  1. Clone this repository:
      ```
      git clone <repository path>
      ```

  2. Navigate to the directory with the project:
  
      ```
      cd <path to project dir>
      ```
      (probably)
      ```
      cd task3_web_application
      ```

  3. Run Docker Compose:
    ```
      docker-compose up
      ```
      (or)
      ```
      sudo docker-compose up
      ```

  4. After the app is running, you can use your browser to access the application at [http://localhost:8080](http://localhost:8080).

</details>
<br>
<details>
  <summary>Guide</summary>

 

  When you start the application for the first time, it will automatically populate the database with test data, ensuring a smooth onboarding process:

  - **4 Teachers**
  - **200 Students**
  - **10 Groups**
  - **10 Courses**
  - **Lectures scheduled from today to six months ahead**

## Getting Started

  To explore the university application, follow these steps:

  1. Open your web browser and navigate to [http://localhost:8080](http://localhost:8080).

## Explore as a Guest

  As a guest user, you have access to:

  - A **Welcome Page** with a brief introduction to the university.
  - An overview of the **Course List**.

## Logged-In Users

  Once you log in as a registered user, you can take advantage of the following features:
  - View lists of students, teachers, courses, and groups.
  - Navigate through the university's schedule.

### Admin User

  If you log in as an admin (username: teacher1, password: password1), you have administrative privileges:

  - **Add**, **Edit**, and **Delete** students, lectures, teachers, groups, and courses.
  - Admin also is teacher.

### Teacher User

  As a teacher (usernames from teacher2 to teacher4, passwords from password2 to password4), you can:

  - View your assigned **Courses**, **Groups**, and **Students**.
  - Check your **Personal Schedule**.

### Student User

  If you log in as a student (usernames from student1 to student200, passwords from password1 to password200), you can:

  - Access details about your **Courses**, **Group**, **Teachers**, and **Classmates**.
  - Navigate your **Personal Schedule**.

</details>
