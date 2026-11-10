package com.uragiri.keys;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.scoreboard.ReadableScoreboardScore;
import net.minecraft.scoreboard.Scoreboard;
import net.minecraft.scoreboard.ScoreboardObjective;

/**
 * なりすまし判定。サーバー(データパック)がタブリスト用の目的 "imp" に
 * 「自分のID + 1000 x 相手のID」を入れておく。数値は blank 表示なので画面には出ない。
 */
public final class Impersonation {
    private static final String OBJECTIVE = "imp";

    private Impersonation() {}

    private static int score(Scoreboard sb, ScoreboardObjective obj, PlayerEntity p) {
        ReadableScoreboardScore s = sb.getScore(p, obj);
        return s == null ? 0 : s.getScore();
    }

    /** selfがなりすましている相手を返す。なりすましていなければ null。 */
    public static AbstractClientPlayerEntity findTarget(AbstractClientPlayerEntity self) {
        MinecraftClient mc = MinecraftClient.getInstance();
        if (mc.world == null) {
            return null;
        }
        Scoreboard sb = mc.world.getScoreboard();
        ScoreboardObjective obj = sb.getNullableObjective(OBJECTIVE);
        if (obj == null) {
            return null;
        }
        int s = score(sb, obj, self);
        if (s < 1000) {
            return null;
        }
        int want = s / 1000;
        for (AbstractClientPlayerEntity q : mc.world.getPlayers()) {
            if (q == self) {
                continue;
            }
            int qs = score(sb, obj, q);
            if (qs > 0 && qs < 1000 && qs == want) {
                return q;
            }
        }
        return null;
    }
}
