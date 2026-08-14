package com.example.data

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

object KumbhDataRepository {

    // Current Crowd Advisory State
    private val _currentCrowdAdvisory = MutableStateFlow(CrowdLevel.MODERATE)
    val currentCrowdAdvisory: StateFlow<CrowdLevel> = _currentCrowdAdvisory.asStateFlow()

    fun updateCrowdAdvisory(level: CrowdLevel) {
        _currentCrowdAdvisory.value = level
    }

    // Reminders State
    private val _reminderEventIds = MutableStateFlow<Set<String>>(setOf("snan_1"))
    val reminderEventIds: StateFlow<Set<String>> = _reminderEventIds.asStateFlow()

    fun toggleReminder(eventId: String) {
        val current = _reminderEventIds.value.toMutableSet()
        if (current.contains(eventId)) {
            current.remove(eventId)
        } else {
            current.add(eventId)
        }
        _reminderEventIds.value = current
    }

    // Core Snan & Ritual Calendar Dataset
    val snanEvents = listOf(
        SnanEvent(
            id = "snan_0",
            title = "Dhwajarohan (Flag Hoisting)",
            dateText = "31 October 2026",
            dateIso = "2026-10-31",
            ritualName = "Simhastha Kumbh Flag Hoisting Ceremony",
            significance = "Official commencement of Simhastha Kumbh Mela 2027 with flag hoisting at Ramkund, Panchavati & Kushavarta Kund.",
            ghatLocation = "Ramkund (Nashik) & Kushavarta Kund (Trimbakeshwar)",
            crowdLevel = CrowdLevel.HIGH,
            isMainShahiSnan = false,
            timings = "06:00 AM - 09:00 AM",
            guidelines = "VVIP movement expected. Arrive early at Panchavati ghats. Use designated footpaths."
        ),
        SnanEvent(
            id = "snan_1",
            title = "First Shahi / Amrit Snan",
            dateText = "2 August 2027",
            dateIso = "2027-08-02",
            ritualName = "Pratham Shahi Snan",
            significance = "First major holy dip taken by Mahants & Akharas procession followed by millions of devotees.",
            ghatLocation = "Ramkund (Nashik) & Kushavarta Kund (Trimbak)",
            crowdLevel = CrowdLevel.VERY_HIGH,
            isMainShahiSnan = true,
            timings = "03:00 AM - 06:00 PM (Akharas 03:00 AM - 11:00 AM)",
            guidelines = "General public permitted at auxiliary ghats during morning Akhara Snan. No heavy vehicles near Panchavati."
        ),
        SnanEvent(
            id = "snan_2",
            title = "Main Shahi / Amrit Snan",
            dateText = "31 August 2027",
            dateIso = "2027-08-31",
            ritualName = "Mukhya Shahi Snan (Shravan Amavasya)",
            significance = "The most auspicious day of Simhastha Kumbh Mela. Celestial alignment brings celestial nectar energy to the Godavari.",
            ghatLocation = "Ramkund Nashik & Kushavarta Kund Trimbakeshwar",
            crowdLevel = CrowdLevel.VERY_HIGH,
            isMainShahiSnan = true,
            timings = "02:00 AM - 08:00 PM",
            guidelines = "Maximum crowd density. Vehicle entry banned within 5km zone. Use free shuttle buses from Tapovan Parking."
        ),
        SnanEvent(
            id = "snan_3",
            title = "Final Shahi / Amrit Snan",
            dateText = "11 - 12 September 2027",
            dateIso = "2027-09-11",
            ritualName = "Antim Shahi Snan (Bhadrapada Purnima)",
            significance = "Final royal bathing procession of Akharas marking the conclusion of peak ritual baths.",
            ghatLocation = "Ramkund, Laxman Kund & Kushavarta Kund",
            crowdLevel = CrowdLevel.VERY_HIGH,
            isMainShahiSnan = true,
            timings = "03:00 AM - 06:00 PM",
            guidelines = "Follow holding area gates. Maintain calm in queues near pontoon bridges."
        ),
        SnanEvent(
            id = "snan_4",
            title = "Rishi Panchami Parva Snan",
            dateText = "5 September 2027",
            dateIso = "2027-09-05",
            ritualName = "Rishi Panchami Holy Bath",
            significance = "Special holy bath honoring ancient Saptarshis (sages) and seeking spiritual purity.",
            ghatLocation = "Panchavati Ghats & Trimbakeshwar Kund",
            crowdLevel = CrowdLevel.HIGH,
            isMainShahiSnan = false,
            timings = "05:00 AM - 05:00 PM",
            guidelines = "High rush of family pilgrims. Keep lost-and-found tags on children."
        ),
        SnanEvent(
            id = "snan_5",
            title = "Kumbh Parikrama & Holy Walk",
            dateText = "18 September 2027",
            dateIso = "2027-09-18",
            ritualName = "Godavari Parikrama Yatra",
            significance = "14km circumambulation walk along sacred Godavari banks and Panchavati holy sites.",
            ghatLocation = "Nashik Parikrama Route to Tapovan",
            crowdLevel = CrowdLevel.MODERATE,
            isMainShahiSnan = false,
            timings = "05:00 AM - 01:00 PM",
            guidelines = "Wear comfortable walking footwear. Water points available every 500m."
        ),
        SnanEvent(
            id = "snan_6",
            title = "Closing Ceremony (Samapan)",
            dateText = "24 July 2028",
            dateIso = "2028-07-24",
            ritualName = "Kumbh Mela Dhwaj Avataran",
            significance = "Formal ceremonial lowering of the Kumbh flag and closing Maha Aarti.",
            ghatLocation = "Trimbakeshwar Temple & Ramkund Nashik",
            crowdLevel = CrowdLevel.MODERATE,
            isMainShahiSnan = false,
            timings = "05:00 PM - 09:00 PM",
            guidelines = "Special Maha Aarti at sunset. Seating available at Ramkund Amphitheatre."
        )
    )

