package com.jiawei.read.bestlibs.maptest;

/*
* 自定义HashMap
* 实现了put方法增加键值对，并解决了键重复时覆盖相应的节点
*
* */

public class MyHashMap01 {

    private Node2[] table;

    public MyHashMap01(){
        table=new Node2[16];
    }

    public void put(Object key,Object value){
        Node2 newNode=new Node2();
        newNode.hash=myHash(key.hashCode(),table.length);
        newNode.key=key;
        newNode.value=value;
        newNode.next=null;


        Node2 temp=table[newNode.hash];
        Node2 lastItem=null;
        boolean keyRepeat=false;
        if (temp==null){
            table[newNode.hash]=newNode;
        }else {

            while (temp!=null){
                //重复的key值要覆盖
                if (key.equals(temp.key)){
                    keyRepeat=true;
                    temp.value=value;
                    break;
                }else {
                    lastItem=temp;
                    temp=temp.next;
                }

            }

            if (!keyRepeat){
                lastItem.next=newNode;
            }
        }


    }

    public static void main(String[] args){
        MyHashMap01 hashMap01=new MyHashMap01();
        hashMap01.put(1,"one");
        hashMap01.put(2,"two");
        hashMap01.put(3,"three");

        //测试是否覆盖重复的key
        hashMap01.put(1,"first");


        hashMap01.put(53,"wusan");
        hashMap01.put(69,"liujiu");
        hashMap01.put(85,"bawu");

        System.out.println(hashMap01);

    }

    public int myHash(int hashCode,int length){
        return hashCode&(length-1);
    }


}
