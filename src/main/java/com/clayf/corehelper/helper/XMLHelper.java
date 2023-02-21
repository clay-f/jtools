package com.clayf.corehelper.helper;

import java.util.ArrayList;
import java.util.List;

/**
 * xml 工具类
 * created by f at 2021-12-09 22:51
 */
public class XMLHelper {
    private XMLHelper() {
    }

    /**
     * 获取唯一节点内容
     *
     * @param iXmlContent xml内容
     * @param iTagName    目标tag名字
     * @return 获取tag值
     */
    public static String getUniqueNodeContent(String iXmlContent, String iTagName) {
        if (iXmlContent != null && iTagName != null) {
            String _openTag = "<" + iTagName;
            String _closeTag = "</" + iTagName + ">";

            int _startIdx;
            for (_startIdx = -1; (_startIdx = iXmlContent.indexOf(_openTag, _startIdx)) != -1; _startIdx += _openTag.length()) {
                char _next = iXmlContent.charAt(_startIdx + iTagName.length() + 1);
                if (_next == '>' || Character.isWhitespace(_next)) {
                    break;
                }
            }

            if (_startIdx == -1) {
                return null;
            } else {
                _startIdx = iXmlContent.indexOf(">", _startIdx);
                if (_startIdx == -1) {
                    return null;
                } else {
                    int _closeIdx = iXmlContent.indexOf(_closeTag, _startIdx);
                    return _closeIdx == -1 ? null : iXmlContent.substring(_startIdx + 1, _closeIdx);
                }
            }
        } else {
            throw new NullPointerException("参数为空");
        }
    }

    /**
     * 获取节点内容
     *
     * @param iXmlContent 内容
     * @param iTagName    目标标签
     * @return 标签内容
     */
    public static List<String> getNodeContent(String iXmlContent, String iTagName) {
        if (iXmlContent != null && iTagName != null) {
            List<String> _contentList = new ArrayList();
            String _openTag = "<" + iTagName;
            String _closeTag = "</" + iTagName + ">";
            int _startIdx = -1;

            int _closeIdx;
            for (int _xmlLen = iXmlContent.length(); iXmlContent.indexOf(_openTag, _startIdx) != -1; _startIdx = Math.min(_closeIdx + iTagName.length() + 1, _xmlLen)) {
                while ((_startIdx = iXmlContent.indexOf(_openTag, _startIdx)) != -1) {
                    char _next = iXmlContent.charAt(_startIdx + iTagName.length() + 1);
                    if (_next == '>' || Character.isWhitespace(_next)) {
                        break;
                    }

                    _startIdx += _openTag.length();
                }

                if (_startIdx == -1) {
                    break;
                }

                _startIdx = iXmlContent.indexOf(">", _startIdx);
                if (_startIdx == -1) {
                    break;
                }

                _closeIdx = iXmlContent.indexOf(_closeTag, _startIdx);
                if (_closeIdx == -1) {
                    break;
                }

                _contentList.add(iXmlContent.substring(_startIdx + 1, _closeIdx));
            }

            return _contentList.isEmpty() ? null : _contentList;
        } else {
            throw new NullPointerException("参数为空");
        }
    }
}