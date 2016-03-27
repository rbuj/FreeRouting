#!/bin/bash
locale=$1
while read resource
do
  mkdir -p $locale/$resource
  for filename in pot/$resource/*.pot; do
    file=`basename $filename`
    msginit -l $locale --no-translator -i pot/$resource/$file -o $locale/$resource/${file/_en.pot/_$locale.po}
  done
done < RESOURCES

