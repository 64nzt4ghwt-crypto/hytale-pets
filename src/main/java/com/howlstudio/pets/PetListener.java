package com.howlstudio.pets;
import com.hypixel.hytale.server.core.HytaleServer;
import com.hypixel.hytale.server.core.Message;
import com.hypixel.hytale.server.core.entity.entities.Player;
import com.hypixel.hytale.server.core.event.events.player.PlayerReadyEvent;
import com.hypixel.hytale.server.core.universe.PlayerRef;
public class PetListener {
    private final PetManager mgr;
    public PetListener(PetManager m){this.mgr=m;}
    public void register(){
        HytaleServer.get().getEventBus().registerGlobal(PlayerReadyEvent.class,e->{
            Player p=e.getPlayer();if(p==null)return;
            PlayerRef ref=p.getPlayerRef();if(ref==null)return;
            Pet pet=mgr.getPet(ref.getUuid());
            if(pet!=null){boolean leveled=mgr.addXp(ref.getUuid(),10);ref.sendMessage(Message.raw("[Pet] §6"+pet.getName()+"§r is happy you're back! "+pet.getDisplay()));if(leveled)ref.sendMessage(Message.raw("[Pet] §a"+pet.getName()+" leveled up to Lv"+pet.getLevel()+"! ★"));}
        });
    }
}
