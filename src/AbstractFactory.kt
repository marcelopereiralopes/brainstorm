interface Plan

class OrangePlan : Plan

class ApplePlan : Plan

abstract class PlanFactory {
    abstract fun makePlan(): Plan

    companion object {
        inline fun <reified T : Plan> createFactory(): PlanFactory = when (T::class){
            OrangePlan::class -> OrangeFactory()
            ApplePlan::class -> AppleFactory()
            else -> throw IllegalArgumentException()
        }
    }
}

class AppleFactory : PlanFactory() {
    override fun makePlan() = ApplePlan()
}

class OrangeFactory : PlanFactory() {
    override fun makePlan() = OrangePlan()
}

fun main(args: Array<String>){
    val orangeFactory = PlanFactory.createFactory<OrangePlan>()
    val orangePlan = orangeFactory.makePlan()
    println("Create plant $orangePlan")
}