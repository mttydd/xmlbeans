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

import java.io.OutputStream;
import java.io.Writer;
import java.io.IOException;

/**
 *
 */
public interface Filer
{

    /**
     * Creates a new schema binary file (.xsb) and returns a stream for writing to it.
     *
     * @param typename fully qualified type name
     * @return a stream to write the type to
     *
     * @throws IOException when the file can't be created
     */
    public OutputStream createBinaryFile(String typename) throws IOException;

    /**
     * Creates a new binding source file (.java) and returns a writer for it.
     *
     * @param typename fully qualified type name
     * @return a stream to write the type to
     *
     * @throws IOException when the file can't be created
     */
    default Writer createSourceFile(String typename) throws IOException {
        return createSourceFile(typename, null);
    }

    /**
     * Creates a new binding source file (.java) and returns a writer for it.
     *
     * @param typename fully qualified type name
     * @param sourceCodeEncoding an optional encoding used when compiling source code (can be <code>null</code>)
     * @return a stream to write the type to
     *
     * @throws IOException when the file can't be created
     */
    public Writer createSourceFile(String typename, String sourceCodeEncoding) throws IOException;
}
