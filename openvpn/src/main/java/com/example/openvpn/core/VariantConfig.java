/*
 * Copyright (c) 2012-2022 Arne Schwabe
 * Distributed under the GNU GPL v2 with additional terms. For full terms see the file doc/LICENSE.txt
 */

package com.example.openvpn.core;

import android.content.Context;
import android.content.Intent;


public class VariantConfig {
    /** Return the normal webview or internal webview depending what is available */
    static Intent getOpenUrlIntent(Context c, boolean external) {
            return new Intent(Intent.ACTION_VIEW);
    }
}
