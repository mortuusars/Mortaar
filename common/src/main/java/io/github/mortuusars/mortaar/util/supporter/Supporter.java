package io.github.mortuusars.mortaar.util.supporter;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.Util;
import net.minecraft.core.UUIDUtil;

import java.util.List;
import java.util.UUID;

public record Supporter(String name, UUID uuid, boolean active) {
    public static final Codec<Supporter> CODEC = RecordCodecBuilder.create(i -> i.group(
          Codec.STRING.fieldOf("name").forGetter(Supporter::name),
          UUIDUtil.STRING_CODEC.fieldOf("uuid").forGetter(Supporter::uuid),
          Codec.BOOL.optionalFieldOf("active", false).forGetter(Supporter::active)
    ).apply(i, Supporter::new));

    public static final Codec<List<Supporter>> LIST_CODEC = Supporter.CODEC.listOf();

    public boolean matches(UUID uuid) {
        if (uuid.equals(Util.NIL_UUID)) return false;
        return uuid().equals(uuid);
    }
}
