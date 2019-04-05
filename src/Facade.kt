class ComplexSystemStore(private val filePath: String) {

    init {
        println("Reading data from file: $filePath")
    }

    private val store = HashMap<String, String>()

    fun store(key: String, payload: String) {
        store[key] = payload
    }

    fun read(key: String): String = store[key] ?: ""

    fun commit() = println("Storing cached data: $store to file: $filePath")
}

data class User1(val login: String)

class UserRepository1 {
    private val systemPreferences = ComplexSystemStore("/data/default.prefs")

    fun save(user: User1) {
        systemPreferences.store("USER_KEY", user.login)
        systemPreferences.commit()
    }

    fun findFirst(): User1 = User1(systemPreferences.read("USER_KEY"))
}

fun main(args: Array<String>) {
    val userRepository = UserRepository1()
    val user = User1("dbacinski")
    userRepository.save(user)
    val resultUser = userRepository.findFirst()
    println("Found stored user: $resultUser")
}