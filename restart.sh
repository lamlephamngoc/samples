echo "------------------------------------------------------------------"
echo "Nhớ update Google Session ở file curl_chat.txt nha các tình yêu :D"
echo "------------------------------------------------------------------"

sleep 3

mvn clean install -DskipTests
kill -9 `ps aux | grep samples.jar | awk '{print $2}'`
nohup java -jar target/samples.jar &
