package f_asm_and_javassist.modifyMethod;

/**
 * @Author zhangboqing
 * @Date 2020/3/30
 */
public class TryCatchMain {
    public void foo() {
        System.out.println("step 1");
        int a = 1 / 0;
        System.out.println("step 2");
    }
}
