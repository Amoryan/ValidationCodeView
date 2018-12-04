package com.fxyan.validationcodeview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.SparseArray;
import android.util.TypedValue;
import android.view.View;
import android.view.WindowManager;

/**
 * @author fxYan
 */
public final class ValidationCodeView extends View {

    private int itemCount;
    private float itemWidth;
    private float itemHeight;
    private float itemDistance;

    private int borderStyle;
    private float borderWidth;
    private float borderRadius;

    private float contentSize;
    private int contentStatus;
    private float contentRadiusWhenIsHidden;

    private int completedBorderColor;
    private int unCompleteBorderColor;
    private int completedContentColor;
    private int unCompleteContentColor;

    private Paint paint;
    private Paint.FontMetrics fontMetrics;
    private RectF itemRect;
    private Rect drawTextBounds;
    private RectF textIsHiddenRect;
    private SparseArray<String> content;

    public ValidationCodeView(Context context) {
        super(context);
        init(null);
    }

    public ValidationCodeView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    private void init(AttributeSet attrs) {
        configDefaultAttrs();

        loadXmlAttrs(attrs);

        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setTextSize(contentSize);
        paint.setStrokeWidth(borderWidth);
        fontMetrics = paint.getFontMetrics();

        itemRect = new RectF();
        drawTextBounds = new Rect();
        textIsHiddenRect = new RectF();

        content = new SparseArray<>();
        content.put(content.size(), "0");
        content.put(content.size(), "1");
        content.put(content.size(), "2");
        content.put(content.size(), "3");
    }

