#! /bin/bash

# Merge Script
if [ -d "scripts" ]; then
cd scripts/
fi


set -e
echo $PWD
cd ..

function echoX {
echo -e "✅ SILVERMOB TESTLOG: $@"
}

echoX "clean previous build"
./gradlew clean

echoX "start unit tests"
./gradlew -i SilverMob-core:testReleaseUnitTest
./gradlew -i SilverMob-gamEventHandlers:testReleaseUnitTest
./gradlew -i SilverMob-admobAdapters:testReleaseUnitTest
./gradlew -i SilverMob-maxAdapters:testReleaseUnitTest