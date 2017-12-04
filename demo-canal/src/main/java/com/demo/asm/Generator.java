package com.demo.asm;

import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintWriter;

//import org.springframework.asm.ClassAdapter;
//import org.springframework.asm.ClassReader;
//import org.springframework.asm.ClassWriter;

import org.objectweb.asm.ClassAdapter;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;

import jdk.internal.org.objectweb.asm.util.CheckClassAdapter;
import jdk.internal.org.objectweb.asm.util.TraceClassVisitor; 
    
public class Generator{ 
    public static void main() throws Exception { 
        ClassReader cr = new ClassReader("Account"); 
        ClassWriter cw = new ClassWriter(ClassWriter.COMPUTE_MAXS);
        ClassAdapter classAdapter = new AddSecurityCheckClassAdapter(cw); 
        cr.accept(classAdapter, ClassReader.SKIP_DEBUG); 
        byte[] data = cw.toByteArray(); 
        File file = new File("Account.class"); 
        FileOutputStream fout = new FileOutputStream(file); 
        fout.write(data); 
        fout.close(); 
    } 
}