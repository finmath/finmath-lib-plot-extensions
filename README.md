# finmath lib plot extensions
- - - -
**Convenient abstractions of some plotting libraries and example usages of finmath lib.**
- - - -

This project provides abstractions for some plotting libraries (JFreeChart, JZY3D) and demo usages for finmath lib.
It is provided for convenience to test and explore finmath lib.

Main features:
- Create 2D and 3D graphs (using JFreeChart (2D) and JZY3D (3D)).
- Animated 3D visualization (using JZY3D (which uses OpenGL)).
- Save the graphs as PDF (using iText).
- Save the graphs as SVG (using JFreeSVG)
- Easy to use classes and methods with a default style.

## Usage

Aiming at quick creation of plots, plots can be specified using a function / operator (e.g. via a lambda expression) and a few parameters specifying the discretization:

		Plot plot = new Plot2D(0.0, 300.0, 100, function);
		plot.setTitle("Black-Scholes Model European Option Value").setXAxisLabel("strike").setYAxisLabel("value");
		plot.show();


