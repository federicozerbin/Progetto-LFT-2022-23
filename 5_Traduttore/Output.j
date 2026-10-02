.class public Output 
.super java/lang/Object

.method public <init>()V
 aload_0
 invokenonvirtual java/lang/Object/<init>()V
 return
.end method

.method public static print(I)V
 .limit stack 2
 getstatic java/lang/System/out Ljava/io/PrintStream;
 iload_0 
 invokestatic java/lang/Integer/toString(I)Ljava/lang/String;
 invokevirtual java/io/PrintStream/println(Ljava/lang/String;)V
 return
.end method

.method public static read()I
 .limit stack 3
 new java/util/Scanner
 dup
 getstatic java/lang/System/in Ljava/io/InputStream;
 invokespecial java/util/Scanner/<init>(Ljava/io/InputStream;)V
 invokevirtual java/util/Scanner/next()Ljava/lang/String;
 invokestatic java/lang/Integer.parseInt(Ljava/lang/String;)I
 ireturn
.end method

.method public static run()V
 .limit stack 1024
 .limit locals 256
 ldc 4
 istore 0
 goto L1
L1:
 iload 0
 invokestatic Output/print(I)V
 goto L3
L3:
 invokestatic Output/read()I
 istore 1
 goto L5
L5:
 invokestatic Output/read()I
 istore 2
 goto L6
L6:
L8:
 iload 1
 iload 2
 if_icmplt L9
 goto L7
L9:
 iload 1
 iload 2
 if_icmpgt L14
 goto L13
L14:
 iload 1
 iload 2
 isub 
 istore 1
 goto L17
L17:
L13:
 iload 1
 iload 2
 isub 
 istore 2
 goto L21
L21:
L12:
 goto L8
L7:
 iload 2
 iload 1
 iload 2
 ldc 1000
 imul 
 invokestatic Output/print(I)V
 goto L25
L25:
 goto L0
L0:
 return
.end method

.method public static main([Ljava/lang/String;)V
 invokestatic Output/run()V
 return
.end method

