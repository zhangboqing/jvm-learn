package javaagent;

public class MyJavaAgentTest {
    public static void main(String[] args) {
        new MyJavaAgentTest().foo();
    }
    public void foo() {
        bar1();
        bar2();
    }

    public void bar1() {
        System.out.println("bar1");
    }

    public void bar2() {
        System.out.println("bar2");
    }
}