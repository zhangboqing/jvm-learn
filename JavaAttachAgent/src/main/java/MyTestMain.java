import java.util.concurrent.TimeUnit;

public class MyTestMain {
    public static void main(String[] args) throws InterruptedException {
        while (true) {
            System.out.println(foo());
            TimeUnit.SECONDS.sleep(3);
        }
    }

    public static int foo() {
        return 100; // 修改后 return 50;
    }
}