#! /bin/bash

#################################
# Generate Artifacts with Signatures and Checksums
#################################

# Merge Script
if [ -d "Maven" ]; then
  cd Maven/
fi

set -e

function echoX() {
  echo -e "SilverMobSdk DEPLOY-LOG: $@"
}

VERSION="2.2.7"
BASE_DIR="$PWD"
DEPLOY_DIR_NAME="filesToDeploy"
DEPLOY_DIR_ABSOLUTE="$BASE_DIR/$DEPLOY_DIR_NAME"

# Cleanup previous deployment folder if exists
rm -rf "$DEPLOY_DIR_ABSOLUTE" || true
mkdir -p "$DEPLOY_DIR_ABSOLUTE"

cd ..
bash ./buildSilverMob.sh

# Copy generated artifacts
cp -r ../generated/* "$DEPLOY_DIR_ABSOLUTE" || true

modules=("SilverMobSdk" "SilverMobSdk-core" "SilverMobSdk-gamEventHandlers" "SilverMobSdk-admobAdapters" "SilverMobSdk-maxAdapters")
artifactNames=("silvermob-sdk" "silvermob-sdk-core" "silvermob-sdk-gam-event-handlers" "silvermob-sdk-admob-adapters" "silvermob-sdk-max-adapters")
extensions=("jar" "aar" "aar" "aar" "aar")

for n in ${!modules[@]}; do
  module="${modules[$n]}"
  artifact="${artifactNames[$n]}"
  extension="${extensions[$n]}"
  targetDir="$DEPLOY_DIR_ABSOLUTE/com/silvermob/${artifact}/$VERSION"
  mkdir -p "$targetDir"

  # Copy artifacts to the new structure
  if [ "$extension" == "aar" ]; then
    compiledPath="../generated/aar/${module}-release.aar"
  else
    compiledPath="../generated/${module}.jar"
  fi
  cp "$compiledPath" "$targetDir/${artifact}-$VERSION.$extension"
  cp "../generated/${module}-sources.jar" "$targetDir/${artifact}-$VERSION-sources.jar"
  cp "../generated/${module}-javadoc.jar" "$targetDir/${artifact}-$VERSION-javadoc.jar"
  cp "$BASE_DIR/${module}-pom.xml" "$targetDir/${artifact}-$VERSION.pom"

  # Generate signatures and checksums
  for file in "$targetDir"/*; do
    gpg --batch --yes --detach-sign --armor "$file"
    md5sum "$file" | cut -d' ' -f1 > "$file.md5"
    sha1sum "$file" | cut -d' ' -f1 > "$file.sha1"
  done

  echoX "Artifacts prepared for ${module}"
done

echoX "Artifacts generation complete"
