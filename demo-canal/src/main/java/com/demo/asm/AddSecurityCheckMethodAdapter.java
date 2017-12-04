package com.demo.asm;

//import org.springframework.asm.MethodAdapter;
//import org.springframework.asm.MethodVisitor;
//import org.springframework.asm.Opcodes;

import org.objectweb.asm.MethodAdapter;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

class AddSecurityCheckMethodAdapter extends MethodAdapter { 
    public AddSecurityCheckMethodAdapter(MethodVisitor mv) { 
        super(mv); 
    } 
 
    public void visitCode() { 
        visitMethodInsn(Opcodes.INVOKESTATIC, "SecurityChecker", 
           "checkSecurity", "()V"); 
    } 
}
