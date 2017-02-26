package org.jsoup.safety;

import com.facebook.internal.ServerProtocol;
import com.facebook.share.internal.ShareConstants;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import org.jsoup.helper.Validate;
import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Element;

public class Whitelist {
    private Map<TagName, Set<AttributeKey>> attributes;
    private Map<TagName, Map<AttributeKey, AttributeValue>> enforcedAttributes;
    private boolean preserveRelativeLinks;
    private Map<TagName, Map<AttributeKey, Set<Protocol>>> protocols;
    private Set<TagName> tagNames;

    static abstract class TypedValue {
        private String value;

        TypedValue(String value) {
            Validate.notNull(value);
            this.value = value;
        }

        public int hashCode() {
            return (this.value == null ? 0 : this.value.hashCode()) + 31;
        }

        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj == null) {
                return false;
            }
            if (getClass() != obj.getClass()) {
                return false;
            }
            TypedValue other = (TypedValue) obj;
            if (this.value == null) {
                if (other.value != null) {
                    return false;
                }
                return true;
            } else if (this.value.equals(other.value)) {
                return true;
            } else {
                return false;
            }
        }

        public String toString() {
            return this.value;
        }
    }

    static class AttributeKey extends TypedValue {
        AttributeKey(String value) {
            super(value);
        }

        static AttributeKey valueOf(String value) {
            return new AttributeKey(value);
        }
    }

    static class AttributeValue extends TypedValue {
        AttributeValue(String value) {
            super(value);
        }

        static AttributeValue valueOf(String value) {
            return new AttributeValue(value);
        }
    }

    static class Protocol extends TypedValue {
        Protocol(String value) {
            super(value);
        }

        static Protocol valueOf(String value) {
            return new Protocol(value);
        }
    }

    static class TagName extends TypedValue {
        TagName(String value) {
            super(value);
        }

        static TagName valueOf(String value) {
            return new TagName(value);
        }
    }

    public static Whitelist none() {
        return new Whitelist();
    }

    public static Whitelist simpleText() {
        return new Whitelist().addTags("b", "em", "i", "strong", "u");
    }

    public static Whitelist basic() {
        Whitelist whitelist = new Whitelist();
        String[] strArr = new String[]{"a", "b", "blockquote", TtmlNode.TAG_BR, "cite", "code", "dd", "dl", "dt", "em", "i", "li", "ol", TtmlNode.TAG_P, "pre", "q", "small", "strike", "strong", "sub", "sup", "u", "ul"};
        String[] strArr2 = new String[]{ShareConstants.WEB_DIALOG_PARAM_HREF};
        strArr2 = new String[]{"cite"};
        strArr2 = new String[]{"cite"};
        return whitelist.addTags(strArr).addAttributes("a", strArr2).addAttributes("blockquote", strArr2).addAttributes("q", strArr2).addProtocols("a", ShareConstants.WEB_DIALOG_PARAM_HREF, "ftp", "http", "https", "mailto").addProtocols("blockquote", "cite", "http", "https").addProtocols("cite", "cite", "http", "https").addEnforcedAttribute("a", "rel", "nofollow");
    }

    public static Whitelist basicWithImages() {
        return basic().addTags("img").addAttributes("img", "align", "alt", "height", "src", ShareConstants.WEB_DIALOG_PARAM_TITLE, "width").addProtocols("img", "src", "http", "https");
    }

    public static Whitelist relaxed() {
        Whitelist whitelist = new Whitelist();
        String[] strArr = new String[]{"a", "b", "blockquote", TtmlNode.TAG_BR, ShareConstants.FEED_CAPTION_PARAM, "cite", "code", "col", "colgroup", "dd", TtmlNode.TAG_DIV, "dl", "dt", "em", "h1", "h2", "h3", "h4", "h5", "h6", "i", "img", "li", "ol", TtmlNode.TAG_P, "pre", "q", "small", "strike", "strong", "sub", "sup", "table", "tbody", "td", "tfoot", "th", "thead", "tr", "u", "ul"};
        String[] strArr2 = new String[]{ShareConstants.WEB_DIALOG_PARAM_HREF, ShareConstants.WEB_DIALOG_PARAM_TITLE};
        strArr2 = new String[]{"cite"};
        strArr2 = new String[]{TtmlNode.TAG_SPAN, "width"};
        strArr2 = new String[]{TtmlNode.TAG_SPAN, "width"};
        strArr2 = new String[]{"align", "alt", "height", "src", ShareConstants.WEB_DIALOG_PARAM_TITLE, "width"};
        strArr2 = new String[]{TtmlNode.START, ShareConstants.MEDIA_TYPE};
        strArr2 = new String[]{"cite"};
        strArr2 = new String[]{"summary", "width"};
        strArr2 = new String[]{"abbr", "axis", "colspan", "rowspan", "width"};
        strArr2 = new String[]{"abbr", "axis", "colspan", "rowspan", ServerProtocol.DIALOG_PARAM_SCOPE, "width"};
        strArr2 = new String[]{ShareConstants.MEDIA_TYPE};
        return whitelist.addTags(strArr).addAttributes("a", strArr2).addAttributes("blockquote", strArr2).addAttributes("col", strArr2).addAttributes("colgroup", strArr2).addAttributes("img", strArr2).addAttributes("ol", strArr2).addAttributes("q", strArr2).addAttributes("table", strArr2).addAttributes("td", strArr2).addAttributes("th", strArr2).addAttributes("ul", strArr2).addProtocols("a", ShareConstants.WEB_DIALOG_PARAM_HREF, "ftp", "http", "https", "mailto").addProtocols("blockquote", "cite", "http", "https").addProtocols("cite", "cite", "http", "https").addProtocols("img", "src", "http", "https").addProtocols("q", "cite", "http", "https");
    }

    public Whitelist() {
        this.tagNames = new HashSet();
        this.attributes = new HashMap();
        this.enforcedAttributes = new HashMap();
        this.protocols = new HashMap();
        this.preserveRelativeLinks = false;
    }

    public Whitelist addTags(String... tags) {
        Validate.notNull(tags);
        for (String tagName : tags) {
            Validate.notEmpty(tagName);
            this.tagNames.add(TagName.valueOf(tagName));
        }
        return this;
    }

    public Whitelist addAttributes(String tag, String... keys) {
        Validate.notEmpty(tag);
        Validate.notNull(keys);
        Validate.isTrue(keys.length > 0, "No attributes supplied.");
        TagName tagName = TagName.valueOf(tag);
        if (!this.tagNames.contains(tagName)) {
            this.tagNames.add(tagName);
        }
        Set<AttributeKey> attributeSet = new HashSet();
        for (String key : keys) {
            Validate.notEmpty(key);
            attributeSet.add(AttributeKey.valueOf(key));
        }
        if (this.attributes.containsKey(tagName)) {
            ((Set) this.attributes.get(tagName)).addAll(attributeSet);
        } else {
            this.attributes.put(tagName, attributeSet);
        }
        return this;
    }

    public Whitelist addEnforcedAttribute(String tag, String key, String value) {
        Validate.notEmpty(tag);
        Validate.notEmpty(key);
        Validate.notEmpty(value);
        TagName tagName = TagName.valueOf(tag);
        if (!this.tagNames.contains(tagName)) {
            this.tagNames.add(tagName);
        }
        AttributeKey attrKey = AttributeKey.valueOf(key);
        AttributeValue attrVal = AttributeValue.valueOf(value);
        if (this.enforcedAttributes.containsKey(tagName)) {
            ((Map) this.enforcedAttributes.get(tagName)).put(attrKey, attrVal);
        } else {
            Map<AttributeKey, AttributeValue> attrMap = new HashMap();
            attrMap.put(attrKey, attrVal);
            this.enforcedAttributes.put(tagName, attrMap);
        }
        return this;
    }

    public Whitelist preserveRelativeLinks(boolean preserve) {
        this.preserveRelativeLinks = preserve;
        return this;
    }

    public Whitelist addProtocols(String tag, String key, String... protocols) {
        Map<AttributeKey, Set<Protocol>> attrMap;
        Set<Protocol> protSet;
        Validate.notEmpty(tag);
        Validate.notEmpty(key);
        Validate.notNull(protocols);
        TagName tagName = TagName.valueOf(tag);
        AttributeKey attrKey = AttributeKey.valueOf(key);
        if (this.protocols.containsKey(tagName)) {
            attrMap = (Map) this.protocols.get(tagName);
        } else {
            attrMap = new HashMap();
            this.protocols.put(tagName, attrMap);
        }
        if (attrMap.containsKey(attrKey)) {
            protSet = (Set) attrMap.get(attrKey);
        } else {
            protSet = new HashSet();
            attrMap.put(attrKey, protSet);
        }
        for (String protocol : protocols) {
            Validate.notEmpty(protocol);
            protSet.add(Protocol.valueOf(protocol));
        }
        return this;
    }

    protected boolean isSafeTag(String tag) {
        return this.tagNames.contains(TagName.valueOf(tag));
    }

    protected boolean isSafeAttribute(String tagName, Element el, Attribute attr) {
        TagName tag = TagName.valueOf(tagName);
        AttributeKey key = AttributeKey.valueOf(attr.getKey());
        if (this.attributes.containsKey(tag) && ((Set) this.attributes.get(tag)).contains(key)) {
            if (!this.protocols.containsKey(tag)) {
                return true;
            }
            boolean z;
            Map<AttributeKey, Set<Protocol>> attrProts = (Map) this.protocols.get(tag);
            if (!attrProts.containsKey(key) || testValidProtocol(el, attr, (Set) attrProts.get(key))) {
                z = true;
            } else {
                z = false;
            }
            return z;
        } else if (tagName.equals(":all") || !isSafeAttribute(":all", el, attr)) {
            return false;
        } else {
            return true;
        }
    }

    private boolean testValidProtocol(Element el, Attribute attr, Set<Protocol> protocols) {
        String value = el.absUrl(attr.getKey());
        if (value.length() == 0) {
            value = attr.getValue();
        }
        if (!this.preserveRelativeLinks) {
            attr.setValue(value);
        }
        for (Protocol protocol : protocols) {
            if (value.toLowerCase().startsWith(protocol.toString() + ":")) {
                return true;
            }
        }
        return false;
    }

    Attributes getEnforcedAttributes(String tagName) {
        Attributes attrs = new Attributes();
        TagName tag = TagName.valueOf(tagName);
        if (this.enforcedAttributes.containsKey(tag)) {
            for (Entry<AttributeKey, AttributeValue> entry : ((Map) this.enforcedAttributes.get(tag)).entrySet()) {
                attrs.put(((AttributeKey) entry.getKey()).toString(), ((AttributeValue) entry.getValue()).toString());
            }
        }
        return attrs;
    }
}
