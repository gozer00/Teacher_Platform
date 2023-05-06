# Bachelor thesis: Web application for creating and sharing lessons
This is a web application using Spring, React and MySQL. 

Steps to start the application with docker:
1. navigate to project folder in the shell
2. start the docker container with 'docker compose up'
3. execute 'docker ps' and copy the ID of the database container
4. open the database container's shell with 'docker exec -it *"id of running container"* bash'
5. execute 'mysql -utest -p'
6. type password: test
7. execute 'use teacher_platform_db;'
8. execute 'insert into roles values (1, 'ROLE_USER');'
9. execute 'exit'