/* Copyright 2013 Artem Melentyev <amelentev@gmail.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.github.braisdom.objsql.intellij.oo;

import java.util.HashMap;
import java.util.Map;

/** Caution this file is symlinked in all plugins */
public interface OOMethods {
    Map<String, String> binary = new HashMap<String, String>() {{
        put("+",    "plus");
        put("-",    "minus");
        put("*",    "times");
        put("/",    "div");
        put("%",    "rem");
        put("<",    "lt");
        put(">",    "gt");
        put("<=",   "le");
        put(">=",   "ge");
        put("&&",   "and");
        put("||",   "or");
    }};
    String revSuffix = "Rev";
    Map<String, String> unary = new HashMap<String, String>() {{
        put("-", "negate");     // jdk7
        put("---", "negate");   // jdk8
        put("~", "not");
    }};
    String   indexGet = "get";
    String[] indexSet = new String[]{"set", "put"};
    String[] valueOf = new String[]{"valueOf", "of"};
}
