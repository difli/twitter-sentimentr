if [ -d "../twitter-sentimentr" ] 
then
	docker rm twitter-sentimentr-en-service --force
	docker rm twitter-ui --force
	docker rm twitter-reader --force
	docker run -d --env-file ./env-file-docker --name twitter-sentimentr-en-service -p 8083:8083 dieterfl/twitter-sentimentr-en-service:latest 
	docker run -d --env-file ./env-file-docker -v "$(pwd)"/your-input-files/secure-connect.zip:/credential-files/secure-connect.zip --name twitter-ui -p 8081:8081 dieterfl/twitter-ui:latest 
	docker run -d --env-file ./env-file-docker -v "$(pwd)"/your-input-files/twitter-credentials.json:/credential-files/twitter-credentials.json --name twitter-reader -p 8082:8082 dieterfl/twitter-reader:latest 
else
    echo "Please execute run-apps-in-docker.sh script within the twitter-sentimentr folder."
fi