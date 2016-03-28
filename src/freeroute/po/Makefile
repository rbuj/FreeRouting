FILE=../LOCALES
LOCALES=$(shell cat ../LOCALES)
RESOURCES=$(shell cat RESOURCES)
LOC_IMP=$(addsuffix .i,$(LOCALES))
LOC_EXP=$(addsuffix .e,$(LOCALES))

.PHONY: all new create import export clean $(LOCALES)

all: create

new: $(LOCALES)

$(LOCALES):
	./new.sh $@

import: $(LOC_IMP)

$(LOC_IMP):
	./import.sh $(@:.i=)

export: $(LOC_EXP)

$(LOC_EXP):
	./export.sh $(@:.e=)

create:
	./create.sh

clean:
	rm -fr *.pot $(LOCALES) pot
