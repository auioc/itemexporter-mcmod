package org.auioc.mcmod.itemexporter;

import org.auioc.mcmod.itemexporter.command.IEClientCommands;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.MinecraftForge;

@OnlyIn(Dist.CLIENT)
public final class Initialization {

    public static void init() {
        MinecraftForge.EVENT_BUS.addListener(IEClientCommands::register);
    }

}
