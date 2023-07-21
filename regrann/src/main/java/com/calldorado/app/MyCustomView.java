package com.calldorado.app;

import static android.view.View.inflate;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.calldorado.sdk.ui.ui.aftercall.cards.native_field.CalldoradoCustomView;
import com.jaredco.regrann.R;
import com.jaredco.regrann.activity.SettingsActivity2;

public class MyCustomView extends CalldoradoCustomView {
    private LinearLayout ll;
    private final Context context;

    public MyCustomView(Context context) {
        super(context);
        this.context = context;
    }

    @Override
    public View getRootView() {

        ll = (LinearLayout) inflate(context, R.layout.
                wic_native_layout, null);

        TextView b = ll.findViewById(R.id.textv2);

        b.setOnFocusChangeListener(new View.OnFocusChangeListener() {

            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                getFocus(v);
            }
        });

        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {

                    Intent playerActivity = new Intent(context, SettingsActivity2.class);
                    playerActivity.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_NO_ANIMATION);

                    try {
                        context.startActivity(playerActivity);


                    } catch (Exception e) {
                        int i = 1;
                    }
                } catch (Exception e) {
                }
            }
        });
        return ll;
    }
}