    // Interactive Map Locations
    val mapLocations = listOf(
        MapLocation(
            id = "loc_1",
            name = "Ramkund Ghat",
            category = MapCategory.GHATS,
            area = "Panchavati, Nashik",
            distanceKm = 0.8,
            xRatio = 0.42f,
            yRatio = 0.35f,
            description = "The central sacred bathing ghat on river Godavari where Lord Rama performed rituals.",
            address = "Panchavati Ghat Road, Nashik",
            timing = "Open 24 Hours",
            contactPhone = "+91 253 2571000",
            facilities = listOf("Bathing Railings", "Changing Rooms", "Life Guards", "CCTV Zone")
        ),
        MapLocation(
            id = "loc_2",
            name = "Kushavarta Kund",
            category = MapCategory.GHATS,
            area = "Trimbakeshwar",
            distanceKm = 28.0,
            xRatio = 0.18f,
            yRatio = 0.65f,
            description = "Holy pond where river Godavari originates; primary Snan venue for Akharas in Trimbak.",
            address = "Near Trimbakeshwar Temple, Trimbak",
            timing = "04:00 AM - 10:00 PM",
            contactPhone = "+91 253 2572100",
            facilities = listOf("Akshaya Kund", "Security Holding Zone", "Medical Post")
        ),
        MapLocation(
            id = "loc_3",
            name = "Sadhugram (Akhara City)",
            category = MapCategory.CAMPS,
            area = "Tapovan, Nashik",
            distanceKm = 2.5,
            xRatio = 0.65f,
            yRatio = 0.40f,
            description = "Tent city housing 13 Akharas, thousands of Sadhus, and cultural discourse pandals.",
            address = "Tapovan Road, Nashik",
            timing = "06:00 AM - 10:00 PM",
            contactPhone = "+91 253 2573000",
            facilities = listOf("Tent Stays", "Free Bhandara", "Discourse Halls", "Fire Station")
        ),
        MapLocation(
            id = "loc_4",
            name = "Trimbakeshwar Jyotirlinga",
            category = MapCategory.TEMPLES,
            area = "Trimbakeshwar",
            distanceKm = 28.2,
            xRatio = 0.15f,
            yRatio = 0.70f,
            description = "One of the 12 sacred Jyotirlingas of Lord Shiva with three faces representing Brahma, Vishnu, Shiva.",
            address = "Trimbak City Center",
            timing = "05:30 AM - 09:00 PM",
            contactPhone = "+91 253 2580100",
            facilities = listOf("E-Pass Queue", "Shoe Counter", "VIP Line", "Drinking Water")
        ),
        MapLocation(
            id = "loc_5",
            name = "Sri Kalaram Sansthan Temple",
            category = MapCategory.TEMPLES,
            area = "Panchavati, Nashik",
            distanceKm = 0.5,
            xRatio = 0.48f,
            yRatio = 0.28f,
            description = "Historic black stone temple dedicated to Lord Rama built in 1792.",
            address = "Kalaram Temple Marg, Panchavati",
            timing = "05:00 AM - 10:00 PM",
            contactPhone = "+91 253 2512000",
            facilities = listOf("Prasad Counter", "Sitting Lawn", "Security Checking")
        ),
        MapLocation(
            id = "loc_6",
            name = "Tapovan Mega Parking Hub",
            category = MapCategory.PARKING,
            area = "Tapovan Outer Ring",
            distanceKm = 3.2,
            xRatio = 0.78f,
            yRatio = 0.48f,
            description = "Primary designated parking zone with capacity for 50,000 buses and cars with free shuttle connectivity.",
            address = "Nashik-Pune Highway Junction, Tapovan",
            timing = "Open 24 Hours",
            contactPhone = "+91 253 2590000",
            facilities = listOf("Free E-Shuttle", "E-Vehicle Charging", "Public Toilets", "Help Booth")
        ),
        MapLocation(
            id = "loc_7",
            name = "Kumbh Central Medical Post A",
            category = MapCategory.MEDICAL,
            area = "Ramkund Police Ground",
            distanceKm = 0.6,
            xRatio = 0.38f,
            yRatio = 0.32f,
            description = "24x7 emergency field hospital with 50 beds, doctors, and critical care ambulances.",
            address = "Ramkund North Entry Gate",
            timing = "Open 24 Hours",
            contactPhone = "108 / +91 253 2570108",
            facilities = listOf("ICU Ambulances", "Heat Stroke Ward", "Free Medicine", "First Aid")
        ),
        MapLocation(
            id = "loc_8",
            name = "Panchavati Police Control Booth",
            category = MapCategory.POLICE,
            area = "Panchavati Circle",
            distanceKm = 0.4,
            xRatio = 0.45f,
            yRatio = 0.25f,
            description = "Main crowd monitor booth equipped with CCTV feeds, public address system & lost-found registration.",
            address = "Panchavati Karanja, Nashik",
            timing = "Open 24 Hours",
            contactPhone = "112 / +91 253 2571112",
            facilities = listOf("Lost & Found Desk", "Public Announcement", "PA System", "CCTV Monitoring")
        ),
        MapLocation(
            id = "loc_9",
            name = "Smart Toilet Block #12",
            category = MapCategory.TOILETS,
            area = "Ramkund Promenade",
            distanceKm = 0.7,
            xRatio = 0.52f,
            yRatio = 0.36f,
            description = "Modern bio-toilet facility maintained 24x7 with running water and sanitation staff.",
            address = "Godavari Bank Walkway",
            timing = "Open 24 Hours",
            contactPhone = "1800-233-1122",
            facilities = listOf("Wheelchair Friendly", "Soap Dispensers", "Disability Ramp")
        ),
        MapLocation(
            id = "loc_10",
            name = "Pure Chilled Water Kiosk #5",
            category = MapCategory.WATER,
            area = "Sadhugram Gate 2",
            distanceKm = 2.1,
            xRatio = 0.62f,
            yRatio = 0.42f,
            description = "RO purified chilled drinking water refill station installed by Nashik Municipal Corp.",
            address = "Tapovan Road Gate 2",
            timing = "Open 24 Hours",
            contactPhone = "1800-233-1122",
            facilities = listOf("RO Water", "Free Bottle Refill", "Shaded Rest Area")
        )
    )

