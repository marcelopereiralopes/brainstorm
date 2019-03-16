interface Currency {
    val code: String
}

class Euro(override val code: String = "EUR") : Currency
class EUA(override val code: String = "USD") : Currency

enum class Country{
    UnitedStates, Spain, Uk, Greece
}

class CurrencyFactory {
    fun currencyForCountry(country: Country) : Currency? {
        return when(country){
            Country.UnitedStates -> EUA()
            Country.Greece, Country.Spain -> Euro()
            else -> null
        }
    }
}

fun main(args: Array<String>){
    val noCurrencyCode = "No Currency Code Available"
    val greeceCode = CurrencyFactory().currencyForCountry(Country.Greece)?.code ?: noCurrencyCode
    val usCode = CurrencyFactory().currencyForCountry(Country.UnitedStates)?.code ?: noCurrencyCode
    val ukCode = CurrencyFactory().currencyForCountry(Country.Uk)?.code ?: noCurrencyCode

    println("Greece currency: $greeceCode")
    println("US currency: $usCode")
    println("UK currency: $ukCode")
}