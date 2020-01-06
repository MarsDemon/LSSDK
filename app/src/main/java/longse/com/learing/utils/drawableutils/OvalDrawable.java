package longse.com.learing.utils.drawableutils;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.Shader.TileMode;
import android.graphics.drawable.BitmapDrawable;

public class OvalDrawable extends BitmapDrawable {

    private Paint mPaint;

    public OvalDrawable(Resources res, Bitmap bitmap) {
        BitmapShader bitmapShader = new BitmapShader(bitmap, Shader.TileMode.CLAMP, TileMode.CLAMP);
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setShader(bitmapShader);
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
        int width = getBitmap().getWidth();
        int height = getBitmap().getHeight();
        RectF oval = new RectF(0, 0, width, height);
        canvas.drawOval(oval, mPaint);
    }
}
