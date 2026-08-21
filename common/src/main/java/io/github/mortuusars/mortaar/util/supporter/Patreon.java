package io.github.mortuusars.mortaar.util.supporter;

import com.mojang.logging.LogUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;

import java.net.URI;
import java.util.*;

public class Patreon {
    private static final Logger LOGGER = LogUtils.getLogger();

    private long lastQueryTime = -1L;
    private @Nullable Map<Tier, List<Supporter>> patrons = null;

    public boolean canQuery() {
        return System.currentTimeMillis() - lastQueryTime > 60000; // 1 min
    }

    public @NotNull Map<Tier, List<Supporter>> getOrQuery() {
        if (patrons != null) return patrons;
        if (!canQuery()) return Collections.emptyMap();
        return query();
    }

    public @NotNull Map<Tier, List<Supporter>> query() {
        lastQueryTime = System.currentTimeMillis();
        try {
            Supporters.Loader loader = new Supporters.Loader();

            if (patrons != null) {
                patrons.clear();
            }

            new Thread(() -> {
                for (Tier patreonTier : Tier.values()) {
                    String json = loader.readFileFromURL(patreonTier.getUuidsUri());
                    if (json == null) return;

                    List<Supporter> parsedSupporters = loader.parseSupporters(json);

                    if (patrons == null) {
                        patrons = new HashMap<>();
                    }

                    patrons.put(patreonTier, parsedSupporters);
                }
            }).start();
        } catch (Exception e) {
            LOGGER.warn("Cannot get list of supporters.", e);
        }

        if (patrons == null) {
            return Collections.emptyMap();
        }

        return patrons;
    }

    // --

    public boolean isCurrentlyOnOrGreater(UUID uuid, Tier tier) {
        Map<Tier, List<Supporter>> tiers = getOrQuery();
        for (Tier t : tier.getCurrentAndAbove()) {
            @Nullable List<Supporter> supporters = tiers.get(t);
            if (supporters != null
                  && !supporters.isEmpty()
                  && supporters.stream().anyMatch(s -> s.active() && s.matches(uuid))) {
                return true;
            }
        }

        return false;
    }

    public boolean isOrWasOnOrGreater(UUID uuid, Tier tier) {
        Map<Tier, List<Supporter>> tiers = getOrQuery();
        for (Tier t : tier.getCurrentAndAbove()) {
            @Nullable List<Supporter> supporters = tiers.get(t);
            if (supporters != null
                  && !supporters.isEmpty()
                  && supporters.stream().anyMatch(s -> s.matches(uuid))) {
                return true;
            }
        }

        return false;
    }

    public enum Tier {
        COPPER,
        IRON,
        GOLD,
        DIAMOND;

        public URI getUuidsUri() {
            return URI.create("https://raw.githubusercontent.com/mortuusars/resources/refs/heads/main/supporters/patreon/"
                    + name().toLowerCase() + ".json");
        }

        public EnumSet<Tier> getCurrentAndAbove() {
            return EnumSet.range(this, DIAMOND);
        }
    }
}
