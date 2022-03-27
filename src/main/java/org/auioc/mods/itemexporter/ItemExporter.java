package org.auioc.mods.itemexporter;

import org.apache.commons.lang3.tuple.Pair;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.Marker;
import org.auioc.mods.arnicalib.utils.LogUtil;
import org.auioc.mods.arnicalib.utils.java.VersionUtils;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.IExtensionPoint;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;

@Mod(ItemExporter.MOD_ID)
public final class ItemExporter {

    public static final String MOD_ID = "itemexporter";
    public static final String MOD_NAME = "ItemExporter";
    public static final String MAIN_VERSION;
    public static final String FULL_VERSION;

    public static final Logger LOGGER = LogUtil.getLogger(MOD_NAME);
    private static final Marker CORE = LogUtil.getMarker("CORE");

    public ItemExporter() {
        ModLoadingContext.get().registerExtensionPoint(
            IExtensionPoint.DisplayTest.class,
            () -> new IExtensionPoint.DisplayTest(() -> "ANY", (remote, isServer) -> isServer)
        );

        DistExecutor.safeRunWhenOn(
            Dist.CLIENT,
            () -> new DistExecutor.SafeRunnable() {
                @Override
                public void run() {
                    Initialization.init();
                }
            }
        );
    }

    static {
        Pair<String, String> version = VersionUtils.getModVersion(ItemExporter.class);
        MAIN_VERSION = version.getLeft();
        FULL_VERSION = version.getRight();
        LOGGER.info(CORE, "Version: " + MAIN_VERSION + " (" + FULL_VERSION + ")");
    }

    public static ResourceLocation id(String path) {
        return new ResourceLocation(MOD_ID, path);
    }

    public static String i18n(String key) {
        return MOD_ID + "." + key;
    }

}
