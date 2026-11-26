package com.example.mobileprogramming.data.remote

const val DEFAULT_LATITUDE = 43.2383
const val DEFAULT_LONGITUDE = 76.9453
const val DEFAULT_RADIUS_METERS = 1500

fun nearbyPlacesQuery(
    latitude: Double = DEFAULT_LATITUDE,
    longitude: Double = DEFAULT_LONGITUDE,
    radiusMeters: Int = DEFAULT_RADIUS_METERS
): String {
    val area = "around:$radiusMeters,$latitude,$longitude"
    return """
        [out:json][timeout:25];
        (
          node["amenity"~"^(clinic|doctors|dentist|bank|post_office|townhall|cafe|fast_food)$"]["name"]($area);
          node["shop"~"^(hairdresser|beauty)$"]["name"]($area);
          node["office"="government"]["name"]($area);
        );
        out 40;
    """.trimIndent()
}