
module net.finmath.plots {

	exports net.finmath.plots;
	exports net.finmath.plots.axis;
	exports net.finmath.plots.demo;
	exports net.finmath.plots.jfreechart;

	requires transitive net.finmath.lib;

	requires javafx.controls;
	requires javafx.base;
	requires transitive javafx.graphics;
	requires transitive javafx.swing;

	requires org.apache.commons.lang3;

	requires org.jfree.jfreechart;

	requires itextpdf;
	requires commons.math3;

	requires com.google.common;
}
