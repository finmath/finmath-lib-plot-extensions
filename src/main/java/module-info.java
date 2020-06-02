
module net.finmath.plots {

	exports net.finmath.plots;
	exports net.finmath.plots.axis;
	exports net.finmath.plots.demo;
	
	requires transitive net.finmath.lib;

	requires jfreechart;
	requires jfreesvg;
	requires jzy3d.api;
	requires jzy3d.javafx;
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
	requires org.apache.commons.lang3;
}