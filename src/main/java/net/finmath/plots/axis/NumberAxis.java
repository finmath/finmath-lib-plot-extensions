package net.finmath.plots.axis;

public class NumberAxis implements Axis {

	private transient org.jfree.chart.axis.NumberAxis numberAxisJF;

	public NumberAxis(final Double min, final Double max) {
		super();

		numberAxisJF = new org.jfree.chart.axis.NumberAxis();
		if(min != null && max != null) {
			numberAxisJF.setRange(min, max);
			numberAxisJF.setAutoRange(false);
		}
		else {
			numberAxisJF.setAutoRange(true);
		}
	}

	public org.jfree.chart.axis.NumberAxis getImplementationJFree() {
		return numberAxisJF;
	}

	public NumberAxis() {
		this(null, null);
	}
}
