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

cd /home/ubuntu/milked/$SRC

git reset --hard HEAD
git pull

echo "app:$APP_NAME"
screen -X -S $APP_NAME quit || true
