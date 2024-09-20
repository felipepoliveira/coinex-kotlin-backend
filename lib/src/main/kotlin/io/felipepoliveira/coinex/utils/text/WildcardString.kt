package io.felipepoliveira.coinex.utils.text

class WildcardString(
    /**
     * The
     */
    private var content: String
) {

    /**
     * Store all wildcards found on the given raw content
     */
    private var wildcardsToProcess: Set<String>

    /**
     * Store the values provided by the user
     */
    private val values = mutableMapOf<String, String>()

    init {
        // Regular expression to match {{expression}}
        val mustacheRegex = Regex("\\{\\{([^{}]*)\\}\\}")

        // Find all matches and extract the content between the mustache brackets
        wildcardsToProcess = mustacheRegex.findAll(content).map { it.groupValues[1] }.toSet()
    }

    /**
     * Add a value to be processed by the toString()
     */
    fun addValue(key: String, value: String) {
        values[key] = value
    }

    fun toString(strict: Boolean = true): String {
        // check if the amount of values is different from the amount of wildcards
        if (values.size != wildcardsToProcess.size && strict) {
            throw Exception("Not all values were passed to replace all wildcards in the string. Number of given values: " +
                    "${values.size}; number of wildcards: ${wildcardsToProcess.size}")
        }

        for ((key, value) in values) {

            // check if the key passed in the values are not present in the wildcards to process
            if (!wildcardsToProcess.contains(key) && strict) {
                throw Exception("There is no wildcard '$key' in the provided String")
            }

            content = content.replace("{{$key}}", value)
        }

        return content
    }

    override fun toString(): String {
        return toString(true)
    }

}