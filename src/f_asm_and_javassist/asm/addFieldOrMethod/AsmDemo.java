package f_asm_and_javassist.addFieldOrMethodOrMethod;

import f_asm_and_javassist.FileUtils;
import jdk.internal.org.objectweb.asm.*;
import jdk.internal.org.objectweb.asm.tree.ClassNode;
import jdk.internal.org.objectweb.asm.tree.FieldNode;

import java.io.FileNotFoundException;

import static jdk.internal.org.objectweb.asm.Opcodes.ASM5;

/**
 * @Author zhangboqing
 * @Date 2020/3/27
 */
public class AsmDemo {

    public static void main(String[] args) throws FileNotFoundException {
//        addFieldOrMethodForscoreApi();
//        addFiledFortreeApi();
        addMethodForScoreApi();
    }

    private static void addMethodForScoreApi() throws FileNotFoundException {
        byte[] bytes = FileUtils.readFileToByteArray("src/f_asm_and_javassist/addFieldOrMethod/MyMain.class");
        ClassReader cr = new ClassReader(bytes);
        ClassWriter cw = new ClassWriter(0);
        ClassVisitor cv = new ClassVisitor(ASM5, cw) {
            @Override
            public void visitEnd() {
                super.visitEnd();
                MethodVisitor mv = cv.visitMethod(Opcodes.ACC_PUBLIC, "xyz", "(ILjava/lang/String;)V", null, null);
                if (mv != null) mv.visitEnd();
            }
        };
        cr.accept(cv, ClassReader.SKIP_CODE | ClassReader.SKIP_DEBUG);
        byte[] bytesModified = cw.toByteArray();
        FileUtils.writeByteArrayToFile("src/f_asm_and_javassist/addFieldOrMethod/MyMain4.class", bytesModified);
    }


    private static void addFiledFortreeApi() throws FileNotFoundException {
        byte[] bytes = FileUtils.readFileToByteArray("src/f_asm_and_javassist/addFieldOrMethod/MyMain.class");
        ClassReader cr = new ClassReader(bytes);
        ClassNode cn = new ClassNode();
        cr.accept(cn, ClassReader.SKIP_DEBUG | ClassReader.SKIP_CODE);

        FieldNode fn = new FieldNode(Opcodes.ACC_PUBLIC, "xyz", "Ljava/lang/String;", null, null);
        cn.fields.add(fn);

        ClassWriter cw = new ClassWriter(0);
        cn.accept(cw);
        byte[] bytesModified = cw.toByteArray();
        FileUtils.writeByteArrayToFile("src/f_asm_and_javassist/addFieldOrMethod/MyMain3.class", bytesModified);
    }

    private static void addFieldOrMethodForscoreApi() throws FileNotFoundException {
        byte[] bytes = FileUtils.readFileToByteArray("src/f_asm_and_javassist/addFieldOrMethod/MyMain.class");
        ClassReader cr = new ClassReader(bytes);
        ClassWriter cw = new ClassWriter(0);
        ClassVisitor cv = new ClassVisitor(ASM5, cw) {
            @Override
            public void visitEnd() {
                super.visitEnd();
                FieldVisitor fv = cv.visitField(Opcodes.ACC_PUBLIC, "xyz", "Ljava/lang/String;", null, null);
                if (fv != null) fv.visitEnd();
            }
        };
        cr.accept(cv, ClassReader.SKIP_CODE | ClassReader.SKIP_DEBUG);
        byte[] bytesModified = cw.toByteArray();
        FileUtils.writeByteArrayToFile("src/f_asm_and_javassist/addFieldOrMethod/MyMain.class", bytesModified);
    }
}
