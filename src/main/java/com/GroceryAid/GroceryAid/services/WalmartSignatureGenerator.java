package com.GroceryAid.GroceryAid.services;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.tomcat.util.codec.binary.Base64;

import java.io.ObjectStreamException;
import java.security.KeyRep;
import java.security.PrivateKey;
import java.security.Signature;
import java.util.*;

public class WalmartSignatureGenerator {
	private String privateKeyVersion = "1";
	private String consumerId = "84575756-b7e9-4452-82d1-38432cdd2373";
	
	public WalmartSignature generate() {
		WalmartSignature signature = new WalmartSignature();
		
		String key = "MIIEvgIBADANBgkqhkiG9w0BAQEFAASCBKgwggSkAgEAAoIBAQDd37Y1Zi3f3V2y\n" +
				"I9Zzyna78yw1PWmC10hpw+J8o0033wMagR5lOnAqh3EkycA+G1/dchze5/Bq0/L9\n" +
				"vtqXdvLOraoOTxqLSVmZwqaxw57GeIYkkWEzsrxUK8xktAS8r6JSRcYO6tFhAQpp\n" +
				"1XXUwdBO3Wi/Z8Fwa8GKkalutlEND7lk5OCtzd4Pf5dMNwopMOqnuwz4VgJA/8yo\n" +
				"vF5iM15yfB0EeIWrSm/gN65i/D9OGtdGpJ/aB0r6QROpu6kGQKRMQ+oZhZdvrNZG\n" +
				"iov0jxLVvHBXGkZsxcr68lluk0z89KgGDSb3lH+7Fgb+cPhIEMAK+PfOTE9WesPI\n" +
				"19N6qz8fAgMBAAECggEATlfg+cgDqMl8fRtkbOxvsnvx/zaZHAcyXY46WfI7oN/R\n" +
				"M7ml5u6Ujp/WlrvactOtrP7PLPJPFRmT8n9CpjrtD4eIxZau88HJrp5px9kgJkqG\n" +
				"a0EeRmf1Ue9JM8HQ7mNB+LUEc1384rjAWZBzE9X/3OkXRIa5ah3ScEBwyFP+GJLt\n" +
				"fnguf+mLu9RDIhmNAbZFqDSExO5Lfux+hS7TaVitv7PSyfW7u6tIpBQ2B/iNQ6Ri\n" +
				"RMNLyHsHNss/u+LuM0zJShMaV6TGTiJYyV7WeXimHInnT8bb+0AAdC92M7oawaTl\n" +
				"A+F1YmO9KKDAsU44AmnEQDZ0vNOZeT1j5KHnB7jJoQKBgQDwMypCAlIg7fIUQZ2S\n" +
				"ZukPV3/qsGWHlpEnbPj1GQdtHoYsPbrMyLF0fhYLHmu2MoVPVwLJ6kwE2lc4YdEk\n" +
				"33XgWUA/Lf5qZIaDN320kTmsKuuaLDxh+2nDnIeQoCtWV4jpka9YGy8vHVJDM7WN\n" +
				"3XJzJ11ryyfj34p90xkONgdi8QKBgQDsd/JuWOlXDVo8sLJ1AzFq6XUUhnQY8n6D\n" +
				"8ESpCavWJ2qReWsv+xcMUPEVLXWbSA9JghKlbenwfazUT1qXMuULLtkBbrfinYMQ\n" +
				"MDspiRxu5VCRNOYPKllHCDfZC0qWPP5fJDwTQG8CwOaKyR4wt8ssjaSsk9Zp+RHT\n" +
				"ITZk9g2jDwKBgQCrpsTCG0p5wdFHbISghO50SkFdg+Vqec0NcbCP5u0Jq9CSeSKv\n" +
				"CZTW/Ovf0kODFqNxyWpyxP7CUQ4Xer+tLAve4akp/UJc4a3Gw7cPfODMCngH2yjj\n" +
				"lHlGkuvGDNAria6Ly9KUkgisELYi1CcgVGpfpHf8elftXmhn9p1nLHEm4QKBgCwX\n" +
				"gYDJ21h8CONgHyZqXgCNX2NgXq14Kdb4KhVK1FuuJdTSSwY7T2TggtNsEo3NtewC\n" +
				"8O8fu4s4I83CriWbO9IF/jiiKG985z9RfvaTtlWHNKqerncNcnAoPCGgVVwGK3ga\n" +
				"DYmZbhmZVmNydMqoup2oG5iCSGyiIIy5mKbiqlBJAoGBAJT4ESvqnhcYHRfl2PZm\n" +
				"6746ZuNYUuN9LlR5SVizwNCfDFNCT5H0ayevSlNKStQ6nuSX+s0J8AzwgpgUOSrk\n" +
				"3zlfDwal6muYav2IFZ2G6yTPEPu8fCOb3b4/9PCPr0kKsp2D7aueJLsIgcQXVqGw\n" +
				"A62rQMC+xnX6Wdh/c/wCGHAT";
		
		long inTimestamp = System.currentTimeMillis();
		
		System.out.println("consumerId: " + consumerId);
		System.out.println("intimestamp: " + inTimestamp);
		
		Map<String, String> map = new HashMap<>();
		map.put("WM_CONSUMER.ID", consumerId);
		map.put("WM_CONSUMER.INTIMESTAMP", Long.toString(inTimestamp));
		map.put("WM_SEC.KEY_VERSION", privateKeyVersion);
		
		String[] array = canonicalize(map);
		
		String authSignature = null;
		
		try {
			authSignature = generateSignature(key, array[1]);
		} catch(Exception e) { }
		
		System.out.println("Signature: " + authSignature);
		System.out.println("timestamp: "+ inTimestamp);
		
		signature.setKeyVersion(privateKeyVersion);
		signature.setConsumerID(consumerId);
		signature.setTimestamp(inTimestamp);
		signature.setAuthSignature(authSignature);
		
		return signature;
	}
	
