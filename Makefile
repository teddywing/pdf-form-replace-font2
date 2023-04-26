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


VERSION := $(shell grep '^  <version>' pom.xml | sed -e 's/  <version>//' -e 's,</version>,,')

SOURCES := $(shell find src -name '*.java')

MAN_PAGE := doc/pdf-form-replace-font2.1


.PHONY: doc
doc: $(MAN_PAGE)

$(MAN_PAGE): $(MAN_PAGE).txt
	a2x --no-xmllint --format manpage $<


.PHONY: compile
compile:
	mvn compile

.PHONY: run
run: compile
	mvn exec:java -Dexec.mainClass='com.teddywing.pdf_form_replace_font2.App'


.PHONY: package
package: target/pdf-form-replace-font2-$(VERSION).jar

target/pdf-form-replace-font2-$(VERSION).jar: $(SOURCES)
	mvn package
