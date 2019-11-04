package com.jiawei.read.bestlibs;


//抽象类练习

/*
* 抽象类的使用要点：
* 1.有抽象方法的类只能定义成抽象类
* 2.抽象类不能被实例化，即不能被new
* 3.抽象类只能被继承
* 4.抽象方法必须被子类实现
* */

//抽象类的意义就在于：为子类提供统一的、规范的模范。子类必须实现相关的抽象方法。

public abstract class chouxianglei {

    //抽象方法特点：1.没有实现 2.子类必须实现
    public abstract void shout();

    public void run(){
        System.out.println("跑啊跑！！");
    }

}

class Dog extends chouxianglei{

    @Override
    public void shout() {
        System.out.println("汪汪汪！！");
    }
}

