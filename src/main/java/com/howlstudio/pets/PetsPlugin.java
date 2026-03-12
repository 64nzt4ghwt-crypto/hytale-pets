package com.howlstudio.pets;
import com.hypixel.hytale.server.core.command.system.CommandManager;
import com.hypixel.hytale.server.core.plugin.JavaPlugin;
import com.hypixel.hytale.server.core.plugin.JavaPluginInit;
/** PetSystem — Cosmetic companion pets that level up and display with players. */
public final class PetsPlugin extends JavaPlugin {
    private PetManager mgr;
    public PetsPlugin(JavaPluginInit init){super(init);}
    @Override protected void setup(){
        System.out.println("[Pets] Loading...");
        mgr=new PetManager(getDataDirectory());
        new PetListener(mgr).register();
        CommandManager.get().register(mgr.getPetCommand());
        System.out.println("[Pets] Ready. "+mgr.getPetCount()+" pets active.");
    }
    @Override protected void shutdown(){if(mgr!=null)mgr.save();System.out.println("[Pets] Stopped.");}
}
