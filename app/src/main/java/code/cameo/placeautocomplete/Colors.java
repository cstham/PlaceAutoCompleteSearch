package code.cameo.placeautocomplete;

import android.graphics.Color;

public class Colors {

    //if factor > 1.0f = lighter color, if factor < 1.0f = darker color, if factor = 1.0f, no changes to color
    public static int manipulateColor(int color, float factor) {
        int a = Color.alpha(color);
        int r = Math.round(Color.red(color) * factor);
        int g = Math.round(Color.green(color) * factor);
        int b = Math.round(Color.blue(color) * factor);
        return Color.argb(a,
                Math.min(r,255),
                Math.min(g,255),
                Math.min(b,255));
    }

}
