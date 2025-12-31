data class Version(
    val major: Int,
    val minor: Int,
    val patch: Int
) {
    val code: Int get() = major * 1_000_000 + minor * 1_000 + patch
    val name: String get() = "${major}.${minor}.${patch}"
}
