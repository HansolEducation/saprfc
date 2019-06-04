import java.util.*;

import org.json.simple.JSONArray;

import com.common.DataSet;
import com.sap.rfc.SapTransRFC;
import com.sap.rfc.TableAdapterReader;

public class SapTransTest {
	@SuppressWarnings({"unchecked", "rawtypes"})
	public static void main(String[] args) throws Throwable{
		SapTransRFC rfc = new SapTransRFC();
		
		DataSet t_list = new DataSet();
		DataSet noname = new DataSet();
		DataSet t_importa = new DataSet();
		DataSet t_importb = new DataSet();
		
		// 각 블럭별로 put() 이후 꼭 add() 를 해줘야 함
		try{
			String[][] blockNames = new String[][] {{"noname", "T_IMPORTA", "T_IMPORTB"}, {"", "S", "T"}};
			noname.put("INPUT", "AA");
			noname.add();
			
			t_importa.put("VKORG", "1000");
			t_importa.put("VTWEG", "10");
			t_importa.put("SPART", "00");
			t_importa.put("VKBUR", "0034");
			t_importa.put("KUNAG", "0011312606");
			t_importa.put("KUNWE", "0011312606");
			t_importa.put("KONDA", "00");
			t_importa.put("AUGRU", "S02");
			t_importa.put("KVGR5", "");
			t_importa.put("PERNR", "50057675");
			t_importa.put("NAME1", "안만식");
			t_importa.put("PSTLZ", "22778");
			t_importa.put("ORT01", "인천광역시 서구");
			t_importa.put("STRAS", "가정로 387");
			t_importa.put("STR_SUPPL1", "103동404호");
			t_importa.put("TELF1", "010-9055-7351");
			t_importa.put("TELF2", "010-9055-7351");
			t_importa.put("PRCPN", "");
			t_importa.put("PAYIN", "");
			t_importa.put("LIERT", "");
			t_importa.add();

			t_importb.put("MATNR", "101077");
			t_importb.put("UEPOS", "");
			t_importb.put("KWMENG", "1");
			t_importb.put("CHARG", "");
			t_importb.put("KUNWE", "0021616700");
			t_importb.put("VKAUS", "");
			t_importb.put("PSTLZ", "");
			t_importb.put("ORT01", "");
			t_importb.put("STRAS", "");
			t_importb.put("STR_SUPPL1", "");
			t_importb.put("GWLDT", "20190508");
			t_importb.put("GUBUN", "");
			t_importb.put("KWMENG1", "");
			t_importb.add();
			
			t_list.put("noname", noname);
			t_list.put("T_IMPORTA", t_importa);
			t_list.put("T_IMPORTB", t_importb);
			// RFC 에 연결할 입력 block명 과 속성들 (일반필드, 테이블, 스트럭쳐 : "", "T", "S")
			//String[][] blockNames = null;

			// T_IMPORTA
			/*
			t_importa.put("CLDAT", "20180725");
			t_importa.put("EFLAG", "");
			t_importa.put("CSTKD", "선택안함");
			t_importa.put("EXDAT", "");
			t_importa.put("GRUND", "0001");
			t_importa.put("EXRSN", "");
			t_importa.put("ORDAT", "20180725");
			t_importa.put("BTEXT", "");
			t_importa.put("POSNR", "002000");
			t_importa.put("VBELN", "0107223776");
			t_importa.add();
			*/

			/*
			// T_IMPORTA 추가1
			t_importa.put("ZFLAG", "D");
			t_importa.put("ORDNO", "160628D0993");
			t_importa.put("VBELN", "");
			t_importa.put("POSNR", "");
			t_importa.put("MATNR", "100342");
			t_importa.put("BZIRK", "");
			t_importa.put("KWMENG", "1");
			t_importa.put("ROYALTY", "0");
			t_importa.put("AMOUNT", "0");
			t_importa.put("CHARG", "");
			t_importa.put("VGBEL", "");
			t_importa.put("ABGRU", "");
			t_importa.add();

			// T_IMPORTA 추가2
			t_importa.put("ZFLAG", "T");
			t_importa.put("ORDNO", "2017T062672");
			t_importa.add();

			// T_IMPORTB
			t_importb.put("ORDER", "160628D0993");
			t_importb.put("PSTLZ", "03908");
			t_importb.put("ORT01", "서울특별시 마포구");
			t_importb.put("STRAS", "월드컵북로 361");
			t_importb.put("TELF1", "010-5632-2660");
			t_importb.put("TELF2", "010-5632-2660");
			t_importb.put("BIGOO", "491818848");
			t_importb.put("STR_SUPPL1", "한솔교육빌딩 22층");
			t_importb.add();
			*/

			// 각 block 을 정의해줌
    		//t_list.put("noname", noname);
    		//t_list.put("T_IMPORTA", t_importa);
    		//t_list.put("T_IMPORTB", t_importb);

    		//System.out.println("blockNames.length ====> " + blockNames.length);

    		/*
    		// 출력 테스트
    		for(int j=0; j<blockNames.length; j++){ 
        		
        		System.out.println("blockNames[j][0] ====> " + blockNames[0][j]);
	    		DataSet map = (DataSet) t_list.get(blockNames[0][j]);
	    		
				@SuppressWarnings("rawtypes")
				Iterator iter = (Iterator) map.get();
	
				// 리스트를 쪼개서... Map을 쪼개서... 셋팅
				while (iter.hasNext()) {
					Map<Object, Object> m = (HashMap<Object, Object>) iter.next();
					
					System.out.println("m.size() ======> " + m.size());
	
					Set<Map.Entry<Object, Object>> entrySet2 = m.entrySet();
					Iterator<Map.Entry<Object, Object>> it2 = entrySet2.iterator();

					while (it2.hasNext()) {
						Map.Entry<Object, Object> entry2 = it2.next();

						key = entry2.getKey().toString();
						value = entry2.getValue().toString();

						System.out.format("map2 ===> %s : %s \r\n", key, value);
					}
				}
    		}
    		*/
			TableAdapterReader tableAdapter = new TableAdapterReader();
    		// RFC 연결
    		DataSet list = rfc.transRFCNews("ZSD_EDUMOM_ORDER_CREATE", blockNames, t_list, tableAdapter);
    		
    		//DataSet list = rfc.transRFCNew("ZSD_SALES_MATNR_CHECK", blockNames, t_list);

    		//System.out.println("list =====> " + list.keySet());
    		
    		/*
    		// 반환 block 중 택1
    		DataSet map = (DataSet) list.get("T_EXPORTA");

			Iterator iter = (Iterator) map.iterator();

			// 반환 block 내의 값 출력
			while (iter.hasNext()) {
				Map<Object, Object> m = (HashMap<Object, Object>) iter.next();
				
				System.out.println("m.size() ======> " + m.size());

				Set<Map.Entry<Object, Object>> entrySet2 = m.entrySet();
				Iterator<Map.Entry<Object, Object>> it2 = entrySet2.iterator();

				while (it2.hasNext()) {
					Map.Entry<Object, Object> entry2 = it2.next();

					key = entry2.getKey().toString();
					value = entry2.getValue().toString();

					System.out.format("map2 ===> %s : %s \r\n", key, value);
				}
			}
			*/
			
    		// 리스트 출력
    		System.out.println(list.convertList(list));
    		// JSON 출력
    		System.out.println(new String(JSONArray.toJSONString((List) list.convertList(list))));
    		// JSON 문자열 변환 출력
    		System.out.println(new String(JSONArray.toJSONString((List) list.convertList(list)).getBytes("utf-8"), "iso-8859-1"));
    		
			System.out.format("\r\n");
		}catch(Exception e){
			e.printStackTrace();
		}
	}
}
