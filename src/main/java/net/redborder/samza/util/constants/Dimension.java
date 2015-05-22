package net.redborder.samza.util.constants;

public class Dimension {
    // Common
    public final static String CLIENT_MAC = "client_mac";
    public final static String WIRELESS_STATION = "wireless_station";
    public final static String WIRELESS_ID = "wireless_id";
    public final static String SRC_IP = "src";
    public final static String SENSOR_IP = "sensor_ip";
    public final static String DST_IP = "dst";
    public final static String SENSOR_NAME = "sensor_name";
    public final static String CLIENT_LATLNG = "client_latlong";
    public final static String CLIENT_RSSI = "client_rssi";
    public final static String CLIENT_RSSI_NUM = "client_rssi_num";
    public final static String CLIENT_SNR = "client_snr";
    public final static String CLIENT_SNR_NUM = "client_snr_num";
    public final static String TIMESTAMP = "timestamp";
    public final static String FIRST_SWITCHED = "first_switched";
    public final static String DURATION = "duration";
    public final static String PKTS = "pkts";
    public final static String BYTES = "bytes";
    public final static String TYPE = "type";
    public final static String SRC_VLAN = "src_vlan";
    public final static String DST_VLAN = "dst_vlan";
    public final static String DOT11STATUS = "dot11_status";
    public final static String SRC = "src";
    public final static String CLIENT_MAC_VENDOR = "client_mac_vendor";
    public final static String CLIENT_ID = "client_id";
    public final static String SRC_AS_NAME = "src_as_name";
    public final static String SRC_PORT = "src_port";
    public final static String SRC_MAP = "src_map";
    public final static String SRV_PORT = "srv_port";
    public final static String DST_AS_NAME = "dst_as_name";
    public final static String DST_PORT = "dst_port";
    public final static String DST_MAP = "dst_map";
    public final static String APPLICATION_ID_NAME = "application_id_name";
    public final static String BITFLOW_DIRECTION = "biflow_direction";
    public final static String CONVERSATION = "conversation";
    public final static String DIRECTION = "direction";
    public final static String ENGINE_ID_NAME = "engine_id_name";
    public final static String HTTP_HOST = "http_host";
    public final static String HTTP_SOCIAL_MEDIA = "http_social_media";
    public final static String HTTP_SOCIAL_USER = "http_social_user";
    public final static String HTTP_USER_AGENT_OS = "http_user_agent_os";
    public final static String HTTP_REFER_L1 = "http_referer_l1";
    public final static String IP_PROTOCOL_VERSION = "ip_protocol_version";
    public final static String L4_PROTO = "l4_proto";
    public final static String SRC_NET_NAME = "src_net_name";
    public final static String DST_NET_NAME = "dst_net_name";
    public final static String TOS = "tos";
    public final static String DST_COUNTRY_CODE = "dst_country_code";
    public final static String SRC_COUNTRY_CODE = "src_country_code";
    public final static String SCATTERPLOT = "scatterplot";
    public final static String INPUT_SNMP = "input_snmp";
    public final static String OUTPUT_SNMP = "output_snmp";

    public final static String CLIENT_BUILDING = "building";
    public final static String CLIENT_BUILDING_ID = "building_id";
    public final static String CLIENT_CAMPUS = "campus";
    public final static String CLIENT_CAMPUS_ID = "campus_id";
    public final static String CLIENT_FLOOR = "floor";
    public final static String CLIENT_FLOOR_ID = "floor_id";
    public final static String CLIENT_ZONE = "zone";

    public final static String COORDINATES_MAP = "coordinates_map";
    public final static String HNBLOCATION = "hnblocation";
    public final static String HNBGEOLOCATION = "hnbgeolocation";
    public final static String RAT = "rat";
    public final static String DOT11PROTOCOL = "dot11_protocol";
    public final static String DEPLOYMENT = "deployment";
    public final static String DEPLOYMENT_ID = "deployment_id";
    public final static String NAMESPACE = "namespace";
    public final static String NAMESPACE_ID = "namespace_id";
    public final static String TIER = "tier";
    public final static String MSG = "msg";

    // Event
    public final static String ACTION = "action";
    public final static String CLASSIFICATION = "classification";
    public final static String DOMAIN_NAME = "domain_name";
    public final static String ETHLENGTH_RANGE = "ethlength_range";
    public final static String GROUP_NAME = "group_name";
    public final static String SIG_GENERATOR = "sig_generator";
    public final static String ICMPTYPE = "icmptype";
    public final static String IPLEN_RANGE = "iplen_range";
    public final static String REV = "rev";
    public final static String SENSOR_ID = "sensor_id";
    public final static String PRIORITY = "priority";
    public final static String SIG_ID = "sig_id";
    public final static String ETHSRC = "ethsrc";
    public final static String ETHSRC_VENDOR = "ethsrc_vendor";
    public final static String ETHDST = "ethdst";
    public final static String ETHDST_VENDOR = "ethdst_vendor";
    public final static String DST = "dst";
    public final static String TTL = "ttl";
    public final static String VLAN = "vlan";
    public final static String MARKET = "market";
    public final static String MARKET_ID = "market_id";
    public final static String ORGANIZATION = "organization";
    public final static String ORGANIZATION_ID = "organization_id";
    public final static String CLIENT_LATLONG = "client_latlong";
    public final static String SHA256 = "sha256";
    public final static String FILE_SIZE = "file_size";
    public final static String FILE_URI = "file_uri";
    public final static String FILE_HOSTNAME = "file_hostname";

