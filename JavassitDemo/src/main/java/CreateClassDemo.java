import javassist.CannotCompileException;
import javassist.ClassPool;
import javassist.CtClass;

import java.io.IOException;

/**
 * @Author zhangboqing
 * @Date 2020/3/31
 */
public class CreateClassDemo {

    public static void main(String[] args) throws CannotCompileException, IOException {
        ClassPool cp = ClassPool.getDefault();
        CtClass ct = cp.makeClass("com.zbq.Hello");
        ct.writeFile("/Users/zhangboqing/Software/MyGithub/jvm-learn/JavassitDemo/src/main/java/");
    }
}
