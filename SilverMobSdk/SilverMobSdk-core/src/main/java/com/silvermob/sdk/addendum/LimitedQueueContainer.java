/*
 *    Copyright 2018-2019 Prebid.org, Inc.
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package com.silvermob.sdk.addendum;

import java.util.LinkedList;
import java.util.Queue;

public class LimitedQueueContainer<T> {
    private Queue<T> list = new LinkedList<>();
    private final int limit;

    public LimitedQueueContainer(int limit) {
        if (limit < 0) {
            throw new IllegalArgumentException("Illegal Limit:" + limit);
        }

        this.limit = limit;
    }

    void add(T t) {

        list.add(t);

        if (list.size() > limit) {
            list.poll();
        }
    }

    boolean isFull() {
        return list.size() == limit;
    }

    Queue<T> getList() {
        return list;
    }
}
