package com.howlstudio.pets;
import java.util.UUID;
public class Pet {
    public enum Type { CAT, DOG, FOX, WOLF, PARROT, RABBIT }
    private final UUID ownerUuid;
    private String name;
    private Type type;
    private int level;
    private long xp;
    private static final long[] XP_THRESHOLDS={0,100,250,500,1000,2000,5000,10000,20000,50000};
    public Pet(UUID ownerUuid,String name,Type type){this.ownerUuid=ownerUuid;this.name=name;this.type=type;this.level=1;this.xp=0;}
    public UUID getOwnerUuid(){return ownerUuid;}
    public String getName(){return name;} public void setName(String n){name=n;}
    public Type getType(){return type;}
    public int getLevel(){return level;} public long getXp(){return xp;}
    public boolean addXp(long amount){xp+=amount;if(level<10&&xp>=XP_THRESHOLDS[Math.min(level,9)]){level++;return true;}return false;}
    public long xpToNextLevel(){return level>=10?0:XP_THRESHOLDS[level]-xp;}
    public String getDisplay(){return "§6[Lv"+level+"] §r"+name+" ("+type.name().toLowerCase()+")";}
    public String toConfig(){return ownerUuid+"|"+name+"|"+type.name()+"|"+level+"|"+xp;}
    public static Pet fromConfig(String s){String[]p=s.split("\\|",5);if(p.length<5)return null;Pet pet=new Pet(UUID.fromString(p[0]),p[1],Type.valueOf(p[2]));try{pet.level=Integer.parseInt(p[3]);pet.xp=Long.parseLong(p[4]);}catch(Exception e){}return pet;}
}
