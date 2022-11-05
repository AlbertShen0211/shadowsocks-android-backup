/*******************************************************************************
 *                                                                             *
 *  Copyright (C) 2020 by Max Lv <max.c.lv@gmail.com>                          *
 *  Copyright (C) 2020 by Mygod Studio <contact-shadowsocks-android@mygod.be>  *
 *                                                                             *
 *  This program is free software: you can redistribute it and/or modify       *
 *  it under the terms of the GNU General Public License as published by       *
 *  the Free Software Foundation, either version 3 of the License, or          *
 *  (at your option) any later version.                                        *
 *                                                                             *
 *  This program is distributed in the hope that it will be useful,            *
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of             *
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the              *
 *  GNU General Public License for more details.                               *
 *                                                                             *
 *  You should have received a copy of the GNU General Public License          *
 *  along with this program. If not, see <http://www.gnu.org/licenses/>.       *
 *                                                                             *
 *******************************************************************************/

package com.github.shadowsocks.utils

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.VpnService
import android.util.Log
import androidx.activity.result.contract.ActivityResultContract
import androidx.activity.result.contract.ActivityResultContracts
import com.github.shadowsocks.Core
import com.github.shadowsocks.preference.DataStore
import timber.log.Timber
import timber.log.Timber.Forest.tag

private val jsonMimeTypes = arrayOf("application/*", "text/*")
val tag="ActivityResultContracts"
object OpenJson : ActivityResultContracts.GetMultipleContents() {
    override fun createIntent(context: Context, input: String) = super.createIntent(context,
            jsonMimeTypes.first()).apply { putExtra(Intent.EXTRA_MIME_TYPES, jsonMimeTypes) }
}

object SaveJson : ActivityResultContracts.CreateDocument("application/json") {
    override fun createIntent(context: Context, input: String) =
            super.createIntent(context, "profiles.json")
}

class StartService : ActivityResultContract<Void?, Boolean>() {
    private var cachedIntent: Intent? = null

    override fun getSynchronousResult(context: Context, input: Void?): SynchronousResult<Boolean>? {
        Log.e(tag,"DataStore.serviceMode:"+DataStore.serviceMode)
        if (DataStore.serviceMode == Key.modeVpn) VpnService.prepare(context)?.let { intent ->
            cachedIntent = intent
            Log.e(tag," Key.modeVpn")
            return null
        }
        Core.startService()
        Log.e(tag," Core.startService()")
        return SynchronousResult(false)
    }

    override fun createIntent(context: Context, input: Void?) = cachedIntent!!.also { cachedIntent = null }

    override fun parseResult(resultCode: Int, intent: Intent?) = if (resultCode == Activity.RESULT_OK) {
        Core.startService()
        false
    } else {
        Timber.e("Failed to start VpnService: $intent")
        true
    }
}
