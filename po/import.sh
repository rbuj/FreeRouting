#!/bin/bash
locale=$1
while read resource
do
  mkdir -p $locale/$resource
  for filename in ../src/main/resources/$resource/resources/*_en.properties; do
    file=`basename $filename`
    cp ../src/main/resources/$resource/resources/${file/_en.properties/_${locale}.properties} $locale/$resource/${file/_en.properties/}-$locale.properties
    prop2po --progress=none --duplicates=msgctxt -t $filename $locale/$resource/${file/_en.properties/}-$locale.properties $locale/$resource/${file/_en.properties/.po}
    case $locale in
      "ca")
       sed -i -e 's/English version/Catalan version/g' $locale/$resource/${file/_en.properties/.po}
       ;;
      "de")
       sed -i -e 's/English version/German version/g' $locale/$resource/${file/_en.properties/.po}
       ;;
      "es")
       sed -i -e 's/English version/Spanish version/g' $locale/$resource/${file/_en.properties/.po}
       ;;
    esac
    rm -f $locale/$resource/${file/_en.properties/}-$locale.properties
  done
done < RESOURCES
