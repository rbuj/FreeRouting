#!/bin/bash
locale=$1
while read resource
do
  mkdir -p $locale/$resource
  cat ../$resource/resources/*_ca.properties > $locale/$resource/$resource-$locale.properties
  for filename in ../$resource/resources/*_en.properties; do
    file=`basename $filename`
    prop2po --duplicates=msgctxt -t $filename $locale/$resource/$resource-$locale.properties $locale/$resource/${file/_en.properties/_$locale.po}
  done
  rm -f $locale/$resource/$resource-$locale.properties
done < RESOURCES

