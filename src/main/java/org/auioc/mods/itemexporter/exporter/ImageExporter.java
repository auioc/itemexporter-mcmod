package org.auioc.mods.itemexporter.exporter;

import org.auioc.mods.arnicalib.utils.java.FileUtils;
import org.auioc.mods.itemexporter.config.IEConfig;
import org.auioc.mods.itemexporter.renderer.IEItemRenderer;
import net.minecraft.world.item.Item;

public class ImageExporter {

    public static void export(Item item) {
        int size = IEConfig.IMAGE_SIZE.get();

        var file = "export/image/" + size + "/" + item.getRegistryName().getPath() + ".png";

        var image = IEItemRenderer.render(item, size);
        try {
            image.writeToFile(FileUtils.getFile(file));
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            image.close();
        }
    }

}
