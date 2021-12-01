package org.kentunc.bittrader.common.infrastructure.webclient.http.connector

import org.springframework.core.ResolvableType
import org.springframework.core.io.buffer.DataBuffer
import org.springframework.core.io.buffer.DataBufferFactory
import org.springframework.http.codec.json.Jackson2JsonEncoder
import org.springframework.util.MimeType
import java.util.function.Consumer

class BodyProvidingJsonEncoder(private val bodyConsumer: Consumer<ByteArray>) : Jackson2JsonEncoder() {

    override fun encodeValue(
        value: Any,
        bufferFactory: DataBufferFactory,
        valueType: ResolvableType,
        mimeType: MimeType?,
        hints: MutableMap<String, Any>?
    ): DataBuffer {
        val data = super.encodeValue(value, bufferFactory, valueType, mimeType, hints)
        bodyConsumer.accept(extractBytes(data))
        return data
    }

    private fun extractBytes(data: DataBuffer): ByteArray {
        val bytes = ByteArray(data.readableByteCount())
        data.apply {
            read(bytes)
            readPosition(0)
        }
        return bytes
    }
}
