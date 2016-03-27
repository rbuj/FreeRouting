#!/bin/bash
locale=$1
while read resource
do
  for filename in ../$resource/resources/*_en.properties; do
    file=`basename $filename`
    po2prop -t $filename $locale/$resource/${file/_en.properties/_$locale.po} ${filename/_en.properties/_ca.properties}
  done
done < RESOURCES
