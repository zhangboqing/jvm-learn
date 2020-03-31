package f_asm_and_javassist.modifyMethod;

import f_asm_and_javassist.FileUtils;
import jdk.internal.org.objectweb.asm.*;

import java.io.FileNotFoundException;

import static jdk.internal.org.objectweb.asm.Opcodes.ASM5;

/**
 * @Author zhangboqing
 * @Date 2020/3/27
 */
public class AsmDemo {

    //为了替换 foo 的方法体，一个可选的做法是在 ClassVisitor 的 visitMethod 方法返回 null 删除掉原 foo 方法，然后在 visitEnd 方法中新增一个 foo 方法，如下面的代码所示：

    /*
        java  f_asm_and_javassist.modifyMethod.MyMain
    Error: A JNI error has occurred, please check your installation and try again
    Exception in thread "main" java.lang.ClassFormatError: Arguments can't fit into locals in class file MyMain

    再回过头来查看生成的字节码，会发现它的 stack 和 locals 都等于 0，从第一章的内容可以知道 Java 虚拟机用 stack 和 locals 的值来分配操作数栈和局部变量表的大小，两个值都等于 0 的话没有办法加载操作数和存储方法参数。

        从源代码可以分析出，最大栈大小为 2（a, 100），局部变量大小为 2（this, a)。一个可选的办法是在 mv.visitEnd(); 代码之前新增 mv.visitMaxs(2, 2); 手动指定 stack 和 locals 的大小。

        另一个方法是让 ASM 自动计算 stack 和 locals，这就要说到 ClassWriter 构造器方法参数的含义了：

        new ClassWriter(0)：一开始就是使用这种方式，这种方式不会自动计算操作数栈和局部变量表的大小，需要我们自己手动指定
        new ClassWriter(ClassWriter.COMPUTE_MAXS）：这种方式会自动计算操作数栈和局部变量表的大小，前提是需要调用一下 visitMaxs 方法来触发计算上述两个值，参数值可以随便指定。
        new ClassWriter(ClassWriter.COMPUTE_FRAMES)：不仅会计算操作数栈和局部变量表，还会自动计算 StackMapFrames。在 Java 6 之后 JVM 在 class 文件的 Code 属性中引入了 StackMapTable 属性，作用是为了提高 JVM 在类型检查的验证过程的效率，里面记录的是一个方法中操作数栈与局部变量区的类型在一些特定位置的状态。
        虽然 COMPUTE_FRAMES 隐式的包含了 COMPUTE_MAXS，一般在使用中，还是会同时指定:
    */
    public static void main(String[] args) throws FileNotFoundException {
        byte[] bytes = FileUtils.readFileToByteArray("src/f_asm_and_javassist/modifyMethod/MyMain.class");
        ClassReader cr = new ClassReader(bytes);
        // 指定 ClassWriter 自动计算参数
        ClassWriter cw = new ClassWriter(ClassWriter.COMPUTE_MAXS | ClassWriter.COMPUTE_FRAMES);
        ClassVisitor cv = new ClassVisitor(ASM5, cw) {
            @Override
            public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {

                if ("foo".equals(name)) {
                    // 删除 foo 方法
                    return null;
                }
                return super.visitMethod(access, name, desc, signature, exceptions);
            }

            @Override
            public void visitEnd() {
                // 新增 foo 方法
                MethodVisitor mv = cv.visitMethod(Opcodes.ACC_PUBLIC, "foo", "(I)I", null, null);

                mv.visitCode();
                mv.visitVarInsn(Opcodes.ILOAD, 1);
                mv.visitIntInsn(Opcodes.BIPUSH, 100);
                mv.visitInsn(Opcodes.IADD);
                mv.visitInsn(Opcodes.IRETURN);
                // 触发计算
                mv.visitMaxs(0, 0);
                mv.visitEnd();
            }
        };
        cr.accept(cv, 0);
        byte[] bytesModified = cw.toByteArray();
        FileUtils.writeByteArrayToFile("src/f_asm_and_javassist/modifyMethod/MyMain2.class", bytesModified);
    }
}
