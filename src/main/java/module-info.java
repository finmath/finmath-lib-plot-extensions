
module net.finmath.plots {

	exports net.finmath.plots;
	exports net.finmath.plots.axis;
	exports net.finmath.plots.demo;

	requires transitive net.finmath.lib;

	requires java.desktop;
	requires java.management;
	requires java.logging;

	requires javafx.controls;
	requires javafx.base;
	requires transitive javafx.graphics;
	requires transitive javafx.swing;

	requires org.apache.commons.lang3;

	requires jfreechart;
	requires jfreesvg;

	requires jzy3d.api;
	requires jzy3d.javafx;

	requires itextpdf;
	requires commons.math3;

	requires com.google.common;
}