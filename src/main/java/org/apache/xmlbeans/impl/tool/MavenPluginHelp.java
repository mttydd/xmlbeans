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

package org.apache.xmlbeans.impl.tool;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.xmlbeans.XmlOptions;
import org.apache.xmlbeans.impl.common.DocumentHelper;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Display help information on xmlbeans-maven-plugin.<br>
 * Call {@code mvn xmlbeans.plugin:help -Ddetail=true -Dgoal=&lt;goal-name&gt;} to display parameter details.
 */
@Mojo(name = "help", requiresProject = false, threadSafe = true)
public class MavenPluginHelp extends AbstractMojo {
    // ******************************************************************************************
    // As we don't use the maven plugin-plugin, the defaultValues and others need to be manually
    // copied into resources/maven/plugin-help.xml
    // ******************************************************************************************


    /**
     * If {@code true}, display all settable properties for each goal.
     */
    @Parameter(property = "detail", defaultValue = "false")
    private boolean detail;

    /**
     * The name of the goal for which to show help. If unspecified, all goals will be displayed.
     */
    @Parameter(property = "goal")
    private java.lang.String goal;

    /**
     * The maximum length of a display line, should be positive.
     */
    @Parameter(property = "lineLength", defaultValue = "80")
    private int lineLength;

    /**
     * The number of spaces per indentation level, should be positive.
     */
    @Parameter(property = "indentSize", defaultValue = "2")
    private int indentSize;

    // groupId/artifactId/plugin-help.xml
    private static final String PLUGIN_HELP_PATH = "/META-INF/maven/plugin.xml";

    private static final int DEFAULT_LINE_LENGTH = 80;

    private Document build() throws MojoExecutionException {
        getLog().debug("load plugin-help.xml: " + PLUGIN_HELP_PATH);
        try (InputStream is = getClass().getResourceAsStream(PLUGIN_HELP_PATH)) {
            return DocumentHelper.readDocument(new XmlOptions(), is);
        } catch (IOException | SAXException e) {
            throw new MojoExecutionException(e.getMessage(), e);
        }
    }

    /**
     * {@inheritDoc}
     */
    public void execute() throws MojoExecutionException {
        if (lineLength <= 0) {
            getLog().warn("The parameter 'lineLength' should be positive, using '80' as default.");
            lineLength = DEFAULT_LINE_LENGTH;
        }
        if (indentSize <= 0) {
            getLog().warn("The parameter 'indentSize' should be positive, using '2' as default.");
            indentSize = 2;
        }

        Document doc = build();

        StringBuilder sb = new StringBuilder();
        Node plugin = getSingleChild(doc, "plugin");


        String name = getValue(plugin, "name");
        String version = getValue(plugin, "version");
        String id = getValue(plugin, "groupId") + ":" + getValue(plugin, "artifactId") + ":" + version;
        if (isNotEmpty(name) && !name.contains(id)) {
            append(sb, name + " " + version, 0);
        } else {
            if (isNotEmpty(name)) {
                append(sb, name, 0);
            } else {
                append(sb, id, 0);
            }
        }
        append(sb, getValue(plugin, "description"), 1);
        append(sb, "", 0);

        //<goalPrefix>plugin</goalPrefix>
        String goalPrefix = getValue(plugin, "goalPrefix");

        Node mojos1 = getSingleChild(plugin, "mojos");

        List<Node> mojos = findNamedChild(mojos1, "mojo");

        if (goal == null || goal.length() <= 0) {
            append(sb, "This plugin has " + mojos.size() + (mojos.size() > 1 ? " goals:" : " goal:"), 0);
            append(sb, "", 0);
        }

        for (Node mojo : mojos) {
            writeGoal(sb, goalPrefix, (Element) mojo);
        }

        if (getLog().isInfoEnabled()) {
            getLog().info(sb.toString());
        }
    }


    private static boolean isNotEmpty(String string) {
        return string != null && !string.isEmpty();
    }

