# fast_P2_PointManagement_Batch

<br/>

03. docker mysql 연동하기
    
> https://www.docker.com/get-started/

<br/>

### Docker MySQL RUN

docker run : 도커 컨테이너 실행  
-p 33060:3306 : 포트 파인딩 컨테이너 내부의 3306포트를 외부의 33060와 연결한다.  
--name : 컨테이너 이름  
-e : 컨테이너의 환경변수 지정 (MYSQL_ROOT_PASSWORD=password 을 통해 password를 password로 지정함)  
-d : 컨테이너 실행은 백그라운드에서 진행

> docker run -p 33070:3307 --name point-mysql -e MYSQL_ROOT_PASSWORD=password -d mysql:8.0.26

> docker ps


|CONTAINER ID|IMAGE|COMMAND|CREATED|STATUS|PORTS|NAMES|
|---|---|---|---|---|---|---|
|690aa0e85f88|mysql:8.0.26|"docker-entrypoint.s…"|10 seconds ago|Up 9 seconds|3306/tcp, 33060/tcp, 0.0.0.0:33070->3307/tcp|point-mysql|

<br/>

### Docker MySQL 접속해보기

- 컴테이너 bash 실행

> docker exec -i -t < CONTAINER ID > /bin/bash

- mysql 실행(password를 물어보면 'password' 입력)

> mysql -u root -p

- root의 password 초기화

> alter user 'root'@'%' identified with mysql_native_password by 'password';

<br/>

### Docker MySQL 컨테이너 삭제하기
- 컨테이너 Stop
> docker stop < container Id >

- 컨테이너 삭제
> docker rm < container Id >

<br/>

### Docker MySQL 이미지 삭제하기
- 이미지 가져오기
> docker pull < image이름:버전 >

- 이미지 조회
> docker images

|REPOSITORY|TAG|IMAGE ID|CREATED|SIZE|
|---|---|---|---|---|
|mysql|8.0.26|9da615fced53|2 years ago|514MB|

- 이미지 삭제
> docker rmi < image Id >