    // Stay & Hotels Dataset
    val stayListings = listOf(
        // 1. RAMKUND & PANCHAVATI
        StayListing(
            id = "stay_ram_1",
            name = "Sri Kalaram Sansthan Dharamshala",
            category = StayCategory.DHARAMSHALA,
            area = "Panchavati",
            locationHub = "Ramkund & Panchavati",
            priceRange = "₹300 - ₹800 / night",
            distanceToGhat = "0.4 km to Ramkund",
            rating = 4.7,
            amenities = listOf("Pure Veg Canteen", "Community Hall", "Solar Hot Water", "Locker Facility"),
            address = "Near Kalaram Temple, Panchavati, Nashik",
            phone = "+91 253 2512000",
            mapQuery = "Kalaram Temple Panchavati Nashik"
        ),
        StayListing(
            id = "stay_ram_2",
            name = "Godavari Riverview Pilgrim Lodge",
            category = StayCategory.MID_RANGE,
            area = "Panchavati Promenade",
            locationHub = "Ramkund & Panchavati",
            priceRange = "₹3,500 - ₹5,500 / night",
            distanceToGhat = "0.3 km to Ramkund",
            rating = 4.5,
            amenities = listOf("River View Balcony", "Satvik Meal Service", "Elevator", "Power Backup", "WiFi"),
            address = "Panchavati Ghat Promenade, Nashik",
            phone = "+91 94222 55443",
            mapQuery = "Panchavati Ghat Promenade Nashik"
        ),
        StayListing(
            id = "stay_ram_3",
            name = "Shree Swami Samarth Bhakta Niwas",
            category = StayCategory.DHARAMSHALA,
            area = "Panchavati Karanja",
            locationHub = "Ramkund & Panchavati",
            priceRange = "₹250 - ₹500 / night",
            distanceToGhat = "0.5 km to Ramkund",
            rating = 4.6,
            amenities = listOf("Clean Dormitories", "Filtered Water", "Morning Tea", "Luggage Storage"),
            address = "Panchavati Karanja Market, Nashik",
            phone = "+91 253 2511234",
            mapQuery = "Panchavati Karanja Nashik"
        ),
        StayListing(
            id = "stay_ram_4",
            name = "Hotel Panchavati Yatri",
            category = StayCategory.BUDGET,
            area = "Panchavati / Vakil Wadi",
            locationHub = "Ramkund & Panchavati",
            priceRange = "₹2,200 - ₹3,800 / night",
            distanceToGhat = "0.8 km to Ramkund",
            rating = 4.4,
            amenities = listOf("AC Rooms", "Free WiFi", "In-house Pure Veg Restaurant", "Parking", "Travel Desk"),
            address = "430 Vakil Wadi, MG Road, Nashik",
            phone = "+91 253 2570101",
            mapQuery = "Hotel Panchavati Yatri Nashik"
        ),

        // 2. TRIMBAKESHWAR
        StayListing(
            id = "stay_tri_1",
            name = "Trimbak Bhakta Niwas Trust",
            category = StayCategory.DHARAMSHALA,
            area = "Trimbakeshwar Temple Zone",
            locationHub = "Trimbakeshwar",
            priceRange = "₹250 - ₹600 / night",
            distanceToGhat = "0.3 km to Kushavarta Kund",
            rating = 4.6,
            amenities = listOf("Temple Proximity", "Clean Bedding", "Solar Hot Water", "Safe Drinking Water"),
            address = "Trimbak Temple Ring Road, Trimbakeshwar",
            phone = "+91 2594 233022",
            mapQuery = "Trimbakeshwar Temple Ring Road"
        ),
        StayListing(
            id = "stay_tri_2",
            name = "Gajanan Maharaj Sansthan Bhakta Niwas",
            category = StayCategory.DHARAMSHALA,
            area = "Trimbakeshwar",
            locationHub = "Trimbakeshwar",
            priceRange = "₹200 - ₹500 / night",
            distanceToGhat = "0.5 km to Kushavarta Kund",
            rating = 4.8,
            amenities = listOf("Pure Satvik Mahaprasad", "Spacious Parking", "Disability Elevator", "24x7 Water"),
            address = "Near Bus Stand, Trimbakeshwar",
            phone = "+91 2594 233150",
            mapQuery = "Gajanan Maharaj Sansthan Trimbakeshwar"
        ),
        StayListing(
            id = "stay_tri_3",
            name = "Hotel Royal Heritage Trimbak",
            category = StayCategory.MID_RANGE,
            area = "Trimbak Highway",
            locationHub = "Trimbakeshwar",
            priceRange = "₹3,200 - ₹5,000 / night",
            distanceToGhat = "0.8 km to Kushavarta Kund",
            rating = 4.3,
            amenities = listOf("AC Deluxe Rooms", "In-house Thali Restaurant", "Hot Showers", "Free Parking"),
            address = "Main Highway Rd, Trimbakeshwar",
            phone = "+91 98225 66778",
            mapQuery = "Hotel Royal Heritage Trimbakeshwar"
        ),
        StayListing(
            id = "stay_tri_4",
            name = "Grape County Eco Resort & Villas",
            category = StayCategory.LUXURY,
            area = "Anjaneri / Trimbak Road",
            locationHub = "Trimbakeshwar",
            priceRange = "₹8,500 - ₹16,000 / night",
            distanceToGhat = "6.0 km to Kushavarta Kund",
            rating = 4.8,
            amenities = listOf("5-Star Eco Villas", "Lake View", "Swimming Pool", "Organic Pure Veg Restaurant"),
            address = "Anjaneri, Trimbak Road, Nashik",
            phone = "+91 70309 15009",
            mapQuery = "Grape County Eco Resort Nashik"
        ),

        // 3. NASHIK RAILWAY STATION
        StayListing(
            id = "stay_rlw_1",
            name = "Central Railway Executive Retiring Rooms",
            category = StayCategory.BUDGET,
            area = "Nashik Road Station",
            locationHub = "Nashik Railway Station",
            priceRange = "₹400 - ₹1,200 / night",
            distanceToGhat = "0.0 km from Station Exit",
            rating = 4.4,
            amenities = listOf("Inside Station Premises", "AC & Non-AC Dorms", "24x7 Railway Security", "Hot Bath"),
            address = "Platform 1, Nashik Road Railway Station",
            phone = "+91 253 2465222",
            mapQuery = "Nashik Road Railway Station"
        ),
        StayListing(
            id = "stay_rlw_2",
            name = "Hotel City Palace (Nashik Road)",
            category = StayCategory.BUDGET,
            area = "Datta Mandir Road",
            locationHub = "Nashik Railway Station",
            priceRange = "₹1,800 - ₹2,800 / night",
            distanceToGhat = "0.4 km from Railway Station",
            rating = 4.3,
            amenities = listOf("AC Rooms", "Room Service", "Elevator", "Power Backup", "Taxi Stand Nearby"),
            address = "Datta Mandir Stop, Nashik Road",
            phone = "+91 253 2461010",
            mapQuery = "Hotel City Palace Nashik Road"
        ),
        StayListing(
            id = "stay_rlw_3",
            name = "Hotel Express Inn Executive Lodge",
            category = StayCategory.MID_RANGE,
            area = "Nashik Road Market",
            locationHub = "Nashik Railway Station",
            priceRange = "₹3,800 - ₹6,000 / night",
            distanceToGhat = "0.6 km from Station",
            rating = 4.5,
            amenities = listOf("Complimentary Breakfast", "Pure Veg Restaurant", "Free WiFi", "24x7 Front Desk"),
            address = "Opposite Station Bus Depot, Nashik Road",
            phone = "+91 253 2462222",
            mapQuery = "Nashik Road Station Market"
        ),

        // 4. CBS BUS STAND
        StayListing(
            id = "stay_cbs_1",
            name = "Hotel President Plaza (CBS Circle)",
            category = StayCategory.MID_RANGE,
            area = "CBS / MG Road",
            locationHub = "CBS Bus Stand",
            priceRange = "₹3,200 - ₹5,200 / night",
            distanceToGhat = "0.3 km from CBS Bus Terminal",
            rating = 4.5,
            amenities = listOf("Central Location", "AC Deluxe Rooms", "In-House Multi-Cuisine", "Elevator"),
            address = "Old Agra Road, Near CBS Terminal, Nashik",
            phone = "+91 253 2578888",
            mapQuery = "President Plaza CBS Nashik"
        ),
        StayListing(
            id = "stay_cbs_2",
            name = "Radisson Blu Hotel & Spa Nashik",
            category = StayCategory.LUXURY,
            area = "Pathardi / CBS Access Corridor",
            locationHub = "CBS Bus Stand",
            priceRange = "₹9,000 - ₹18,000 / night",
            distanceToGhat = "4.0 km to Ramkund",
            rating = 4.9,
            amenities = listOf("5-Star Luxury", "Luxury Spa & Pool", "Fine Dining", "Valet Parking", "Airport Shuttle"),
            address = "State Highway 26, Nashik",
            phone = "+91 253 6644444",
            mapQuery = "Radisson Blu Hotel Nashik"
        ),
        StayListing(
            id = "stay_cbs_3",
            name = "Central Bus Stand Yatri Niwas",
            category = StayCategory.DHARAMSHALA,
            area = "CBS Campus",
            locationHub = "CBS Bus Stand",
            priceRange = "₹300 - ₹700 / night",
            distanceToGhat = "0.1 km from CBS Bus Stand",
            rating = 4.2,
            amenities = listOf("Cloak Room", "Clean Bathrooms", "24x7 Water", "Direct City Bus Access"),
            address = "CBS Bus Depot Premises, Nashik",
            phone = "+91 253 2573333",
            mapQuery = "Central Bus Stand CBS Nashik"
        ),

        // 5. NIMANI BUS STAND
        StayListing(
            id = "stay_nim_1",
            name = "Nimani Pilgrim Guest House",
            category = StayCategory.DHARAMSHALA,
            area = "Nimani Depot Area",
            locationHub = "Nimani Bus Stand",
            priceRange = "₹350 - ₹750 / night",
            distanceToGhat = "0.8 km to Ramkund",
            rating = 4.3,
            amenities = listOf("Walking Distance to Ramkund", "Filtered Drinking Water", "Hot Water", "Canteen"),
            address = "Nimani Bus Stand Complex, Panchavati, Nashik",
            phone = "+91 253 2514000",
            mapQuery = "Nimani Bus Stand Panchavati Nashik"
        ),
        StayListing(
            id = "stay_nim_2",
            name = "Hotel Panchavati Elite (Nimani Circle)",
            category = StayCategory.BUDGET,
            area = "Panchavati College Road",
            locationHub = "Nimani Bus Stand",
            priceRange = "₹2,000 - ₹3,200 / night",
            distanceToGhat = "0.9 km to Ramkund",
            rating = 4.4,
            amenities = listOf("Clean AC Rooms", "Pure Veg Dining", "Free WiFi", "Travel Helpdesk"),
            address = "Panchavati College Road, Near Nimani Circle, Nashik",
            phone = "+91 98220 77889",
            mapQuery = "Panchavati College Road Nimani Nashik"
        ),

        // 6. TAPOVAN & SADHUGRAM
        StayListing(
            id = "stay_tap_1",
            name = "Kumbh Sadhugram Deluxe Tent City",
            category = StayCategory.TENT_CITY,
            area = "Sadhugram, Tapovan",
            locationHub = "Tapovan & Sadhugram",
            priceRange = "₹1,800 - ₹4,800 / night",
            distanceToGhat = "1.8 km to Ramkund",
            rating = 4.7,
            amenities = listOf("Swiss Luxury Tents", "Satvik Food Included", "24x7 Security", "Shuttle Service"),
            address = "Tapovan Sadhugram Sector 3, Nashik",
            phone = "+91 98230 11223",
            mapQuery = "Tapovan Sadhugram Nashik"
        ),
        StayListing(
            id = "stay_tap_2",
            name = "Akhara Parishad Pilgrim Camp Tents",
            category = StayCategory.TENT_CITY,
            area = "Sadhugram Sector 1",
            locationHub = "Tapovan & Sadhugram",
            priceRange = "₹500 - ₹1,200 / night",
            distanceToGhat = "2.0 km to Ramkund",
            rating = 4.6,
            amenities = listOf("Community Satsang Hall", "Free Langar Meal", "Shared Bathrooms", "Police Booth Nearby"),
            address = "Sadhugram Main Sector 1, Tapovan, Nashik",
            phone = "+91 253 2578899",
            mapQuery = "Sadhugram Tapovan Sector 1"
        ),
        StayListing(
            id = "stay_tap_3",
            name = "Tapovan Riverside Cottage Stay",
            category = StayCategory.MID_RANGE,
            area = "Tapovan Godavari Bank",
            locationHub = "Tapovan & Sadhugram",
            priceRange = "₹3,000 - ₹5,000 / night",
            distanceToGhat = "1.5 km to Ramkund",
            rating = 4.4,
            amenities = listOf("Peaceful Garden", "Power Backup", "In-House Satvik Kitchen", "Spacious Parking"),
            address = "Godavari Bank, Tapovan, Nashik",
            phone = "+91 94220 66778",
            mapQuery = "Tapovan Riverside Nashik"
        ),

        // 7. SATPUR & AMBAD
        StayListing(
            id = "stay_sat_1",
            name = "The Taj Gateway Hotel Ambad",
            category = StayCategory.LUXURY,
            area = "Ambad MIDC",
            locationHub = "Satpur & Ambad",
            priceRange = "₹9,500 - ₹19,000 / night",
            distanceToGhat = "8.0 km to Ramkund",
            rating = 4.9,
            amenities = listOf("5-Star Taj Hospitality", "Swimming Pool", "Spa & Gym", "Fine Dining", "Valet"),
            address = "P-17, Ambad MIDC, Mumbai-Agra Road, Nashik",
            phone = "+91 253 6692300",
            mapQuery = "The Gateway Hotel Ambad Nashik"
        ),
        StayListing(
            id = "stay_sat_2",
            name = "Hotel Grand Rio (Satpur)",
            category = StayCategory.MID_RANGE,
            area = "Satpur MIDC / Trimbak Road",
            locationHub = "Satpur & Ambad",
            priceRange = "₹3,800 - ₹6,500 / night",
            distanceToGhat = "6.5 km to Ramkund",
            rating = 4.6,
            amenities = listOf("AC Rooms", "Free WiFi", "Pure Veg Restaurant", "Conference Hall", "Parking"),
            address = "Trimbak Road, Satpur MIDC, Nashik",
            phone = "+91 253 2355555",
            mapQuery = "Hotel Grand Rio Satpur Nashik"
        )
    )

