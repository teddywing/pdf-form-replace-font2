pdf-form-replace-font2
======================

Replace a given font in a PDF form’s fields with a different one.

This is a rewrite of [pdf-form-replace-font] using the [iText7] PDF library. The
result is more robust than the original PDF-Form-Replace-Font.


## Usage
The following command replaces Helvetica with Courier New (font names are
specified by their PostScript names):

	$ pdf-form-replace-font2 \
		--find HelveticaLTStd-Bold \
		--replace CourierNewPSMT \
		--output f1040-courier.pdf \
		f1040.pdf


## Install
Mac OS X users can install with MacPorts, after [adding a custom repository
source][teddywing ports repository]:

	$ sudo port install pdf-form-replace-font2


[teddywing ports repository]: https://github.com/teddywing/macports-ports#adding-this-repository-source


## License
Copyright © 2023 Teddy Wing. Licensed under the GNU GPLv3+ (see the included
COPYING file).


[pdf-form-replace-font]: https://github.com/teddywing/pdf-form-replace-font/
[iText7]: https://itextpdf.com/
