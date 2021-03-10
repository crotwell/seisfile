
package edu.sc.seis.seisFile.fdsnws;

import java.time.Instant;

import edu.sc.seis.seisFile.BoxArea;
import edu.sc.seis.seisFile.DonutArea;

/** Autogenerated by groovy FDSNQueryParamGenerator.groovy in src/metacode/groovy
 */
public class FDSNEventQueryParams extends AbstractQueryParams implements Cloneable {

    public FDSNEventQueryParams() {
        this(USGS_HOST);
    }

    public FDSNEventQueryParams(String host) {
        super(host==null ? USGS_HOST : host);
    }

    public FDSNEventQueryParams clone() {
        FDSNEventQueryParams out = new FDSNEventQueryParams(getHost());
        out.cloneNonParams(this);
        for (String key : params.keySet()) {
            out.setParam(key, params.get(key));
        }
        return out;
    }

    public FDSNEventQueryParams setHost(String host) {
        this.host = host;
        return this;
    }
    public FDSNEventQueryParams setPort(int port) {
        this.port = port;
        return this;
    }


    public static final String STARTTIME = "starttime";

    public static final String STARTTIME_SHORT = "start";


    public FDSNEventQueryParams clearStartTime() {
        clearParam(STARTTIME);
        return this;
    }

    /** Limit to events on or after the specified start time.
     *  @param value value to set
     *  @return the queryParams for method chaining
     */
    public FDSNEventQueryParams setStartTime(Instant value) {
        setParam(STARTTIME, value);
        return this;
    }


    public static final String ENDTIME = "endtime";

    public static final String ENDTIME_SHORT = "end";


    public FDSNEventQueryParams clearEndTime() {
        clearParam(ENDTIME);
        return this;
    }

    /** Limit to events on or before the specified end time.
     *  @param value value to set
     *  @return the queryParams for method chaining
     */
    public FDSNEventQueryParams setEndTime(Instant value) {
        setParam(ENDTIME, value);
        return this;
    }


    public static final String MINLATITUDE = "minlatitude";

    public static final String MINLATITUDE_SHORT = "minlat";


    public FDSNEventQueryParams clearMinLatitude() {
        clearParam(MINLATITUDE);
        return this;
    }

    /** Limit to events with a latitude larger than the specified minimum.
     *  @param value value to set
     *  @return the queryParams for method chaining
     */
    public FDSNEventQueryParams setMinLatitude(float value) {
        setParam(MINLATITUDE, value);
        return this;
    }


    public static final String MAXLATITUDE = "maxlatitude";

    public static final String MAXLATITUDE_SHORT = "maxlat";


    public FDSNEventQueryParams clearMaxLatitude() {
        clearParam(MAXLATITUDE);
        return this;
    }

    /** Limit to events with a latitude smaller than the specified maximum.
     *  @param value value to set
     *  @return the queryParams for method chaining
     */
    public FDSNEventQueryParams setMaxLatitude(float value) {
        setParam(MAXLATITUDE, value);
        return this;
    }


    public static final String MINLONGITUDE = "minlongitude";

    public static final String MINLONGITUDE_SHORT = "minlon";


    public FDSNEventQueryParams clearMinLongitude() {
        clearParam(MINLONGITUDE);
        return this;
    }

    /** Limit to events with a longitude larger than the specified minimum.
     *  @param value value to set
     *  @return the queryParams for method chaining
     */
    public FDSNEventQueryParams setMinLongitude(float value) {
        setParam(MINLONGITUDE, value);
        return this;
    }


    public static final String MAXLONGITUDE = "maxlongitude";

    public static final String MAXLONGITUDE_SHORT = "maxlon";


    public FDSNEventQueryParams clearMaxLongitude() {
        clearParam(MAXLONGITUDE);
        return this;
    }

    /** Limit to events with a longitude smaller than the specified maximum.
     *  @param value value to set
     *  @return the queryParams for method chaining
     */
    public FDSNEventQueryParams setMaxLongitude(float value) {
        setParam(MAXLONGITUDE, value);
        return this;
    }


    public static final String LATITUDE = "latitude";

    public static final String LATITUDE_SHORT = "lat";


    public FDSNEventQueryParams clearLatitude() {
        clearParam(LATITUDE);
        return this;
    }

    /** Specify the latitude to be used for a radius search.
     *  @param value value to set
     *  @return the queryParams for method chaining
     */
    public FDSNEventQueryParams setLatitude(float value) {
        setParam(LATITUDE, value);
        return this;
    }


    public static final String LONGITUDE = "longitude";

    public static final String LONGITUDE_SHORT = "lon";


    public FDSNEventQueryParams clearLongitude() {
        clearParam(LONGITUDE);
        return this;
    }

