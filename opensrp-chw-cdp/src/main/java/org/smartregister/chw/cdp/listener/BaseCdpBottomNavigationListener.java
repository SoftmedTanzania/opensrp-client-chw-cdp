package org.smartregister.chw.cdp.listener;

import android.app.Activity;
import androidx.annotation.NonNull;
import android.view.MenuItem;
import android.widget.Toast;

import org.smartregister.listener.BottomNavigationListener;
import org.smartregister.cdp.R;
import org.smartregister.view.activity.BaseRegisterActivity;

public class BaseCdpBottomNavigationListener extends BottomNavigationListener {
    private Activity context;

    public BaseCdpBottomNavigationListener(Activity context) {
        super(context);
        this.context = context;
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        super.onNavigationItemSelected(item);

        BaseRegisterActivity baseRegisterActivity = (BaseRegisterActivity) context;
        int itemId =item.getItemId();
        if ( itemId == R.id.action_family) {
            baseRegisterActivity.switchToBaseFragment();
        } else if (itemId == R.id.action_order_receive) {
            baseRegisterActivity.switchToFragment(1);
        } else if(itemId == R.id.action_add_outlet){
            Toast.makeText(context, "Adding an Outlet", Toast.LENGTH_SHORT).show();
        }

        return true;
    }
}