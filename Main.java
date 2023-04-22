import com.itextpdf.kernel.pdf.*;
import com.itextpdf.kernel.font.*;
import com.itextpdf.io.font.constants.StandardFonts;
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

		PdfAcroForm form = PdfAcroForm.getAcroForm(pdf, false);

		PdfFont courier = PdfFontFactory.createFont(StandardFonts.COURIER);
		Map<String, PdfFormField> fields = form.getFormFields();

		for (var entry : fields.entrySet()) {
			PdfFormField field = entry.getValue();
			field.setFont(courier);
		}

		pdf.close();
	}
}
