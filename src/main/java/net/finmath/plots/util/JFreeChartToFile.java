package net.finmath.plots.util;

import java.awt.Rectangle;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.Writer;

import org.apache.batik.dom.GenericDOMImplementation;
import org.apache.batik.svggen.SVGGraphics2D;
import org.apache.batik.transcoder.SVGAbstractTranscoder;
import org.apache.batik.transcoder.TranscoderException;
import org.apache.batik.transcoder.TranscoderInput;
import org.apache.batik.transcoder.TranscoderOutput;
import org.apache.fop.svg.PDFTranscoder;
import org.jfree.chart.JFreeChart;
import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Document;

public class JFreeChartToFile {

	public static void saveToSVG(JFreeChart chart, File svgFile, int width, int height) throws IOException {
		// Get a DOMImplementation and create an XML document
		DOMImplementation domImpl = GenericDOMImplementation.getDOMImplementation();
		Document document = domImpl.createDocument(null, "svg", null);

		// Create an instance of the SVG Generator
		SVGGraphics2D svgGenerator = new SVGGraphics2D(document);

		// draw the chart in the SVG generator
		chart.draw(svgGenerator, new Rectangle(width, height));

		// Write svg file
		OutputStream outputStream = new FileOutputStream(svgFile);
		Writer out = new OutputStreamWriter(outputStream, "UTF-8");
		svgGenerator.stream(out, true /* use css */);						
		outputStream.flush();
		outputStream.close();
	}

	public static void saveToPDF(JFreeChart chart, File pdfFile, int width, int height) throws IOException {
		// Get a DOMImplementation and create an XML document
		DOMImplementation domImpl = GenericDOMImplementation.getDOMImplementation();
		Document document = domImpl.createDocument(null, "svg", null);

		// Create an instance of the SVG Generator
		SVGGraphics2D svgGenerator = new SVGGraphics2D(document);

		// draw the chart in the SVG generator
		chart.draw(svgGenerator, new Rectangle(width, height));

		StringWriter stringWriter = new StringWriter();
		svgGenerator.stream(stringWriter, true);
		
		TranscoderInput input = new TranscoderInput(new StringReader(stringWriter.toString()));

		PDFTranscoder transcoder = new PDFTranscoder();
		transcoder.addTranscodingHint(SVGAbstractTranscoder.KEY_WIDTH, (float)width);
		transcoder.addTranscodingHint(SVGAbstractTranscoder.KEY_HEIGHT, (float)height);

		OutputStream outputStream = new FileOutputStream(pdfFile);
		BufferedOutputStream bos = new BufferedOutputStream(outputStream);
		TranscoderOutput transOutput = new TranscoderOutput(bos);

		try {
			transcoder.transcode(input, transOutput);
		} catch (TranscoderException e) {
			throw new RuntimeException(e);
		}
	}
}
