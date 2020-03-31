package f_asm_and_javassist.modifyMethod;

import f_asm_and_javassist.FileUtils;
import jdk.internal.org.objectweb.asm.*;
import jdk.internal.org.objectweb.asm.commons.AdviceAdapter;

import java.io.File;
import java.io.FileNotFoundException;

import static jdk.internal.org.objectweb.asm.Opcodes.ASM5;

/**
 * @Author zhangboqing
 * @Date 2020/3/30
 */
public class AdviceAdapterDemo {

    /*
      AdviceAdapter 是一个抽象类，继承自 MethodVisitor，可以很方便的在方法的开始和结束前插入代码。它有两个比较方便的方法：

        onMethodEnter：方法开始或者构造器方法中父类的构造器被调用以后被回调
        onMethodExit：正常退出和异常退出时被调用，正常退出指的是遇到 RETURN、ARETURN、LRETURN 等方法正常返回的情况。异常退出指的是遇到 ATHROW 指令，有异常抛出的情况下方法返回的情况。
    */

    //  foo 方法，现在用 AdviceAdapter 在函数开始和结束的时候都加上一行打印。
    public static void main(String[] args) throws FileNotFoundException {
        byte[] bytes = FileUtils.readFileToByteArray("src/f_asm_and_javassist/modifyMethod/AdviceAdapterMain.class");

        ClassReader cr = new ClassReader(bytes);
        ClassWriter cw = new ClassWriter(ClassWriter.COMPUTE_MAXS | ClassWriter.COMPUTE_FRAMES);
        ClassVisitor cv = new ClassVisitor(ASM5, cw) {
            @Override
            public MethodVisitor visitMethod(int access, final String name, String desc, String signature, String[] exceptions) {

                MethodVisitor mv = super.visitMethod(access, name, desc, signature, exceptions);
                if (!"foo".equals(name)) return mv;

                return new AdviceAdapter(ASM5, mv, access, name, desc) {
                    @Override
                    protected void onMethodEnter() {
                        // 新增 System.out.println("enter " +  name);
                        super.onMethodEnter();
                        mv.visitFieldInsn(GETSTATIC, "java/lang/System", "out", "Ljava/io/PrintStream;");
                        mv.visitLdcInsn("enter " + name);
                        mv.visitMethodInsn(INVOKEVIRTUAL, "java/io/PrintStream", "println", "(Ljava/lang/String;)V", false);
                    }

                    @Override
                    protected void onMethodExit(int opcode) {
                        // 新增 System.out.println("[normal,err] exit " +  name);
                        super.onMethodExit(opcode);
                        mv.visitFieldInsn(GETSTATIC, "java/lang/System", "out", "Ljava/io/PrintStream;");
                        if (opcode == Opcodes.ATHROW) {
                            mv.visitLdcInsn("err exit " + name);
                        } else {
                            mv.visitLdcInsn("normal exit " + name);
                        }
                        mv.visitMethodInsn(INVOKEVIRTUAL, "java/io/PrintStream", "println", "(Ljava/lang/String;)V", false);
                    }
                };
            }
        };
        cr.accept(cv, 0);
        byte[] bytesModified = cw.toByteArray();
        FileUtils.writeByteArrayToFile("src/f_asm_and_javassist/modifyMethod/AdviceAdapterMain2.class", bytesModified);
    }
}
