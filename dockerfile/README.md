# 도커파일 사용법

- docker-compose down

- docker-compose up --build

## 컨테이너에 vim 없을 경우
  apt-get update
  apt-get install vim

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
