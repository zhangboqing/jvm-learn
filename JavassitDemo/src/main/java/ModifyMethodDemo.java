import javassist.*;

import java.io.IOException;

/**
 * @Author zhangboqing
 * @Date 2020/3/31
 */
public class ModifyMethodDemo {

    /*
      CtMethod 提供了几个实用的方法来修改方法体：

        setBody 方法来替换整个方法体，setBody 方法接收一段源代码字符串，Javassist 会将这段源码字符串编译为字节码，替换原有的方法体。
        insertBefore、insertAfter：在方法开始和结束插入语句

        源代码在 javac 编译以后，局部变量名字都被抹去了，只留下了类型和局部变量表的位置，比如上面的 a 和 b 对应局部变量表 1 和 2 的位置，位置 0 由 this 占用。

    在 Javassist 中访问方法参数使用 $0 $1 ...，而不是直接使用变量名，把上面的代码改为：

    除了方法的参数，Javassist 定义了以 $ 开头的特殊标识符，如下表所示：

符号	含义
$0, $1, $2, ...	this 和方法参数
$args	方法参数数组，类型为 Object[]
$$	所有参数，foo($$) 相当于 foo($1, $2, ...)
$cflow(...)	control flow 变量
$r	返回结果的类型，用于强制类型转换
$w	包装器类型，用于强制类型转换
$_	返回值
$sig	类型为 java.lang.Class 的参数类型数组
$type	一个 java.lang.Class 对象，表示返回值类型
$class	一个 java.lang.Class 对象，表示当前正在修改的类

下面来逐一介绍

$0 $1 $2 ... 方法参数

0 等价于 this，如果是静态方法 $0 不可用，从 $1 开始依次表示方法参数

$args 参数

$args变量表示所有参数的数组，它是一个 Object 类型的数组，如果参数中有原始类型，会被转为对应的对象类型，比如上面 foo(int a, int b) 对应的 $args 为

new Object[]{ new Integer(a), new Integer(b) }
$$ 参数

$$ 参数表示所有的参数的展开，参数直接用逗号分隔，

foo($$)
相当于：

foo($1, $2, ...)
$cflow 参数

$cflow 是 "control flow" 的缩写，这是一个只读的属性，表示某方法递归调用的深度。一个典型的使用场景是监控某递归方法执行的时间，只想记录一次最顶层调用的时间，可以使用 $cflow 来判断当前递归调用的深度，如果不是最顶层调用忽略记录时间。

比如下面的计算 fibonacci 的方法：

public long fibonacci(int n) {
    if (n <= 1) return n;
    else return fibonacci(n-1) + fibonacci(n-2);
}
如果只想在第一次调用的时候执行打印，可以用下面的的代码：

CtMethod method = ct.getMethod("fibonacci", "(I)J");
method.useCflow("fibonacci");
method.insertBefore(
        "if ($cflow(fibonacci) == 0) {" +
            "System.out.println(\"fibonacci init \" + $1);" +
        "}"
);
执行生成的 MyMain，可以看到只输出了一次打印：

java -cp /path/to/javassist.jar:. MyMain
fibonacci init 10
$_ 参数

CtMethod 的 insertAfter() 方法在目标方法的末尾插入一段代码。 $_ 来表示方法的返回值，在 insertAfter 方法可以引用。比如下面的代码：

method.insertAfter("System.out.println(\"result: \"  + $_);");
查看反编译生成的 class 文件：

public int foo(int a, int b) {
    int var4 = a + b;
    System.out.println(var4);
    return var4;
}
细心的读者看到这里会有疑问，如果是方法异常退出，它的方法返回值是什么呢？假如 foo 代码如下：

public int foo(int a, int b) {
    int c = 1 / 0;
    return a + b;
}
执行上的改写后，反编译以后代码如下：

public int foo(int a, int b) {
    int c = 1 / 0;
    int var5 = a + b;
    System.out.println(var5);
    return var5;
}
这种情况下，代码块抛出异常，是无法执行插入的语句的。如果想代码抛出异常的时候也能执行，就需要把 insertAfter 的第二个参数 asFinally 设置为 true：

method.insertAfter("System.out.println(\"result: \"  + $_);", true);
执行输出结果如下，可以看到已经输出了 "result: 0"

result: 0
Exception in thread "main" java.lang.ArithmeticException: / by zero
        at MyMain.foo(MyMain.java:9)
        at MyMain.main(MyMain.java:6)
还有几个 Javassist 提供的内置变量（$r等），用的非常少，这里不再介绍，具体可以查看 javassist 的官网。

    */

    public static void main(String[] args) {
        try {
            ClassPool cp = ClassPool.getDefault();
            cp.insertClassPath("MyMain2.class");
            cp.makeClass("MyMain2.class");
            CtClass ct = cp.get("MyMain2");
            CtMethod method = ct.getMethod("foo", "(II)I");
            method.setBody("return $1+$2;");
            ct.writeFile("/Users/zhangboqing/Software/MyGithub/jvm-learn/JavassitDemo/src/main/java/");
        } catch (NotFoundException e) {
            e.printStackTrace();
        } catch (CannotCompileException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
