package f_asm_and_javassist.modifyMethod;

import f_asm_and_javassist.FileUtils;
import jdk.internal.org.objectweb.asm.*;
import jdk.internal.org.objectweb.asm.commons.AdviceAdapter;

import java.io.FileNotFoundException;

import static jdk.internal.org.objectweb.asm.Opcodes.ASM5;

/**
 * @Author zhangboqing
 * @Date 2020/3/30
 */
public class TryCatchDemo {

    /**
     * 这里需要介绍 ASM 的 Label 类，与它的英文含义一样，可以给字节码指令地址打标签，标记特定的字节码位置，用于后续跳转等。新增一个 Label 可以用 MethodVisitor 的 visitLabel 方法：
     * <p>
     * Label startLabel = new Label();
     * mv.visitLabel(startLabel);
     */
    /*
        JVM 的异常处理是通过异常表来实现的，try catch finally 关键字实际上是标定了异常处理的范围。ASM 中可以用 visitTryCatchBlock 方法来给一段代码块增加异常表，它的方法签名如下：

        public void visitTryCatchBlock(Label start, Label end, Label handler, String type)
        其中 start、end 表示异常表开始和结束的位置，handler 表示异常发生后需要跳转到哪里继续执行，可以理解为 catch 语句块开始的位置，type 是异常的类型。

        为了给整个方法体包裹 try-catch 语句，start Label 应该在方法 visitCode 之后，end Label 应该在 visitMaxs 调用之前
    */

    public static void main(String[] args) throws FileNotFoundException {
        byte[] bytes = FileUtils.readFileToByteArray("src/f_asm_and_javassist/modifyMethod/TryCatchMain.class");

        ClassReader cr = new ClassReader(bytes);
        ClassWriter cw = new ClassWriter(ClassWriter.COMPUTE_MAXS | ClassWriter.COMPUTE_FRAMES);
        ClassVisitor cv = new ClassVisitor(ASM5, cw) {
            @Override
            public MethodVisitor visitMethod(int access, final String name, String desc, String signature, String[] exceptions) {

                MethodVisitor mv = super.visitMethod(access, name, desc, signature, exceptions);
                if (!"foo".equals(name)) return mv;

                return new AdviceAdapter(ASM5, mv, access, name, desc) {
                    Label startLabel = new Label();

                    @Override
                    protected void onMethodEnter() {
                        super.onMethodEnter();
                        mv.visitLabel(startLabel);

                        mv.visitFieldInsn(GETSTATIC, "java/lang/System", "out", "Ljava/io/PrintStream;");
                        mv.visitLdcInsn("enter " + name);
                        mv.visitMethodInsn(INVOKEVIRTUAL, "java/io/PrintStream", "println", "(Ljava/lang/String;)V", false);
                    }

                    @Override
                    public void visitMaxs(int maxStack, int maxLocals) {
                        // 生成异常表
                        Label endLabel = new Label();
                        mv.visitTryCatchBlock(startLabel, endLabel, endLabel, null);
                        mv.visitLabel(endLabel);

                        // 生成异常处理代码块
                        finallyBlock(ATHROW);
                        mv.visitInsn(ATHROW);
                        super.visitMaxs(maxStack, maxLocals);
                    }

                    private void finallyBlock(int opcode) {
                        mv.visitFieldInsn(GETSTATIC, "java/lang/System", "out", "Ljava/io/PrintStream;");
                        if (opcode == Opcodes.ATHROW) {
                            mv.visitLdcInsn("err exit " + name);
                        } else {
                            mv.visitLdcInsn("normal exit " + name);
                        }
                        mv.visitMethodInsn(INVOKEVIRTUAL, "java/io/PrintStream", "println", "(Ljava/lang/String;)V", false);
                    }

                    @Override
                    protected void onMethodExit(int opcode) {
                        super.onMethodExit(opcode);
                        // 处理正常返回的场景
                        if (opcode != ATHROW) finallyBlock(opcode);
                    }
                };
            }
        };
        cr.accept(cv, 0);
        byte[] bytesModified = cw.toByteArray();
        FileUtils.writeByteArrayToFile("src/f_asm_and_javassist/modifyMethod/TryCatchMain2.class", bytesModified);
    }

}
