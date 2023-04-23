package com.teddywing.pdf_form_replace_font2;

import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfReader;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.io.font.constants.StandardFonts;
import com.itextpdf.io.font.FontProgram;
import com.itextpdf.io.font.FontProgramFactory;
import com.itextpdf.io.font.PdfEncodings;
import com.itextpdf.forms.PdfAcroForm;
import com.itextpdf.forms.fields.PdfFormField;

import java.io.IOException;
import java.util.Map;

public class App {

	public static void main(String[] args) throws IOException {
		PdfDocument pdf = new PdfDocument(
			new PdfReader("f1040.pdf"),
			new PdfWriter("f1040-courier.pdf")
		);

		FontProgramFactory
			.registerFontDirectory("/System/Library/Fonts/Supplemental/");

		System.out.println(FontProgramFactory.getRegisteredFonts());
		System.out.println(FontProgramFactory.getRegisteredFontFamilies());

		FontProgram courier_program = FontProgramFactory
			.createRegisteredFont("CourierNewPSMT");
		PdfFont courier = PdfFontFactory.createFont(
			courier_program,
			PdfEncodings.UTF8,
			PdfFontFactory.EmbeddingStrategy.FORCE_NOT_EMBEDDED
		);

		PdfAcroForm form = PdfAcroForm.getAcroForm(pdf, false);
		Map<String, PdfFormField> fields = form.getFormFields();

		for (var entry : fields.entrySet()) {
			PdfFormField field = entry.getValue();

			PdfFont original_font = field.getFont();
			String original_postscript_name = original_font
				.getFontProgram()
				.getFontNames()
				.getFontName();

			if (original_postscript_name.equals("HelveticaLTStd-Bold")) {
				field.setFont(courier);
			}
		}

		pdf.close();
	}
}
