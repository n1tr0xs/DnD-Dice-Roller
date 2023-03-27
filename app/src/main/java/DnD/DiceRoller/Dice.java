package DnD.DiceRoller;

import android.annotation.SuppressLint;

import java.util.Random;

public class Dice {
    private final int max;
    private final Random random;

    Dice(int m) {
        random = new Random();
        max = m;
    }


    public int[] roll(int d, boolean adv, boolean dis) {
        int[] rolls = new int[d];
        for (int i = 0; i < d; ++i)
            if (adv)
                rolls[i] = Math.max(random.nextInt(max) + 1, random.nextInt(max) + 1);
            else
                rolls[i] = Math.min(random.nextInt(max) + 1, random.nextInt(max) + 1);
        return rolls;
    }

    public int[] roll(int d) {
        int[] rolls = new int[d];
        for (int i = 0; i < d; ++i)
            rolls[i] = random.nextInt(max) + 1;
        return rolls;
    }

    public int[] roll() {
        return roll(1);
    }

    @SuppressLint("DefaultLocale")
    public String getName() {
        return String.format("d%d", max);
    }
}