    private String getValue(Node node, String elementName)
        throws MojoExecutionException {
        return getSingleChild(node, elementName).getTextContent();
    }

    private Node getSingleChild(Node node, String elementName)
        throws MojoExecutionException {
        List<Node> namedChild = findNamedChild(node, elementName);
        if (namedChild.isEmpty()) {
            throw new MojoExecutionException("Could not find " + elementName + " in plugin.xml");
        }
        if (namedChild.size() > 1) {
            throw new MojoExecutionException("Multiple " + elementName + " in plugin.xml");
        }
        return namedChild.get(0);
    }

    private List<Node> findNamedChild(Node node, String elementName) {
        List<Node> result = new ArrayList<>();
        NodeList childNodes = node.getChildNodes();
        for (int i = 0; i < childNodes.getLength(); i++) {
            Node item = childNodes.item(i);
            if (elementName.equals(item.getNodeName())) {
                result.add(item);
            }
        }
        return result;
    }

    private Node findSingleChild(Node node, String elementName)
        throws MojoExecutionException {
        List<Node> elementsByTagName = findNamedChild(node, elementName);
        if (elementsByTagName.isEmpty()) {
            return null;
        }
        if (elementsByTagName.size() > 1) {
            throw new MojoExecutionException("Multiple " + elementName + "in plugin.xml");
        }
        return elementsByTagName.get(0);
    }

    private void writeGoal(StringBuilder sb, String goalPrefix, Element mojo)
        throws MojoExecutionException {
        String mojoGoal = getValue(mojo, "goal");
        Node configurationElement = findSingleChild(mojo, "configuration");
        Node description = findSingleChild(mojo, "description");
        if (goal == null || goal.length() <= 0 || mojoGoal.equals(goal)) {
            append(sb, goalPrefix + ":" + mojoGoal, 0);
            Node deprecated = findSingleChild(mojo, "deprecated");
            if ((deprecated != null) && isNotEmpty(deprecated.getTextContent())) {
                append(sb, "Deprecated. " + deprecated.getTextContent(), 1);
                if (detail && description != null) {
                    append(sb, "", 0);
                    append(sb, description.getTextContent(), 1);
                }
            } else if (description != null) {
                append(sb, description.getTextContent(), 1);
            }
            append(sb, "", 0);

            if (detail) {
                Node parametersNode = getSingleChild(mojo, "parameters");
                List<Node> parameters = findNamedChild(parametersNode, "parameter");
                append(sb, "Available parameters:", 1);
                append(sb, "", 0);

                for (Node parameter : parameters) {
                    writeParameter(sb, parameter, configurationElement);
                }
            }
        }
    }

    private void writeParameter(StringBuilder sb, Node parameter, Node configurationElement)
        throws MojoExecutionException {
        String parameterName = getValue(parameter, "name");
        String parameterDescription = getValue(parameter, "description");

        Element fieldConfigurationElement = null;
        if (configurationElement != null) {
            fieldConfigurationElement = (Element) findSingleChild(configurationElement, parameterName);
        }

        String parameterDefaultValue = "";
        if (fieldConfigurationElement != null && fieldConfigurationElement.hasAttribute("default-value")) {
            parameterDefaultValue = " (Default: " + fieldConfigurationElement.getAttribute("default-value") + ")";
        }
        append(sb, parameterName + parameterDefaultValue, 2);
        Node deprecated = findSingleChild(parameter, "deprecated");
        if ((deprecated != null) && isNotEmpty(deprecated.getTextContent())) {
            append(sb, "Deprecated. " + deprecated.getTextContent(), 3);
            append(sb, "", 0);
        }
        append(sb, parameterDescription, 3);
        if ("true".equals(getValue(parameter, "required"))) {
            append(sb, "Required: Yes", 3);
        }
        if ((fieldConfigurationElement != null) && isNotEmpty(fieldConfigurationElement.getTextContent())) {
            String property = getPropertyFromExpression(fieldConfigurationElement.getTextContent());
            append(sb, "User property: " + property, 3);
        }

        append(sb, "", 0);
    }

