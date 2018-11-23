#!/bin/bash

setsid java -jar -Xmx4g  backend/build/libs/backend.jar ~/books $0 ~/book-log