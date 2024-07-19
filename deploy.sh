#!/bin/bash

echo "ACTION DEPLOY START"

# 현재 프로세스 가져오기
CURRENT_PID=$(lsof -i tcp:8080 | awk 'NR!=1 {print$2}')

# 있으면 종료
echo $CURRENT_PID
if [ -z "$CURRENT_PID" ]; then
        echo "no such app didnt find."
else
        kill -9 $CURRENT_PID
        echo "killed current pid"
        sleep 3
fi
echo "deploy started"
nohup java -jar build/libs/backend-0.0.1-SNAPSHOT.jar --spring.profiles.active=dev &
sleep 2