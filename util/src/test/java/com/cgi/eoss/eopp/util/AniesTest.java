package com.cgi.eoss.eopp.util;


import com.google.protobuf.Any;
import com.google.protobuf.BoolValue;
import com.google.protobuf.ByteString;
import com.google.protobuf.BytesValue;
import com.google.protobuf.Int32Value;
import com.google.protobuf.Int64Value;
import com.google.protobuf.InvalidProtocolBufferException;
import com.google.protobuf.StringValue;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import static com.google.common.truth.Truth.assertThat;
import static com.google.common.truth.extensions.proto.ProtoTruth.assertThat;

@RunWith(JUnit4.class)
public class AniesTest {

    @Test
    public void testFromString() {
        assertThat(AniesKt.from("test")).isEqualTo(Any.pack(StringValue.of("test")));
    }

    @Test
    public void testFromInt() {
        assertThat(AniesKt.from(123)).isEqualTo(Any.pack(Int32Value.of(123)));
    }

    @Test
    public void testFromLong() {
        assertThat(AniesKt.from(123L)).isEqualTo(Any.pack(Int64Value.of(123L)));
    }

    @Test
    public void testFromBoolean() {
        assertThat(AniesKt.from(true)).isEqualTo(Any.pack(BoolValue.of(true)));
    }

    @Test
    public void testFromByteArray() {
        assertThat(AniesKt.from(new byte[]{})).isEqualTo(Any.pack(BytesValue.of(ByteString.EMPTY)));
    }

    @Test
    public void testJavaUnpack() {
        Any packedString = AniesKt.from("test");
        assertThat(AniesKt.unpack(packedString, StringValue.class)).isEqualTo(StringValue.of("test"));
    }

    @Test
    public void testJavaUnpackWrongType() {
        Any packedString = AniesKt.from("test");
        try {
            AniesKt.unpack(packedString, Int32Value.class);
        } catch (Exception e) {
            assertThat(e).isInstanceOf(InvalidProtocolBufferException.class);
            assertThat(e).hasMessageThat().isEqualTo("Type of the Any message does not match the given class.");
        }
    }

    @Test
    public void testJavaSafeUnpack() {
        Any packedString = AniesKt.from("test");
        assertThat(AniesKt.safeUnpack(packedString, StringValue.class)).hasValue(StringValue.of("test"));
    }

    @Test
    public void testJavaSafeUnpackWrongType() {
        Any packedString = AniesKt.from("test");
        assertThat(AniesKt.safeUnpack(packedString, Int32Value.class)).isEmpty();
    }

}
