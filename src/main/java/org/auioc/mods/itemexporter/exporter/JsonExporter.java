package org.auioc.mods.itemexporter.exporter;

import java.io.IOException;
import org.auioc.mods.arnicalib.utils.java.FileUtils;
import net.minecraft.world.item.Item;

public class JsonExporter {

    public static void export(Item item) {
        var fileName = "export/json/" + item.getRegistryName().getPath() + ".json";

        var jsonText = JsonBuilder.buildItemJson(item).toString();

        try {
            FileUtils.writeText(fileName, jsonText);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
