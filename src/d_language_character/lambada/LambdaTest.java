package d_language_character.lambada;

/**
 * @Author zhangboqing
 * @Date 2020/3/25
 */
public class AnonymityInnerClassTest {
    public static void main(String[] args) {
        Runnable r1 = new Runnable() {
            @Override
            public void run() {
                System.out.println("hello, inner class");
            }
        };
        r1.run();
    }
}
