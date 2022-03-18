if [ -d "../twitter-sentimentr" ] 
then
    docker image rm dieterfl/twitter-sentimentr-en-service:latest
    docker image rm dieterfl/twitter-ui:latest
    docker image rm dieterfl/twitter-reader:latest
    cd twitter-sentimentr-service-en
    mvn spring-boot:build-image -Dspring-boot.build-image.imageName=dieterfl/twitter-sentimentr-en-service:latest
    cd ../twitter-ui
    mvn spring-boot:build-image -Dspring-boot.build-image.imageName=dieterfl/twitter-ui:latest
    cd ../twitter-reader
    mvn spring-boot:build-image -Dspring-boot.build-image.imageName=dieterfl/twitter-reader:latest
    cd .. 
else
    echo "Please execute create-docker-images.sh script within the twitter-sentimentr folder."
fi
