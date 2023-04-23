import com.itextpdf.kernel.pdf.*;
import com.itextpdf.kernel.font.*;
import com.itextpdf.io.font.constants.StandardFonts;
import com.itextpdf.io.font.FontProgram;
import com.itextpdf.io.font.FontProgramFactory;
import com.itextpdf.io.font.PdfEncodings;
import com.itextpdf.forms.*;
import com.itextpdf.forms.fields.*;

import java.io.*;
import java.util.Map;

public class Main {
	public static void main(String args[]) throws IOException {
		PdfDocument pdf = new PdfDocument(
			new PdfReader("f1040.pdf"),
			new PdfWriter("f1040-courier.pdf")
		);

		// FontProgramFactory.registerSystemFontDirectories();
		// FontProgramFactory.registerFontDirectory("/System/Library/Fonts/");
		FontProgramFactory.registerFontDirectory("/System/Library/Fonts/Supplemental/");
		// FontProgramFactory
		// 	.registerFont("/System/Library/Fonts/Supplemental/Courier New.ttf", "hihoaliaso");
		// System.out.println("is registered: "
		// 	+ FontProgramFactory.isRegisteredFont("CourierNewPSMT"));

		System.out.println(FontProgramFactory.getRegisteredFonts());
		System.out.println(FontProgramFactory.getRegisteredFontFamilies());

		// FontProgram courier_program = FontProgramFactory.createFont("CourierNewPSMT");
		// FontProgram courier_program = FontProgramFactory
		// 	.createFont("/System/Library/Fonts/Supplemental/Courier New.ttf");
		// FontProgram courier_program = FontProgramFactory
		// 	.createRegisteredFont("CourierNewPSMT");
		// FontProgram courier_program = FontProgramFactory
		// 	.createFont("Courier New.ttf");
		FontProgram courier_program = FontProgramFactory
			.createFont("/System/Library/Fonts/Supplemental/Copperplate.ttc", 0, false);
		PdfFont courier = PdfFontFactory
			.createFont(courier_program, PdfEncodings.UTF8, PdfFontFactory.EmbeddingStrategy.PREFER_EMBEDDED);

		PdfAcroForm form = PdfAcroForm.getAcroForm(pdf, false);
		Map<String, PdfFormField> fields = form.getFormFields();

		for (var entry : fields.entrySet()) {
			// TODO: Try printing original font family and size on field.
			PdfFormField field = entry.getValue();

			PdfFont original_font = field.getFont();
			// System.out.println("Font: " + field.getFontSize());

			String original_postscript_name = original_font
				.getFontProgram()
				.getFontNames()
				.getFontName();

			if (original_postscript_name.equals("HelveticaLTStd-Bold")) {
				field.setFont(courier);
				// System.out.println("F: " + field.getFont());
				// System.out.println("R: " + field.regenerateField());
			}
		}

		pdf.close();
	}
}
