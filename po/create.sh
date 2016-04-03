#!/bin/bash
locale=en
while read resource
do
  echo mkdir -p pot/$resource | sh
  for filename in ../src/main/resources/$resource/resources/*_en.properties; do
    file=`basename $filename`
    echo prop2po --progress=none --duplicates=msgctxt -P -t $filename $filename pot/$resource/${file/_en.properties/.pot} | sh
  done
done < RESOURCES
