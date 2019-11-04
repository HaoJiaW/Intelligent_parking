package com.jiawei.read.bestlibs;

public class MyArrayList<T> {
    private Object[] elementData;
    private int size;

    private static final int DEFALT_CAPACITY=10;

    public MyArrayList(){

        elementData=new Object[DEFALT_CAPACITY];
    }

    public MyArrayList(int capacity){
        if (capacity<0){
            throw new RuntimeException("容器的容量不能为负数");
        }else if (capacity==0){
            elementData=new Object[DEFALT_CAPACITY];
        }else {
            elementData=new Object[capacity];
        }
    }

    public void add(T element){

        //进行扩容
        if (size == elementData.length){
            //创建一个新数组(对旧数组的扩容)
            Object[] newArray=new Object[elementData.length+(elementData.length>>1)];
            //复制旧数组的内容给新数组
            System.arraycopy(elementData,0,newArray,0,elementData.length);
            //更新旧数组
            elementData = newArray;
        }

        elementData[size++] = element;
    }


    private void remove(T element){

        for (int i=0;i<size;i++){
            if (element.equals(elementData[i])){
                remove(i);
            }
        }

    }

    public void remove(int index){

        int moveNum=elementData.length-index-1;
        if (moveNum>0){
            System.arraycopy(elementData,index+1,elementData,index,moveNum);
            //            elementData[size-1]=null;
           //            size--;
            elementData[--size]=null;
        }else {
//            elementData[size-1]=null;
//            size-=1;
            //简化
            elementData[--size]=null;
        }

    }

    public int size(){
        return size;
    }

    public boolean isEmpty(){
        return size==0?true:false;
    }


    @Override
    public String toString() {

        StringBuilder stringBuilder=new StringBuilder();
        stringBuilder.append("[");
        for (int i=0;i<size;i++){
            stringBuilder.append(elementData[i]+",");
        }
        stringBuilder.setCharAt(stringBuilder.length()-1,"]".toCharArray()[0]);

        return stringBuilder.toString();

    }

    public T get(int index){

        checkRange(index);

        return (T) elementData[index];
    }

    public void set(T element,int index){

        checkRange(index);

        elementData[index]=element;
    }

    private void checkRange(int index){
        //索引合法判断
        if (index<0||index>elementData.length-1){
            throw new RuntimeException("索引不合法："+index);
        }
    }


    public static void main(String[] args){
        MyArrayList<String> myArrayList=new MyArrayList(10);
//        myArrayList.add("AA");
//        myArrayList.add("BB");

        for (int i=0;i<40;i++){
            myArrayList.add("hao"+i);
        }


        myArrayList.set("dddd",14);
        System.out.println(myArrayList);
        System.out.println(myArrayList.get(12));

        myArrayList.remove(3);
        System.out.println(myArrayList);

        System.out.println(myArrayList.size());
        System.out.println(myArrayList.isEmpty());
    }
}
