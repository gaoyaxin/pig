/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.pig.impl.io;

import java.io.DataInput;
import java.io.IOException;

import org.apache.pig.data.BinInterSedes;
import org.apache.pig.data.Tuple;
import org.apache.pig.data.TupleFactory;

/**
 *
 */
public class NullableTuple extends PigNullableWritable {

    private TupleFactory mFactory = null;
    private static final BinInterSedes bis = new BinInterSedes();

    public NullableTuple() {
    }

    /**
     * @param t
     */
    public NullableTuple(Tuple t) {
        mValue = t;
    }

    public NullableTuple(NullableTuple copy) {
        setNull(copy.isNull());
        mValue = copy.mValue;
        setIndex(copy.getIndex());
    }

    @Override
    public Object getValueAsPigType() {
        return isNull() ? null : (Tuple)mValue;
    }

    @Override
    public void readFields(DataInput in) throws IOException {
        boolean nullness = in.readBoolean();
        setNull(nullness);
        // Free up the previous value for GC
        mValue = null;
        if (!nullness) {
            mValue = bis.readTuple(in);
        }
        setIndex(in.readByte());
    }

}
