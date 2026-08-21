package io.github.mortuusars.mortaar.util.supporter;

import com.mojang.logging.LogUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;

import java.net.URI;
import java.util.Collections;
import java.util.List;

public class Gilded {
    private static final Logger LOGGER = LogUtils.getLogger();

    private long lastQueryTime = -1L;
    private @Nullable List<Supporter> gildedSupporters = null;

    public boolean canQuery() {
        return System.currentTimeMillis() - lastQueryTime > 60000; // 1 min
    }

    public @NotNull List<Supporter> getOrQuery() {
        if (gildedSupporters != null) return gildedSupporters;
        if (!canQuery()) return Collections.emptyList();
        return query();
    }

    public @NotNull List<Supporter> query() {
        lastQueryTime = System.currentTimeMillis();
        try {
            Supporters.Loader loader = new Supporters.Loader();

            new Thread(() -> {
                String json = loader.readFileFromURL(getUuidsUri());
                if (json == null) return;
                gildedSupporters = loader.parseSupporters(json);
            }).start();
        } catch (Exception e) {
            LOGGER.warn("Cannot get list of supporters.", e);
        }

        if (gildedSupporters == null) {
            return Collections.emptyList();
        }

        return gildedSupporters;
    }

    protected URI getUuidsUri() {
        return URI.create("https://raw.githubusercontent.com/mortuusars/resources/refs/heads/main/supporters/uuids/gilded.json");
    }
}
