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

package com.smartnsoft.monerominerapp

import android.os.Bundle
import android.os.Handler
import android.support.v7.app.AppCompatActivity
import com.smartnsoft.monerominer.MoneroMiner

/**
 * A simple Activity that shows how to start the Monero Miner and change its parameters
 *
 * @author David Fournier
 * @since 2018.03.09
 */
class MainActivity
  : AppCompatActivity()
{

  companion object
  {

    private const val PERIODICITY = 5000L
  }

  override fun onCreate(savedInstanceState: Bundle?)
  {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main)

    MoneroMiner.start()

    val handler = Handler()

    var i = 1
    handler.postDelayed(object : Runnable
    {
      override fun run()
      {
        MoneroMiner.throttle = i++ % 2 * 60 + 20
        handler.postDelayed(this, PERIODICITY)
      }
    }, PERIODICITY)

  }
}
