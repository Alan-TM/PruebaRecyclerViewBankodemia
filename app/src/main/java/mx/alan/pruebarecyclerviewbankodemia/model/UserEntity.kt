package mx.alan.pruebarecyclerviewbankodemia.model

data class UserEntity(
    val email: String,
    val name: String,
    val lastName: String,
    val occupation: String,
    val birthDate: String,
    val password: String,
    val phone: String,
    val isPhoneVerified: Boolean,
    val identityImage: String,
    val identityImageType: String
)
