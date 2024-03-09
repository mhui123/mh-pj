FROM tomcat:latest
ADD mhproject.war /usr/local/tomcat/webapps/
EXPOSE 9900
RUN ln -snf /usr/share/zoneinfo/Asia/Seoul /etc/localtime && echo Asia/Seoul > /etc/timezone
CMD ["catalina.sh", "run"]