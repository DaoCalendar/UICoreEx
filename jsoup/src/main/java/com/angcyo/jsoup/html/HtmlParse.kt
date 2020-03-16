package com.angcyo.jsoup.html

import com.angcyo.jsoup.DslJsoup
import com.angcyo.jsoup.dslJsoup
import com.angcyo.jsoup.html.css.AttrSelect
import com.angcyo.jsoup.html.css.AttrSelect.Companion.ATTR_KEY_HTML
import com.angcyo.jsoup.html.css.AttrSelect.Companion.ATTR_KEY_OUT_HTML
import com.angcyo.jsoup.html.css.AttrSelect.Companion.ATTR_KEY_OUT_VALUE
import com.angcyo.jsoup.html.css.AttrSelect.Companion.ATTR_KEY_TEXT
import com.angcyo.jsoup.html.css.HtmlElementSelect
import com.angcyo.jsoup.toAbsUrl
import com.angcyo.library.L
import com.angcyo.library.ex.patternList
import org.jsoup.nodes.Document
import org.jsoup.nodes.Element

/**
 *
 * Email:angcyo@126.com
 * @author angcyo
 * @date 2020/03/16
 * Copyright (c) 2019 ShenZhen O&M Cloud Co., Ltd. All rights reserved.
 */

/**开始解析结构*/
fun HtmlCategory.parse(
    config: DslJsoup.() -> Unit = {},
    /**解析结束, 协程线程回调*/
    onParseEnd: suspend (Document) -> Unit
) {
    dslJsoup {
        url = htmlUrl
        onDocumentReady = {
            it.parse(this@parse)
            onParseEnd(it)
        }
        config()
    }
}

/**从[Element]中, 获取所有想要的[HtmlElement]结构bean*/
fun Element.parseTo(htmlElement: HtmlElement?, htmlElementSelect: HtmlElementSelect) {
    val node = this
    htmlElement?.also { result ->
        val title = node.parseAttr(htmlElementSelect.titleSelect)
        result.title = result.title ?: title

        val summary = node.parseAttr(htmlElementSelect.summarySelect)
        result.summary = result.summary ?: summary

        val cover = node.parseAttr(htmlElementSelect.coverSelect)
        result.cover = result.cover ?: cover

        val targetUrl = node.parseAttr(htmlElementSelect.targetUrlSelect)
        result.targetUrl = result.targetUrl ?: targetUrl
    }
}

/**从[Element]中, 获取指定属性[AttrSelect]的值*/
fun Element.parseAttr(attrSelect: AttrSelect?): String? {
    val node = this
    return attrSelect?.run {

        //调整目标Element
        val targetElement = if (attrTargetCss.isNullOrBlank()) {
            node
        } else {
            node.select(attrTargetCss).firstOrNull()
        }

        //获取属性
        val attrString = targetElement?.run {
            when {
                attrKey.isNullOrBlank() -> null
                attrKey!!.toLowerCase() == ATTR_KEY_TEXT -> text()
                attrKey!!.toLowerCase() == ATTR_KEY_HTML -> html()
                attrKey!!.toLowerCase() == ATTR_KEY_OUT_HTML -> outerHtml()
                attrKey!!.toLowerCase() == ATTR_KEY_OUT_VALUE -> `val`()
                else -> attr(attrKey)
            }
        }

        //属性正则过滤
        val patternString = attrString?.run {
            if (attrPattern.isNullOrEmpty()) {
                this
            } else {
                var get: String? = null
                attrPattern?.forEach {
                    if (get.isNullOrBlank()) {
                        get = attrString.patternList(it).firstOrNull()
                    }
                }
                get
            }
        }

        //url修正, 加上base uri
        if (attrIsUrl) {
            patternString?.toAbsUrl(baseUri())
        } else {
            patternString
        }
    }
}

/**从[Document]中获取结构数据*/
fun Document.parse(category: HtmlCategory): HtmlCategory {
    category.elements = mutableListOf()
    if (category.elementsCss.isNullOrBlank()) {
        L.w("无元素选择器.")
    } else if (category.htmlElementSelect == null) {
        L.w("无属性选择器.")
    } else {
        val htmlElementSelect = category.htmlElementSelect
        val cssList = category.elementsCss?.split(AttrSelect.CSS_SPILT)
        cssList?.forEachIndexed { elementCategoryIndex, css ->
            //多组css选择, 优先使用第一组数据, 之后的数据只做追加, 不做替换.
            val isFirst = elementCategoryIndex == 0
            select(css)?.forEachIndexed { index, element ->
                var htmlElement: HtmlElement? = category.elements?.getOrNull(index)
                if (isFirst) {
                    htmlElement = HtmlElement()
                }
                element.parseTo(htmlElement, htmlElementSelect!!)

                if (isFirst) {
                    htmlElement?.apply { category.elements?.add(this) }
                }
            }
        }
    }
    return category
}