import javassist.*;

import java.io.IOException;

/**
 * @Author zhangboqing
 * @Date 2020/3/31
 */
public class AddMethodDemo {
    public static void main(String[] args) throws NotFoundException, CannotCompileException, IOException {
        ClassPool cp = ClassPool.getDefault();
        cp.insertClassPath("MyMain.class");
        CtClass ct = cp.get("MyMain");
        CtMethod method = new CtMethod(
                CtClass.voidType,
                "foo",
                new CtClass[]{CtClass.intType, CtClass.intType},
                ct);
        method.setModifiers(Modifier.PUBLIC);
        ct.addMethod(method);
        ct.writeFile("/Users/zhangboqing/Software/MyGithub/jvm-learn/JavassitDemo/src/main/java/");
    }
}
