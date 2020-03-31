### 小结
```text
    这篇文章我们主要讲解了 ASM 字节码操作框架，一起来回顾一下要点：
    
    第一，ASM 是一个久经考验的工业级字节码操作框架。
    第二，ASM 的三个核心类 ClassReader、ClassVisitor、ClassWriter。ClassReader 对象创建之后，
        调用 ClassReader.accept() 方法，传入一个 ClassVisitor 对象。ClassVisitor 在解析字节码的过程中遇到不同的节点时会调用不同的 visit() 方法。
        ClassWriter 负责把最终修改的字节码以 byte 数组的形式返回
```