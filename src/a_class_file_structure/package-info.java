/**
 * @Author zhangboqing
 * @Date 2020/3/24
 */
package class_file_structure;

/**
 1.javac
    将java文件编译成class文件
    HelloWorld.java ==> HelloWorld.class

 java -g HelloWorld.java 可生成更多的调试信息

 查看class文件的工具，通过jd-gui显示java代码
 http://java-decompiler.github.io/
 2.javap
   反编译命令，解析class文件内容
 ➜  MyGithub javap
 Usage: javap <options> <classes>
 where possible options include:
 -help  --help  -?        Print this usage message
 -version                 Version information
 -v  -verbose             Print additional information
 -l                       Print line number and local variable tables
 -public                  Show only public classes and members
 -protected               Show protected/public classes and members
 -package                 Show package/protected/public classes
 and members (default)
 -p  -private             Show all classes and members
 -c                       Disassemble the code
 -s                       Print internal type signatures
 -sysinfo                 Show system info (path, size, date, MD5 hash)
 of class being processed
 -constants               Show final constants
 -classpath <path>        Specify where to find user class files
 -cp <path>               Specify where to find user class files
 -bootclasspath <path>    Override location of bootstrap class files
 javap 有比较多的参数选项，其中-c -v -l -p -s是最常用的。
 * -c选项 查看方法的字节码
 * -p 选项 加上 -p 选项以后可以显示 private 方法和字段
 * -v 选项  javap 加上 -v 参数的输出更多详细的信息，比如栈大小、方法参数的个数
 * -s选项 javap 还有一个好用的选项 -s，可以输出签名的类型描述符

 3.字段描述符（Field Descriptor）
    是一个表示类、实例或局部变量的语法符号，它的表示形式是紧凑的，比如 int 是用 I 表示的。
 标识字符	含义
 B	byte
 C	char
 D	double
 F	float
 I	int
 J	long
 S	short
 Z	boolean
 V	void
 L	对象类型,如Ljava/lang/Object

 4.方法描述符（Method Descriptor）
    表示一个方法所需参数和返回值信息，表示形式为( ParameterDescriptor* ) ReturnDescriptor。 ParameterDescriptor 表示参数类型，ReturnDescriptor表示返回值信息，当没有返回值时用V表示。
 比如方法Object foo(int i, double d, Thread t)的描述符为(IDLjava/lang/Thread;)Ljava/lang/Object;

 5.class 文件结构剖析
 ClassFile {
     u4             magic;
     u2             minor_version;
     u2             major_version;
     u2             constant_pool_count;
     cp_info        constant_pool[constant_pool_count-1];
     u2             access_flags;
     u2             this_class;
     u2             super_class;
     u2             interfaces_count;
     u2             interfaces[interfaces_count];
     u2             fields_count;
     field_info     fields[fields_count];
     u2             methods_count;
     method_info    methods[methods_count];
     u2             attributes_count;
     attribute_info attributes[attributes_count];
 }

 打开HelloWorld.class，显示的内容是16进制内容
 cafe babe 0000 0034 001d 0a00 0600 0f09
 0010 0011 0800 120a 0013 0014 0700 1507
 0016 0100 063c 696e 6974 3e01 0003 2829
 5601 0004 436f 6465 0100 0f4c 696e 654e
 756d 6265 7254 6162 6c65 0100 046d 6169
 6e01 0016 285b 4c6a 6176 612f 6c61 6e67
 2f53 7472 696e 673b 2956 0100 0a53 6f75
 7263 6546 696c 6501 000f 4865 6c6c 6f57
 6f72 6c64 2e6a 6176 610c 0007 0008 0700
 170c 0018 0019 0100 0c48 656c 6c6f 2077
 6f72 6c64 2107 001a 0c00 1b00 1c01 001f
 636c 6173 735f 6669 6c65 5f73 7472 7563
 7475 7265 2f48 656c 6c6f 576f 726c 6401
 0010 6a61 7661 2f6c 616e 672f 4f62 6a65
 6374 0100 106a 6176 612f 6c61 6e67 2f53
 7973 7465 6d01 0003 6f75 7401 0015 4c6a
 6176 612f 696f 2f50 7269 6e74 5374 7265
 616d 3b01 0013 6a61 7661 2f69 6f2f 5072
 696e 7453 7472 6561 6d01 0007 7072 696e
 746c 6e01 0015 284c 6a61 7661 2f6c 616e
 672f 5374 7269 6e67 3b29 5600 2100 0500
 0600 0000 0000 0200 0100 0700 0800 0100
 0900 0000 1d00 0100 0100 0000 052a b700
 01b1 0000 0001 000a 0000 0006 0001 0000
 0007 0009 000b 000c 0001 0009 0000 0025
 0002 0001 0000 0009 b200 0212 03b6 0004
 b100 0000 0100 0a00 0000 0a00 0200 0000
 0a00 0800 0b00 0100 0d00 0000 0200 0e

 class 文件由下面十个部分组成

 魔数（Magic Number）
 版本号（Minor&Major Version）
 常量池（Constant Pool）
 类访问标记(Access Flags)
 类索引（This Class）
 超类索引（Super Class）
 接口表索引（Interfaces）
 字段表（Fields）
 方法表（Methods）
 属性表（Attributes）


 1)魔数 0xCAFEBABE 是 JVM 识别 .class 文件的标志，虚拟机在加载类文件之前会先检查这四个字节，如果不是 0xCAFEBABE 则拒绝加载该文件。
 cafe babe

 2)在魔数之后的四个字节分别表示副版本号（Minor Version）和主版本号（Major Version
 0000（副版本号） 0034（主版本号）
 这里的主版本是 52（0x34），虚拟机解析这个类时就知道这是一个 Java 8 编译出的类，如果类文件的版本号高于 JVM 自身的版本号，加载该类会被直接抛出java.lang.UnsupportedClassVersionError异常

 3)紧随版本号之后的是常量池数据区域，常量池是类文件中最复杂的数据结构
 对于 JVM 字节码来说，如果操作数非常小或者很常用的数字 0 之类的，这些操作数是内嵌到字节码中的。如果是字符串常量和较大的整数等，class 文件是把这些操作数存储在一个叫常量池（Constant Pool）的地方，当使用这些操作数时，使用的是常量池数据中的索引位置
 常量池结构如下所示：
 {
 u2             constant_pool_count;
 cp_info        constant_pool[constant_pool_count-1];
 }
 分为两部分：

 常量池大小（cp_info_count），常量池是 class 文件中第一个出现的变长结构，既然是池，就有大小，常量池大小的由两个字节表示。假设为值为 n，常量池真正有效的索引是 1 ~ n-1。0 属于保留索引，用来表示不指向任何常量池项。
 常量池项（cp_info）集合，最多包含 n-1 个。为什么是最多呢？Long 和 Double 类型的常量会占用两个索引位置，如果常量池包含了这两种类型，实际的常量池项的元素个数比 n-1 要小。

 如果想查看类文件的常量池，可以在 javap 命令中加上-v选项
 Java 虚拟机目前一共定义了 14 种常量类型，这些常量名都以 "CONSTANT" 开头，以 "info" 结尾，如下表所示：
 类型	值
 CONSTANT_Utf8_info	1
 CONSTANT_Integer_info	3
 CONSTANT_Float_info	4
 CONSTANT_Long_info	5
 CONSTANT_Double_info	6
 CONSTANT_Class_info	7
 CONSTANT_String_info	8
 CONSTANT_Fieldref_info	9
 CONSTANT_Methodref_info	10
 CONSTANT_InterfaceMethodref_info	11
 CONSTANT_NameAndType_info	12
 CONSTANT_MethodHandle_info	15
 CONSTANT_MethodType_info	16
 CONSTANT_InvokeDynamic_info	18

 每个常量项都由两部分构成：表示类型的 tag 和表示内容的字节数组，如下所示：
 cp_info {
 u1 tag;
 u1 info[];
 }
 详细介绍：
 1.CONSTANT_Utf8_info 存储的是经过 MUTF-8(modified UTF-8) 编码的字符串
 CONSTANT_Utf8_info {
 u1 tag;
 u2 length;
 u1 bytes[length];
 }
 由三部分构成：第一个字节是 tag，值为固定为 1，tag 之后的两个字节 length 表示字符串的长度，第三部分是采用 MUTF-8 编码的长度为 length 的字节数组。
 2.CONSTANT_Integer_info 和 CONSTANT_Float_info 这两种结构分别用来表示 int 和 float 类型的常量，这两种类型的结构很类似，都用四个字节来表示具体的数值常量，
 Java 语言规范还定义了 boolean、byte、short 和 char 类型的变量，在常量池中都会被当做 int 来处理
 CONSTANT_Integer_info {
 u1 tag;
 u4 bytes;
 }
 CONSTANT_Float_info {
 u1 tag;
 u4 bytes;
 }
 3.CONSTANT_Long_info 和 CONSTANT_Double_info 这两种结构分别用来表示 long 和 double 类型的常量，这两个结构类似，都用 8 个字节表示具体的常量数值。
 CONSTANT_Long_info 和 CONSTANT_Double_info 占用两个常量池位置
 ONSTANT_Long_info {
 u1 tag;
 u4 high_bytes;
 u4 low_bytes;
 }
 CONSTANT_Double_info {
 u1 tag;
 u4 high_bytes;
 u4 low_bytes;
 }
 4.CONSTANT_String_info 用来表示 java.lang.String 类型的常量对象
 CONSTANT_String_info {
 u1 tag;
 u2 string_index;
 }
 第一个字节是 tag，值为 8，tag 后面的两个字节是一个叫 string_index 的索引值，指向常量池中的CONSTANT_Utf8_info，这个 CONSTANT_Utf8_info 中存储的才是真正的字符串常量。
 5.CONSTANT_Class_info
 CONSTANT_Class_info {
 u1 tag;
 u2 name_index;
 }
 它由两部分组成，第一个字节是 tag，值为 7，tag 后面的两个字节 name_index 是一个常量池索引，指向类型为 CONSTANT_Utf8_info 常量，这个字符串存储的是类或接口的全限定名。
 6.CONSTANT_NameAndType_info 结构用来表示字段或者方法
 格式有下面三部分组成:
 tag CONSTANT_NameAndType_info 结构 tag 的值为 12
 name_index name_index 指向常量池中的 CONSTANT_Utf8_info，存储的是字段名或者方法名。
 descriptor_index descriptor_index 也是指向常量池中的 CONSTANT_Utf8_info，存储的是字段描述符或者方法描述符。
 7.CONSTANT_Fieldref_info、CONSTANT_Methodref_info 和 CONSTANT_InterfaceMethodref_info
 CONSTANT_Fieldref_info {
 u1 tag;
 u2 class_index;
 u2 name_and_type_index;
 }
 CONSTANT_Methodref_info {
 u1 tag;
 u2 class_index;
 u2 name_and_type_index;
 }
 CONSTANT_InterfaceMethodref_info {
 u1 tag;
 u2 class_index;
 u2 name_and_type_index;
 }
 下面以 CONSTANT_Methodref_info 为例来介绍，怎么样描述一个方法呢？

 方法 = 方法所属的类 + 方法名 + 方法参数和返回值描述符
 这就是 CONSTANT_Methodref_info 的作用，它表示类中方法的符号引用
 它由三部分构成

 第一个字节也是 tag，值为 10，
 第二个部分是 class_index，是一个指向 CONSTANT_Class_info 的常量池索引值
 第三部分是 name_and_type_index，是一个指向 CONSTANT_NameAndType_info 的常量池索引值，表示方法的参数类型和返回值的签名
 8.CONSTANT_MethodType_info、CONSTANT_MethodHandle_info 和CONSTANT_InvokeDynamic_info
 从 JDK1.7 开始，为了更好的支持动态语言调用，新增了 3 种常量池类型（CONSTANT_MethodType_info、CONSTANT_MethodHandle_info 和 CONSTANT_InvokeDynamic_info），以 CONSTANT_InvokeDynamic_info 为例，CONSTANT_InvokeDynamic_info 主要为 invokedynamic 指令提供启动引导方法，它由三部分构成
 CONSTANT_InvokeDynamic_info {
 u1 tag;
 u2 bootstrap_method_attr_index;
 u2 name_and_type_index;
 }
 tag：值为 18
 bootstrap_method_attr_index：指向引导方法表 bootstrap_methods[] 数组的索引
 name_and_type_index：指向索引类常量池里的CONSTANT_NameAndType_info，表示方法描述符

 4)类访问标记
 常量池之后存储的是访问标记（Access flags），用来标识一个类是是不是final、abstract 等，由两个字节表示总共可以有 16 个标记位可供使用，目前只使用了其中的 8 个。
 ACC_PUBLIC	1	标识是否是 public
 ACC_FINAL	10	标识是否是 final
 ACC_SUPER	20	已经不用了
 ACC_INTERFACE	200	标识是类还是接口
 ACC_ABSTRACT	400	标识是否是 abstract
 ACC_SYNTHETIC	1000	编译器自动生成，不是用户源代码编译生成
 ACC_ANNOTATION	2000	标识是否是注解类
 ACC_ENUM	4000	标识是否是枚举类

 5)类、超类、接口索引表
 这三个部分用来确定类的继承关系，this_class 表示类索引，super_name 表示父类索引，interfaces 表示类或者接口的直接父接口;
 以 this_class 为例，它是一个两字节组成，分别指向常量池。【0x00】【0x05】,这个元素是由两部分组成，第一部分是类型，这里是 Class 表示是一个类，第二部分是指向常量池下标 21 的元素，这个元素是字符串 "HelloWorldMain"。

 6）字段表
 紧随接口索引表之后的是字段表（Fields），类中定义的字段会被存储到这个集合中，包括类中定义的静态和非静态的字段，不包括方法内部定义的变量。
 {
 u2             fields_count;
 field_info     fields[fields_count];
 }
 由两部分组成
 字段数量（fields_count）：字段表也是一个变长的结构，类中定义的若干个字段的个数会被存储到字段数量里。
 字段集合（fields）：字段集合是一个类数组的结构，共有 fields_count 个，对应类中定义的若干个字段，每一个字段 field_info 的结构会在下面介绍。
 字段 field_info 结构
 field_info {
 u2             access_flags;
 u2             name_index;
 u2             descriptor_index;
 u2             attributes_count;
 attribute_info attributes[attributes_count];
 }
 字段结构分为四部分：

 access_flags：表示字段的访问标记，是 public、private 还是 protected，是否是 static，是否是 final 等。
 name_index：字段名的索引值，指向常量池的的字符串常量。
 descriptor_index：字段描述符的索引，指向常量池的字符串常量。
 attributes_count、attribute_info：表示属性的个数和属性集合。

 字段访问标记
 与类一样，字段也拥有自己的字段访问标记，不过要比类的访问标记要更丰富一些，共有 9 种，详细的列表如下：

 访问标记名	十六进制值	描述
 ACC_PUBLIC	0x0001	声明为 public
 ACC_PRIVATE	0x0002	声明为 private
 ACC_PROTECTED	0x0004	声明为 protected
 ACC_STATIC	0x0008	声明为 static
 ACC_FINAL	0x0010	声明为 final
 ACC_VOLATILE	0x0040	声明为 volatile，解决内存可见性的问题
 ACC_TRANSIENT	0x0080	声明为 transient，被transient 修饰的字段默认不会被序列化
 ACC_SYNTHETIC	0x1000	表示这个字段是由编译器自动生成，而不是用户代码编译产生
 ACC_ENUM	0x4000	表示这是一个枚举类型的变量

 字段描述符
 描述符	类型
 B	byte 类型
 C	char 类型
 D	double 类型
 F	float 类型
 I	int 类型
 J	long 类型
 S	short 类型
 Z	bool 类型
 L ClassName ;	引用类型，"L" + 对象类型的全限定名 + ";"
 [	一维数组

 字段属性
 与字段相关的属性有下面这几个：ConstantValue、Synthetic 、Signature、Deprecated、RuntimeVisibleAnnotations 和 RuntimeInvisibleAnnotations 这六个，比较常见的是 ConstantValue 这属性，用来表示一个常量字段的值

 7)方法表
 在字段表后面的是方法表，类中定义的方法会被存储在这里，与前面介绍的字段表很类似，方法表也是一个变长结构：
 {
 u2             methods_count;
 method_info    methods[methods_count];
 }
 方法 method_info 结构
 method_info {
 u2             access_flags;
 u2             name_index;
 u2             descriptor_index;
 u2             attributes_count;
 attribute_info attributes[attributes_count];
 }
 方法 method_info 结构分为四部分：
 access_flags：表示方法的访问标记，是 public、private 还是 protected，是否是 static，是否是 final 等。
 name_index：方法名的索引值，指向常量池的的字符串常量。
 descriptor_index：方法描述符的索引，指向常量池的字符串常量。
 attributes_count、attribute_info：表示方法相关属性的个数和属性集合，包含了很多有用的信息，比如方法内部的字节码就是存放在 Code 属性中。

 方法访问标记（access flags）
 方法的访问标记比类和字段的访问标记类型更丰富，有 12 种之多
 方法访问标记	值	描述
 ACC_PUBLIC	0x0001	声明为 public
 ACC_PRIVATE	0x0002	声明为 private
 ACC_PROTECTED	0x0004	声明为 protected
 ACC_STATIC	0x0008	声明为 static
 ACC_FINAL	0x0010	声明为 final
 ACC_SYNCHRONIZED	0x0020	声明为 synchronized
 ACC_BRIDGE	0x0040	bridge 方法, 由编译器生成
 ACC_VARARGS	0x0080	方法包含可变长度参数，比如 String... args
 ACC_NATIVE	0x0100	声明为 native
 ACC_ABSTRACT	0x0400	声明为 abstract
 ACC_STRICT	0x0800	声明为 strictfp，表示使用 IEEE-754 规范的精确浮点数，极少使用
 ACC_SYNTHETIC	0x1000	表示这个方法是由编译器自动生成，而不是用户代码编译产生

 方法名与描述符
 方法描述符索引 descriptor_index，它也是方法名指向常量池中类型为 CONSTANT_Utf8_info 的字符串常量项。方法描述符用来表示一个方法所需参数和返回值，格式为：
 (参数1类型 参数2类型 参数3类型 ...)返回值类型
 比如方法Object foo(int i, double d, Thread t)的描述符为(IDLjava/lang/Thread;)Ljava/lang/Object;

 方法属性表
 前面介绍了方法的访问标记、方法签名，还有一些重要的信息没有出现，如方法声明抛出的异常，方法的字节码，方法是否被标记为 deprecated，这些信息存在哪里呢？这就是方法属性表的作用。跟方法相关的属性有很多，其中重要的是 Code 和 Exceptions 属性，其中 Code 属性存放方法体的字节码指令，Exceptions属性 用于存储方法声明抛出的异常。

 8) 属性表
 在方法表之后的结构是 class 文件的最后一步部分属性表。属性出现的地方比较广泛，不止出现在字段和方法中，在顶层的 class 文件中也会出现。
 {
 u2             attributes_count;
 attribute_info attributes[attributes_count];
 }

 与其它结构类似，属性表使用两个字节表示属性的个数 attributes_count，接下来是若干个属性项的集合，可以看做是一个数组，数组的每一项都是一个属性项 attribute_info，数组的大小为 attributes_count。
 虚拟机预定义了 23 种属性，
 字段表相关的 ConstantValue 属性和方法表相关的 Code 属性。
 ConstantValue 属性
 ConstantValue_attribute {
 u2 attribute_name_index;
 u4 attribute_length;
 u2 constantvalue_index;
 }
 其中 attribute_name_index 是指向常量池中值为 "ConstantValue" 的常量项，ConstantValue 属性的 attribute_length 值恒定为 2，constantvalue_index 指向常量池中具体的常量值索引，根据变量的类型不同 constantvalue_index 指向不同的常量项。

 Code 属性
 Code 属性可以说是类文件中最重要的组成部分了，它包含了所有方法的字节码，结构如下：
 Code_attribute {
 u2 attribute_name_index;
 u4 attribute_length;
 u2 max_stack;
 u2 max_locals;
 u4 code_length;
 u1 code[code_length];
 u2 exception_table_length;
 {   u2 start_pc;
 u2 end_pc;
 u2 handler_pc;
 u2 catch_type;
 } exception_table[exception_table_length];
 u2 attributes_count;
 attribute_info attributes[attributes_count];
 }
 Code 属性表的字段含义如下：

 属性名索引（attribute_name_index）占两个字节，指向常量池中 CONSTANT_Utf8_info 常量，表示属性的名字，比如这里对应的常量池的字符串常量"Code"。

 属性长度（attribute_length）占用两个字节，表示属性值大小

 max_stack 表示操作数栈的最大深度，方法执行的任意期间操作数栈的深度都不会超过这个值。它的计算规则是有入栈的指令 stack 增加，有出栈的指令 stack 减少，在整个过程中 stack 的最大值就是 max_stack 的值，增加和减少的值一般都是 1，但也有例外：LONG 和 DOUBLE 相关的指令入栈 stack 会增加 2，VOID 相关的指令则为 0。

 max_locals 表示局部变量表的大小，它的值并不是等于方法中所有局部变量的数量之和。当一个局部作用域结束，它内部的局部变量占用的位置就可以被接下来的局部变量复用了。

 code_length 和 code 用来表示字节码相关的信息，其中 code_length 表示字节码指令的长度，占用 4 个字节。code 是一个长度为 code_length 的字节数组，存储真正的字节码指令。

 exception_table_length 和 exception_table 用来表示代码内部的异常表信息，如我们熟知的 try-catch 语法就会生成对应的异常表。

 attributes_count 和 attributes[] 用来表示 Code 属性相关的附属属性，Java 虚拟机规定 Code 属性只能包含这四种可选属性：LineNumberTable、LocalVariableTable、LocalVariableTypeTable、StackMapTable。以LineNumberTable 为例，LineNumberTable 用来存放源码行号和字节码偏移量之间的对应关系，这 LineNumberTable 属于调试信息，不是类文件运行的必需的属性，默认情况下都会生成。如果没有这两个属性，那么在调试时没有办法在源码中设置断点，也没有办法在代码抛出异常的时候在错误堆栈中显示出错的行号信息。



6.字节码概述
 Java 虚拟机的指令由一个字节长度的操作码（opcode）和紧随其后的可选的操作数（operand）构成。“字节码”这个名字的由来也是因为操作码的长度用一个字节表示。
 <opcode> [<operand1>, <operand2>]
 比如将整型常量 100 压栈到栈顶的指令是bipush 100，其中 bipush 就是操作码，100 就是操作数。
 因为操作码长度只有 1 个字节长度，这使得编译后的字节码文件非常小巧紧凑，但同时也直接限制了整个 JVM 操作码指令集的数量最多只能有 256 个，目前已经使用了 200+。
 大部分字节码指令都包含了所要操作的类型信息。比如ireturn 用于返回一个 int 类型的数据，dreturn 用于返回一个 double 类型的的数据，freturn 指令用于返回一个 float 类型的数据，这种方式也使得字节码实际的指令类型远小于 200 个。

 字节码使用大端序（Big-Endian）表示，即高位在前，低位在后的方式，比如字节码getfield 00 02，表示的是 getfiled 0x00<<8 | 0x02（getfield #2)。

 字节码并不是某种虚拟 CPU 的机器码，而是一种介于源码和机器码中间的一种抽象表示方法，不过字节码通过 JIT（Just in time）技术可以被进一步编译成机器码。









*/