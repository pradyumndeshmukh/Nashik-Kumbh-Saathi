package com.example.data

enum class CrowdLevel(val label: String, val hexColor: Long, val description: String) {
    LOW("Low Crowd", 0xFF2E7D32, "Smooth movement at ghats. Recommended for elderly pilgrims."),
    MODERATE("Moderate Crowd", 0xFFF57F17, "Manageable queue times. Expect 15-30 mins wait."),
    HIGH("High Crowd", 0xFFE65100, "Heavy rush at Ramkund & Kushavarta. Follow police barricades."),
    VERY_HIGH("Very High / Shahi Snan", 0xFFC62828, "Peak Holy Bath Days. Strict traffic diversions & walking zones in effect.")
}

data class SnanEvent(
    val id: String,
    val title: String,
    val dateText: String,
    val dateIso: String, // e.g. "2027-08-02"
    val ritualName: String,
    val significance: String,
    val ghatLocation: String,
    val crowdLevel: CrowdLevel,
    val isMainShahiSnan: Boolean,
    val timings: String,
    val guidelines: String,
    var isReminderSet: Boolean = false
)

enum class MapCategory(val label: String) {
    ALL("All Places"),
    GHATS("Holy Ghats"),
    TEMPLES("Temples"),
    CAMPS("Sadhugram Camps"),
    PARKING("Parking Zones"),
    MEDICAL("Medical Camps"),
    POLICE("Police Booths"),
    TOILETS("Clean Toilets"),
    WATER("Water Points")
}

data class MapLocation(
    val id: String,
    val name: String,
    val category: MapCategory,
    val area: String,
    val distanceKm: Double,
    val xRatio: Float, // Relative X coordinate for visual map canvas (0.0 to 1.0)
    val yRatio: Float, // Relative Y coordinate for visual map canvas (0.0 to 1.0)
    val description: String,
    val address: String,
    val timing: String,
    val contactPhone: String,
    val facilities: List<String>
)

enum class StayCategory(val label: String) {
    ALL("All Stays"),
    BUDGET("Budget Hotels"),
    MID_RANGE("Mid-Range"),
    LUXURY("Luxury Resorts"),
    DHARAMSHALA("Dharamshala"),
    TENT_CITY("Sadhugram Tent City")
}

data class StayListing(
    val id: String,
    val name: String,
    val category: StayCategory,
    val area: String,
    val locationHub: String = "Ramkund & Panchavati",
    val priceRange: String,
    val distanceToGhat: String,
    val rating: Double,
    val amenities: List<String>,
    val address: String,
    val phone: String,
    val mapQuery: String = ""
)

data class FoodListing(
    val id: String,
    val name: String,
    val typeText: String,
    val isFree: Boolean = false,
    val isVegOnly: Boolean = true,
    val area: String,
    val locationHub: String = "Ramkund & Panchavati",
    val timing: String,
    val description: String,
    val address: String,
    val phone: String,
    val rating: Double = 4.7,
    val category: String = "Restaurant",
    val priceForTwo: String = "₹200 for 2",
    val mapQuery: String = ""
)

data class TransportRoute(
    val id: String,
    val title: String,
    val fromHub: String,
    val toDestination: String,
    val mode: String,
    val estimatedTime: String,
    val fare: String,
    val frequency: String,
    val guidelines: String
)

data class EmergencyContact(
    val id: String,
    val title: String,
    val number: String,
    val category: String,
    val description: String,
    val isPrimary: Boolean = false
)

data class HealthAdvisory(
    val id: String,
    val title: String,
    val description: String,
    val category: String
)

data class CultureTopic(
    val id: String,
    val title: String,
    val summary: String,
    val detailedText: String,
    val iconName: String
)

enum class MedicalType(val label: String) {
    ALL("All Facilities"),
    HOSPITAL("Hospitals & Trauma"),
    MEDICAL_STORE("24x7 Medical Stores"),
    GOVT_CAMP("Govt Medical Posts")
}

data class MedicalFacility(
    val id: String,
    val name: String,
    val facilityType: MedicalType,
    val locationHub: String, // "Ramkund & Panchavati", "Trimbakeshwar", "Nashik Railway Station", "CBS Bus Stand", "Nimani Bus Stand", "Tapovan & Sadhugram", "Satpur & Ambad"
    val is24x7: Boolean = true,
    val address: String,
    val distance: String,
    val phone: String,
    val emergencyBeds: String = "",
    val services: List<String>,
    val mapQuery: String = ""
)

