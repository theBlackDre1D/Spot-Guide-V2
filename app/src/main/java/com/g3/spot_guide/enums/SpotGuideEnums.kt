package com.g3.spot_guide.enums

enum class SpotType {
    SKATEPARK, GAP, BOX, STAIRS, HANDRAIL, DIY, OTHER
}

enum class GroundType {
    HORRIBLE, BAD, OK, GOOD, PERFECT
}

enum class FirestoreEntityName(val collectionName: String) {
    SPOTS("spots")
}