package dev.linhnv.fbee.fragments;

import android.support.v4.app.Fragment;

import com.hlab.fabrevealmenu.view.FABRevealMenu;

/**
 * Created by DevLinhnv on 1/7/2017.
 */

public class BaseFragment extends Fragment {
    public boolean onBackPressed() {
        if (fabMenu != null) {
            if (fabMenu.isShowing()) {
                fabMenu.closeMenu();
                return false;
            }
        }
        return true;
    }

    private FABRevealMenu fabMenu;

    public FABRevealMenu getFabMenu() {
        return fabMenu;
    }

    public void setFabMenu(FABRevealMenu fabMenu) {
        this.fabMenu = fabMenu;
    }
}
