package com.example.zsoltvarga.hydrate;


public class Badge {
    private static boolean unlocked;
    private static int id;

    Badge(){
        unlocked = false;
    }

    public static void unlockBadge(){
        unlocked = true;
    }
    public static void lockBadge(){
        unlocked = false;
    }
    public static boolean getBadgeStatus(){

        return unlocked;
    }

    public static void setID(int num){

        id = num;
    }

    public static int getID(){

        return id;
    }
}
