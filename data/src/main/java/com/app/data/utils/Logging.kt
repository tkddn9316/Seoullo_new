package com.app.data.utils

import android.util.Log
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject

object Logging {
    private const val LENGTH = 1024

    private fun v(tag: String, message: Any) {
        Log.v(tag, message.toString())
    }

    private fun d(tag: String, message: Any) {
        Log.d(tag, message.toString())
    }

    private fun i(tag: String, message: Any) {
        Log.i(tag, message.toString())
    }

    private fun w(tag: String, message: Any) {
        Log.w(tag, message.toString())
    }

    private fun e(tag: String, message: Any) {
        var temp = message.toString()

        while (temp.isNotEmpty()) {
            if (temp.length > LENGTH) {
                Log.e(tag, temp.substring(0, LENGTH))
                temp = temp.substring(LENGTH)
            } else {
                Log.e(tag, temp)
                break
            }
        }
    }

    fun e(clas: Class<*>, message: Any) {
        Log.e(clas.simpleName, message.toString())
    }

    fun w(clas: Class<*>, message: Any) {
        Log.w(clas.simpleName, message.toString())
    }

    fun i(clas: Class<*>, message: Any) {
        Log.i(clas.simpleName, message.toString())
    }

    fun d(clas: Class<*>, message: Any) {
        Log.v(clas.simpleName, message.toString())
    }

    fun v(clas: Class<*>, message: Any) {
        Log.v(clas.simpleName, message.toString())
    }

    fun e(message: Any) {
        val arrSte = getTrace()
        val ste = getCurInfo(arrSte)
        e(getTag(ste), message.toString() + callInfo(arrSte, 1))
    }

    fun e(vararg message: Any) {
        val arrSte = getTrace()
        val ste = getCurInfo(arrSte)
        e(getTag(ste), message.contentToString() + callInfo(arrSte, 1))
    }

    fun w(message: Any) {
        val arrSte = getTrace()
        val ste = getCurInfo(arrSte)
        w(getTag(ste), message.toString() + callInfo(arrSte, 1))
    }

    fun i(message: Any) {
        val arrSte = getTrace()
        val ste = getCurInfo(arrSte)
        i(getTag(ste), message.toString() + callInfo(arrSte, 1))
    }

    fun d(vararg messages: Any) {
        val arrSte = getTrace()
        val ste = getCurInfo(arrSte)
        val message = StringBuilder()
        for (i in messages.indices) {
            message.append(messages[i])
            if (i < messages.size - 1) message.append(" : ")
        }

        d(getTag(ste), message.toString() + callInfo(arrSte, 1))
    }

    fun d(message: Any) {
        val arrSte = getTrace()
        val ste = getCurInfo(arrSte)
        d(getTag(ste), message.toString() + callInfo(arrSte, 1))
    }

    fun v(message: Any) {
        val arrSte = getTrace()
        val ste = getCurInfo(arrSte)
        v(getTag(ste), message.toString() + callInfo(arrSte, 1))
    }

    fun L(source: Any) {
        val arrSte = getTrace()
        val ste = getCurInfo(arrSte)
        L(getTag(ste), source)
    }

    private fun L(tag: String, source: Any) {
        val o = getJsonObjFromStr(source)
        if (o != null) {
            when (o) {
                is JSONObject -> {
                    val json = o.toString()
                    val length = json.length

                    for (i in 0..length step (1024)) {
                        if (i + 1024 < length) Log.d(tag, json.substring(i, i + 1024))
                        else Log.d(tag, json.substring(i, length))
                    }
                }
                is JSONArray -> {
                    val json = o.toString()
                    val length = json.length

                    for (i in 0..length step (1024)) {
                        if (i + 1024 < length) Log.d(tag, json.substring(i, i + 1024))
                        else Log.d(tag, json.substring(i, length))
                    }
                }
                else -> {
                    Log.d(tag, source.toString())
                }
            }
        } else {
            Log.d(tag, source.toString())
        }
    }

    private fun getJsonObjFromStr(test: Any): Any? {
        val o = try {
            JSONObject(test.toString())
        } catch (ex: JSONException) {
            try {
                JSONArray(test)
            } catch (ex1: JSONException) {
                return null
            }
        }
        return o
    }

    private fun getTrace(): Array<StackTraceElement> {
        return Throwable().stackTrace
    }

    private fun getCurInfo(arrSte: Array<StackTraceElement>): StackTraceElement {
        return arrSte[2]
    }

    private fun getTag(ste: StackTraceElement): String {
        return ste.className.replace("app.map.covid" /** 패키지 네임 넣기 */, "")
            .replace("activity.", "")
            .replace("activity", "")
            .replace("fragment.", "")
            .replace("fragment", "")
    }

    private fun callInfo(arrSte: Array<StackTraceElement>, depth: Int): String {
        val builder = StringBuilder()
        var cnt = 0
        var tDepth = if (depth <= 0) 1 else depth

        if (depth > 1) {
            builder.append("\nmethod: ")
        } else {
            builder.append(" method: ")
        }

        for (ste in arrSte) {
            if (cnt < 2) {
                cnt++
                continue
            }
            if (tDepth <= 0) break
            builder.append(getExtraInfo(ste)).append("\n")
            cnt++
            tDepth--
        }

        return builder.toString()
    }

    private fun getExtraInfo(ste: StackTraceElement): String {
        return ste.methodName + "[" + ste.lineNumber + "] "
    }
}