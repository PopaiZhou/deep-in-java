/**
 * fshows.com
 * Copyright (C) 2013-2020 All Rights Reserved.
 */
package com.study;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * @author zhoujp
 * @version MyClassLoader.java, v 0.1 2020-11-03 16:02 zhoujp
 */
public class MyClassLoader extends ClassLoader{
    private String classpath;

    public MyClassLoader( String classpath){
        this.classpath = classpath;
    }

    @Override
    protected Class<?> findClass(String name) throws ClassNotFoundException {
        String fileName = getClassFile(name);
        byte[] classByte=null;
        classByte = loadClassData(fileName);
        //利用自身的加载器加载类
        Class retClass = defineClass( null,classByte , 0 , classByte.length);
        if( retClass != null ) {
            System.out.println("自定义加载器加载:");
            return retClass;
        }
        //否者使用默认加载器
        return super.findClass(name);
    }

    private byte[] loadClassData(String name) {
        //类文件的全路径
        InputStream in = null;
        ByteArrayOutputStream out = null;
        try {
            //读取字节码文件中的字节流
            in = new FileInputStream(new File(name));
            out = new ByteArrayOutputStream();
            int i = 0;
            while((i = in.read()) != -1) {
                out.write(255-i);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // 保证资源的释放
            try {
                out.close();
                in.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
        return out.toByteArray();
    }

    private String getClassFile ( String name ){
        StringBuilder sb = new StringBuilder(classpath);
        sb.append("/").append(name.replace('.','/')).append(".xlass");
        return sb.toString();
    }
    public static void main ( String [] args ) throws ClassNotFoundException {
        MyClassLoader myClassLoader = new MyClassLoader("F://JAVA//week1");
        try {
            myClassLoader.loadClass("Hello");
        }catch ( ClassNotFoundException e ){
            e.printStackTrace();
        }
    }
}