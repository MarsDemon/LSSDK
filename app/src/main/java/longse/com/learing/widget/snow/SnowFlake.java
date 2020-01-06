package longse.com.learing.widget.snow;

import android.graphics.Paint;
import android.graphics.Point;

import java.util.Random;

public class SnowFlake {
    private static final float ANGLE_RANGE = 0.1f;
    private static final float HALF_ANGLE_RANGE = ANGLE_RANGE / 2f;
    private static final float HALF_PI = (float) (Math.PI / 2f);
    private static final float ANGLE_SEED = 25f;
    private static final float ANGLE_DIVISOR = 10000f;
    private static final float INCREMWNT_LOWER = 2f;
    private static final float INCREMWNT_UPPER = 4f;
    private static final float FLAKE_SIZE_LOWER = 7f;
    private static final float FLAKE_SIZE_UPPER = 20f;

    private final Random random;
    private final Point position;
    private float angle;
    private final float increment;

    public SnowFlake(Random random, Point position, float angle, float increment) {
        this.random = random;
        this.position = position;
        this.angle = angle;
        this.increment = increment;
    }
}
