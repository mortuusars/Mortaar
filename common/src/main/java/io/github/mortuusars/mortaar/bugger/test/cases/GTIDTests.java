package io.github.mortuusars.mortaar.bugger.test.cases;

import com.google.gson.JsonPrimitive;
import com.mojang.serialization.JsonOps;
import io.github.mortuusars.mortaar.bugger.test.BuggerTests;
import io.github.mortuusars.mortaar.bugger.test.Test;
import io.github.mortuusars.mortaar.util.GTID;

public class GTIDTests extends BuggerTests {
    public GTIDTests() {
        add("GTID_UniqueIfSameTick", Test.isTrue(player -> {
            GTID first = GTID.createChecked(1337L, false);
            GTID second = GTID.createChecked(1337L, false);
            return first.getTick() == second.getTick()
                  && first.getSuffix() < second.getSuffix();
        }));

        add("GTID_EncodesWithoutSuffix", Test.equals("7B", player ->
              GTID.CODEC.encodeStart(JsonOps.INSTANCE, GTID.createChecked(123L, false)).getOrThrow().getAsString()));
        add("GTID_EncodesNewOnTheSameTickWithSuffix", Test.equals("7B-1", player ->
              GTID.CODEC.encodeStart(JsonOps.INSTANCE, GTID.createChecked(123L, false)).getOrThrow().getAsString()));

        add("GTID_DecodesWithoutSuffix", Test.equals(GTID.createUncheked(999), player ->
              GTID.CODEC.decode(JsonOps.INSTANCE, new JsonPrimitive("3E7")).getOrThrow().getFirst()));
        add("GTID_DecodesWithSuffix", Test.equals(GTID.createUncheked(999, 123), player ->
              GTID.CODEC.decode(JsonOps.INSTANCE, new JsonPrimitive("3E7-7B")).getOrThrow().getFirst()));
        add("GTID_DecodingThrowsForDashAndEmptySuffix", Test.throwsException(IllegalStateException.class, player ->
              GTID.CODEC.decode(JsonOps.INSTANCE, new JsonPrimitive("3E7-")).getOrThrow()));
        add("GTID_DecodesFromDecimalWithoutSuffix", Test.equals(GTID.createUncheked(999), player ->
              GTID.CODEC_DECIMAL.decode(JsonOps.INSTANCE, new JsonPrimitive("999")).getOrThrow().getFirst()));
        add("GTID_DecodesFromDecimalWithSuffix", Test.equals(GTID.createUncheked(949487, 123123), player ->
              GTID.CODEC_DECIMAL.decode(JsonOps.INSTANCE, new JsonPrimitive("949487-123123")).getOrThrow().getFirst()));
    }
}
