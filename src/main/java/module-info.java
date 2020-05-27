
module net.finmath.plots {

	exports net.finmath.plots;
	exports net.finmath.plots.axis;

	requires transitive net.finmath.lib;

	requires jfreechart;
	requires jfreesvg;
	requires jzy3d.api;
	requires itextpdf;
	requires commons.math3;
	requires jogl.all.main;

	requires com.google.common;

	requires javafx.controls;
	requires javafx.base;
	requires transitive javafx.graphics;
	requires transitive javafx.swing;

	requires java.desktop;
	requires java.management;
	requires java.logging;
}