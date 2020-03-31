package c_bytecode_instructions;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;

public class InvokeDynamic {
    public void print(String s) {
        System.out.println("hello, " + s);
    }
    public static void main(String[] args) throws Throwable {
        InvokeDynamic foo = new InvokeDynamic();

        MethodType methodType = MethodType.methodType(void.class, String.class);
        MethodHandle methodHandle = MethodHandles.lookup().findVirtual(InvokeDynamic.class, "print", methodType);
        methodHandle.invokeExact(foo, "world");
    }
}