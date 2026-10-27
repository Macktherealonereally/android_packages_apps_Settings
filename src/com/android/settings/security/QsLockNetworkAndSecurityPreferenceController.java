/*
 * SPDX-FileCopyrightText: 2026 The LineageOS Project
 * SPDX-License-Identifier: Apache-2.0
 */

package com.android.settings.security;

import android.content.Context;
import android.os.UserHandle;
import android.provider.Settings;

import androidx.preference.Preference;

import com.android.internal.widget.LockPatternUtils;
import com.android.settings.R;
import com.android.settings.core.TogglePreferenceController;

/**
 * "Lock network and security": SystemUI asks for an unlock before the network and security
 * Quick Settings tiles can be used on the lock screen (read by SystemUI QSTileImpl). It only
 * does something with a secure screen lock, so the switch is greyed out without one.
 */
public class QsLockNetworkAndSecurityPreferenceController extends TogglePreferenceController {

    private static final String SETTING_KEY = "qs_lock_network_and_security";

    private final LockPatternUtils mLockPatternUtils;

    public QsLockNetworkAndSecurityPreferenceController(Context context, String preferenceKey) {
        super(context, preferenceKey);
        mLockPatternUtils = new LockPatternUtils(context);
    }

    @Override
    public boolean isChecked() {
        return Settings.Secure.getInt(mContext.getContentResolver(), SETTING_KEY, 0) != 0;
    }

    @Override
    public boolean setChecked(boolean isChecked) {
        return Settings.Secure.putInt(mContext.getContentResolver(), SETTING_KEY,
                isChecked ? 1 : 0);
    }

    @Override
    public int getAvailabilityStatus() {
        return mLockPatternUtils.isSecure(UserHandle.myUserId())
                ? AVAILABLE : DISABLED_DEPENDENT_SETTING;
    }

    @Override
    public CharSequence getSummary() {
        return mContext.getText(getAvailabilityStatus() == DISABLED_DEPENDENT_SETTING
                ? R.string.lockscreen_qs_lock_network_and_security_no_lock_summary
                : R.string.lockscreen_qs_lock_network_and_security_summary);
    }

    @Override
    public void updateState(Preference preference) {
        super.updateState(preference);
        preference.setEnabled(getAvailabilityStatus() != DISABLED_DEPENDENT_SETTING);
        refreshSummary(preference);
    }

    @Override
    public int getSliceHighlightMenuRes() {
        return R.string.menu_key_security;
    }
}
