package com.jiawei.read.bestlibs;


/*
* 测试泛型
*我们可以在类的声明处增加泛型列表，如<T,E,V>
* */

public class FanXingTest {

    public static void main(String[] args){
        MyCollection<String> collection=new MyCollection<String>();
        collection.set("nihao",0);
        collection.set("1",1);

     //   Integer a=(Integer) collection.get(1);
        String b=(String) collection.get(0);

    }
}

class MyCollection<T>{
    Object[] obj=new Object[5];

    public void set(T o,int index){
        obj[index]=0;
    }

    public T get(int index){
        return (T)obj[index];
    }

}
