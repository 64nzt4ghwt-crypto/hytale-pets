package com.howlstudio.pets;
import com.hypixel.hytale.component.Ref; import com.hypixel.hytale.component.Store;
import com.hypixel.hytale.server.core.Message;
import com.hypixel.hytale.server.core.command.system.CommandContext;
import com.hypixel.hytale.server.core.command.system.basecommands.AbstractPlayerCommand;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import com.hypixel.hytale.server.core.universe.world.World;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import java.nio.file.*; import java.util.*;
public class PetManager {
    private final Path dataDir;
    private final Map<UUID,Pet> pets=new HashMap<>();
    public PetManager(Path d){this.dataDir=d;try{Files.createDirectories(d);}catch(Exception e){}load();}
    public int getPetCount(){return pets.size();}
    public Pet getPet(UUID uid){return pets.get(uid);}
    public void adoptPet(UUID uid,String name,Pet.Type type){pets.put(uid,new Pet(uid,name,type));save();}
    public void releasePet(UUID uid){pets.remove(uid);save();}
    public boolean addXp(UUID uid,long xp){Pet p=pets.get(uid);if(p==null)return false;return p.addXp(xp);}
    public void save(){try{StringBuilder sb=new StringBuilder();for(Pet p:pets.values())sb.append(p.toConfig()).append("\n");Files.writeString(dataDir.resolve("pets.txt"),sb.toString());}catch(Exception e){}}
    private void load(){try{Path f=dataDir.resolve("pets.txt");if(!Files.exists(f))return;for(String l:Files.readAllLines(f)){Pet p=Pet.fromConfig(l);if(p!=null)pets.put(p.getOwnerUuid(),p);}}catch(Exception e){}}
    public AbstractPlayerCommand getPetCommand(){
        return new AbstractPlayerCommand("pet","Manage your pet companion. /pet adopt <name> <type>|info|rename <name>|release|types"){
            @Override protected void execute(CommandContext ctx,Store<EntityStore> store,Ref<EntityStore> ref,PlayerRef playerRef,World world){
                UUID uid=playerRef.getUuid();String[]args=ctx.getInputString().trim().split("\\s+",3);
                String sub=args.length>0?args[0].toLowerCase():"info";
                switch(sub){
                    case"adopt"->{if(args.length<3){playerRef.sendMessage(Message.raw("Usage: /pet adopt <name> <type> — see /pet types"));break;}
                        if(getPet(uid)!=null){playerRef.sendMessage(Message.raw("[Pet] You already have a pet! /pet release first."));break;}
                        try{Pet.Type t=Pet.Type.valueOf(args[2].toUpperCase());adoptPet(uid,args[1],t);playerRef.sendMessage(Message.raw("[Pet] §6"+args[1]+"§r the "+args[2].toLowerCase()+" is now your companion! ♡"));}
                        catch(Exception e){playerRef.sendMessage(Message.raw("[Pet] Unknown type. /pet types"));}}
                    case"info"->{Pet p=getPet(uid);if(p==null){playerRef.sendMessage(Message.raw("[Pet] No pet. /pet adopt <name> <type>"));break;}playerRef.sendMessage(Message.raw("=== Your Pet ==="));playerRef.sendMessage(Message.raw("  "+p.getDisplay()));playerRef.sendMessage(Message.raw("  XP: "+p.getXp()+" | To next level: "+(p.getLevel()<10?p.xpToNextLevel()+"xp":"MAX")));}
                    case"rename"->{if(args.length<2)break;Pet p=getPet(uid);if(p==null)break;p.setName(args[1]);save();playerRef.sendMessage(Message.raw("[Pet] Renamed to §6"+args[1]+"§r!"));}
                    case"release"->{if(getPet(uid)==null){playerRef.sendMessage(Message.raw("[Pet] No pet to release."));break;}releasePet(uid);playerRef.sendMessage(Message.raw("[Pet] Your pet has been released. Farewell..."));}
                    case"types"->playerRef.sendMessage(Message.raw("[Pet] Types: "+Arrays.stream(Pet.Type.values()).map(t->t.name().toLowerCase()).reduce((a,b)->a+", "+b).orElse("")));
                    default->playerRef.sendMessage(Message.raw("Usage: /pet adopt|info|rename|release|types"));
                }
            }
        };
    }
}
