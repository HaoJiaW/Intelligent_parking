package com.jiawei.read.bestlibs;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class SerializableTest {

    public static void main(String arg[]){
        User user=new User("haojiawei",9,true);
        try {
            ObjectOutputStream objectOutputStream=new ObjectOutputStream(new FileOutputStream("user.txt"));
            objectOutputStream.writeObject(user);
            objectOutputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            ObjectInputStream objectInputStream=new ObjectInputStream(new FileInputStream("user.txt"));
            User newUser= (User) objectInputStream.readObject();
            System.out.println("name:"+newUser.name+",userId:"+newUser.userId+",isMale:"+newUser.isMale);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }




}
