package com.jiawei.read.bestlibs.LinkedList;
/*
*
* 自定义一个链表
*
* */
public class MyLinkedList01 {

    private Node first;
    private Node last;

    public void add(Object obj){
        Node node=new Node(obj);
        if (first==null){
            first=node;
            last=node;
        }else {
            node.previous=last;
            node.next=null;

            last.next=node;
            last=node;
        }
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder=new StringBuilder();
        stringBuilder.append("[");
        Node temp=first;
        while (temp!=null){
            stringBuilder.append(temp.object+",");
            temp=temp.next;
        }
        stringBuilder.setCharAt(stringBuilder.length()-1,"]".toCharArray()[0]);
        return stringBuilder.toString();
    }

    public static void main(String[] args){
        MyLinkedList01 linkedList=new MyLinkedList01();
        linkedList.add("a");
        linkedList.add("b");
        linkedList.add("c");
        linkedList.add("d");
        linkedList.add("e");
        linkedList.add("f");

        System.out.println(linkedList.toString());
    }


}
