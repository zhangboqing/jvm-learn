/**
 * @Author zhangboqing
 * @Date 2020/3/25
 */
package c_bytecode_instruct;

/**
 字节码指令：
 根据字节码的不同用途，可以大概分为如下几类
 加载和存储指令，比如 iload 将一个整形值从局部变量表加载到操作数栈
 控制转移指令，比如条件分支 ifeq
 对象操作，比如创建类实例的指令 new
 方法调用，比如 invokevirtual 指令用于调用对象的实例方法
 运算指令和类型转换，比如加法指令 iadd
 线程同步，monitorenter 和 monitorexit 两条指令来支持 synchronized 关键字的语义
 异常处理，比如 athrow 显式抛出异常
1）控制转移指令
 控制转移指令根据条件进行分支跳转，我们常见的 if-then-else、三目表达式、for 循环、异常处理等都属于这个范畴。
 对应的指令集包括：
 条件分支：ifeq、iflt、ifle、ifne、ifgt、ifge、ifnull、ifnonnull、if_icmpeq、 if_icmpne、if_icmplt, if_icmpgt、if_icmple、if_icmpge、if_acmpeq 和 if_acmpne。
 复合条件分支：tableswitch、lookupswitch
 无条件分支：goto、goto_w、jsr、jsr_w、ret



 */