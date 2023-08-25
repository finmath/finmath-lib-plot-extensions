finmath lib plot extensions Release Notes
==========

****************************************

# Release Notes


## 0.4.9 (25.08.2023)

### General

 - Fixed a bug that resulted in a missing first time step in PlotProcess2D
 - save returns this pointer (to allow multiple save calls or show)


## 0.4.0 (25.01.2021)

### General

 - Added a method to create a scatter with multiple series from a Map
 - Removed Plot3D (using jzy3d).
 - Referencing finmath-lib 5.1.1


## 0.3.10 (28.07.2020)

### General

 - More convenient methods for plot construction in Plots.
 - Minor improvements.
 - Referencing finmath-lib 5.0.4


## 0.3.2 (08.03.2020)

### General

 - Refactor rename of methods in Plots.
 - Referencing finmath-lib 5.0.2
 - More convenient methods in Plots.
 

## 0.3.2 (08.03.2020)

### General

- Improved documentation / JavaDoc and code style.
- Refactor rename setxAxisFormat to setXAxisFormat.
- Moved project page to gh-pages.


## 0.2.0 (19.02.2020)

### General

- Added a functional interface for processe `Double -> Randomvariable` which may throw exceptions to reduce boiler plate code in lambdas (catching exceptions).


## 0.1.0 (15.02.2020)

### General

- Fixing plotting of scatter when point lies at boundary.

