package com.angcyo.bmob

import cn.bmob.v3.exception.BmobException
import cn.bmob.v3.listener.QueryListener
import com.angcyo.library.L

/**
 *
 * Email:angcyo@126.com
 * @author angcyo
 * @date 2020/02/07
 */
private class QueryListenerHandler<Data>(
    val onResult: (result: Data?, error: BmobException?) -> Unit = { result, error ->
        L.d(result, error)
    }
) : QueryListener<Data>() {
    override fun done(result: Data?, e: BmobException?) {
        onResult(result, e)
    }
}