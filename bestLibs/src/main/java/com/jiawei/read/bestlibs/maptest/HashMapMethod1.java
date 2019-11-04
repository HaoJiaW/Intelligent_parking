package com.jiawei.read.bestlibs.maptest;

import java.util.HashMap;
import java.util.Map;

public class HashMapMethod1 {

    public static void main(String[] args){
        Map<Integer,String> m1=new HashMap<>();

        //put()
        m1.put(1,"one");
        m1.put(2,"two");
        m1.put(3,"three");
        m1.put(4,"four");

        //get()
        System.out.println(m1.get(1));

        //remove(key)
        m1.remove(1);
        System.out.println(m1);

        System.out.println(m1.containsKey(1));
        System.out.println(m1.containsValue("two"));

        Map<Integer,String> m2=new HashMap<>();
        m2.put(5,"五");

        m1.putAll(m2);

        System.out.println(m1);

        System.out.println(m1.size());
        System.out.println(m1.isEmpty());

        m1.clear();

        System.out.println("d".hashCode());
    }

}
