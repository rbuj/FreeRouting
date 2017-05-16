#!/bin/bash
locale=$1
if [ "$locale" == "en" ]; then exit 0; fi
while read resource
do
  for filename in ../src/main/resources/$resource/resources/*_en.properties; do
    file=`basename $filename`
    po2prop --progress=none --personality=java-utf8 -t $filename $locale/$resource/${file/_en.properties/.po} ${filename/_en.properties/_$locale.properties}
    case $locale in
      "ca")
       sed -i -e 's/English version of/Catalan version of/g' ${filename/_en.properties/_$locale.properties}
       ;;
      "de")
       sed -i -e 's/English version of/German version of/g' ${filename/_en.properties/_$locale.properties}
       ;;
      "es")
       sed -i -e 's/English version of/Spanish version of/g' ${filename/_en.properties/_$locale.properties}
       ;;
    esac
  done
done < RESOURCES
