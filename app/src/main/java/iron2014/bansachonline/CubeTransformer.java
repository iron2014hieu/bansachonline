package iron2014.bansachonline;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.ViewPager;

public class CubeTransformer implements ViewPager.PageTransformer {
    @Override
    public void transformPage(@NonNull View page, float position) {
        //if position = 0 (current image) pivot on x axis is on the right, else if
        // position > 0, (next image) pivot on x axis is on the left (origin of the axis)
        page.setPivotX(position <= 0 ? page.getWidth() : 0.0f);
        page.setPivotY(page.getHeight() * 0.5f);

        //it rotates with 90 degrees multiplied by current position
        page.setRotationY(20f * position);
    }
}
