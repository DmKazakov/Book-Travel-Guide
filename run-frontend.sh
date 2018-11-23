#!/bin/bash

service mongod restart
setsid java -jar frontend/build/libs/backend.jar $0 