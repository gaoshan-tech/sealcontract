

package com.gaoshan.chain.util;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.gaoshan.chain.entity.ContractEntity;
import com.gaoshan.chain.entity.Credential;
import com.gaoshan.chain.entity.RsvSignature;
import com.gaoshan.chain.constant.GIdConstant;
import com.github.fge.jackson.JsonLoader;
import com.github.fge.jsonschema.core.report.ProcessingMessage;
import com.github.fge.jsonschema.core.report.ProcessingReport;
import com.github.fge.jsonschema.main.JsonSchema;
import com.github.fge.jsonschema.main.JsonSchemaFactory;
import net.logstash.logback.encoder.org.apache.commons.lang.StringEscapeUtils;
import org.bouncycastle.util.encoders.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.storm3j.abi.datatypes.Address;
import org.storm3j.abi.datatypes.DynamicArray;
import org.storm3j.abi.datatypes.DynamicBytes;
import org.storm3j.abi.datatypes.StaticArray;
import org.storm3j.abi.datatypes.generated.Bytes32;
import org.storm3j.abi.datatypes.generated.Int256;
import org.storm3j.abi.datatypes.generated.Uint256;
import org.storm3j.abi.datatypes.generated.Uint8;
import org.storm3j.crypto.ECKeyPair;
import org.storm3j.crypto.Hash;
import org.storm3j.crypto.Keys;
import org.storm3j.crypto.Sign;
import org.storm3j.crypto.Sign.SignatureData;
import org.storm3j.utils.Numeric;

import java.io.*;
import java.math.BigInteger;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.security.InvalidAlgorithmParameterException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.SignatureException;
import java.util.*;
import java.util.regex.Pattern;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

/**
 * 数据工具类.
 *
 */
public final class DataToolUtils {

    private static final Logger logger = LoggerFactory.getLogger(DataToolUtils.class);
    private static final String SEPARATOR_CHAR = "-";

    /**
     * default salt length.
     */
    private static final String DEFAULT_SALT_LENGTH = "5";

    private static final int SERIALIZED_SIGNATUREDATA_LENGTH = 65;

    private static final int radix = 10;

    private static final String TO_JSON = "toJson";

    private static final String KEY_CREATED = "created";

    private static final String KEY_ISSUANCEDATE = "issuanceDate";

    private static final String KEY_EXPIRATIONDATE = "expirationDate";

    private static final String KEY_CLAIM = "claim";

    private static final String KEY_FROM_TOJSON = "$from";

    private static final List<String> CONVERT_UTC_LONG_KEYLIST = new ArrayList<>();

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();


    static {
        // sort by letter
        OBJECT_MAPPER.configure(MapperFeature.SORT_PROPERTIES_ALPHABETICALLY, true);
        // when map is serialization, sort by key
        OBJECT_MAPPER.configure(SerializationFeature.ORDER_MAP_ENTRIES_BY_KEYS, true);
        // ignore mismatched fields
        OBJECT_MAPPER.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        OBJECT_MAPPER.enable(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY);
        // use field for serialize and deSerialize
        OBJECT_MAPPER.setVisibility(PropertyAccessor.SETTER, JsonAutoDetect.Visibility.NONE);
        OBJECT_MAPPER.setVisibility(PropertyAccessor.GETTER, JsonAutoDetect.Visibility.NONE);
        OBJECT_MAPPER.setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);
        CONVERT_UTC_LONG_KEYLIST.add(KEY_CREATED);
        CONVERT_UTC_LONG_KEYLIST.add(KEY_ISSUANCEDATE);
        CONVERT_UTC_LONG_KEYLIST.add(KEY_EXPIRATIONDATE);

    }

    /**
     * Keccak-256 hash function.
     *
     * @param utfString the utfString
     * @return hash value as hex encoded string
     */
    public static String sha3(String utfString) {
        return Numeric.toHexString(sha3(utfString.getBytes(StandardCharsets.UTF_8)));
    }

    /**
     * Sha 3.
     *
     * @param input the input
     * @return the byte[]
     */
    public static byte[] sha3(byte[] input) {
        return Hash.sha3(input, 0, input.length);
    }