    // Restaurants & Local Misal Food Directory
    val foodListings = listOf(
        // 1. RAMKUND & PANCHAVATI
        FoodListing(
            id = "food_ram_1",
            name = "Ambika Misal Center",
            typeText = "Famous Kala Rassa Misal Specialty",
            isFree = false,
            isVegOnly = true,
            area = "Panchavati Karanja",
            locationHub = "Ramkund & Panchavati",
            timing = "07:30 AM - 04:00 PM",
            description = "Iconic local spicy Kala Rassa Misal seasoned with secret handmade Maharashtrian spices & soft pav.",
            address = "Panchavati Karanja, Near Kalaram Temple, Nashik",
            phone = "+91 94222 78910",
            rating = 4.8,
            category = "Misal Pav",
            priceForTwo = "₹180 for 2",
            mapQuery = "Ambika Misal Center Panchavati Nashik"
        ),
        FoodListing(
            id = "food_ram_2",
            name = "Sri Kalaram Temple Bhojanalaya",
            typeText = "Authentic Satvik Unlimited Thali",
            isFree = false,
            isVegOnly = true,
            area = "Kalaram Temple Gate 1",
            locationHub = "Ramkund & Panchavati",
            timing = "11:30 AM - 03:00 PM & 07:00 PM - 09:30 PM",
            description = "Traditional pure satvik Maharashtrian meals served on banana leaves with Puran Poli, Varan Bhat & Solkadhi.",
            address = "Inside Kalaram Sansthan Premises, Panchavati, Nashik",
            phone = "+91 253 2512000",
            rating = 4.7,
            category = "Pure Veg Thali",
            priceForTwo = "₹240 for 2",
            mapQuery = "Kalaram Temple Panchavati Nashik"
        ),
        FoodListing(
            id = "food_ram_3",
            name = "Sayantara Sabudana Vada & Fasting Food",
            typeText = "World-Famous Upvas (Fasting) Delicacies",
            isFree = false,
            isVegOnly = true,
            area = "Bhadrakali / Panchavati Border",
            locationHub = "Ramkund & Panchavati",
            timing = "08:00 AM - 09:30 PM",
            description = "90-year-old legendary shop world-famous for crispy Sabudana Vada, Farali Misal & Peanut Chutney for fasting pilgrims.",
            address = "Bhadrakali Market, Near Panchavati Bridge, Nashik",
            phone = "+91 253 2503456",
            rating = 4.9,
            category = "Snacks & Upvas",
            priceForTwo = "₹150 for 2",
            mapQuery = "Sayantara Sabudana Vada Bhadrakali Nashik"
        ),

        // 2. TRIMBAKESHWAR
        FoodListing(
            id = "food_tri_1",
            name = "Trimbakeshwar Satvik Thali Bhojanalaya",
            typeText = "Pure Satvik No-Onion No-Garlic Meal",
            isFree = false,
            isVegOnly = true,
            area = "Trimbakeshwar Temple Gate 2",
            locationHub = "Trimbakeshwar",
            timing = "11:00 AM - 09:00 PM",
            description = "Clean satvik thali cooked with pure cow ghee served to temple pilgrims near Kushavarta Kund.",
            address = "Near East Entrance, Trimbak Temple, Trimbakeshwar",
            phone = "+91 253 2580333",
            rating = 4.7,
            category = "Pure Veg Thali",
            priceForTwo = "₹300 for 2",
            mapQuery = "Trimbakeshwar Temple Gate 2"
        ),
        FoodListing(
            id = "food_tri_2",
            name = "Anjaneri Chulivarchi Misal",
            typeText = "Rustic Clay Stove Chulha Misal",
            isFree = false,
            isVegOnly = true,
            area = "Anjaneri Phata, Trimbak Road",
            locationHub = "Trimbakeshwar",
            timing = "08:00 AM - 07:00 PM",
            description = "Authentic wood-fire cooked spicy Tarri Misal served with fresh curd, papad & piping hot Jalebi.",
            address = "Anjaneri Phata, Trimbak Road, Nashik",
            phone = "+91 98231 11223",
            rating = 4.8,
            category = "Misal Pav",
            priceForTwo = "₹220 for 2",
            mapQuery = "Anjaneri Trimbak Road Nashik"
        ),
        FoodListing(
            id = "food_tri_3",
            name = "Grape County Pure Veg Restaurant",
            typeText = "Multi-Cuisine Family Dining",
            isFree = false,
            isVegOnly = true,
            area = "Anjaneri, Trimbak Road",
            locationHub = "Trimbakeshwar",
            timing = "07:00 AM - 11:00 PM",
            description = "Spacious air-conditioned family restaurant serving North Indian, South Indian, Paneer delicacies & fresh juices.",
            address = "Anjaneri, Trimbak Road, Nashik",
            phone = "+91 70309 15009",
            rating = 4.7,
            category = "Family Restaurant",
            priceForTwo = "₹700 for 2",
            mapQuery = "Grape County Eco Resort Nashik"
        ),

        // 3. NASHIK RAILWAY STATION
        FoodListing(
            id = "food_rlw_1",
            name = "Janak Pure Veg Thali & South Indian",
            typeText = "Quick Service Railway Station Dining",
            isFree = false,
            isVegOnly = true,
            area = "Station Road, Nashik Road",
            locationHub = "Nashik Railway Station",
            timing = "06:00 AM - 11:00 PM",
            description = "Clean & fast pure veg restaurant offering crisp Masala Dosa, South Indian Tiffin, Punjabi Thali & Filter Coffee.",
            address = "Opposite Railway Station Gate 1, Nashik Road",
            phone = "+91 253 2460011",
            rating = 4.5,
            category = "Pure Veg Thali",
            priceForTwo = "₹280 for 2",
            mapQuery = "Nashik Road Station Gate 1"
        ),
        FoodListing(
            id = "food_rlw_2",
            name = "Railway Station Road Misal House",
            typeText = "Spicy Nashik Style Tarri Misal",
            isFree = false,
            isVegOnly = true,
            area = "Nashik Road Market",
            locationHub = "Nashik Railway Station",
            timing = "07:00 AM - 08:00 PM",
            description = "Hot crispy farsan misal served with butter pav, onions & hot Irani Chai right outside the station.",
            address = "Station Market Plaza, Nashik Road",
            phone = "+91 98222 44556",
            rating = 4.4,
            category = "Misal Pav",
            priceForTwo = "₹160 for 2",
            mapQuery = "Nashik Road Station Market"
        ),

        // 4. CBS BUS STAND
        FoodListing(
            id = "food_cbs_1",
            name = "Hotel Panchavati Yatri Thali",
            typeText = "Authentic Maharashtrian Unlimited Thali",
            isFree = false,
            isVegOnly = true,
            area = "MG Road / CBS",
            locationHub = "CBS Bus Stand",
            timing = "11:00 AM - 03:30 PM & 07:00 PM - 10:30 PM",
            description = "Renowned traditional pure veg dining serving unlimited Gujarati & Maharashtrian Thali with Puran Poli & Basundi.",
            address = "430 Vakil Wadi, MG Road, CBS Area, Nashik",
            phone = "+91 253 2570101",
            rating = 4.8,
            category = "Pure Veg Thali",
            priceForTwo = "₹500 for 2",
            mapQuery = "Hotel Panchavati Yatri Nashik"
        ),
        FoodListing(
            id = "food_cbs_2",
            name = "Nandu's Paratha & Family Restaurant",
            typeText = "Multi-Cuisine North Indian & Parathas",
            isFree = false,
            isVegOnly = true,
            area = "CBS Circle / College Road",
            locationHub = "CBS Bus Stand",
            timing = "09:00 AM - 11:00 PM",
            description = "Huge stuffed Punjabi Parathas with white butter, Dal Makhani, Paneer Tikka & Chhole Bhature.",
            address = "Near CBS Circle, Old Agra Road, Nashik",
            phone = "+91 253 2588990",
            rating = 4.6,
            category = "Family Restaurant",
            priceForTwo = "₹400 for 2",
            mapQuery = "CBS Circle Old Agra Road Nashik"
        ),

        // 5. NIMANI BUS STAND
        FoodListing(
            id = "food_nim_1",
            name = "Nimani Circle Famous Misal & Snacks",
            typeText = "Panchavati Special Spicy Misal",
            isFree = false,
            isVegOnly = true,
            area = "Nimani Bus Stand Depot",
            locationHub = "Nimani Bus Stand",
            timing = "07:00 AM - 09:00 PM",
            description = "Popular transit food joint serving fiery Kala Rassa Misal, Kanda Bhaji & cutting chai for travelers.",
            address = "Nimani Bus Depot Entrance, Panchavati, Nashik",
            phone = "+91 98220 33445",
            rating = 4.5,
            category = "Misal Pav",
            priceForTwo = "₹150 for 2",
            mapQuery = "Nimani Bus Stand Panchavati Nashik"
        ),
        FoodListing(
            id = "food_nim_2",
            name = "Panchavati Thali & Fast Food",
            typeText = "Affordable Pilgrim Thali & Dosa",
            isFree = false,
            isVegOnly = true,
            area = "Panchavati College Road",
            locationHub = "Nimani Bus Stand",
            timing = "08:00 AM - 10:00 PM",
            description = "Budget-friendly unlimited Maharashtrian Thali, Mysore Masala Dosa, Pav Bhaji & Fresh Juices.",
            address = "College Road Corner, Near Nimani Depot, Nashik",
            phone = "+91 253 2515566",
            rating = 4.4,
            category = "Pure Veg Thali",
            priceForTwo = "₹220 for 2",
            mapQuery = "Nimani Depot Panchavati Nashik"
        ),

        // 6. TAPOVAN & SADHUGRAM
        FoodListing(
            id = "food_tap_1",
            name = "Sadhugram Free Annakshetra (Langar)",
            typeText = "Free Community Pilgrim Meal (Langar)",
            isFree = true,
            isVegOnly = true,
            area = "Sadhugram Sector 2",
            locationHub = "Tapovan & Sadhugram",
            timing = "06:00 AM - 10:00 PM (Continuous)",
            description = "Organized by Akhara Parishad & Religious Trusts. Free hot satvik meal (Poori, Sabzi, Kheer & Khichdi) served to all pilgrims.",
            address = "Sadhugram Sector 2, Tapovan, Nashik",
            phone = "1800-233-1122",
            rating = 4.9,
            category = "Pure Veg Thali",
            priceForTwo = "FREE (Seva)",
            mapQuery = "Tapovan Sadhugram Nashik"
        ),
        FoodListing(
            id = "food_tap_2",
            name = "Tapovan Riverside Satvik Bhojanalaya",
            typeText = "Satvik No-Garlic Pilgrim Meals",
            isFree = false,
            isVegOnly = true,
            area = "Tapovan Godavari Bank",
            locationHub = "Tapovan & Sadhugram",
            timing = "08:00 AM - 09:00 PM",
            description = "Clean satvik meals, Kadhi Khichdi, Sabudana Khichdi & fresh sugarcane juice near Tapovan caves.",
            address = "Tapovan Riverside Promenade, Nashik",
            phone = "+91 94220 88990",
            rating = 4.6,
            category = "Snacks & Upvas",
            priceForTwo = "₹180 for 2",
            mapQuery = "Tapovan Caves Nashik"
        ),

        // 7. SATPUR & AMBAD
        FoodListing(
            id = "food_sat_1",
            name = "Sadhana Chulivarchi Misal",
            typeText = "World Famous Heritage Chulha Misal",
            isFree = false,
            isVegOnly = true,
            area = "Bardan Phata / Satpur Road",
            locationHub = "Satpur & Ambad",
            timing = "08:00 AM - 08:00 PM",
            description = "Iconic clay stove (chulha) Misal Pav served with fresh hot Jalebi, Solkadhi & Gulab Jamun in a rustic garden ambiance.",
            address = "Hardev Nagar, Bardan Phata, Near Swaminarayan Temple, Nashik",
            phone = "+91 98231 88990",
            rating = 4.9,
            category = "Misal Pav",
            priceForTwo = "₹250 for 2",
            mapQuery = "Sadhana Chulivarchi Misal Nashik"
        ),
        FoodListing(
            id = "food_sat_2",
            name = "Shamsundar Misal Satpur",
            typeText = "Spicy Satpur Industrial Misal Specialty",
            isFree = false,
            isVegOnly = true,
            area = "Satpur MIDC",
            locationHub = "Satpur & Ambad",
            timing = "08:00 AM - 07:00 PM",
            description = "Award-winning spicy Misal with farsan, spicy tarri, chopped onions & lemon curd.",
            address = "P-17, MIDC Satpur, Trimbak Road, Nashik",
            phone = "+91 98505 12345",
            rating = 4.6,
            category = "Misal Pav",
            priceForTwo = "₹200 for 2",
            mapQuery = "Shamsundar Misal Satpur Nashik"
        )
    )

