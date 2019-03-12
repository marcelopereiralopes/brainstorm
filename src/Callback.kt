import java.util.*
import kotlin.concurrent.thread

enum class Gender {
    MALE, FEMALE;
}

typealias UserId = Int

data class User(val id: UserId, val firstName: String, val lastName: String, val gender: Gender)

data class Fact(val id: Int, val value: String, val user: User? = null)

interface UserService {
    fun getFact(id: UserId): Fact
}

interface UserClient {
    fun getUser(id: UserId): User
}

interface FactClient {
    fun getFact(user: User): Fact
}

interface UserRepository {
    fun getUserById(id: UserId): User?
    fun insertUser(user: User)
}

interface FactRepository {
    fun getFactByUserId(id: UserId): Fact?
    fun insertFact(fact: Fact)
}

class MockUserClient : UserClient {
    override fun getUser(id: UserId): User {
        println("MockUserClient.getUser")
        Thread.sleep(500)
        return User(id, "Foo", "Bar", Gender.FEMALE)
    }
}

class MockFactClient : FactClient {
    override fun getFact(user: User): Fact {
        println("MockFactClient.getFact")
        Thread.sleep(500)
        return Fact(Random().nextInt(), "FACT ${user.firstName}, ${user.lastName}", user)
    }
}

class MockUserRepository : UserRepository {
    private val users = hashMapOf<UserId, User>()

    override fun getUserById(id: UserId): User? {
        println("MockUserRepository.getUserById")
        Thread.sleep(200)
        return users[id]
    }

    override fun insertUser(user: User) {
        println("MockUserRepository.insertUser")
        Thread.sleep(200)
        users[user.id] = user
    }
}

class MockFactRepository : FactRepository {

    private val facts = hashMapOf<UserId, Fact>()

    override fun getFactByUserId(id: UserId): Fact? {
        println("MockFactRepository.getFactByUserId")
        Thread.sleep(200)
        return facts[id]
    }

    override fun insertFact(fact: Fact) {
        println("MockFactRepository.insertFact")
        Thread.sleep(200)
        facts[fact.user?.id ?: 0] = fact
    }

}

class CallbackUserClient(private val client: UserClient) {
    fun getUser(id: Int, callback: (User) -> Unit) {
        thread {
            callback(client.getUser(id))
        }
    }
}

class CallbackFactClient(private val client: FactClient) {
    fun get(user: User, callback: (Fact) -> Unit) {
        thread {
            callback(client.getFact(user))
        }
    }
}

class CallbackUserRepository(private val userRepository: UserRepository) {
    fun getUserById(id: UserId, callback: (User?) -> Unit) {
        thread {
            callback(userRepository.getUserById(id))
        }
    }

    fun insertUser(user: User, callback: () -> Unit) {
        thread {
            userRepository.insertUser(user)
            callback()
        }

    }
}

class CallbackFactRepository(private val factRepository: FactRepository) {
    fun getFactByUserId(id: Int, callback: (Fact?) -> Unit) {
        thread {
            callback(factRepository.getFactByUserId(id))
        }
    }

    fun insertFact(fact: Fact, callback: () -> Unit) {
        thread {
            factRepository.insertFact(fact)
            callback()
        }
    }
}

class CallbackUserService(private val userClient: CallbackUserClient,
                          private val factClient: CallbackFactClient,
                          private val userRepository: CallbackUserRepository,
                          private val factRepository: CallbackFactRepository) : UserService {

    override fun getFact(id: UserId): Fact {
        var aux: Fact? = null
        userRepository.getUserById(id) { user ->
            if (user == null) {
                userClient.getUser(id) { userFromClient ->
                    userRepository.insertUser(userFromClient) {}
                    factClient.get(userFromClient) { fact ->
                        factRepository.insertFact(fact) {}
                        aux = fact
                    }

                }
            } else {
                factRepository.getFactByUserId(id) { fact ->
                    if (fact == null) {
                        factClient.get(user) { factFromClient ->
                            factRepository.insertFact(factFromClient) {}
                            aux = factFromClient
                        }
                    } else {
                        aux = fact
                    }
                }
            }
        }
        while (aux == null) {
            Thread.sleep(2)
        }
        return aux!!
    }
}

fun main(args: Array<String>){
    val userService = CallbackUserService(CallbackUserClient(MockUserClient()),
            CallbackFactClient(MockFactClient()),
            CallbackUserRepository(MockUserRepository()),
            CallbackFactRepository(MockFactRepository()))

    println(userService.getFact(1).toString())
}