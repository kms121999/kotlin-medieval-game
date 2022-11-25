class Item(val name : String, private var qty : Int) {
    fun add(amount : Int) {
        qty += amount
    }

    fun destroy(amount : Int) : Int {
        val destroyCount = amount.coerceAtMost(qty)

        qty -= destroyCount

        return destroyCount
    }

    fun getQuantity() : Int {
        return qty
    }

    /**
     * Displays the item in format of "$qty $name"
     */
    fun display() {
        println("${getQuantity()} ${name}s")
    }
}