    // Transport & Routes
    val transportRoutes = listOf(
        TransportRoute(
            id = "tr_1",
            title = "Nashik Road Station → Ramkund Ghat",
            fromHub = "Nashik Road Railway Station",
            toDestination = "Ramkund Ghat (Panchavati)",
            mode = "Kumbh Electric Shuttle Bus (Route #1)",
            estimatedTime = "25 - 35 mins",
            fare = "Free during Shahi Snan (₹20 regular)",
            frequency = "Every 5 minutes",
            guidelines = "Buses drop pilgrims at Nimani Bus Stop (800m walk to Ramkund). Luggage storage available at station."
        ),
        TransportRoute(
            id = "tr_2",
            title = "Ojhar Airport (ISK) → Nashik City Center",
            fromHub = "Ojhar Nashik Airport",
            toDestination = "CBS Bus Stand / Panchavati",
            mode = "MSRTC Airport Express AC Coach",
            estimatedTime = "40 mins",
            fare = "₹120 / seat",
            frequency = "Hourly synchronized with flight arrivals",
            guidelines = "Pre-book airport taxi counters also available outside terminal."
        ),
        TransportRoute(
            id = "tr_3",
            title = "Tapovan Parking → Ramkund Ghats",
            fromHub = "Tapovan Mega Parking Hub",
            toDestination = "Ramkund & Sadhugram",
            mode = "Battery E-Rickshaw & Free Golf Carts",
            estimatedTime = "10 mins",
            fare = "Free for Senior Citizens & PwD (₹10 others)",
            frequency = "Continuous loop",
            guidelines = "Pedestrian walkways available for 1.5km walking route along river promenade."
        ),
        TransportRoute(
            id = "tr_4",
            title = "Nashik City → Trimbakeshwar Temple",
            fromHub = "CBS Central Bus Stand Nashik",
            toDestination = "Trimbakeshwar Bus Depot",
            mode = "MSRTC Direct Ring Buses",
            estimatedTime = "45 - 60 mins",
            fare = "₹55 / seat",
            frequency = "Every 10 minutes",
            guidelines = "On Shahi Snan days, private vehicles stopped at Pegasus Outer Gate. Take official buses only."
        )
    )