    // Darklist
    public final static String DARKLIST_SCORE = "darklist_score";
    public final static String DARKLIST_SCORE_NAME = "darklist_score_name";
    public final static String DARKLIST_PROTOCOL = "darklist_protocol";
    public final static String DARKLIST_DIRECTION = "darklist_direction";
    public final static String DARKLIST_CATEGORY = "darklist_category";

    // NMSP
    public final static String NMSP_AP_MAC = "ap_mac";
    public final static String NMSP_RSSI = "rssi";
    public final static String NMSP_DOT11STATUS = "dot11_status";
    public final static String NMSP_VLAN_ID = "vlan_id";
    public final static String NMSP_DOT11PROTOCOL = "dot11_protocol";
    public final static String NMSP_WIRELESS_ID = "wireless_id";

    // Location
    public final static String LOC_TIMESTAMP_MILLIS = "timestampMillis";
    public final static String LOC_MSEUDI = "mseUdi";
    public final static String LOC_NOTIFICATIONS = "notifications";
    public final static String LOC_NOTIFICATION_TYPE = "notificationType";
    public final static String LOC_STREAMING_NOTIFICATION = "StreamingNotification";
    public final static String LOC_LOCATION = "location";
    public final static String LOC_GEOCOORDINATEv8 = "GeoCoordinate";
    public final static String LOC_GEOCOORDINATEv9 = "geoCoordinate";
    public final static String LOC_MAPINFOv8 = "MapInfo";
    public final static String LOC_MAPINFOv9 = "mapInfo";
    public final static String LOC_MAPCOORDINATEv8 = "MapCoordinate";
    public final static String LOC_MACADDR = "macAddress";
    public final static String LOC_MAP_HIERARCHY = "mapHierarchyString";
    public final static String LOC_MAP_HIERARCHY_V10 = "locationMapHierarchy";
    public final static String LOC_DOT11STATUS = "dot11Status";
    public final static String LOC_SSID = "ssId";
    public final static String LOC_IPADDR = "ipAddress";
    public final static String LOC_AP_MACADDR = "apMacAddress";
    public final static String LOC_SUBSCRIPTION_NAME = "subscriptionName";
    public final static String LOC_LONGITUDE = "longitude";
    public final static String LOC_LATITUDEv8 = "latitude";
    public final static String LOC_LATITUDEv9 = "lattitude";
    public final static String LOC_DEVICEID = "deviceId";
    public final static String LOC_BAND = "band";
    public final static String LOC_STATUS = "status";
    public final static String LOC_USERNAME = "username";
    public final static String LOC_ENTITY = "entity";
    public final static String LOC_COORDINATE = "locationCoordinate";
    public final static String LOC_COORDINATE_X = "x";
    public final static String LOC_COORDINATE_Y = "y";
    public final static String LOC_COORDINATE_Z = "z";
    public final static String LOC_COORDINATE_UNIT = "unit";

    // State
    public final static String WIRELESS_CHANNEL = "wireless_channel";
    public final static String WIRELESS_TX_POWER = "wireless_tx_power";
    public final static String WIRELESS_ADMIN_STATE = "wireless_admin_state";
    public final static String WIRELESS_OP_STATE = "wireless_op_state";
    public final static String WIRELESS_MODE = "wireless_mode";
    public final static String WIRELESS_SLOT = "wireless_slot";

    //Hashtag
    public final static String VALUE = "value";

    // Social
    public final static String USER_SCREEN_NAME = "user_screen_name";
    public final static String USER_NAME = "user_name";
    public final static String USER_ID = "user_id";
    public final static String HASHTAGS = "hashtags";
    public final static String MENTIONS = "mentions";
    public final static String SENTIMENT = "sentiment";
    public final static String MSG_SEND_FROM = "msg_send_from";
    public final static String USER_FROM = "user_from";
    public final static String USER_PROFILE_IMG_HTTPS = "user_profile_img_https";
    public final static String INFLUENCE = "influence";
    public final static String PICTURE_URL = "picture_url";
    public final static String LANGUAGE = "language";
    public final static String CATEGORY = "category";
    public final static String FOLLOWERS = "followers";
}
