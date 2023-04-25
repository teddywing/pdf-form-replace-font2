package com.teddywing.pdf_form_replace_font2;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

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
import java.io.InputStream;
import java.util.Map;
import java.util.Properties;

public class App {

	public static void main(String[] args) throws IOException {
		Options options = new Options();
		options.addOption("f", "find", true, "original font");
		options.addOption("r", "replace", true, "replacement font");
		options.addOption("o", "output", true, "output file");

		options.addOption("h", "help", false, "print this help menu");
		options.addOption("V", "version", false, "show the program version");

		CommandLineParser option_parser = new DefaultParser();
		try {
			CommandLine options_matches = option_parser.parse(options, args);

			boolean should_show_help = options_matches.hasOption("help");
			if (should_show_help) {
				HelpFormatter help_formatter = new HelpFormatter();
				help_formatter.printHelp("pdf-form-replace-font2", options);

				System.exit(0);
			}

			boolean should_show_version = options_matches.hasOption("version");
			if (should_show_version) {
				Properties properties = new Properties();
				InputStream stream = App.class.getResourceAsStream("/META-INF/maven/com.teddywing.pdf_form_replace_font2/pdf-form-replace-font2/pom.properties");
				properties.load(stream);
				String version = properties.getProperty("version", "");

				System.out.println(version);

				System.exit(0);
			}

			String input = "-";
			String[] free_args = options_matches.getArgs();
			if (free_args.length > 0) {
				input = free_args[0];
			}

			String find = options_matches.getOptionValue("find");
			if (find == null) {
				System.err.println("error: required option 'find' missing");
				System.exit(64);
			}

			String replace = options_matches.getOptionValue("replace");
			if (replace == null) {
				System.err.println("error: required option 'replace' missing");
				System.exit(64);
			}

			String output = options_matches.getOptionValue("output");
			if (output == null) {
				output = "-";
			}

			System.out.println("i:" + input + " f:" + find + " r:" + replace + " o:" + output);
		}
		catch (ParseException e) {
			System.err.println("error: " + e.getMessage());
			System.exit(64);
		}

		App.replacePdfFieldFont(
			new PdfReader("f1040.pdf"),
			new PdfWriter("f1040-courier.pdf"),
			"HelveticaLTStd-Bold",
			"CourierNewPSMT"
		);
	}

	private static void replacePdfFieldFont(
		PdfReader reader,
		PdfWriter writer,
		String original_font_postscript_name,
		String replacement_font_postscript_name
	) throws IOException {
		PdfDocument pdf = new PdfDocument(reader, writer);

		FontProgramFactory
			.registerFontDirectory("/System/Library/Fonts/Supplemental/");

		System.out.println(FontProgramFactory.getRegisteredFonts());
		System.out.println(FontProgramFactory.getRegisteredFontFamilies());

		FontProgram courier_program = FontProgramFactory
			.createRegisteredFont(replacement_font_postscript_name);
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

			if (original_postscript_name.equals(original_font_postscript_name)) {
				field.setFont(courier);
			}
		}

		pdf.close();
	}

}
