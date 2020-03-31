package f_asm_and_javassist.visitFieldAndMethod;

import jdk.internal.org.objectweb.asm.*;
import jdk.internal.org.objectweb.asm.tree.ClassNode;
import jdk.internal.org.objectweb.asm.tree.FieldNode;
import jdk.internal.org.objectweb.asm.tree.MethodNode;

import java.io.*;
import java.util.List;

import static jdk.internal.org.objectweb.asm.Opcodes.ASM5;

/**
 * @Author zhangboqing
 * @Date 2020/3/26
 */
public class AsmDemo {


    public static void main(String[] args) throws FileNotFoundException {
//        scoreApiTest();
        treeApiTest();
    }

    //1）访问类的方法和字段
    /*
    值得注意的是 ClassReader 类 accept 方法的第二个参数 flags，这个参数是一个比特掩码（bit-mask），可以选择组合的值如下：

    SKIP_DEBUG：跳过类文件中的调试信息，比如行号信息（LineNumberTable）等
    SKIP_CODE：跳过方法体中的 Code 属性（方法字节码、异常表等）
    EXPAND_FRAMES：展开 StackMapTable 属性，
    SKIP_FRAMES：跳过 StackMapTable 属性
    */
    private static void scoreApiTest() throws FileNotFoundException {
        byte[] bytes  = getBytes(); // MyMain.class 文件的字节数组
        ClassReader cr = new ClassReader(bytes);
        ClassWriter cw = new ClassWriter(0);
        ClassVisitor cv = new ClassVisitor(ASM5, cw) {
            @Override
            public FieldVisitor visitField(int access, String name, String desc, String signature, Object value) {
                System.out.println("field: " + name);
                return super.visitField(access, name, desc, signature, value);
            }

            @Override
            public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {
                System.out.println("method: " + name);
                return super.visitMethod(access, name, desc, signature, exceptions);
            }
        };
        cr.accept(cv, ClassReader.SKIP_CODE | ClassReader.SKIP_DEBUG);
    }
    private static void treeApiTest() throws FileNotFoundException {
        byte[] bytes = getBytes();

        ClassReader cr = new ClassReader(bytes);
        ClassNode cn = new ClassNode();
        cr.accept(cn, ClassReader.SKIP_DEBUG | ClassReader.SKIP_CODE);

        List<FieldNode> fields = cn.fields;
        for (int i = 0; i < fields.size(); i++) {
            FieldNode fieldNode = fields.get(i);
            System.out.println("field: " + fieldNode.name);
        }
        List<MethodNode> methods = cn.methods;
        for (int i = 0; i < methods.size(); ++i) {
            MethodNode method = methods.get(i);
            System.out.println("method: " + method.name);
        }
        ClassWriter cw = new ClassWriter(0);
        cr.accept(cn, 0);
        byte[] bytesModified = cw.toByteArray();
    }


    private static byte[] getBytes() throws FileNotFoundException {
        return toByteArray("/Users/zhangboqing/Software/MyGithub/jvm-learn/src/f_asm_and_javassist/MyMain.class");
    }

    public static byte[] toByteArray(String filename) throws FileNotFoundException {
        File file = new File(filename);
        if (!file.exists()) {
            throw new FileNotFoundException(filename);
        }
        try (
            FileInputStream fileInputStream = new FileInputStream(file);
            BufferedInputStream bufferedInputStream = new BufferedInputStream(fileInputStream);
        ) {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

            int bufferLength = 1024;
            byte[] buffer = new byte[bufferLength];
            int len = bufferedInputStream.read(buffer, 0, bufferLength);
            while ( len != -1) {
                byteArrayOutputStream.write(buffer,0,len);
                len = bufferedInputStream.read(buffer, 0, bufferLength);
            }

            return byteArrayOutputStream.toByteArray();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }


}
