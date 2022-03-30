package org.auioc.mods.itemexporter.exporter;

import java.util.function.Function;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import org.auioc.mods.itemexporter.config.IEConfig;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public class JsonBuilder {

    public static JsonObject buildItemJson(Item item) {
        var json = new JsonObject();

        json.addProperty("registry_name", item.getRegistryName().toString());
        if (IEConfig.JSON_INCLUDE_DISPLAY_NAME.get()) {
            json.add("display_name", buildDisplayNameJson(item));
        }
        if (IEConfig.JSON_INCLUDE_TAG.get()) {
            json.add("tags", buildTagJson(item));
        }
        if (IEConfig.JSON_INCLUDE_PROPERTIES.get()) {
            json.add("properties", buildPropertiesJson(item));
        }

        return json;
    }

    private static JsonObject buildDisplayNameJson(Item item) {
        var json = new JsonObject();

        IEConfig.DISPLAY_NAME_LANGUAGE.get().forEach((langCode, clientLanguage) -> {
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

    @SuppressWarnings("deprecation")
    private static JsonObject buildPropertiesJson(Item item) {
        var json = new JsonObject();

        json.add("creative_mode_tabs", ((Function<Item, JsonArray>) (_item) -> {
            var arr = new JsonArray();
            _item.getCreativeTabs()
                .stream()
                .filter((tab) -> tab != null)
                .map((tab) -> (TranslatableComponent) tab.getDisplayName())
                .map(TranslatableComponent::getKey)
                .map((name) -> name.replaceFirst("^itemGroup\\.", ""))
                .forEach(arr::add);
            return arr;
        }).apply(item));
        json.addProperty("max_stack_size", item.getMaxStackSize());
        json.addProperty("max_damage", item.getMaxDamage());
        json.addProperty("rarity", item.getRarity(new ItemStack(item)).toString().toLowerCase());

        return json;
    }

}
