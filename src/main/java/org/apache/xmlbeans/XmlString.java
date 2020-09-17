/*   Copyright 2004 The Apache Software Foundation
 *
 *   Licensed under the Apache License, Version 2.0 (the "License");
 *   you may not use this file except in compliance with the License.
 *   You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *   Unless required by applicable law or agreed to in writing, software
 *   distributed under the License is distributed on an "AS IS" BASIS,
 *   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *   See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package org.apache.xmlbeans;

/**
 * Corresponds to the XML Schema
 * <a target="_blank" href="http://www.w3.org/TR/xmlschema-2/#string">xs:string</a> type.
 * <p>
 * A basic string in XML schema is not whitespace normalized.  If you
 * want your string type to be insensitive to variations in runs of
 * whitespace, consider using
 * <a target="_blank" href="http://www.w3.org/TR/xmlschema-2/#token">xs:token</a>
 * (aka {@link XmlToken}) instead.
 * To forbid whitespace and permit just alphanumeric and other
 * common identifier characters consider
 * <a target="_blank" href="http://www.w3.org/TR/xmlschema-2/#NMTOKEN">xs:NMTOKEN</a>
 * (aka {@link XmlNMTOKEN}) instead.
 * <p>
 * Convertible to {@link String}.
 */
public interface XmlString extends XmlAnySimpleType {
    /**
     * The constant {@link SchemaType} object representing this schema type.
     */
    SchemaType type = XmlBeans.getBuiltinTypeSystem().typeForHandle("_BI_string");

    /**
     * A class with methods for creating instances
     * of {@link XmlString}.
     */
    final class Factory {
        /**
         * Creates an empty instance of {@link XmlString}
         */
        public static XmlString newInstance() {
            return (XmlString) XmlBeans.getContextTypeLoader().newInstance(type, null);
        }

        /**
         * Creates an empty instance of {@link XmlString}
         */
        public static XmlString newInstance(org.apache.xmlbeans.XmlOptions options) {
            return (XmlString) XmlBeans.getContextTypeLoader().newInstance(type, options);
        }

        /**
         * Creates an immutable {@link XmlString} value
         */
        public static XmlString newValue(Object obj) {
            return (XmlString) type.newValue(obj);
        }

        /**
         * Parses a {@link XmlString} fragment from a String. For example: "<code>&lt;xml-fragment&gt; arbitrary string &lt;/xml-fragment&gt;</code>".
         */
        public static XmlString parse(java.lang.String s) throws org.apache.xmlbeans.XmlException {
            return (XmlString) XmlBeans.getContextTypeLoader().parse(s, type, null);
        }

        /**
         * Parses a {@link XmlString} fragment from a String. For example: "<code>&lt;xml-fragment&gt; arbitrary string &lt;/xml-fragment&gt;</code>".
         */
        public static XmlString parse(java.lang.String s, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException {
            return (XmlString) XmlBeans.getContextTypeLoader().parse(s, type, options);
        }

        /**
         * Parses a {@link XmlString} fragment from a File.
         */
        public static XmlString parse(java.io.File f) throws org.apache.xmlbeans.XmlException, java.io.IOException {
            return (XmlString) XmlBeans.getContextTypeLoader().parse(f, type, null);
        }

        /**
         * Parses a {@link XmlString} fragment from a File.
         */
        public static XmlString parse(java.io.File f, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, java.io.IOException {
            return (XmlString) XmlBeans.getContextTypeLoader().parse(f, type, options);
        }

        /**
         * Parses a {@link XmlString} fragment from a URL.
         */
        public static XmlString parse(java.net.URL u) throws org.apache.xmlbeans.XmlException, java.io.IOException {
            return (XmlString) XmlBeans.getContextTypeLoader().parse(u, type, null);
        }

        /**
         * Parses a {@link XmlString} fragment from a URL.
         */
        public static XmlString parse(java.net.URL u, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, java.io.IOException {
            return (XmlString) XmlBeans.getContextTypeLoader().parse(u, type, options);
        }

        /**
         * Parses a {@link XmlString} fragment from an InputStream.
         */
        public static XmlString parse(java.io.InputStream is) throws org.apache.xmlbeans.XmlException, java.io.IOException {
            return (XmlString) XmlBeans.getContextTypeLoader().parse(is, type, null);
        }

        /**
         * Parses a {@link XmlString} fragment from an InputStream.
         */
        public static XmlString parse(java.io.InputStream is, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, java.io.IOException {
            return (XmlString) XmlBeans.getContextTypeLoader().parse(is, type, options);
        }

        /**
         * Parses a {@link XmlString} fragment from a Reader.
         */
        public static XmlString parse(java.io.Reader r) throws org.apache.xmlbeans.XmlException, java.io.IOException {
            return (XmlString) XmlBeans.getContextTypeLoader().parse(r, type, null);
        }

        /**
         * Parses a {@link XmlString} fragment from a Reader.
         */
        public static XmlString parse(java.io.Reader r, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, java.io.IOException {
            return (XmlString) XmlBeans.getContextTypeLoader().parse(r, type, options);
        }

        /**
         * Parses a {@link XmlString} fragment from a DOM Node.
         */
        public static XmlString parse(org.w3c.dom.Node node) throws org.apache.xmlbeans.XmlException {
            return (XmlString) XmlBeans.getContextTypeLoader().parse(node, type, null);
        }

        /**
         * Parses a {@link XmlString} fragment from a DOM Node.
         */
        public static XmlString parse(org.w3c.dom.Node node, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException {
            return (XmlString) XmlBeans.getContextTypeLoader().parse(node, type, options);
        }

        /**
         * Parses a {@link XmlString} fragment from an XMLStreamReader.
         */
        public static XmlString parse(javax.xml.stream.XMLStreamReader xsr) throws org.apache.xmlbeans.XmlException {
            return (XmlString) XmlBeans.getContextTypeLoader().parse(xsr, type, null);
        }

        /**
         * Parses a {@link XmlString} fragment from an XMLStreamReader.
         */
        public static XmlString parse(javax.xml.stream.XMLStreamReader xsr, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException {
            return (XmlString) XmlBeans.getContextTypeLoader().parse(xsr, type, options);
        }

        private Factory() {
            // No instance of this class allowed
        }
    }
}
