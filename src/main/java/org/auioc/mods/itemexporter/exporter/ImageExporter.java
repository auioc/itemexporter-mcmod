package org.auioc.mods.itemexporter.exporter;

import static org.auioc.mods.itemexporter.ItemExporter.LOGGER;
import org.apache.logging.log4j.Marker;
import org.auioc.mods.arnicalib.utils.LogUtil;
import org.auioc.mods.arnicalib.utils.java.FileUtils;
import org.auioc.mods.itemexporter.config.IEConfig;
import org.auioc.mods.itemexporter.renderer.IEItemRenderer;
import net.minecraft.world.item.Item;

public class ImageExporter {

    private static final Marker MARKER = LogUtil.getMarker(ImageExporter.class);

    public static void export(Item item) {
        int size = IEConfig.IMAGE_SIZE.get();

        var fileName = "export/image/" + size + "/" + item.getRegistryName().getPath() + ".png";

        var image = IEItemRenderer.render(item, size);
        try {
            var file = FileUtils.getFile(fileName);
            if (file.exists()) {
                LOGGER.warn(MARKER, "File \"" + file + "\" already exists, overwrite");
            }
            image.writeToFile(file);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            image.close();
        }
    }

}
