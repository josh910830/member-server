mvn clean package
docker build -t member:latest ./
docker tag member:latest scenecho/member:latest
docker push scenecho/member:latest