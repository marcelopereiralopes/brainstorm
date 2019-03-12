interface OrderCommand {
    fun execute()
}

class OrderAddCommand(private val id: Long): OrderCommand {
    override fun execute() = println("adding order with id: $id")
}

class OrderPayCommand(private val id: Long): OrderCommand {
    override fun execute() = println("paying for order with id: $id")
}

class CommandProcessor {
    private val queue = ArrayList<OrderCommand>()

    fun addToQueue(command: OrderCommand): CommandProcessor = apply { queue.add(command) }

    fun processCommand() {
        queue.forEach { it.execute() }
        queue.clear()
    }
}

fun main(args: Array<String>) {
    CommandProcessor()
            .addToQueue(OrderAddCommand(1L))
            .addToQueue(OrderAddCommand(2L))
            .addToQueue(OrderPayCommand(2L))
            .addToQueue(OrderPayCommand(1L))
            .processCommand()
}