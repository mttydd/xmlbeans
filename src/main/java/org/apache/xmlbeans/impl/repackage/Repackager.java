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

package org.apache.xmlbeans.impl.repackage;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Repackager {

    private final List<List<String>> _fromPackages = new ArrayList<>();
    private final List<List<String>> _toPackages = new ArrayList<>();

    private Matcher[] _fromMatchers;
    private String[] _toPackageNames;

    public Repackager(String repackageSpecs) {
        List<String> repackages = splitPath(repackageSpecs, ';');

        // Sort the repackage spec so that longer from's are first to match
        // longest package first

        for (; ; ) {
            boolean swapped = false;

            for (int i = 1; i < repackages.size(); i++) {
                String spec1 = repackages.get(i - 1);
                String spec2 = repackages.get(i);

                if (spec1.indexOf(':') < spec2.indexOf(':')) {
                    repackages.set(i - 1, spec2);
                    repackages.set(i, spec1);

                    swapped = true;
                }
            }

            if (!swapped) {
                break;
            }
        }

        for (String repackage : repackages) {

            int j = repackage.indexOf(':');

            if (j < 0 || repackage.indexOf(':', j + 1) >= 0) {
                throw new RuntimeException("Illegal repackage specification: " + repackage);
            }

            String from = repackage.substring(0, j);
            String to = repackage.substring(j + 1);

            _fromPackages.add(Repackager.splitPath(from, '.'));
            _toPackages.add(Repackager.splitPath(to, '.'));
        }

        _fromMatchers = new Matcher[_fromPackages.size() * 2];
        _toPackageNames = new String[_fromPackages.size() * 2];

        addPatterns('.', 0);
        addPatterns('/', _fromPackages.size());
    }

    void addPatterns(char sep, int off) {
        for (int i = 0; i < _fromPackages.size(); i++) {
            List<String> from = _fromPackages.get(i);
            List<String> to = _toPackages.get(i);

            String pattern = "";

            for (int j = 0; j < from.size(); j++) {
                if (j > 0) {
                    pattern += "\\" + sep;
                }

                pattern += from.get(j);
            }

            String toPackage = "";

            for (int j = 0; j < to.size(); j++) {
                if (j > 0) {
                    toPackage += sep;
                }

                toPackage += to.get(j);
            }

            _fromMatchers[off + i] = Pattern.compile(pattern).matcher("");
            _toPackageNames[off + i] = toPackage;
        }
    }

    public StringBuffer repackage(StringBuffer sb) {
        StringBuffer result = null;

        for (int i = 0; i < _fromMatchers.length; i++) {
            Matcher m = _fromMatchers[i];

            m.reset(sb);

            for (boolean found = m.find(); found; found = m.find()) {
                if (result == null) {
                    result = new StringBuffer();
                }

                m.appendReplacement(result, _toPackageNames[i]);
            }

            if (result != null) {
                m.appendTail(result);
                sb = result;
                result = null;
            }
        }

        return sb;
    }

    public List<List<String>> getFromPackages() {
        return _fromPackages;
    }

    public List<List<String>> getToPackages() {
        return _toPackages;
    }

    public static List<String> splitPath(String path, char separator) {
        ArrayList<String> components = new ArrayList<>();

        for (; ; ) {
            int i = path.indexOf(separator);

            if (i < 0) {
                break;
            }

            components.add(path.substring(0, i));

            path = path.substring(i + 1);
        }

        if (!path.isEmpty()) {
            components.add(path);
        }

        return components;
    }

    public static String dirForPath(String path) {
        return new File(path).getParent();
    }
}