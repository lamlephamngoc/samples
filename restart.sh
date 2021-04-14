mvn clean install -DskipTests
kill -9 `ps aux | grep samples.jar | awk '{print $2}'`
nohup java -jar target/samples.jar &