    /** Specify the longitude to the used for a radius search.
     *  @param value value to set
     *  @return the queryParams for method chaining
     */
    public FDSNEventQueryParams setLongitude(float value) {
        setParam(LONGITUDE, value);
        return this;
    }


    public static final String MINRADIUS = "minradius";



    public FDSNEventQueryParams clearMinRadius() {
        clearParam(MINRADIUS);
        return this;
    }

    /** Limit to events within the specified minimum number of degrees from the geographic point defined by the latitude and longitude parameters.
     *  @param value value to set
     *  @return the queryParams for method chaining
     */
    public FDSNEventQueryParams setMinRadius(float value) {
        setParam(MINRADIUS, value);
        return this;
    }


    public static final String MAXRADIUS = "maxradius";



    public FDSNEventQueryParams clearMaxRadius() {
        clearParam(MAXRADIUS);
        return this;
    }

    /** Limit to events within the specified maximum number of degrees from the geographic point defined by the latitude and longitude parameters.
     *  @param value value to set
     *  @return the queryParams for method chaining
     */
    public FDSNEventQueryParams setMaxRadius(float value) {
        setParam(MAXRADIUS, value);
        return this;
    }


    public static final String MINDEPTH = "mindepth";



    public FDSNEventQueryParams clearMinDepth() {
        clearParam(MINDEPTH);
        return this;
    }

    /** Limit to events with depth more than the specified minimum.
     *  @param value value to set
     *  @return the queryParams for method chaining
     */
    public FDSNEventQueryParams setMinDepth(float value) {
        setParam(MINDEPTH, value);
        return this;
    }


    public static final String MAXDEPTH = "maxdepth";



    public FDSNEventQueryParams clearMaxDepth() {
        clearParam(MAXDEPTH);
        return this;
    }

    /** Limit to events with depth less than the specified maximum.
     *  @param value value to set
     *  @return the queryParams for method chaining
     */
    public FDSNEventQueryParams setMaxDepth(float value) {
        setParam(MAXDEPTH, value);
        return this;
    }


    public static final String MINMAGNITUDE = "minmagnitude";

    public static final String MINMAGNITUDE_SHORT = "minmag";


    public FDSNEventQueryParams clearMinMagnitude() {
        clearParam(MINMAGNITUDE);
        return this;
    }

    /** Limit to events with a magnitude larger than the specified minimum.
     *  @param value value to set
     *  @return the queryParams for method chaining
     */
    public FDSNEventQueryParams setMinMagnitude(float value) {
        setParam(MINMAGNITUDE, value);
        return this;
    }


    public static final String MAXMAGNITUDE = "maxmagnitude";

    public static final String MAXMAGNITUDE_SHORT = "maxmag";


    public FDSNEventQueryParams clearMaxMagnitude() {
        clearParam(MAXMAGNITUDE);
        return this;
    }

    /** Limit to events with a magnitude smaller than the specified maximum.
     *  @param value value to set
     *  @return the queryParams for method chaining
     */
    public FDSNEventQueryParams setMaxMagnitude(float value) {
        setParam(MAXMAGNITUDE, value);
        return this;
    }


    public static final String MAGNITUDETYPE = "magnitudetype";

    public static final String MAGNITUDETYPE_SHORT = "magtype";


    public FDSNEventQueryParams clearMagnitudeType() {
        clearParam(MAGNITUDETYPE);
        return this;
    }

    /** Specify a magnitude type to use for testing the minimum and maximum limits.
     *  @param value value to set
     *  @return the queryParams for method chaining
     */
    public FDSNEventQueryParams setMagnitudeType(String value) {
        setParam(MAGNITUDETYPE, value);
        return this;
    }


    public static final String INCLUDEALLORIGINS = "includeallorigins";



    public FDSNEventQueryParams clearIncludeAllOrigins() {
        clearParam(INCLUDEALLORIGINS);
        return this;
    }

    /** Specify if all origins for the event should be included, default is data center dependent but is suggested to be the preferred origin only.
     *  @param value value to set
     *  @return the queryParams for method chaining
     */
    public FDSNEventQueryParams setIncludeAllOrigins(boolean value) {
        setParam(INCLUDEALLORIGINS, value);
        return this;
    }


    public static final String INCLUDEALLMAGNITUDES = "includeallmagnitudes";



    public FDSNEventQueryParams clearIncludeAllMagnitudes() {
        clearParam(INCLUDEALLMAGNITUDES);
        return this;
    }

    /** Specify if all magnitudes for the event should be included, default is data center dependent but is suggested to be the preferred magnitude only.
     *  @param value value to set
     *  @return the queryParams for method chaining
     */
    public FDSNEventQueryParams setIncludeAllMagnitudes(boolean value) {
        setParam(INCLUDEALLMAGNITUDES, value);
        return this;
    }


    public static final String INCLUDEARRIVALS = "includearrivals";



