
module net.finmath.plots {

	exports net.finmath.plots;
	exports net.finmath.plots.axis;
	exports net.finmath.plots.demo;
	exports net.finmath.plots.jfreechart;
	exports net.finmath.plots.util;

	requires transitive net.finmath.lib;

	requires javafx.controls;
	requires javafx.base;
	requires transitive javafx.graphics;
	requires transitive javafx.swing;

	requires org.apache.commons.lang3;

	requires org.jfree.jfreechart;

	requires commons.math3;
	requires batik.all;
	requires fop.core;
	requires java.logging;
}
