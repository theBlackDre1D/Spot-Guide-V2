package com.g3.spot_guide.enums

// spots names will be same in all languages
enum class SpotType(val spotName: String) {
    SKATEPARK("Skatepark"),
    GAP("Gap"),
    BOX("Box"),
    STAIRS("Stairs"),
    HANDRAIL("Handrail"),
    DIY("DIY"),
    OTHER("Other")
}

enum class GroundType {
    HORRIBLE, BAD, OK, GOOD, PERFECT
}

enum class FirestoreEntityName(val collectionName: String) {
    SPOTS("spots")
}