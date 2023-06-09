# Copyright (c) 2023  Teddy Wing
#
# This file is part of PDF Form Replace Font2.
#
# PDF Form Replace Font2 is free software: you can redistribute it
# and/or modify it under the terms of the GNU General Public License
# as published by the Free Software Foundation, either version 3 of
# the License, or (at your option) any later version.
#
# PDF Form Replace Font2 is distributed in the hope that it will be
# useful, but WITHOUT ANY WARRANTY; without even the implied warranty
# of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
# General Public License for more details.
#
# You should have received a copy of the GNU General Public License
# along with PDF Form Replace Font2. If not, see
# <https://www.gnu.org/licenses/>.


prefix ?= /usr/local
exec_prefix ?= $(prefix)
bindir ?= $(exec_prefix)/bin
datarootdir ?= $(prefix)/share
mandir ?= $(datarootdir)/man
man1dir ?= $(mandir)/man1


MVN := mvn

VERSION := $(shell grep '^  <version>' pom.xml | sed -e 's/  <version>//' -e 's,</version>,,')

SOURCES := $(shell find src -name '*.java')
RELEASE_PRODUCT := target/pdf-form-replace-font2-$(VERSION).jar

MAN_PAGE := doc/pdf-form-replace-font2.1


.PHONY: doc
doc: $(MAN_PAGE)

$(MAN_PAGE): $(MAN_PAGE).txt
	a2x --no-xmllint --format manpage $<


.PHONY: compile
compile:
	$(MVN) compile $(MVNFLAGS)

.PHONY: run
run: compile
	$(MVN) exec:java $(MVNFLAGS) -Dexec.mainClass='com.teddywing.pdf_form_replace_font2.App'


.PHONY: package
package: $(RELEASE_PRODUCT) pdf-form-replace-font2

$(RELEASE_PRODUCT): $(SOURCES)
	$(MVN) package $(MVNFLAGS)

pdf-form-replace-font2: pdf-form-replace-font2.in
	m4 \
		--define="JAR_PATH=$(DESTDIR)$(datarootdir)/java/pdf-form-replace-font2-$(VERSION).jar" \
		$< \
		> $@


.PHONY: release
release:
	$(MVN) release:prepare $(MVNFLAGS)
	git tag --annotate v$(VERSION:-SNAPSHOT=) \
		--force \
		"$$(git rev-parse v$(VERSION:-SNAPSHOT=)^{})"


.PHONY: install
install: $(RELEASE_PRODUCT) pdf-form-replace-font2 $(MAN_PAGE)
	install -d $(DESTDIR)$(datarootdir)/java
	install -m 644 $(RELEASE_PRODUCT) $(DESTDIR)$(datarootdir)/java

	install -d $(DESTDIR)$(bindir)
	install -m 755 pdf-form-replace-font2 \
		$(DESTDIR)$(bindir)

	install -d $(DESTDIR)$(man1dir)
	install -m 644 $(MAN_PAGE) $(DESTDIR)$(man1dir)
