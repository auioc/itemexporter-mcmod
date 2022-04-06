package org.auioc.mcmod.itemexporter.config;

import org.auioc.mcmod.arnicalib.api.java.holder.BooleanHolder;
import org.auioc.mcmod.arnicalib.api.java.holder.IntegerHolder;
import org.auioc.mcmod.itemexporter.api.IELanguageList;

public final class IEConfig {

    public static final BooleanHolder EXPORT_JSON = new BooleanHolder(true);

    public static final BooleanHolder EXPORT_JSON_TO_STDOUT = new BooleanHolder(false);

    public static final BooleanHolder JSON_INCLUDE_TAG = new BooleanHolder(true);
    public static final BooleanHolder MINECRAFT_TAG_ONLY = new BooleanHolder(true);

    public static final BooleanHolder JSON_INCLUDE_DISPLAY_NAME = new BooleanHolder(true);
    public static final IELanguageList DISPLAY_NAME_LANGUAGE = new IELanguageList();

    public static final BooleanHolder JSON_INCLUDE_PROPERTIES = new BooleanHolder(true);

    // ====================================================================== //

    public static final BooleanHolder EXPORT_IMAGE = new BooleanHolder(true);
    public static final IntegerHolder IMAGE_SIZE = new IntegerHolder(16);

}
