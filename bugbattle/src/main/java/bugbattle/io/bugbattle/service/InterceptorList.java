package bugbattle.io.bugbattle.service;

import android.view.View;

import java.util.ArrayList;

public class InterceptorList extends ArrayList<View> {

    @Override
    public boolean add(View view) {
        DecorateView.infiltrateFor(view);
        return super.add(view);
    }
}