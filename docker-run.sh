docker pull scenecho/member:latest
docker stop member
docker rm member
docker run \
  -v /app:/app \
  -p 8080:8080 \
  -d \
  --name member \
  scenecho/member:latest