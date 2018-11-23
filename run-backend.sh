#!/bin/bash

service mongod restart
setsid java -jar -Xmx5g  backend/build/libs/backend.jar ~/books $0 ~/book-log