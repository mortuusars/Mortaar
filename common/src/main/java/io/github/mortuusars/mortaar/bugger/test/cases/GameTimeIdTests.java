package io.github.mortuusars.mortaar.bugger.test.cases;

import com.google.gson.JsonPrimitive;
import com.mojang.serialization.JsonOps;
import io.github.mortuusars.mortaar.bugger.test.BuggerTests;
import io.github.mortuusars.mortaar.bugger.test.Test;
import io.github.mortuusars.mortaar.util.GameTimeId;

public class GameTimeIdTests extends BuggerTests {
    public GameTimeIdTests() {
        add("GTID_UniqueIfSameTick", Test.isTrue(player -> {
            GameTimeId first = GameTimeId.createChecked(1337L, false);
            GameTimeId second = GameTimeId.createChecked(1337L, false);
            return first.getTick() == second.getTick()
                  && first.getSuffix() < second.getSuffix();
        }));

        add("GTID_EncodesWithoutSuffix", Test.equals("7B", player ->
              GameTimeId.CODEC.encodeStart(JsonOps.INSTANCE, GameTimeId.createChecked(123L, false)).getOrThrow().getAsString()));
        add("GTID_EncodesNewOnTheSameTickWithSuffix", Test.equals("7B-1", player ->
              GameTimeId.CODEC.encodeStart(JsonOps.INSTANCE, GameTimeId.createChecked(123L, false)).getOrThrow().getAsString()));

        add("GTID_DecodesWithoutSuffix", Test.equals(GameTimeId.createUncheked(999), player ->
              GameTimeId.CODEC.decode(JsonOps.INSTANCE, new JsonPrimitive("3E7")).getOrThrow().getFirst()));
        add("GTID_DecodesWithSuffix", Test.equals(GameTimeId.createUncheked(999, 123), player ->
              GameTimeId.CODEC.decode(JsonOps.INSTANCE, new JsonPrimitive("3E7-7B")).getOrThrow().getFirst()));
        add("GTID_DecodingThrowsForDashAndEmptySuffix", Test.throwsException(IllegalStateException.class, player ->
              GameTimeId.CODEC.decode(JsonOps.INSTANCE, new JsonPrimitive("3E7-")).getOrThrow()));
        add("GTID_DecodesFromDecimalWithoutSuffix", Test.equals(GameTimeId.createUncheked(999), player ->
              GameTimeId.CODEC_DECIMAL.decode(JsonOps.INSTANCE, new JsonPrimitive("999")).getOrThrow().getFirst()));
        add("GTID_DecodesFromDecimalWithSuffix", Test.equals(GameTimeId.createUncheked(949487, 123123), player ->
              GameTimeId.CODEC_DECIMAL.decode(JsonOps.INSTANCE, new JsonPrimitive("949487-123123")).getOrThrow().getFirst()));
    }
}
