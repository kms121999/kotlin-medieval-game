// Set the power benefit per qty for each item
val itemPower = mapOf(
    "sword" to 1,
    "bow" to 3,
    "armour" to 3,
    "horse" to 7
)

// Set the purchase settings for each item
val itemCosts = mapOf(
    "sword" to mapOf("cost" to 100, "qty" to 50),
    "bow" to mapOf("cost" to 200, "qty" to 50),
    "armour" to mapOf("cost" to 200, "qty" to 50),
    "horse" to mapOf("cost" to 400, "qty" to 50)
)

class Item(val name : String, private var qty : Int) {
    companion object {
        /**
         * Get the purchase info for a given itemName
         */
        fun getPurchaseInfo(itemName : String) : Map<String, Int> {
            return itemCosts[itemName]!!
        }
    }

    /**
     * Add to the qty
     */
    fun add(amount : Int) {
        qty += amount
    }

    /**
     * Safely reduce the size of qty
     */
    fun destroy(amount : Int) : Int {
        val destroyCount = amount.coerceAtMost(qty)

        qty -= destroyCount

        return destroyCount
    }

    /**
     * Get the total power of all items of this type
     */
    fun getPower() :Int {
        return itemPower[this.name]!! * this.qty
    }

    /**
     * Get the qty
     */
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