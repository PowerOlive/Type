/*
 * Copyright 2016 Matthew Lee
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */

package io.github.mthli.type.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import io.github.mthli.type.R;

public class DotsView extends View {
    private int dotColor;
    private int dotSize;
    private int dotSpacing;

    private Paint paint;

    public DotsView(Context context) {
        super(context);
        init(null);
    }

    public DotsView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public DotsView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    private void init(AttributeSet attrs) {
        if (attrs == null) {
            return;
        }

        TypedArray array = getContext().obtainStyledAttributes(attrs, R.styleable.DotsView);
        dotColor = array.getColor(R.styleable.DotsView_dotColor, 0);
        dotSize = array.getDimensionPixelSize(R.styleable.DotsView_dotSize, 0);
        dotSpacing = array.getDimensionPixelSize(R.styleable.DotsView_dotSpacing, 0);
        array.recycle();

        paint = new Paint();
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(dotColor);
    }

    @Override
    public void onDraw(Canvas canvas) {
        if (dotColor == 0 || dotSize == 0 || dotSpacing == 0) {
            return;
        }

        float centerX = canvas.getWidth() / 2;
        float centerY = canvas.getHeight() / 2;

        centerX -= dotSpacing;
        canvas.drawOval(centerX - dotSize / 2, centerY - dotSize / 2, centerX + dotSize / 2, centerY + dotSize / 2, paint);

        centerX += dotSpacing;
        canvas.drawOval(centerX - dotSize / 2, centerY - dotSize / 2, centerX + dotSize / 2, centerY + dotSize / 2, paint);

        centerX += dotSpacing;
        canvas.drawOval(centerX - dotSize / 2, centerY - dotSize / 2, centerX + dotSize / 2, centerY + dotSize / 2, paint);
    }
}