//    public static String getHash(String hexInput) {
//        return sha3(hexInput);
//    }


    /**
     * serialize a class instance to Json String.
     *
     * @param object the class instance to serialize
     * @param <T>    the type of the element
     * @return JSON String
     */
    public static <T> String serialize(T object) {
        Writer write = new StringWriter();
        try {
            OBJECT_MAPPER.writeValue(write, object);
        } catch (JsonGenerationException e) {
            logger.error("JsonGenerationException when serialize object to json", e);
        } catch (JsonMappingException e) {
            logger.error("JsonMappingException when serialize object to json", e);
        } catch (IOException e) {
            logger.error("IOException when serialize object to json", e);
        }
        return write.toString();
    }

    /**
     * deserialize a JSON String to an class instance.
     *
     * @param json  json string
     * @param clazz Class.class
     * @param <T>   the type of the element
     * @return class instance
     */
    public static <T> T deserialize(String json, Class<T> clazz) {
        Object object = null;
        try {
            if (isValidFromToJson(json)) {
                logger.error("this jsonString is converted by toJson(), "
                        + "please use fromJson() to deserialize it");
                throw new RuntimeException("deserialize json to Object error");
            }
            object = OBJECT_MAPPER.readValue(json, TypeFactory.rawClass(clazz));
        } catch (JsonParseException e) {
            logger.error("JsonParseException when deserialize json to object", e);
            throw new RuntimeException(e);
        } catch (JsonMappingException e) {
            logger.error("JsonMappingException when deserialize json to object", e);
            throw new RuntimeException(e);
        } catch (IOException e) {
            logger.error("IOException when deserialize json to object", e);
            throw new RuntimeException(e);
        }
        return (T) object;
    }

    /**
     * Convert a Map to compact Json output, with keys ordered. Use Jackson JsonNode toString() to
     * ensure key order and compact output.
     *
     * @param map input map
     * @return JsonString
     * @throws Exception IOException
     */
    public static String mapToCompactJson(Map<String, Object> map) throws Exception {
        return OBJECT_MAPPER.readTree(serialize(map)).toString();
    }

    /**
     * Convert a POJO to Map.
     *
     * @param object POJO
     * @return Map
     * @throws Exception IOException
     */
    public static Map<String, Object> objToMap(Object object) throws Exception {
        JsonNode jsonNode = OBJECT_MAPPER.readTree(serialize(object));
        return (HashMap<String, Object>) OBJECT_MAPPER.convertValue(jsonNode, HashMap.class);
    }

    /**
     * 对象深度复制(对象必须是实现了Serializable接口).
     *
     * @param obj pojo
     * @param <T> the type of the element
     * @return Object clonedObj
     */
    @SuppressWarnings("unchecked")
    public static <T extends Serializable> T clone(T obj) {
        T clonedObj = null;
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(baos);
            oos.writeObject(obj);
            oos.close();

            ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
            ObjectInputStream ois = new ObjectInputStream(bais);
            clonedObj = (T) ois.readObject();
            ois.close();
        } catch (Exception e) {
            logger.error("clone object has error.", e);
        }
        return clonedObj;
    }

    /**
     * Load Json Object. Can be used to return both Json Data and Json Schema.
     *
     * @param jsonString the json string
     * @return JsonNode
     * @throws IOException Signals that an I/O exception has occurred.
     */
    public static JsonNode loadJsonObject(String jsonString) throws IOException {
        return JsonLoader.fromString(jsonString);
    }


    /**
     * Generate a new Key-pair.
     *
     * @return the ECKeyPair
     * @throws InvalidAlgorithmParameterException Invalid algorithm.
     * @throws NoSuchAlgorithmException           No such algorithm.
     * @throws NoSuchProviderException            No such provider.
     */
    public static ECKeyPair createKeyPair()
            throws InvalidAlgorithmParameterException,
            NoSuchAlgorithmException,
            NoSuchProviderException {
        return Keys.createEcKeyPair();
    }


    /**
     * Sign a message based on the given privateKey in Decimal String BigInt. The message passed in
     * WILL BE HASHED.
     *
     * @param message          the message
     * @param privateKeyString the private key string
     * @return SignatureData
     */
    public static SignatureData signMessage(
            String message,//正常的数据 {"claim":"did0xd2171990f332e3edff54906bc33"。。。。。。,"proof":null}
            String privateKeyString) {
        ECKeyPair keyPair = ECKeyPair.create(Numeric.toBigInt(privateKeyString));
        return Sign.signMessage(sha3(message.getBytes(StandardCharsets.UTF_8)), keyPair);
    }

    public static SignatureData signPrefixedMessage(
            String message,
            String privateKeyString) {
        ECKeyPair keyPair = ECKeyPair.create(Numeric.toBigInt(privateKeyString));
        String address = org.web3j.crypto.Keys.getAddress(keyPair.getPublicKey());
        logger.info("address：{}", address);
        return Sign.signPrefixedMessage(sha3(Numeric.hexStringToByteArray(message)), keyPair);
    }

    /**
     * Extract the Public Key from the message and the SignatureData.
     *
     * @param message       the message
     * @param signatureData the signature data
     * @return publicKey
     * @throws SignatureException Signature is the exception.
     */
    public static BigInteger signatureToPublicKey(
            String message,
            SignatureData signatureData)
            throws SignatureException {

        return Sign.signedMessageToKey(sha3(message.getBytes(StandardCharsets.UTF_8)),
                signatureData);
    }

    public static BigInteger signaturePrefixedToPublicKey(
            byte[] message,
            SignatureData signatureData)
            throws SignatureException {

        return Sign.signedPrefixedMessageToKey(sha3(message),
                signatureData);
    }

    /**
     * Verify whether the message and the Signature matches the given public Key.
     *
     * @param message       This should be from the same plain-text source with the signature Data.
     * @param signatureData This must be in SignatureData. Caller should call deserialize.
     * @return true if yes, false otherwise
     * @throws SignatureException Signature is the exception.
     */
    public static boolean verifySignature(
            String message,
            SignatureData signatureData,
            String did)
            throws SignatureException {
        if (message == null) {
            return false;
        }
        BigInteger extractedPublicKey = signatureToPublicKey(message, signatureData);
        String extractedDid = GIdUtils.convertPublicKeyToGId(extractedPublicKey.toString());
        return extractedDid.equals(did);
    }

    public static boolean verifyPrefixedSignature(byte[] message,
                                                  SignatureData signatureData,
                                                  String did)
            throws SignatureException {
        if (message == null) {
            return false;
        }
        BigInteger extractedPublicKey = signaturePrefixedToPublicKey(message, signatureData);
        String extractedDid = GIdUtils.convertPublicKeyToGId(extractedPublicKey.toString());
        return extractedDid.equalsIgnoreCase(did);
    }

    /**
     * Obtain the PublicKey from given PrivateKey.
     *
     * @param privateKey the private key
     * @return publicKey
     */
    public static BigInteger publicKeyFromPrivate(BigInteger privateKey) {
        return Sign.publicKeyFromPrivate(privateKey);
    }


    /**
     * The Base64 encode/decode class.
     *
     * @param base64Bytes the base 64 bytes
     * @return the byte[]
     */
    public static byte[] base64Decode(byte[] base64Bytes) {
        return Base64.decode(base64Bytes);
    }

    /**
     * Base 64 encode.
     *
     * @param nonBase64Bytes the non base 64 bytes
     * @return the byte[]
     */
    public static byte[] base64Encode(byte[] nonBase64Bytes) {
        return Base64.encode(nonBase64Bytes);
    }

    /**
     * Checks if is valid base 64 string.
     *
     * @param string the string
     * @return true, if is valid base 64 string
     */
    public static boolean isValidBase64String(String string) {
        return org.apache.commons.codec.binary.Base64.isBase64(string);
    }

    /**
     *
     * @param signatureData the signature data
     * @return the byte[]
     */
    public static byte[] simpleSignatureSerialization(SignatureData signatureData) {
        byte[] serializedSignatureData = new byte[65];
        serializedSignatureData[0] = signatureData.getV()[0];
        System.arraycopy(signatureData.getR(), 0, serializedSignatureData, 1, 32);
        System.arraycopy(signatureData.getS(), 0, serializedSignatureData, 33, 32);
        return serializedSignatureData;
    }


    /**
     * The De-Serialization class of Signatures. This is simply a de-concatenation of bytes of the
     * v, r, and s.
     *
     * @param serializedSignatureData the serialized signature data
     * @return the sign. signature data
     */
    public static SignatureData simpleSignatureDeserialization(
            byte[] serializedSignatureData) {
        if (SERIALIZED_SIGNATUREDATA_LENGTH != serializedSignatureData.length) {
            throw new RuntimeException("signature data illegal");
        }
        byte v = serializedSignatureData[0];
        byte[] r = new byte[32];
        byte[] s = new byte[32];
        System.arraycopy(serializedSignatureData, 1, r, 0, 32);
        System.arraycopy(serializedSignatureData, 33, s, 0, 32);
        SignatureData signatureData = new SignatureData(v, r, s);
        return signatureData;
    }



    public static boolean verifyPrefixedSignatureFromWeId(
            byte[] rawData,
            String signature,
            String dId) throws SignatureException {
        SignatureData signatureData = null;
        try {
            signatureData = convertBase64StringToSignatureData(signature);
        } catch (Exception e) {
            logger.error("verify Signature failed.", e);
            return false;
        }
        return verifyPrefixedSignature(rawData, signatureData, dId);
    }

    /**
     * Convert an off-chain Base64 signature String to signatureData format.
     *
     * @param base64Signature the signature string in Base64
     * @return signatureData structure
     */
    public static SignatureData convertBase64StringToSignatureData(String base64Signature) {
        return simpleSignatureDeserialization(
                base64Decode(base64Signature.getBytes(StandardCharsets.UTF_8))
        );
    }

    /**
     * string to byte.
     *
     * @param value stringData
     * @return byte[]
     */
    public static byte[] stringToByteArray(String value) {
        if (StrUtil.isBlank(value)) {
            return new byte[1];
        }
        return value.getBytes(StandardCharsets.UTF_8);
    }

    /**
     * convert timestamp to UTC of json string.
     *
     * @param jsonString json string
     * @return timestampToUtcString
     */
    public static String convertTimestampToUtc(String jsonString) {
        String timestampToUtcString;
        try {
            timestampToUtcString = dealNodeOfConvertUtcAndLong(
                    loadJsonObject(jsonString),
                    CONVERT_UTC_LONG_KEYLIST,
                    TO_JSON
            ).toString();
        } catch (IOException e) {
            logger.error("replaceJsonObj exception.", e);
            throw new RuntimeException(e);
        }
        return timestampToUtcString;
    }
    private static JsonNode dealNodeOfConvertUtcAndLong(
            JsonNode jsonObj,
            List<String> list,
            String type) {
        if (jsonObj.isObject()) {
            return dealObjectOfConvertUtcAndLong((ObjectNode) jsonObj, list, type);
        } else if (jsonObj.isArray()) {
            return dealArrayOfConvertUtcAndLong((ArrayNode) jsonObj, list, type);
        } else {
            return jsonObj;
        }
    }

    private static JsonNode dealObjectOfConvertUtcAndLong(
            ObjectNode jsonObj,
            List<String> list,
            String type) {
        ObjectNode resJson = OBJECT_MAPPER.createObjectNode();
        jsonObj.fields().forEachRemaining(entry -> {
            String key = entry.getKey();
            JsonNode obj = entry.getValue();
            if (obj.isObject()) {
                //JSONObject
                if (key.equals(KEY_CLAIM)) {
                    resJson.set(key, obj);
                } else {
                    resJson.set(key, dealObjectOfConvertUtcAndLong((ObjectNode) obj, list, type));
                }
            } else if (obj.isArray()) {
                //JSONArray 
                resJson.set(key, dealArrayOfConvertUtcAndLong((ArrayNode) obj, list, type));
            } else {
                if (list.contains(key)) {
                    if (TO_JSON.equals(type)) {
                        if (isValidLongString(obj.asText())) {
                            resJson.put(
                                    key,
                                    DateUtils.convertNoMillisecondTimestampToUtc(
                                            Long.parseLong(obj.asText())));
                        } else {
                            resJson.set(key, obj);
                        }
                    } else {
                        if (DateUtils.isValidDateString(obj.asText())) {
                            resJson.put(
                                    key,
                                    DateUtils.convertUtcDateToNoMillisecondTime(obj.asText()));
                        } else {
                            resJson.set(key, obj);
                        }
                    }
                } else {
                    resJson.set(key, obj);
                }
            }
        });
        return resJson;
    }

    private static JsonNode dealArrayOfConvertUtcAndLong(
            ArrayNode jsonArr,
            List<String> list,
            String type) {
        ArrayNode resJson = OBJECT_MAPPER.createArrayNode();
        for (int i = 0; i < jsonArr.size(); i++) {
            JsonNode jsonObj = jsonArr.get(i);
            if (jsonObj.isObject()) {
                resJson.add(dealObjectOfConvertUtcAndLong((ObjectNode) jsonObj, list, type));
            } else if (jsonObj.isArray()) {
                resJson.add(dealArrayOfConvertUtcAndLong((ArrayNode) jsonObj, list, type));
            } else {
                resJson.add(jsonObj);
            }
        }
        return resJson;
    }

    /**
     * valid string is a long type.
     *
     * @param str string
     * @return result
     */
    public static boolean isValidLongString(String str) {
        if (StrUtil.isBlank(str)) {
            return false;
        }

        long result = 0;
        int i = 0;
        int len = str.length();
        long limit = -Long.MAX_VALUE;
        long multmin;
        int digit;

        char firstChar = str.charAt(0);
        if (firstChar <= '0') {
            return false;
        }
        multmin = limit / radix;
        while (i < len) {
            digit = Character.digit(str.charAt(i++), radix);
            if (digit < 0) {
                return false;
            }
            if (result < multmin) {
                return false;
            }
            result *= radix;
            if (result < limit + digit) {
                return false;
            }
            result -= digit;
        }
        return true;
    }

    /**
     * valid the json string is converted by toJson().
     *
     * @param json jsonString
     * @return result
     */
    public static boolean isValidFromToJson(String json) {
        if (StrUtil.isBlank(json)) {
            logger.error("input json param is null.");
            return false;
        }
        JsonNode jsonObject = null;
        try {
            jsonObject = loadJsonObject(json);
        } catch (IOException e) {
            logger.error("convert jsonString to JSONObject failed." + e);
            return false;
        }
        return jsonObject.has(KEY_FROM_TOJSON);
    }

    /**
     * add tag which the json string is converted by toJson().
     *
     * @param json jsonString
     * @return result
     */
    public static String addTagFromToJson(String json) {
        JsonNode jsonObject;
        try {
            jsonObject = loadJsonObject(json);
            if (!jsonObject.has(KEY_FROM_TOJSON)) {
                ((ObjectNode) jsonObject).put(KEY_FROM_TOJSON, TO_JSON);
            }
        } catch (IOException e) {
            logger.error("addTagFromToJson fail." + e);
            return json;
        }
        return jsonObject.toString();
    }

}

