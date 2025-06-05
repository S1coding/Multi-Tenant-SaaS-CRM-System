package com.sajjan.Multi_Tenant.SaaS.CRM.System.services.reports.reportPdf.makePdfFile;

import com.lowagie.text.DocumentException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.xhtmlrenderer.pdf.ITextRenderer;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Map;

@Service
public class PdfGeneratorService {

	private final TemplateEngine templateEngine;

	@Autowired
	public PdfGeneratorService(TemplateEngine templateEngine) {
		this.templateEngine = templateEngine;
	}

	public byte[] generatePdfFromTemplate(String templateName, Map<String, Object> model) {
		// Prepare Thymeleaf context
		Context context = new Context();
		model.forEach(context::setVariable);

		// Process the template to HTML
		String processedHtml = templateEngine.process(templateName, context);

		// Convert HTML to PDF
		try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
			ITextRenderer renderer = new ITextRenderer();
			renderer.setDocumentFromString(processedHtml);
			renderer.layout();
			renderer.createPDF(outputStream);

			return outputStream.toByteArray();
		} catch (DocumentException | IOException e) {
			throw new RuntimeException("Failed to generate PDF", e);
		}
	}
}