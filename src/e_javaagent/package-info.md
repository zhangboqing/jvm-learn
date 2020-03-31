 ```
 Java Instrumentation 这个技术看起来非常神秘，很少有书会详细介绍。但是有很多工具是基于 Instrumentation 来实现的：

 APM 产品: pinpoint、skywalking、newrelic、听云的 APM 产品等都基于 Instrumentation 实现
 热部署工具：Intellij idea 的 HotSwap、Jrebel 等
 Java 诊断工具：Arthas、Btrace 等
 由于对字节码修改功能的巨大需求，JDK 从 JDK5 版本开始引入了java.lang.instrument 包。它可以通过 addTransformer 方法设置一个 ClassFileTransformer，可以在这个 ClassFileTransformer 实现类的转换。

 JDK 1.5 支持静态 Instrumentation，基本的思路是在 JVM 启动的时候添加一个代理（javaagent），每个代理是一个 jar 包，其 MANIFEST.MF 文件里指定了代理类，这个代理类包含一个 premain 方法。JVM 在类加载时候会先执行代理类的 premain 方法，再执行 Java 程序本身的 main 方法，这就是 premain 名字的来源。在 premain 方法中可以对加载前的 class 文件进行修改。这种机制可以认为是虚拟机级别的 AOP，无需对原有应用做任何修改，就可以实现类的动态修改和增强。

 从 JDK 1.6 开始支持更加强大的动态 Instrument，在JVM 启动后通过 Attach API 远程加载，后面会详细介绍。
```

 ### 1)Javaagent 介绍
 Javaagent 是一个特殊的 jar 包，它并不能单独启动的，而必须依附于一个 JVM 进程，可以看作是 JVM 的一个寄生插件，使用 Instrumentation 的 API 用来读取和改写当前 JVM 的类文件。

 #### Agent 的两种使用方式: 
 1.在 JVM 启动的时候加载，通过 javaagent 启动参数 java -javaagent:myagent.jar MyMain，这种方式在程序 main 方法执行之前执行 agent 中的 premain 方法
 public static void premain(String agentArgument, Instrumentation instrumentation) throws Exception
 2.在 JVM 启动后 Attach，通过 Attach API 进行加载，这种方式会在 agent 加载以后执行 agentmain 方法
 public static void agentmain(String agentArgument, Instrumentation instrumentation) throws Exception

 #### 这两个方法都有两个参数
 第一个 agentArgument 是 agent 的启动参数，可以在 JVM 启动命令行中设置，比如java -javaagent:<jarfile>=appId:agent-demo,agentType:singleJar test.jar的情况下 agentArgument 的值为 "appId:agent-demo,agentType:singleJar"。
 第二个 instrumentation 是 java.lang.instrument.Instrumentation 的实例，可以通过 addTransformer 方法设置一个 ClassFileTransformer。

 ### Agent 打包
 为了能够以 javaagent 的方式运行 premain 和 agentmain 方法，我们需要将其打包成 jar 包，并在其中的 MANIFEST.MF 配置文件中，指定 Premain-class 等信息，一个典型的生成好的 MANIFEST.MF 内容如下
 ```text
 Premain-Class: me.geek01.javaagent.AgentMain
 Agent-Class: me.geek01.javaagent.AgentMain
 Can-Redefine-Classes: true
 Can-Retransform-Classes: true
```

 下面是一个可以帮助生成上面 MANIFEST.MF 的 maven 配置
 ```xml
 <build>
 <finalName>my-javaagent</finalName>
 <plugins>
 <plugin>
 <groupId>org.apache.maven.plugins</groupId>
 <artifactId>maven-jar-plugin</artifactId>
 <configuration>
 <archive>
 <manifestEntries>
 <Agent-Class>me.geek01.AgentMain</Agent-Class>
 <Premain-Class>me.geek01.AgentMain</Premain-Class>
 <Can-Redefine-Classes>true</Can-Redefine-Classes>
 <Can-Retransform-Classes>true</Can-Retransform-Classes>
 </manifestEntries>
 </archive>
 </configuration>
 </plugin>
 </plugins>
 </build>
```

 ###Agent 使用方式一：JVM 启动参数：
#### 步骤1：你的java代码
```java
 public class MyJavaAgentTest {
 public static void main(String[] args) {
    new MyJavaAgentTest().foo();
    }
    public void foo() {
    bar1();
    bar2();
    }

    public void bar1() {
    }

    public void bar2() {
    }
 }
```
 #### 步骤2：创建AgentMain类
 ```java
 public class AgentMain {
 public static class MyMethodVisitor extends AdviceAdapter {

 protected MyMethodVisitor(MethodVisitor mv, int access, String name, String desc) {
 super(ASM7, mv, access, name, desc);
 }

 @Override
 protected void onMethodEnter() {
 mv.visitFieldInsn(GETSTATIC, "java/lang/System", "out", "Ljava/io/PrintStream;");
 mv.visitLdcInsn("<<<enter " + this.getName());
 mv.visitMethodInsn(INVOKEVIRTUAL, "java/io/PrintStream", "println", "(Ljava/lang/String;)V", false);
 super.onMethodEnter();
 }

 @Override
 protected void onMethodExit(int opcode) {
 super.onMethodExit(opcode);
 mv.visitFieldInsn(GETSTATIC, "java/lang/System", "out", "Ljava/io/PrintStream;");
 mv.visitLdcInsn(">>>exit " + this.getName());
 mv.visitMethodInsn(INVOKEVIRTUAL, "java/io/PrintStream", "println", "(Ljava/lang/String;)V", false);
 }
 }

 public static class MyClassVisitor extends ClassVisitor {

 public MyClassVisitor(ClassVisitor classVisitor) {
 super(ASM7, classVisitor);
 }

 @Override
 public MethodVisitor visitMethod(int access, String name, String descriptor, String signature, String[] exceptions) {
 MethodVisitor mv = super.visitMethod(access, name, descriptor, signature, exceptions);
 if (name.equals("<init>")) return mv;
 return new MyMethodVisitor(mv, access, name, descriptor);
 }
 }

 public static class MyClassFileTransformer implements ClassFileTransformer {
 @Override
 public byte[] transform(ClassLoader loader, String className, Class<?> classBeingRedefined,
 ProtectionDomain protectionDomain, byte[] bytes) throws IllegalClassFormatException {
 if (!"MyTest".equals(className)) return bytes;
 ClassReader cr = new ClassReader(bytes);
 ClassWriter cw = new ClassWriter(cr, ClassWriter.COMPUTE_FRAMES);
 ClassVisitor cv = new MyClassVisitor(cw);
 cr.accept(cv, ClassReader.SKIP_FRAMES | ClassReader.SKIP_DEBUG);
 return cw.toByteArray();
 }
 }

 public static void premain(String agentArgs, Instrumentation inst) throws ClassNotFoundException, UnmodifiableClassException {
 inst.addTransformer(new MyClassFileTransformer(), true);
 }
 }
```