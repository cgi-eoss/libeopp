package com.cgi.eoss.eopp.geojson.kt

import com.fasterxml.jackson.annotation.JsonSubTypes
import com.fasterxml.jackson.annotation.JsonTypeInfo

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.EXISTING_PROPERTY, property = "type")
@JsonSubTypes(
//    JsonSubTypes.Type(Point::class),
//    JsonSubTypes.Type(MultiPoint::class),
//    JsonSubTypes.Type(LineString::class),
//    JsonSubTypes.Type(MultiLineString::class),
//    JsonSubTypes.Type(Polygon::class),
//    JsonSubTypes.Type(MultiPolygon::class),
//    JsonSubTypes.Type(GeometryCollection::class),
//    JsonSubTypes.Type(Feature::class),
//    JsonSubTypes.Type(FeatureCollection::class)
)
interface GeoJSON {

}

sealed class KGeoJSON(

) : GeoJSON {
}