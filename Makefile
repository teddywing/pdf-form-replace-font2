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
