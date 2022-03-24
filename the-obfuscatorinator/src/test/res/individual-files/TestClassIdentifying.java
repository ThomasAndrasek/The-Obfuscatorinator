public class TestClassIdentifying {
    
    private String testString;
    private String testString2;
    private String testString3;

    public TestClassIdentifying() {
        testString = "class TestClass {";
        testString2 = "class TestClass2 {";
        testString3 = " class TestClass3 {";
    }

    class TestFormat{

    }

    class TestFormat2
    {

    }

    class InnerClass {
        private String name;
        private String code;

        public InnerClass(String name, String code) {
            this.name = name;
            this.code = code;
        }

        public String getName() {
            return name;
        }

        public String getCode() {
            return code;
        }

        public void setName(String name) {
            this.name = name;
        }

        public void setCode(String code) {
            this.code = code;
        }

        @Override
        public String toString() {
            return "InnerClass{" +
                    "name='" + name + '\'' +
                    ", code='" + code + '\'' +
                    '}';
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            InnerClass innerClass = (InnerClass) o;
            return name.equals(innerClass.name) &&
                    code.equals(innerClass.code);
        }

        @Override
        public int hashCode() {
            return Objects.hash(name, code);
        }

        class DeepInnerClass {
            private String name;
            private String code;

            public DeepInnerClass(String name, String code) {
                this.name = name;
                this.code = code;
            }

            public String getName() {
                return name;
            }

            public String getCode() {
                return code;
            }

            public void setName(String name) {
                this.name = name;
            }

            public void setCode(String code) {
                this.code = code;
            }

            @Override
            public String toString() {
                return "DeepInnerClass{" +
                        "name='" + name + '\'' +
                        ", code='" + code + '\'' +
                        '}';
            }

            @Override
            public boolean equals(Object o) {
                if (this == o) return true;
                if (o == null || getClass() != o.getClass()) return false;
                DeepInnerClass that = (DeepInnerClass) o;
                return name.equals(that.name) &&
                        code.equals(that.code);
            }

            @Override
            public int hashCode() {
                return Objects.hash(name, code);
            }
        }
    }


    class SecondInnerClass {
        private String name;
        private String code;

        public SecondInnerClass(String name, String code) {
            this.name = name;
            this.code = code;
        }

        public String getName() {
            return name;
        }

        public String getCode() {
            return code;
        }

        public void setName(String name) {
            this.name = name;
        }

        public void setCode(String code) {
            this.code = code;
        }

        @Override
        public String toString() {
            return "SecondInnerClass{" +
                    "name='" + name + '\'' +
                    ", code='" + code + '\'' +
                    '}';
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            SecondInnerClass that = (SecondInnerClass) o;
            return name.equals(that.name) &&
                    code.equals(that.code);
        }

        @Override
        public int hashCode() {
            return Objects.hash(name, code);
        }
    }
}