    public FDSNEventQueryParams clearIncludeArrivals() {
        clearParam(INCLUDEARRIVALS);
        return this;
    }

    /** Specify if phase arrivals should be included.
     *  @param value value to set
     *  @return the queryParams for method chaining
     */
    public FDSNEventQueryParams setIncludeArrivals(boolean value) {
        setParam(INCLUDEARRIVALS, value);
        return this;
    }


    public static final String EVENTID = "eventid";



    public FDSNEventQueryParams clearEventid() {
        clearParam(EVENTID);
        return this;
    }

    /** Select a specific event by ID; event identifiers are data center specific.
     *  @param value value to set
     *  @return the queryParams for method chaining
     */
    public FDSNEventQueryParams setEventid(String value) {
        setParam(EVENTID, value);
        return this;
    }


    public static final String LIMIT = "limit";



    public FDSNEventQueryParams clearLimit() {
        clearParam(LIMIT);
        return this;
    }

    /** Limit the results to the specified number of events.
     *  @param value value to set
     *  @return the queryParams for method chaining
     */
    public FDSNEventQueryParams setLimit(int value) {
        setParam(LIMIT, value);
        return this;
    }


    public static final String OFFSET = "offset";



    public FDSNEventQueryParams clearOffset() {
        clearParam(OFFSET);
        return this;
    }

    /** Return results starting at the event count specified, starting at 1.
     *  @param value value to set
     *  @return the queryParams for method chaining
     */
    public FDSNEventQueryParams setOffset(int value) {
        setParam(OFFSET, value);
        return this;
    }


    public static final String ORDERBY = "orderby";



    public FDSNEventQueryParams clearOrderBy() {
        clearParam(ORDERBY);
        return this;
    }

    /** Order the result by time or magnitude with the following possibilities: time: order by origin descending time time-asc : order by origin ascending time magnitude: order by descending magnitude magnitude-asc : order by ascending magnitude
     *  @param value value to set
     *  @return the queryParams for method chaining
     */
    public FDSNEventQueryParams setOrderBy(String value) {
        setParam(ORDERBY, value);
        return this;
    }


    public static final String CATALOG = "catalog";



    public FDSNEventQueryParams clearCatalog() {
        clearParam(CATALOG);
        return this;
    }

    /** Limit to events from a specified catalog
     *  @param value value to set
     *  @return the queryParams for method chaining
     */
    public FDSNEventQueryParams setCatalog(String value) {
        setParam(CATALOG, value);
        return this;
    }


    public static final String CONTRIBUTOR = "contributor";



    public FDSNEventQueryParams clearContributor() {
        clearParam(CONTRIBUTOR);
        return this;
    }

    /** Limit to events contributed by a specified contributor.
     *  @param value value to set
     *  @return the queryParams for method chaining
     */
    public FDSNEventQueryParams setContributor(String value) {
        setParam(CONTRIBUTOR, value);
        return this;
    }


    public static final String UPDATEDAFTER = "updatedafter";



    public FDSNEventQueryParams clearUpdatedAfter() {
        clearParam(UPDATEDAFTER);
        return this;
    }

    /** Limit to events updated after the specified time.
     *  @param value value to set
     *  @return the queryParams for method chaining
     */
    public FDSNEventQueryParams setUpdatedAfter(Instant value) {
        setParam(UPDATEDAFTER, value);
        return this;
    }



    public FDSNEventQueryParams boxArea(BoxArea box) {
        return area(box.south, box.north, box.west, box.east);
    }

    public FDSNEventQueryParams area(float minLat, float maxLat, float minLon, float maxLon) {
        return setMinLatitude(minLat).setMaxLatitude(maxLat).setMinLongitude(minLon).setMaxLongitude(maxLon);
    }

    public FDSNEventQueryParams ring(float lat, float lon, float maxRadius) {
        return setLatitude(lat).setLongitude(lon).setMaxRadius(maxRadius);
    }

    public FDSNEventQueryParams donut(DonutArea donut) {
        return ring(donut.latitude, donut.longitude, donut.maxradius).setMinRadius(donut.minradius);
    }


    

    public static final String USGS_HOST = "earthquake.usgs.gov";
    public static final String ISC_HOST = "www.isc.ac.uk";
    public static final String ISC_MIRROR_HOST = "isc-mirror.iris.washington.edu";

    /** time: order by origin descending time */
    public static final String ORDER_TIME = "time";

    /** time-asc : order by origin ascending time */
    public static final String ORDER_TIME_ASC = "time-asc";

    /**magnitude: order by descending magnitude */
    public static final String ORDER_MAGNITUDE = "magnitude";

    /**magnitude-asc : order by ascending magnitude*/
    public static final String ORDER_MAGNITUDE_ASC = "magnitude-asc";

    @Override
    public String getServiceName() {
        return EVENT_SERVICE;
    }

    public static final String EVENT_SERVICE = "event";


}

