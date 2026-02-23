package org.lsposed.manager.util

import android.os.Build
import okhttp3.ConnectionSpec
import okhttp3.Dns
import okhttp3.HttpUrl
import okhttp3.OkHttpClient
import okhttp3.dnsoverhttps.DnsOverHttps
import okhttp3.internal.platform.Platform
import org.lsposed.manager.App
import java.net.InetAddress
import java.net.Proxy
import java.net.ProxySelector
import java.net.UnknownHostException
import java.util.List

class CloudflareDNS : Dns {
    private val url = HttpUrl.get("https://cloudflare-dns.com/dns-query")
    var DoH: Boolean = App.getPreferences().getBoolean("doh", false)
    var noProxy: Boolean = ProxySelector.getDefault().select(url.uri())[0] == Proxy.NO_PROXY
    private val cloudflare: Dns

    init {
        val trustManager = Platform.get().platformTrustManager()
        var tls = ConnectionSpec.RESTRICTED_TLS
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
            tls = ConnectionSpec.Builder(tls)
                .supportsTlsExtensions(false)
                .build()
        }
        val builder = DnsOverHttps.Builder()
            .resolvePrivateAddresses(true)
            .url(HttpUrl.get("https://cloudflare-dns.com/dns-query"))
            .client(
                OkHttpClient.Builder()
                    .cache(App.getOkHttpCache())
                    .sslSocketFactory(NoSniFactory(), trustManager)
                    .connectionSpecs(listOf(tls))
                    .build()
            )
        try {
            builder.bootstrapDnsHosts(
                listOf(
                    InetAddress.getByName("1.1.1.1"),
                    InetAddress.getByName("1.0.0.1"),
                    InetAddress.getByName("2606:4700:4700::1111"),
                    InetAddress.getByName("2606:4700:4700::1001")
                )
            )
        } catch (_: UnknownHostException) {
        }
        cloudflare = builder.build()
    }

    override fun lookup(hostname: String): List<InetAddress> {
        return if (DoH && noProxy) {
            cloudflare.lookup(hostname)
        } else {
            Dns.SYSTEM.lookup(hostname)
        }
    }
}