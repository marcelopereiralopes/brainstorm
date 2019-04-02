interface CoffeeMachine {
    fun makeSmallCoffee()
    fun makeLargeCoffee()
}

class NormalCoffeeMachine : CoffeeMachine {
    override fun makeSmallCoffee() = println("Normal: Making small coffee")


    override fun makeLargeCoffee() = println("Normal: Making large coffee")
}

class EnhancedCoffeeMachine(private val coffeeMachine: CoffeeMachine): CoffeeMachine by coffeeMachine {

    override fun makeLargeCoffee() {
        println("Enhanced: Making large coffee")
        coffeeMachine.makeLargeCoffee()
    }

    fun makeCoffeeWithMilk() {
        println("Enhanced: Making coffee with milk")
        coffeeMachine.makeSmallCoffee()
        println("Enhanced: Adding milk")
    }

}

fun main(args: Array<String>){
    val normalMachine = NormalCoffeeMachine()
    val enhancedMachine = EnhancedCoffeeMachine(normalMachine)

    enhancedMachine.makeSmallCoffee()
    enhancedMachine.makeLargeCoffee()
    enhancedMachine.makeCoffeeWithMilk()
}