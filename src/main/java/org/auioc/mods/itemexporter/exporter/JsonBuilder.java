package org.auioc.mods.itemexporter.exporter;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import org.auioc.mods.itemexporter.config.IEConfig;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;

public class JsonBuilder {

    public static JsonObject buildItemJson(Item item) {
        var json = new JsonObject();

        json.addProperty("registry_name", item.getRegistryName().toString());
        json.add("display_name", buildDisplayNameJson(item));
        json.add("tags", buildTagJson(item));
        json.add("creative_tabs", buildCreativeTabJson(item));

        return json;
    }

    private static JsonObject buildDisplayNameJson(Item item) {
        var json = new JsonObject();

        IEConfig.LANGUAGES.get().forEach((langCode, clientLanguage) -> {
            json.addProperty(langCode, clientLanguage.getOrDefault(item.getDescriptionId()));
        });

        return json;
    }

    @SuppressWarnings("deprecation")
    private static JsonArray buildTagJson(Item item) {
        var json = new JsonArray();

        item.builtInRegistryHolder().tags()
            .map(TagKey::location)
            .filter((tag) -> IEConfig.MINECRAFT_TAG_ONLY.get() ? tag.getNamespace().equals("minecraft") : true)
            .map(ResourceLocation::toString)
            .forEach(json::add);

        return json;
    }

    private static JsonArray buildCreativeTabJson(Item item) {
        var json = new JsonArray();

        item.getCreativeTabs()
            .stream()
            .filter((tab) -> tab != null)
            .map((tab) -> (TranslatableComponent) tab.getDisplayName())
            .map(TranslatableComponent::getKey)
            .map((name) -> name.replaceFirst("^itemGroup\\.", ""))
            .forEach(((name) -> json.add(name)));

        return json;
    }

}
