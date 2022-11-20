package com.example.openvpn.extal

import com.example.openvpn.VpnProfile
import com.example.openvpn.core.ConfigParser
import java.io.ByteArrayInputStream
import java.io.IOException
import java.io.InputStreamReader

object CertUtils {
    internal var profile: VpnProfile? = null
        private set

    @JvmStatic
    fun setCert(cert: String): Int? {
        val inputStreamReader = InputStreamReader(ByteArrayInputStream(cert.toByteArray()))
        val configParser = ConfigParser()
        profile = try {
            configParser.parseConfig(inputStreamReader)
            inputStreamReader.close()
            configParser.convertProfile()
        } catch (e: IOException) {
            e.printStackTrace()
            VpnProfile("Test")
        } catch (e: ConfigParser.ConfigParseError) {
            e.printStackTrace()
            VpnProfile("Test")
        }
        return profile?.checkProfile(VPN.context)
    }
}
