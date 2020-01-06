package longse.com.learing.utils.drawableutils;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Shader.TileMode;
import android.graphics.drawable.BitmapDrawable;

/**
 * @author LY
 * 圆角图片剪裁
 */
public class RoundDrawable extends BitmapDrawable {

    private Paint mPaint;
    private RectF mRectF;
    private int mCornerRadius = 10;

    public RoundDrawable (Resources resources, Bitmap bitmap) {
        BitmapShader bitmapShader = new BitmapShader(bitmap, TileMode.CLAMP, TileMode.CLAMP);
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setShader(bitmapShader);
    }

    public void setCornerRadius(int corner_radius) {
        this.mCornerRadius = corner_radius;
    }

    public int getCornerRadius() {
        return mCornerRadius;
    }

    @Override
    public void setBounds(int left, int top, int right, int bottom) {
        super.setBounds(left, top, right, bottom);
        mRectF = new RectF(left, top, right, bottom);
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
        canvas.drawRoundRect(mRectF, mCornerRadius, mCornerRadius, mPaint);
    }
}
