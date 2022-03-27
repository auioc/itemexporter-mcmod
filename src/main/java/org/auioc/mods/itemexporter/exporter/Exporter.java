package org.auioc.mods.itemexporter.exporter;

import java.util.stream.Stream;
import net.minecraft.world.item.Item;

public class Exporter {

    public static void export(Stream<Item> items) {
        items.forEach((item) -> {
            export(item);
        });
    }

    public static void export(Item item) {
        ImageExporter.export(item);
        JsonExporter.export(item);
    }

}