    // Emergency Helpline List
    val emergencyContacts = listOf(
        EmergencyContact(
            id = "em_1",
            title = "Police Emergency Control Room",
            number = "112",
            category = "Police",
            description = "24x7 National Emergency Response System for law & order, stampede alerts, and crime reporting.",
            isPrimary = true
        ),
        EmergencyContact(
            id = "em_2",
            title = "Kumbh Medical & Ambulance Service",
            number = "108",
            category = "Medical",
            description = "Free ICU Ambulance & Immediate Field Doctor Dispatch for cardiac or crowd injuries.",
            isPrimary = true
        ),
        EmergencyContact(
            id = "em_3",
            title = "Fire & Rescue Department",
            number = "101",
            category = "Fire",
            description = "Kumbh Mela Fire Stations at Sadhugram, Ramkund, and Trimbakeshwar.",
            isPrimary = true
        ),
        EmergencyContact(
            id = "em_4",
            title = "Lost & Found Central Control Room",
            number = "1800-233-1122",
            category = "Lost & Found",
            description = "Toll-free hotline to report missing family members, missing luggage, or lost children.",
            isPrimary = true
        ),
        EmergencyContact(
            id = "em_5",
            title = "Trimbakeshwar Temple Helpdesk",
            number = "+91 253 2580100",
            category = "Temple",
            description = "Direct assistance for Trimbak Jyotirlinga darshan lines and senior citizen pass.",
            isPrimary = false
        ),
        EmergencyContact(
            id = "em_6",
            title = "Nashik District Collector Control Room",
            number = "+91 253 2570000",
            category = "Administration",
            description = "General district helpline for festival queries, vendor passes, and complaints.",
            isPrimary = false
        )
    )

    // Health & Crowd Safety Advisories
    val healthAdvisories = listOf(
        HealthAdvisory(
            id = "ha_1",
            title = "Hydration & Heat Protection",
            description = "Drink at least 3-4 liters of water daily. Free chilled RO water stations are located every 200m along Godavari ghats. Wear a cloth cap or scarf.",
            category = "Health"
        ),
        HealthAdvisory(
            id = "ha_2",
            title = "Family Group Safety & RFID Tagging",
            description = "Obtain a free waterproof wristband at Police Help Desks for children and elders with your mobile number written clearly.",
            category = "Crowd Safety"
        ),
        HealthAdvisory(
            id = "ha_3",
            title = "Stampede Prevention & Queue Protocol",
            description = "Never walk against the flow of moving crowds on pontoon bridges or narrow ghat lanes. Keep calm and follow police megaphone commands.",
            category = "Crowd Safety"
        ),
        HealthAdvisory(
            id = "ha_4",
            title = "Holy River Bathing Hygiene",
            description = "Use designated changing rooms. Do not use soap, plastic bags, or shampoo directly in Godavari river waters to keep the sacred stream pure.",
            category = "River Rules"
        )
    )

