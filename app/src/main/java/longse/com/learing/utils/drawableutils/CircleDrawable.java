package longse.com.learing.utils.drawableutils;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;

import android.graphics.drawable.BitmapDrawable;
import android.graphics.Shader.TileMode;

/**
 * @author LY
 * 圆形剪裁图片
 */
public class CircleDrawable extends BitmapDrawable {

    private Paint mPaint;

    public CircleDrawable(Resources res, Bitmap bitmap) {
        BitmapShader bitmapShader = new BitmapShader(bitmap, TileMode.CLAMP, TileMode.CLAMP);
        mPaint = new Paint();
        mPaint.setAntiAlias(true);  // 抗锯齿
        mPaint.setShader(bitmapShader);
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
        int width = getBitmap().getWidth();
        int height = getBitmap().getHeight();
        int radius = Math.min(width, height) / 2;
        int x_pos = (width > radius + radius) ? width / 2 : radius;
        int y_pos = (height > radius + radius) ? height / 2 : radius;

        canvas.drawCircle(x_pos, y_pos, radius, mPaint);
    }
}
