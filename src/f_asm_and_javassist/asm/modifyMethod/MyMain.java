package f_asm_and_javassist.modifyMethod;

public class MyMain {
    public static void main(String[] args) {
        System.out.println(new MyMain().foo(1));
    }

    public int foo(int a) {
        return a; // 修改为 return a + 100;
    }
}