    // Cultural & Heritage Info
    val cultureTopics = listOf(
        CultureTopic(
            id = "ct_1",
            title = "Simhastha Kumbh Mela Significance",
            summary = "Why Simhastha occurs when Jupiter enters Leo (Simha Rashi) every 12 years.",
            detailedText = "According to Hindu mythology, during the Samudra Manthan (churning of the cosmic ocean), a drop of Amrit (immortality nectar) fell at four sacred places: Haridwar, Ujjain, Prayagraj, and Nashik-Trimbakeshwar. The Nashik Simhastha occurs specifically when the planet Jupiter (Guru) enters the zodiac sign of Leo (Simha) and the Sun enters Cancer. Taking a holy bath in River Godavari during this celestial window is believed to cleanse all karma and grant moksha.",
            iconName = "auto_awesome"
        ),
        CultureTopic(
            id = "ct_2",
            title = "Nashik vs Trimbakeshwar Dual Venues",
            summary = "The unique tradition of Vaishnava Akharas in Nashik & Shaiva Akharas in Trimbak.",
            detailedText = "Unlike other Kumbh Melas, the Simhastha Kumbh is celebrated simultaneously across two historic towns separated by 28 kilometers. By ancient decree, the Vaishnava Akharas (devotees of Lord Vishnu) perform their royal Snan at Ramkund in Nashik city, while the Shaiva Akharas (devotees of Lord Shiva) perform their royal Snan at Kushavarta Kund in Trimbakeshwar. Both ghats radiate immense spiritual energy.",
            iconName = "account_balance"
        ),
        CultureTopic(
            id = "ct_3",
            title = "The 13 Sacred Akharas & Naga Sadhus",
            summary = "Understanding the monastic orders and royal processions (Shahi Yatra).",
            detailedText = "The 13 recognized Akharas represent ancient martial-ascetic traditions founded by Adi Shankaracharya. During Shahi Snan days, Naga Sadhus (naked ascetics decorated with sacred ash) lead grand processions on decorated elephants, chariots, and horses accompanied by tridents, conch shells, and drums. Devotees gather along barricaded routes to receive their blessings.",
            iconName = "groups"
        ),
        CultureTopic(
            id = "ct_4",
            title = "Ghat Etiquette, Dress Code & Rules",
            summary = "Do's and Don'ts for pilgrims to maintain sanctity and respect.",
            detailedText = "• Dress Code: Modest clothing is mandatory. Men wear dhotis/kurta/shorts, women wear sarees or salwar suits.\n• River Sanctity: Strictly no plastic bags, oil bottles, soap, or trash in the river.\n• Photography: Be respectful around ascetics and private bathing zones.\n• Elderly Care: Utilize free electric golf carts available at holding zones.",
            iconName = "verified_user"
        )
    )

