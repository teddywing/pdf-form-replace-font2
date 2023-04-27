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
	mvn compile $(MVNFLAGS)

.PHONY: run
run: compile
	mvn exec:java $(MVNFLAGS) -Dexec.mainClass='com.teddywing.pdf_form_replace_font2.App'


.PHONY: package
package: $(RELEASE_PRODUCT)

$(RELEASE_PRODUCT): $(SOURCES)
	mvn package $(MVNFLAGS)


.PHONY: release
release:
	mvn release:prepare $(MVNFLAGS)
	mvn release:perform $(MVNFLAGS)


.PHONY: install
install: $(RELEASE_PRODUCT) $(MAN_PAGE)
	install -d $(DESTDIR)$(datarootdir)/java
	install -m 644 $(RELEASE_PRODUCT) $(DESTDIR)$(datarootdir)/java

	install -d $(DESTDIR)$(bindir)
	m4 \
		--define="JAR_PATH=$(DESTDIR)$(datarootdir)/java/pdf-form-replace-font2-$(VERSION).jar" \
		pdf-form-replace-font2.in \
		> pdf-form-replace-font2
	install -m 755 pdf-form-replace-font2 \
		$(DESTDIR)$(bindir)

	install -d $(DESTDIR)$(man1dir)
	install -m 644 $(MAN_PAGE) $(DESTDIR)$(man1dir)
