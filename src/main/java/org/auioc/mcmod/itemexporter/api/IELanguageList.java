package org.auioc.mcmod.itemexporter.api;

import java.util.HashMap;
import java.util.Map;
import com.mojang.brigadier.suggestion.SuggestionProvider;
import org.auioc.mcmod.arnicalib.utils.game.LanguageUtils;
import net.minecraft.client.resources.language.ClientLanguage;
import net.minecraft.client.resources.language.LanguageInfo;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.SharedSuggestionProvider;

public class IELanguageList {

    private final Map<String, ClientLanguage> map = new HashMap<String, ClientLanguage>() {
        {
            put("en_us", LanguageUtils.getLanguage(LanguageUtils.DEFAULT_LANGUAGE));
            put("zh_cn", LanguageUtils.getLanguage("zh_cn"));
        }
    };

    public boolean add(LanguageInfo langInfo) {
        if (this.map.containsKey(langInfo.getCode())) {
            return false;
        }
        this.map.put(langInfo.getCode(), LanguageUtils.getLanguage(langInfo));
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