    // Comprehensive Medical Facilities & Nearby Hospitals (All Locations)
    val medicalFacilities = listOf(
        // 1. RAMKUND & PANCHAVATI
        MedicalFacility(
            id = "med_ram_1",
            name = "District Civil Govt Hospital (Panchavati)",
            facilityType = MedicalType.HOSPITAL,
            locationHub = "Ramkund & Panchavati",
            is24x7 = true,
            address = "Kalaram Mandir Road, Panchavati, Nashik",
            distance = "0.5 km from Ramkund",
            phone = "+91 253 2576101",
            emergencyBeds = "250 Emergency Beds • ICU Unit",
            services = listOf("24x7 Trauma Care", "Free Oxygen Cylinders", "Blood Bank", "24x7 Pharmacy", "Ambulance Hub"),
            mapQuery = "District Civil Hospital Panchavati Nashik"
        ),
        MedicalFacility(
            id = "med_ram_2",
            name = "Kumbh Ramkund Sector 1 Field Hospital",
            facilityType = MedicalType.GOVT_CAMP,
            locationHub = "Ramkund & Panchavati",
            is24x7 = true,
            address = "Adjacent to Ramkund Main Stairs, Panchavati",
            distance = "0.1 km from Ramkund",
            phone = "+91 253 2572200",
            emergencyBeds = "40 Emergency Triage Beds",
            services = listOf("First-Aid & CPR", "Heatstroke Treatment", "Free Medicines", "Stretchers", "ICU Ambulance Standby"),
            mapQuery = "Ramkund Ghat Panchavati Nashik"
        ),
        MedicalFacility(
            id = "med_ram_3",
            name = "Apollo Pharmacy (Ramkund Karanja)",
            facilityType = MedicalType.MEDICAL_STORE,
            locationHub = "Ramkund & Panchavati",
            is24x7 = true,
            address = "Shop 4, Panchavati Karanja Market, Nashik",
            distance = "0.3 km from Ramkund",
            phone = "+91 98220 11223",
            emergencyBeds = "",
            services = listOf("24x7 Prescription Drugs", "First-Aid Kits", "ORS Packet Distribution", "Pain Relief Sprays", "BP Check"),
            mapQuery = "Apollo Pharmacy Panchavati Karanja Nashik"
        ),
        MedicalFacility(
            id = "med_ram_4",
            name = "Jan Aushadhi Kendra (Panchavati Circle)",
            facilityType = MedicalType.MEDICAL_STORE,
            locationHub = "Ramkund & Panchavati",
            is24x7 = false,
            address = "Near Sita Gufa Road, Panchavati, Nashik",
            distance = "0.4 km from Ramkund",
            phone = "+91 253 2511400",
            emergencyBeds = "",
            services = listOf("Affordable Generic Medicines", "Bandages & Antiseptic", "Diabetic & Cardiac Drugs", "Free Water"),
            mapQuery = "Jan Aushadhi Kendra Panchavati Nashik"
        ),

        // 2. TRIMBAKESHWAR & KUSHAVARTA
        MedicalFacility(
            id = "med_tri_1",
            name = "Trimbakeshwar Sub-District Hospital",
            facilityType = MedicalType.HOSPITAL,
            locationHub = "Trimbakeshwar",
            is24x7 = true,
            address = "Main Highway Rd, Near Temple Gate 2, Trimbak",
            distance = "0.6 km from Kushavarta Kund",
            phone = "+91 2594 233022",
            emergencyBeds = "100 Emergency Beds • Operation Theater",
            services = listOf("24x7 Emergency Ward", "Snake-bite Anti-venom", "Cardiac Monitor", "24x7 Medical Store"),
            mapQuery = "Sub District Hospital Trimbakeshwar"
        ),
        MedicalFacility(
            id = "med_tri_2",
            name = "Kushavarta Disaster Medical Post",
            facilityType = MedicalType.GOVT_CAMP,
            locationHub = "Trimbakeshwar",
            is24x7 = true,
            address = "North Gate, Kushavarta Kund Complex, Trimbak",
            distance = "0.05 km from Kushavarta",
            phone = "+91 2594 233100",
            emergencyBeds = "30 Triage Beds",
            services = listOf("Immediate CPR", "Drowning Recovery Unit", "Free Water & Glucose", "108 Ambulance Bay"),
            mapQuery = "Kushavarta Kund Trimbakeshwar"
        ),
        MedicalFacility(
            id = "med_tri_3",
            name = "Kushavarta 24x7 Medical & Surgical",
            facilityType = MedicalType.MEDICAL_STORE,
            locationHub = "Trimbakeshwar",
            is24x7 = true,
            address = "Main Bazar Rd, Near Kushavarta Entrance, Trimbak",
            distance = "0.2 km from Kushavarta Kund",
            phone = "+91 94222 55667",
            emergencyBeds = "",
            services = listOf("24x7 Medicines", "Bandages & Ointments", "Asthma Inhalers", "Pediatric Drops", "UPI Accepted"),
            mapQuery = "Medical Store Trimbakeshwar Bazar"
        ),

        // 3. NASHIK ROAD RAILWAY STATION
        MedicalFacility(
            id = "med_rlw_1",
            name = "Central Railway Emergency Medical Post",
            facilityType = MedicalType.GOVT_CAMP,
            locationHub = "Nashik Railway Station",
            is24x7 = true,
            address = "Platform No. 1 Main Exit, Nashik Road Station",
            distance = "0.0 km from Railway Station",
            phone = "+91 253 2465222",
            emergencyBeds = "20 Emergency Transit Beds",
            services = listOf("24x7 Railway Doctor on Duty", "First-Aid & Dressing", "Free Wheelchairs", "108 Ambulance Hub"),
            mapQuery = "Nashik Road Railway Station"
        ),
        MedicalFacility(
            id = "med_rlw_2",
            name = "Magnum Heart & Multi-Specialty Hospital",
            facilityType = MedicalType.HOSPITAL,
            locationHub = "Nashik Railway Station",
            is24x7 = true,
            address = "Datta Mandir Stop, Nashik Road, Nashik",
            distance = "0.8 km from Railway Station",
            phone = "+91 253 2469900",
            emergencyBeds = "120 Beds • 24x7 ICU & Cardiac Care",
            services = listOf("24x7 Emergency & Trauma", "Cardiology & Ventilator", "24x7 In-house Pharmacy", "CT Scan"),
            mapQuery = "Magnum Heart Institute Nashik Road"
        ),
        MedicalFacility(
            id = "med_rlw_3",
            name = "Wellness Forever 24x7 Chemist (Station Gate)",
            facilityType = MedicalType.MEDICAL_STORE,
            locationHub = "Nashik Railway Station",
            is24x7 = true,
            address = "Opp. Station Bus Stand, Nashik Road, Nashik",
            distance = "0.1 km from Railway Station",
            phone = "+91 253 2461111",
            emergencyBeds = "",
            services = listOf("24x7 Medicines & Travel Kits", "Refrigerated Vaccines", "Baby Food & Hygiene", "Credit Card / UPI"),
            mapQuery = "Wellness Forever Nashik Road Station"
        ),

        // 4. CBS (CENTRAL BUS STAND) & OLD AGRA ROAD
        MedicalFacility(
            id = "med_cbs_1",
            name = "NMC City Emergency Hospital (CBS)",
            facilityType = MedicalType.HOSPITAL,
            locationHub = "CBS Bus Stand",
            is24x7 = true,
            address = "Old Agra Road, Near CBS Bus Terminal, Nashik",
            distance = "0.2 km from CBS Bus Stand",
            phone = "+91 253 2573333",
            emergencyBeds = "80 Emergency Beds",
            services = listOf("24x7 Accident & Emergency", "Free Ambulance Service", "Oxygen Support", "Municipal Pharmacy"),
            mapQuery = "NMC Hospital Old Agra Road CBS Nashik"
        ),
        MedicalFacility(
            id = "med_cbs_2",
            name = "Six Sigma Star Multi-Specialty Hospital",
            facilityType = MedicalType.HOSPITAL,
            locationHub = "CBS Bus Stand",
            is24x7 = true,
            address = "Mahatma Gandhi Road, Near CBS Circle, Nashik",
            distance = "0.5 km from CBS Bus Stand",
            phone = "+91 253 6699999",
            emergencyBeds = "150 Beds • Advanced ICU",
            services = listOf("24x7 Trauma Center", "Surgery & Cardiology", "24x7 Blood Bank", "Helipad Access"),
            mapQuery = "Six Sigma Hospital MG Road Nashik"
        ),
        MedicalFacility(
            id = "med_cbs_3",
            name = "CBS 24 Hours Medical & Surgical",
            facilityType = MedicalType.MEDICAL_STORE,
            locationHub = "CBS Bus Stand",
            is24x7 = true,
            address = "Shop 12, Bus Stand Shopping Complex, CBS Nashik",
            distance = "0.05 km from CBS Terminal",
            phone = "+91 98900 44332",
            emergencyBeds = "",
            services = listOf("24x7 Emergency Medicines", "First-Aid Travel Kits", "ORS & Energy Drinks", "Insulin Storage"),
            mapQuery = "Central Bus Stand CBS Nashik"
        ),

        // 5. NIMANI BUS STAND & PANCHAVATI CIRCLE
        MedicalFacility(
            id = "med_nim_1",
            name = "Nimani Public Health Center & Trauma Unit",
            facilityType = MedicalType.HOSPITAL,
            locationHub = "Nimani Bus Stand",
            is24x7 = true,
            address = "Nimani Bus Terminal Campus, Panchavati, Nashik",
            distance = "0.1 km from Nimani Depot",
            phone = "+91 253 2514455",
            emergencyBeds = "50 Beds",
            services = listOf("24x7 OPD & Emergency", "Heatstroke Ward", "Free Anti-biotic Medicines", "Ambulance Bay"),
            mapQuery = "Nimani Bus Stand Panchavati Nashik"
        ),
        MedicalFacility(
            id = "med_nim_2",
            name = "Nimani 24x7 Chemist & Druggist",
            facilityType = MedicalType.MEDICAL_STORE,
            locationHub = "Nimani Bus Stand",
            is24x7 = true,
            address = "Opp. Nimani City Bus Depot, Panchavati, Nashik",
            distance = "0.1 km from Nimani Depot",
            phone = "+91 98230 88990",
            emergencyBeds = "",
            services = listOf("24x7 All Allopathic Drugs", "Surgical Dressings", "Nebulizer & Vaporizers", "Homeo/Ayurvedic"),
            mapQuery = "Nimani Bus Stand Nashik"
        ),
        MedicalFacility(
            id = "med_nim_3",
            name = "Jan Aushadhi Store (Nimani Circle)",
            facilityType = MedicalType.MEDICAL_STORE,
            locationHub = "Nimani Bus Stand",
            is24x7 = false,
            address = "Panchavati College Road, Near Nimani Depot",
            distance = "0.2 km from Nimani",
            phone = "+91 253 2510090",
            emergencyBeds = "",
            services = listOf("Affordable Govt Generic Drugs", "BP & Sugar Monitors", "Nutritional Supplements"),
            mapQuery = "Panchavati Nimani Nashik"
        ),

        // 6. TAPOVAN & SADHUGRAM CAMPS
        MedicalFacility(
            id = "med_tap_1",
            name = "Tapovan Kumbh Central Field Base Hospital",
            facilityType = MedicalType.GOVT_CAMP,
            locationHub = "Tapovan & Sadhugram",
            is24x7 = true,
            address = "Sector 4 Central Sector, Sadhugram Camp, Tapovan",
            distance = "0.2 km from Sadhugram Entrance",
            phone = "+91 253 2578899",
            emergencyBeds = "100 ICU Beds • Mobile X-Ray",
            services = listOf("24x7 Critical Care Triage", "Disaster Management Unit", "50 Ambulances Standby", "Free Pharmacy"),
            mapQuery = "Tapovan Sadhugram Nashik"
        ),
        MedicalFacility(
            id = "med_tap_2",
            name = "Sadhugram Red Cross Emergency Pharmacy",
            facilityType = MedicalType.MEDICAL_STORE,
            locationHub = "Tapovan & Sadhugram",
            is24x7 = true,
            address = "Main Market Alley, Sadhugram Camp, Tapovan",
            distance = "0.3 km from Akhara Camps",
            phone = "+91 253 2578800",
            emergencyBeds = "",
            services = listOf("24x7 Free Essential Drugs", "Ayurvedic Pain Relief Oils", "Skin Ointments", "First-Aid Boxes"),
            mapQuery = "Sadhugram Tapovan Nashik"
        ),

        // 7. SATPUR & AMBAD
        MedicalFacility(
            id = "med_sat_1",
            name = "ESI Municipal Hospital (Satpur MIDC)",
            facilityType = MedicalType.HOSPITAL,
            locationHub = "Satpur & Ambad",
            is24x7 = true,
            address = "Trimbak Road, Satpur Industrial Area, Nashik",
            distance = "1.2 km from Satpur Hub",
            phone = "+91 253 2350123",
            emergencyBeds = "120 Beds • Burns & Trauma",
            services = listOf("24x7 Emergency Ward", "Trauma & Fracture Surgery", "Blood Storage", "24x7 Pharmacy"),
            mapQuery = "ESI Hospital Satpur Nashik"
        ),
        MedicalFacility(
            id = "med_sat_2",
            name = "Satpur 24x7 Pharma Plaza",
            facilityType = MedicalType.MEDICAL_STORE,
            locationHub = "Satpur & Ambad",
            is24x7 = true,
            address = "Near ITI Signal, Trimbak Road, Satpur, Nashik",
            distance = "0.3 km from Satpur Circle",
            phone = "+91 94220 33441",
            emergencyBeds = "",
            services = listOf("24 Hours All Medicines", "Injectables & IV Fluids", "Home Delivery to Camps"),
            mapQuery = "Satpur Trimbak Road Nashik"
        )
    )
}

