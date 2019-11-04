package com.jiawei.read.bestlibs.maptest;


/*
*重写toString方法
* */
public class MyHashMap02 {

    private Node2[] table;

    public MyHashMap02(){
        table=new Node2[16];
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("{");

        for (int i=0;i<table.length;i++){
            Node2 temp=table[i];

            while (temp!=null){
                sb.append(""+temp.key+":"+temp.value+"("+temp.hash+")"+",");
                temp=temp.next;
            }
        }

        sb.setCharAt(sb.length()-1,"}".toCharArray()[0]);

        return sb.toString();
    }

    public void put(Object key, Object value){
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
        MyHashMap02 hashMap02=new MyHashMap02();
        hashMap02.put(1,"one");
        hashMap02.put(2,"two");
        hashMap02.put(3,"three");

        //测试是否覆盖重复的key
        hashMap02.put(1,"first");


        hashMap02.put(53,"wusan");
        hashMap02.put(69,"liujiu");
        hashMap02.put(85,"bawu");

        System.out.println(hashMap02);

    }

    public int myHash(int hashCode,int length){
        return hashCode&(length-1);
    }


}
