# 도커파일 사용법

## docker-compose down

## docker-compose up --build

## vim 없을 경우 apt-get update

apt-get update
apt-get install vim
apt-get install -y net-tools
apt-get install -y iproute2

## db연동문제 해결

[docker-compose.yml]

- healthcheck:
  test: ["CMD", "mysqladmin", "ping", "-h", "localhost"]
  timeout: 20s
  retries: 10
  interval: 10s

- app:
  build: .
  ports: - 9900:9900
  environment:
  SPRING_DATASOURCE_URL: jdbc:mysql://docker-mysql:3306/mh_db
  SPRING_DATASOURCE_USERNAME: root
  SPRING_DATASOURCE_PASSWORD: 1234
  depends_on:
  docker-mysql:
  condition: service_healthy

[application.yml]

    #도커용.
    url: jdbc:mysql://docker-mysql:3306/mh_db   # db 컨테이너이름:내부포트
    username: root
    password: 1234
    driver-class-name: com.mysql.cj.jdbc.Driver #org.h2.Driver

## 9900 접속시 아무반응 없음 :

- app container > server.xml 의 <Connector port="8080" ...> 을 port="9900"으로 수정

- /usr/local/tomcat/conf/server.xml >
  - #<Host> 태그 아래에 #<Context path="/" docBase="mhproject" reloadable="true" /> 속성을 추가하여
    /mhproject 대신 /로 접속하도록 contextPath 세팅 변경
