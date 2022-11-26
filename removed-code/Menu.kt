import java.util.Scanner

open class Menu(private val options : List<MenuOption>) {
    fun use() {
        var input : String
        var choice : Int
        var success : Boolean?

        while (true) {
            display()
            input = readLine()!!
            try {
                choice = input.toInt()
            } catch (e : NumberFormatException) {
                choice = -1
            }
            success = applyChoice(choice)

            if (success == null) {
                break
            } else if (success) {
                // Success
            } else {
                // Bad input
                println("Please input a number in the range of 0 and ${options.size}")
            }

        }
    }


    private fun display() {
        var index = 0
        for (option in options) {
            index += 1
            println("$index) ${option.description}")
        }

        println("0) Exit")

    }

    private fun applyChoice(userInput : Int) : Boolean? {
        if (userInput < 0 || userInput > options.size) return false

        if (userInput == 0) return null


        options[userInput - 1].action()

        return true

    }
}