package com.mand;

public class MandelbrotResult {

        private int Px;
        private int Py;
        private double x0;
        private double y0;
        private double x;
        private double y;
        private double iterations;

        public MandelbrotResult(int Px, int Py, double x0, double y0, double x, double y, double iterations) {
            this.Px = Px;
            this.Py = Py;
            this.x0 = x0;
            this.y0 = y0;
            this.x = x;
            this.y = y;
            this.iterations = iterations;
        }

        public double getIterations() {
            return iterations;
        }

        public int getPx() {
            return Px;
        }

        public int getPy() {
            return Py;
        }

        public double getX() {
            return x;
        }

        public double getY() {
            return y;
        }

        public double getX0() {
            return x0;
        }

        public double getY0() {
            return y0;
        }

        public void setIterations(double iterations) {
            this.iterations = iterations;
        }

        public void setX(double x) {
            this.x = x;
        }

        public void setY(double y) {
            this.y = y;
        }

    }
