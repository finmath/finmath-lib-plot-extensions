package net.finmath.plots.axis;

import java.text.NumberFormat;
import java.util.Locale;

public class NumberAxis implements Axis {

	private transient org.jfree.chart.axis.NumberAxis numberAxisJF;

	public NumberAxis(final String label, final Double min, final Double max, final NumberFormat numberFormat) {
		super();
		numberAxisJF = new org.jfree.chart.axis.NumberAxis();
		if(label != null) {
			numberAxisJF.setLabel(label);
		}
		if(min != null && max != null) {
			numberAxisJF.setRange(min, max);
			numberAxisJF.setAutoRange(false);
		}
		else {
			numberAxisJF.setAutoRange(true);
		}
		if(numberFormat != null) {
			numberAxisJF.setNumberFormatOverride(numberFormat);
		}
	}

	public NumberAxis(final String label, final Double min, final Double max) {
		this(label, min, max, NumberFormat.getInstance(Locale.ENGLISH));
	}

	public NumberAxis() {
		this(null, null, null);
	}

	public org.jfree.chart.axis.NumberAxis getImplementationJFree() {
		return numberAxisJF;
	}
}
