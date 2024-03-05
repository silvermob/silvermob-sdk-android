/*
 *    Copyright 2018-2021 Prebid.org, Inc.
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package com.silvermob.sdk.renderingtestapp.plugplay.utilities.version

import android.os.Bundle
import android.view.View
import com.google.android.gms.ads.MobileAds
import com.silvermob.sdk.renderingtestapp.R
import com.silvermob.sdk.renderingtestapp.databinding.FragmentsVersionInfoBinding
import com.silvermob.sdk.renderingtestapp.utils.BaseFragment

class VersionInfoFragment : BaseFragment() {

    override val layoutRes: Int = R.layout.fragments_version_info
    private val binding: FragmentsVersionInfoBinding
        get() = getBinding()

    override fun initUi(view: View, savedInstanceState: Bundle?) {
        MobileAds.initialize(requireContext())

        binding.tvPrebidRenderingVersion.text = com.silvermob.sdk.SilverMob.SDK_VERSION
        binding.tvGamVersion.text = MobileAds.getVersion().toString()
        binding.tvOmsdkVersion.text = com.silvermob.sdk.SilverMob.OMSDK_VERSION
    }

}