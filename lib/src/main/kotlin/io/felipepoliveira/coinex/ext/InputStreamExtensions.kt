package io.felipepoliveira.coinex.ext

import java.io.InputStream
import java.nio.charset.Charset

/**
 * Read the content of the InputStream as a String. The charset parameter will be used to parse the byte[] into a
 * specific encoding.
 */
fun InputStream.readAllToString(charset: Charset = Charsets.UTF_8): String = String(this.readAllBytes(), charset)