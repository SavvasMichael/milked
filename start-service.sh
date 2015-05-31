#!/bin/bash
. ~/.bashrc

APP_NAME=`ec2-describe-tags --region eu-west-1 \
  --filter "resource-type=instance" \
  --filter "resource-id=$(ec2metadata | grep "instance-id" | perl -pe 's/instance-id: (.*)/\1/g')" \
  --filter "key=app" | cut -f5`

SRC=`ec2-describe-tags --region eu-west-1 \
  --filter "resource-type=instance" \
  --filter "resource-id=$(ec2metadata | grep "instance-id" | perl -pe 's/instance-id: (.*)/\1/g')" \
  --filter "key=src" | cut -f5`

APP_VERSION=`ec2-describe-tags --region eu-west-1 \
  --filter "resource-type=instance" \
  --filter "resource-id=$(ec2metadata | grep "instance-id" | perl -pe 's/instance-id: (.*)/\1/g')" \
  --filter "key=version" | cut -f5`

echo "app:$APP_NAME"
echo "src:$SRC"
echo "version:$APP_VERSION"

cd /home/ubuntu/milked/$SRC

git reset --hard HEAD
git pull
git checkout $APP_VERSION

rm target/*.jar || true
mvn clean package -DskipTests

screen -X -S $APP_NAME quit || true
sudo killall -9 java || true
screen -LdmS $APP_NAME bash -c "sudo java -DXms512M -Dlogging.file=/home/ubuntu/logs/server.log -jar target/*.jar --server.port=80"