	public String generateSignature(String key, String stringToSign) throws Exception {
		Signature signatureInstance = Signature.getInstance("SHA256WithRSA");
		
		ServiceKeyRep keyRep = new ServiceKeyRep(KeyRep.Type.PRIVATE, "RSA", "PKCS#8", Base64.decodeBase64(key));
		
		PrivateKey resolvedPrivateKey = (PrivateKey) keyRep.readResolve();
		
		signatureInstance.initSign(resolvedPrivateKey);
		
		byte[] bytesToSign = stringToSign.getBytes("UTF-8");
		signatureInstance.update(bytesToSign);
		byte[] signatureBytes = signatureInstance.sign();
		
		String signatureString = Base64.encodeBase64String(signatureBytes);
		
		return signatureString;
	}
	
	protected String[] canonicalize(Map<String, String> headersToSign) {
		StringBuffer canonicalizedStrBuffer=new StringBuffer();
		StringBuffer parameterNamesBuffer=new StringBuffer();
		Set<String> keySet=headersToSign.keySet();
		
		// Create sorted key set to enforce order on the key names
		SortedSet<String> sortedKeySet=new TreeSet<String>(keySet);
		for (String key :sortedKeySet) {
			Object val=headersToSign.get(key);
			parameterNamesBuffer.append(key.trim()).append(";");
			canonicalizedStrBuffer.append(val.toString().trim()).append("\n");
		}
		return new String[] {parameterNamesBuffer.toString(), canonicalizedStrBuffer.toString()};
	}
	
	class ServiceKeyRep extends KeyRep  {
		private static final long serialVersionUID = -7213340660431987616L;
		public ServiceKeyRep(Type type, String algorithm, String format, byte[] encoded) {
			super(type, algorithm, format, encoded);
		}
		protected Object readResolve() throws ObjectStreamException {
			return super.readResolve();
		}
	}
	
	@Data
	@AllArgsConstructor
	@NoArgsConstructor
	public class WalmartSignature {
		String keyVersion = "1";
		String consumerID = "84575756-b7e9-4452-82d1-38432cdd2373";
		Long timestamp;
		String authSignature;
	}
	
	@Data
	@AllArgsConstructor
	@NoArgsConstructor
	@JsonIgnoreProperties(ignoreUnknown = true)
	public static class WalmartResponse {
		Long itemId;
		String name;
		float salePrice;
	}
	
	@Data
	@NoArgsConstructor
	@AllArgsConstructor
	@JsonIgnoreProperties(ignoreUnknown = true)
	public static class WalmartResponseMapper {
		ArrayList<WalmartResponse> items;
	}
}