    private void configDefaultAttrs() {
        WindowManager wm = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics metrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(metrics);

        itemCount = 4;
        itemWidth = itemHeight = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 40F, metrics);
        itemDistance = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 10F, metrics);

        borderStyle = BorderStyle.RECTANGLE;
        borderWidth = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 1F, metrics);
        borderRadius = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 0F, metrics);

        contentSize = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 14F, metrics);
        contentStatus = ContentStatus.SHOW;
        contentRadiusWhenIsHidden = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 4F, metrics);

        completedContentColor = completedBorderColor = Color.parseColor("#1b8fe6");
        unCompleteContentColor = unCompleteBorderColor = Color.parseColor("#e5e5e5");
    }

    private void loadXmlAttrs(AttributeSet attrs) {
        if (attrs == null) return;

        TypedArray array = getContext().obtainStyledAttributes(attrs, R.styleable.ValidationCodeView);
        if (array.hasValue(R.styleable.ValidationCodeView_vcvItemCount)) {
            itemCount = array.getInt(R.styleable.ValidationCodeView_vcvItemCount, itemCount);
        }
        if (array.hasValue(R.styleable.ValidationCodeView_vcvItemWidth)) {
            itemWidth = array.getDimension(R.styleable.ValidationCodeView_vcvItemWidth, itemWidth);
        }
        if (array.hasValue(R.styleable.ValidationCodeView_vcvItemHeight)) {
            itemHeight = array.getDimension(R.styleable.ValidationCodeView_vcvItemHeight, itemHeight);
        }
        if (array.hasValue(R.styleable.ValidationCodeView_vcvItemDistance)) {
            itemDistance = array.getDimension(R.styleable.ValidationCodeView_vcvItemDistance, itemDistance);
        }

        if (array.hasValue(R.styleable.ValidationCodeView_vcvBorderWidth)) {
            borderWidth = array.getDimension(R.styleable.ValidationCodeView_vcvBorderWidth, borderWidth);
        }
        if (array.hasValue(R.styleable.ValidationCodeView_vcvBorderStyle)) {
            borderStyle = array.getInt(R.styleable.ValidationCodeView_vcvBorderStyle, borderStyle);
        }
        if (array.hasValue(R.styleable.ValidationCodeView_vcvBorderRadius)) {
            borderRadius = array.getDimension(R.styleable.ValidationCodeView_vcvBorderRadius, borderRadius);
        }

        if (array.hasValue(R.styleable.ValidationCodeView_vcvContentSize)) {
            contentSize = array.getDimension(R.styleable.ValidationCodeView_vcvContentSize, contentSize);
        }
        if (array.hasValue(R.styleable.ValidationCodeView_vcvContentStatus)) {
            contentStatus = array.getInt(R.styleable.ValidationCodeView_vcvContentStatus, contentStatus);
        }
        if (array.hasValue(R.styleable.ValidationCodeView_vcvContentRadiusWhenIsHidden)) {
            contentRadiusWhenIsHidden = array.getDimension(R.styleable.ValidationCodeView_vcvContentRadiusWhenIsHidden, contentRadiusWhenIsHidden);
        }

        if (array.hasValue(R.styleable.ValidationCodeView_vcvCompletedBorderColor)) {
            completedBorderColor = array.getColor(R.styleable.ValidationCodeView_vcvCompletedBorderColor, completedBorderColor);
        }
        if (array.hasValue(R.styleable.ValidationCodeView_vcvUnCompleteBorderColor)) {
            unCompleteBorderColor = array.getColor(R.styleable.ValidationCodeView_vcvUnCompleteBorderColor, unCompleteBorderColor);
        }
        if (array.hasValue(R.styleable.ValidationCodeView_vcvCompletedContentColor)) {
            completedContentColor = array.getColor(R.styleable.ValidationCodeView_vcvCompletedContentColor, completedContentColor);
        }
        if (array.hasValue(R.styleable.ValidationCodeView_vcvUnCompleteContentColor)) {
            unCompleteContentColor = array.getColor(R.styleable.ValidationCodeView_vcvUnCompleteContentColor, unCompleteContentColor);
        }
        array.recycle();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int wMode = MeasureSpec.getMode(widthMeasureSpec);
        int wSize = MeasureSpec.getSize(widthMeasureSpec);
        if (wMode == MeasureSpec.AT_MOST) {
            wSize = (int) Math.floor(getWrapContentWidth() + 0.5);
        }

        int hMode = MeasureSpec.getMode(heightMeasureSpec);
        int hSize = MeasureSpec.getSize(heightMeasureSpec);
        if (hMode == MeasureSpec.AT_MOST) {
            hSize = (int) Math.floor(getWrapContentHeight() + 0.5);
        }

        setMeasuredDimension(wSize, hSize);
    }

    private float getWrapContentWidth() {
        int hPadding = getPaddingStart() + getPaddingEnd();
        float totalItemWidth = itemWidth * itemCount;
        float totalItemDistance = 0F;
        if (itemCount > 0) {
            totalItemDistance = itemDistance * (itemCount - 1);
        }
        return hPadding + totalItemWidth + totalItemDistance + borderWidth;
    }

    private float getWrapContentHeight() {
        int vPadding = getPaddingTop() + getPaddingBottom();
        return vPadding + itemHeight + borderWidth;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        float fixGravityCenterWidth = 0F;
        if (getWidth() > getWrapContentWidth()) {
            fixGravityCenterWidth = (getWidth() - getWrapContentWidth()) / 2;
        }
        float fixGravityCenterHeight = 0F;
        if (getHeight() > getWrapContentHeight()) {
            fixGravityCenterHeight = (getHeight() - getWrapContentHeight()) / 2;
        }

        for (int i = 0; i < itemCount; i++) {
            float leftItemWidth = itemWidth * i;
            float leftItemDistance = itemDistance * i;
            itemRect.left = fixGravityCenterWidth + getPaddingStart() + borderWidth / 2 + leftItemWidth + leftItemDistance;
            itemRect.top = fixGravityCenterHeight + getPaddingTop() + borderWidth / 2;
            itemRect.right = itemRect.left + itemWidth;
            itemRect.bottom = itemRect.top + itemHeight;

            if (i < content.size()) {
                drawItemBorder(canvas, completedBorderColor);
                drawItemContent(canvas, content.get(i));
            } else {
                drawItemBorder(canvas, unCompleteBorderColor);
            }
        }
    }

    private void drawItemBorder(Canvas canvas, int color) {
        paint.setColor(color);
        paint.setStyle(Paint.Style.STROKE);
        if (borderStyle == BorderStyle.RECTANGLE) {
            canvas.drawRoundRect(itemRect, borderRadius, borderRadius, paint);
        } else if (borderStyle == BorderStyle.LINE) {
            canvas.drawLine(itemRect.left, itemRect.bottom, itemRect.right, itemRect.bottom, paint);
        }
    }

    private void drawItemContent(Canvas canvas, String tmp) {
        paint.setColor(completedBorderColor);
        paint.setStyle(Paint.Style.FILL);
        if (contentStatus == ContentStatus.SHOW) {
            paint.getTextBounds(tmp, 0, tmp.length(), drawTextBounds);
            float xOffset = (itemRect.left + itemRect.right - drawTextBounds.width()) / 2;
            float yOffset = itemRect.top + itemHeight / 2 + fontMetrics.descent;
            canvas.drawText(tmp, xOffset, yOffset, paint);
        } else if (contentStatus == ContentStatus.HIDE) {
            float centerX = (itemRect.left + itemRect.right) / 2;
            float centerY = (itemRect.top + itemRect.bottom) / 2;
            textIsHiddenRect.left = centerX - contentRadiusWhenIsHidden;
            textIsHiddenRect.top = centerY - contentRadiusWhenIsHidden;
            textIsHiddenRect.right = centerX + contentRadiusWhenIsHidden;
            textIsHiddenRect.bottom = centerY + contentRadiusWhenIsHidden;
            canvas.drawOval(textIsHiddenRect, paint);
        }
    }

    public static class BorderStyle {
        public static final int RECTANGLE = 0;
        public static final int LINE = 1;
    }

    public static class ContentStatus {
        public static final int SHOW = 0;
        public static final int HIDE = 1;
    }

}
