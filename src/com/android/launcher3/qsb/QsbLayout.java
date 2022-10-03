package com.android.launcher3.qsb;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.ResolveInfoFlags;
import android.content.pm.ResolveInfo;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;

import com.android.launcher3.DeviceProfile;
import com.android.launcher3.R;
import com.android.launcher3.qsb.QsbContainerView;
import com.android.launcher3.views.ActivityContext;

public final class QsbLayout extends FrameLayout {

    private static final Intent SEARCH_INTENT = new Intent("android.search.action.GLOBAL_SEARCH")
        .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

    private static final Intent LENS_INTENT = Intent.makeMainActivity(
        new ComponentName(
            "com.google.ar.lens",
            "com.google.vr.apps.ornament.app.lens.LensLauncherActivity"
        )
    ).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);

    private static final Intent VOICE_INTENT = new Intent(Intent.ACTION_VOICE_COMMAND)
        .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

    private static final String QSB_PACKAGE = "com.google.android.googlequicksearchbox";

    public QsbLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public QsbLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        final String searchPackage = QsbContainerView.getSearchWidgetPackageName(getContext());
        if (searchPackage == null) return;
        setOnClickListener(v -> startActivity(SEARCH_INTENT.setPackage(searchPackage)));

        final AssistantIconView assistantIcon = findViewById(R.id.mic_icon);
        if (assistantIcon != null) {
            assistantIcon.setIcon();
            assistantIcon.setOnClickListener(v -> startActivity(VOICE_INTENT.setPackage(searchPackage)));
        }

        if (searchPackage.equals(QSB_PACKAGE)) {
            setupLensIcon();
        }
    }

    private void startActivity(final Intent intent) {
        getContext().startActivity(intent);
    }

    private void setupLensIcon() {
        final ResolveInfo resolvedActivity = getContext().getPackageManager()
            .resolveActivity(LENS_INTENT, ResolveInfoFlags.of(PackageManager.MATCH_ALL));
        if (resolvedActivity == null) return;
        final ImageButton lensIcon = findViewById(R.id.lens_icon);
        if (lensIcon != null) {
            lensIcon.setVisibility(View.VISIBLE);
            lensIcon.setImageResource(R.drawable.ic_lens_color);
            lensIcon.setOnClickListener(v -> startActivity(LENS_INTENT));
        }
    }

    @Override
    protected void onMeasure(final int widthMeasureSpec, final int heightMeasureSpec) {
        final int requestedWidth = MeasureSpec.getSize(widthMeasureSpec);
        final int height = MeasureSpec.getSize(heightMeasureSpec);

        final DeviceProfile dp = ActivityContext.lookupContext(getContext()).getDeviceProfile();
        final int cellWidth = DeviceProfile.calculateCellWidth(requestedWidth, dp.cellLayoutBorderSpacePx.x, dp.numShownHotseatIcons);
        final int iconSize = (int) Math.round(dp.iconSizePx * 0.92f);
        final int width = requestedWidth - (cellWidth - iconSize);
        setMeasuredDimension(width, height);

        for (int i = 0; i < getChildCount(); i++) {
            final View child = getChildAt(i);
            if (child != null) {
                measureChildWithMargins(child, widthMeasureSpec, 0, heightMeasureSpec, 0);
            }
        }
    }
}
