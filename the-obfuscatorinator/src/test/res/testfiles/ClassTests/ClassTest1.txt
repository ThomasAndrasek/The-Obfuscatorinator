public class HelperFunctions {
    private int testVal;
    private String testString;

    public HelperFunctions() {
        this.testVal = 5;
        this.testString = "Rust!";
    }

    public int getTestVal() {
        return this.testVal;
    }

    public String getTestString() {
        return this.testString;
    }

    public void setTestVal(int newVal) {
        this.testVal = newVal;
    }

    public void setTestString(String newString) {
        this.testString = newString;
    }
}
