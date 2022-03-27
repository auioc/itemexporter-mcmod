package org.auioc.mods.itemexporter;

import org.auioc.mods.itemexporter.command.IEClientCommands;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.MinecraftForge;

@OnlyIn(Dist.CLIENT)
public final class Initialization {

    public static void init() {
        MinecraftForge.EVENT_BUS.addListener(IEClientCommands::register);
    }

}
