package parser.values

sealed interface JsonValue

object JsonValueTrue : JsonValue {
    override fun toString(): String = "true"
}

object JsonValueFalse : JsonValue {
    override fun toString(): String = "false"
}

object JsonValueNull : JsonValue {
    override fun toString(): String = "null"
}

data class JsonValueNumber(val value: Double) : JsonValue {
    override fun toString(): String = "Number($value)"
}

data class JsonValueString(val value: String) : JsonValue {
    override fun toString(): String = "String($value)"
}

data class JsonValueArray(val value: List<JsonValue>) : JsonValue {
    override fun toString(): String = "Array($value)"
}

data class JsonValueObject(val value: Map<String, JsonValue>) : JsonValue {
    override fun toString(): String = "Object($value)"
}
