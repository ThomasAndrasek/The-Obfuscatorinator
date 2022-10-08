public class Car {
    private String color;
    private int year;
    private Wheel[] wheels;

    public Car(String color, int year, int wheelCount) {
        this.color = color;
        this.year = year;

        this.wheels = new Wheel[wheelCount];

        for (int i = 0; i < wheelCount; i++) {
            this.wheels[i] = new Wheel("NewYear", this.year);
        }
    }

    class Wheel {
        private String brand;
        private int year;

        public Wheel(String brand, int year) {
            this.brand = brand;
            this.year = year;
        }

        public String getBrand() {
            return this.brand;
        }

        public int getYear() {
            return this.year;
        }
    }

    public int getYear() {
        return this.year;
    }

    public String getColor() {
        return this.color;
    }

    public void repaint(String color) {
        this.color = color;
    }

    public void replaceWheel(int wheelIndex, int year, String brand) {
        if (wheelIndex < this.wheels.length) {
            this.wheels[wheelIndex] = new Wheel(brand, year);
        }
    }
}
