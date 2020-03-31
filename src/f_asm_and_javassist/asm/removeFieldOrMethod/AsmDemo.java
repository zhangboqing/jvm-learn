package f_asm_and_javassist.removeFieldOrMethod;

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
        byte[] bytes = FileUtils.readFileToByteArray("src/f_asm_and_javassist/removeFieldOrMethod/MyMain.class");
        ClassReader cr = new ClassReader(bytes);
        ClassWriter cw = new ClassWriter(0);
        ClassVisitor cv = new ClassVisitor(ASM5, cw) {
            @Override
            public FieldVisitor visitField(int access, String name, String desc, String signature, Object value) {
                if ("abc".equals(name)) {
                    return null;
                }
                return super.visitField(access, name, desc, signature, value);
            }

            @Override
            public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {
                if ("xyz".equals(name)) {
                    return null;
                }
                return super.visitMethod(access, name, desc, signature, exceptions);
            }
        };

        cr.accept(cv, ClassReader.SKIP_CODE | ClassReader.SKIP_DEBUG);
        byte[] bytesModified = cw.toByteArray();
        FileUtils.writeByteArrayToFile("src/f_asm_and_javassist/removeFieldOrMethod/MyMain.class", bytesModified);
    }

}
