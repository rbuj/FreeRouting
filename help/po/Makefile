all: create xml

#update all po fles
update-po: book.pot
	for po in *.po; do \
		msgmerge -o $$po.tmp $$po book.pot ; \
		mv $$po.tmp $$po ; \
	done

#create all xml files
xml: *.po
	for po in *.po; do \
		lingua=`basename $$po .po`; \
		/usr/bin/xml2po -p $$po -o ../manual/$$lingua/book.xml ../manual/en/book.xml ; \
	done

#create pot file
create: ../manual/en/book.xml
	xml2po -o book.pot ../manual/en/book.xml
