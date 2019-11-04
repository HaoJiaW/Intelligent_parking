package com.jiawei.read.bestlibs.LinkedList;

/*
 *
 * 自定义一个链表
 * 新增get()方法
 * */
public class MyLinkedList02 {

    private Node first;
    private Node last;
    private int size;

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
        size++;
    }

    public Object get(int index){
        if (index<0 || index>size-1){
            throw new RuntimeException("当前索引不合法");
        }

        Node temp=null;

        if (index>=size>>1){
            //size>>1 相当于 size/2
            //从屁股开始循环
            temp=last;
            for (int i=size-1;i>index;i--){
                temp=last.previous;
            }
        }else {
            temp=first;
            for (int i=0;i<index;i++){
                temp=temp.next;
            }
        }

        return temp.object;

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
        MyLinkedList02 linkedList=new MyLinkedList02();
        linkedList.add("a");
        linkedList.add("b");
        linkedList.add("c");
        linkedList.add("d");
        linkedList.add("e");
        linkedList.add("f");
        linkedList.add("f1");
        linkedList.add("f2");
        linkedList.add("f3");
        linkedList.add("f4");
        linkedList.add("f5");
        linkedList.add("f6");

        System.out.println(linkedList.toString());

        System.out.println(linkedList.get(10));
    }


}
