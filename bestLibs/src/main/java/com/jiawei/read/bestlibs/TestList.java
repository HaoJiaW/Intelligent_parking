package com.jiawei.read.bestlibs;

/*
 * 测试Collection接口中的方法
 *
 * */

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.logging.Handler;

public class TestList {

    public static void main(String[] args) {

//        testLastIndex();
//        addList();
       // checkList();

    }

    public static void testList() {
        Collection<String> cc = new ArrayList<>();

        cc.size();
        cc.isEmpty();
        Object[] objs = cc.toArray();

        cc.clear();
        cc.contains("test");
        cc.remove("nihao");
    }

    public static void testMulList() {
        List<String> list01 = new ArrayList<>();
        list01.add("aa");
        list01.add("bb");
        list01.add("cc");

        List<String> list02 = new ArrayList<>();
        list02.add("aa");
        list02.add("dd");
        list02.add("ee");

        System.out.println("list01：" + list01);

        //list01.addAll(list02);
        // list01.removeAll(list02);
        //list01.retainAll(list02);
        list01.containsAll(list02);

        System.out.println("list01：" + list01);

    }

    public static void testLastIndex() {
        Collection<String> list = new ArrayList<String>();
        list.add("AA");
        list.add("BB");
        list.add("CC");

        System.out.println(list);

        ((ArrayList<String>) list).add(0, "nihao");
        System.out.println(list);

        ((ArrayList<String>) list).set(3, "nihao");
        System.out.println(list);

        ((ArrayList<String>) list).remove(0);
        System.out.println(list);

        System.out.println(((ArrayList<String>) list).get(0));

        ((ArrayList<String>) list).add("AA");
        ((ArrayList<String>) list).add("BB");
        ((ArrayList<String>) list).add("AA");

        //indexOf:返回指定元素在list中第一次出现的位置索引，如果不存在返回-1
        System.out.println(((ArrayList<String>) list).indexOf("AA"));
        //lastIndexOf:返回指定元素在list中最后一次出现的位置索引，如果不存在返回-1
        System.out.println(((ArrayList<String>) list).lastIndexOf("AA"));

        //  JSONStringer jsonText=new JSONStringer();

    }

    public static List<String> list = new ArrayList<>();

//    public static void addList() {
//        while (System.currentTimeMillis()%10000==0){
//            Long time=System.currentTimeMillis();
//            System.out.println(""+time);
//            list.add("" + time);
//            if (time%20==0){
//                for (int i = 0; i < list.size(); i++) {
//                    System.out.println(list.get(i));
//                }
//            }
//        }
//    }
//
//    public static void checkList() {
//            for (int i = 0; i < list.size(); i++) {
//                System.out.println(list.get(i));
//            }
//    }

}
