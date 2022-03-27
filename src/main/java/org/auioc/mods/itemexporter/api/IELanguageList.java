package org.auioc.mods.itemexporter.api;

import java.util.HashMap;
import java.util.Map;
import com.mojang.brigadier.suggestion.SuggestionProvider;
import org.auioc.mods.arnicalib.utils.game.LanguageUtils;
import net.minecraft.client.resources.language.ClientLanguage;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.SharedSuggestionProvider;

public class IELanguageList {

    private final Map<String, ClientLanguage> map = new HashMap<String, ClientLanguage>() {
        {
            put("en_us", LanguageUtils.getLanguage("en_us"));
            put("zh_cn", LanguageUtils.getLanguage("zh_cn"));
        }
    };

    public boolean add(String langCode) {
        if (this.map.containsKey(langCode)) {
            return false;
        }
        this.map.put(langCode, LanguageUtils.getLanguage(langCode));
        return true;
    }

    public boolean remove(String langCode) {
        var result = this.map.remove(langCode);
        return result == null ? false : true;
    }

    public Map<String, ClientLanguage> get() {
        return this.map;
    }

    public SuggestionProvider<CommandSourceStack> getCommandSuggestion() {
        return (ctx, builder) -> {
            return SharedSuggestionProvider.suggest(this.map.keySet(), builder);
        };
    }

}
