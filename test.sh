#!/usr/bin/env bash

mvn clean package shade:shade

# PARSE TESTS TO FIGURE OUT IF WE SHOULD EVEN LAUNCH INTO ENVIRONMENT

mv target/MelonDevApp.jar test-server/plugins/
cd test-server
java -jar server.jar