package net.finmath.plots;

public class NumberAxis implements Axis {

	private transient org.jfree.chart.axis.NumberAxis numberAxisJF;

	public NumberAxis(Double min, Double max) {
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
	
	org.jfree.chart.axis.NumberAxis getImplementationJFree() {
		return numberAxisJF;
	}

	public NumberAxis() {
		this(null, null);
	}
}
