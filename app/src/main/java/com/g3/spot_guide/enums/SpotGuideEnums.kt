package com.g3.spot_guide.enums

// spots names will be same in all languages
enum class SpotType(val spotName: String) {
    SKATEPARK("Skatepark"),
    GAP("Gap"),
    BOX("Box"),
    STAIRS("Stairs"),
    HANDRAIL("Handrail"),
    DIY("DIY"),
    INDOOR("Indoor"),
    OTHER("Other")
}

enum class GroundType(val typeName: String) {
    HORRIBLE("Horrible"),
    BAD("Bad"),
    OK("Ok"),
    GOOD("Good"),
    PERFECT("Perfect")
}

enum class FirestoreEntityName(val collectionName: String) {
    SPOTS("spots"), USERS("users"), REVIEWS("reviews")
}