    /**
     * <p>Repeat a String {@code n} times to form a new string.</p>
     *
     * @param str    String to repeat
     * @param repeat number of times to repeat str
     * @return String with repeated String
     * @throws NegativeArraySizeException if {@code repeat &lt; 0}
     * @throws NullPointerException       if str is {@code null}
     */
    private static String repeat(String str, int repeat) {
        StringBuilder buffer = new StringBuilder(repeat * str.length());

        for (int i = 0; i < repeat; i++) {
            buffer.append(str);
        }

        return buffer.toString();
    }

    /**
     * Append a description to the buffer by respecting the indentSize and lineLength parameters.
     * <b>Note</b>: The last character is always a new line.
     *
     * @param sb          The buffer to append the description, not {@code null}.
     * @param description The description, not {@code null}.
     * @param indent      The base indentation level of each line, must not be negative.
     */
    private void append(StringBuilder sb, String description, int indent) {
        for (String line : toLines(description, indent, indentSize, lineLength)) {
            sb.append(line).append('\n');
        }
    }

    /**
     * Splits the specified text into lines of convenient display length.
     *
     * @param text       The text to split into lines, must not be {@code null}.
     * @param indent     The base indentation level of each line, must not be negative.
     * @param indentSize The size of each indentation, must not be negative.
     * @param lineLength The length of the line, must not be negative.
     * @return The sequence of display lines, never {@code null}.
     * @throws NegativeArraySizeException if {@code indent &lt; 0}
     */
    private static List<String> toLines(String text, int indent, int indentSize, int lineLength) {
        List<String> lines = new ArrayList<>();

        String ind = repeat("\t", indent);

        String[] plainLines = text.split("(\r\n)|(\r)|(\n)");

        for (String plainLine : plainLines) {
            toLines(lines, ind + plainLine, indentSize, lineLength);
        }

        return lines;
    }

    /**
     * Adds the specified line to the output sequence, performing line wrapping if necessary.
     *
     * @param lines      The sequence of display lines, must not be {@code null}.
     * @param line       The line to add, must not be {@code null}.
     * @param indentSize The size of each indentation, must not be negative.
     * @param lineLength The length of the line, must not be negative.
     */
    private static void toLines(List<String> lines, String line, int indentSize, int lineLength) {
        int lineIndent = getIndentLevel(line);
        StringBuilder buf = new StringBuilder(256);

        String[] tokens = line.split(" +");

        for (String token : tokens) {
            if (buf.length() > 0) {
                if (buf.length() + token.length() >= lineLength) {
                    lines.add(buf.toString());
                    buf.setLength(0);
                    buf.append(repeat(" ", lineIndent * indentSize));
                } else {
                    buf.append(' ');
                }
            }

            for (int j = 0; j < token.length(); j++) {
                char c = token.charAt(j);
                if (c == '\t') {
                    buf.append(repeat(" ", indentSize - buf.length() % indentSize));
                } else if (c == '\u00A0') {
                    buf.append(' ');
                } else {
                    buf.append(c);
                }
            }
        }
        lines.add(buf.toString());
    }

    /**
     * Gets the indentation level of the specified line.
     *
     * @param line The line whose indentation level should be retrieved, must not be {@code null}.
     * @return The indentation level of the line.
     */
    private static int getIndentLevel(String line) {
        int level = 0;
        for (int i = 0; i < line.length() && line.charAt(i) == '\t'; i++) {
            level++;
        }
        for (int i = level + 1; i <= level + 4 && i < line.length(); i++) {
            if (line.charAt(i) == '\t') {
                level++;
                break;
            }
        }
        return level;
    }

    private String getPropertyFromExpression(String expression) {
        if (expression != null && expression.startsWith("${") && expression.endsWith("}")
            && !expression.substring(2).contains("${")) {
            // expression="${xxx}" -> property="xxx"
            return expression.substring(2, expression.length() - 1);
        }
        // no property can be extracted
        return null;
    }
}
