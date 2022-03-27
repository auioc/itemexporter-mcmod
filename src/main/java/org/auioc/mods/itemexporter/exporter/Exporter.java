package org.auioc.mods.itemexporter.exporter;

import static org.auioc.mods.itemexporter.ItemExporter.LOGGER;
import java.io.IOException;
import java.util.stream.Stream;
import org.apache.logging.log4j.Marker;
import org.auioc.mods.arnicalib.utils.LogUtil;
import org.auioc.mods.arnicalib.utils.java.FileUtils;
import org.auioc.mods.itemexporter.config.IEConfig;
import org.auioc.mods.itemexporter.renderer.IEItemRenderer;
import net.minecraft.world.item.Item;

public class Exporter {

    private static final Marker MARKER = LogUtil.getMarker(Exporter.class);

    private static void exportJson(Item item) {
        var fileName = "export/json/" + item.getRegistryName().getPath() + ".json";

        var jsonText = JsonBuilder.buildItemJson(item).toString();

        try {
            FileUtils.writeText(fileName, jsonText);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void exportImage(Item item) {
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

    public static void export(Item item) {
        exportImage(item);
        exportJson(item);
    }

    public static void export(Stream<Item> items) {
        items.forEach((item) -> export(item));
    }

}
