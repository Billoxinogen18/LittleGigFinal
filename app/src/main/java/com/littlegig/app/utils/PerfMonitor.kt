package com.littlegig.app.utils

import com.google.firebase.perf.FirebasePerformance
import com.google.firebase.perf.metrics.Trace
import java.util.concurrent.ConcurrentHashMap

object PerfMonitor {
    private val traces = ConcurrentHashMap<String, Trace>()

    fun startTrace(name: String) {
        if (traces.containsKey(name)) return
        val trace = FirebasePerformance.getInstance().newTrace(name)
        trace.start()
        traces[name] = trace
    }

    fun putMetric(name: String, key: String, value: Long) {
        traces[name]?.putMetric(key, value)
    }

    fun stopTrace(name: String) {
        traces.remove(name)?.stop()
    }
}