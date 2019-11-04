package com.github.jiawei.intelligent_parking_system.activity;

import android.os.Parcel;
import android.os.Parcelable;

public class Person implements Parcelable {

    public int id;
    public String name;

    public Person(int id, String name) {
        this.id = id;
        this.name = name;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(name);
    }

    public static final Parcelable.Creator<Person> CREATOR=new Parcelable.Creator<Person>(){

        @Override
        public Person createFromParcel(Parcel source) {
            return new Person(source);
        }

        @Override
        public Person[] newArray(int size) {
            return new Person[size];
        }
    };

    private Person(Parcel parcel){
        id=parcel.readInt();
        name=parcel.readString();
    }
}
