package wordnetAccess.utils;

public class Text {
	public static int compareWNStrings(String s1, String s2){
		int r = 0;
		int i = 0;
		int j = 0;
		int idx1 = 0;
		int idx2 = 0;
		String chars = "'-._012345" +
				"6789abcdef" +
				"ghijklmnño" +
				"pqrstuvwxy" +
				"züáéíóú";
		Integer[] indexes = {1,2,3,4,5,6,7,8,9,10,
				11,12,13,14,15,16,17,18,19,20,
				21,22,23,24,25,26,27,28,29,30,
				31,32,33,34,35,36,37,38,39,40,
				41,36,15,19,23,30,36};
		s1 = s1.toLowerCase();
		s2 = s2.toLowerCase();
		while(r == 0 && i < s1.length() && i < s2.length()){
			
			if(s1.charAt(i) != s2.charAt(i)){
				
				for (j = 0; j < chars.length(); j++) {
					if(s1.charAt(i) == chars.charAt(j))
						idx1 = indexes[j];
					if(s2.charAt(i) == chars.charAt(j))
						idx2 = indexes[j];
				}
				if(idx1 != 0 && idx2 != 0)
					r = idx1 - idx2;
				else r = s1.charAt(i) - s2.charAt(i);
			}
			i++;
		}
			
		if(r == 0)
			r = s1.length() - s2.length();
		return r;
	}
	
	public int compareNormalStrings(String s1, String s2){
		int r = 0;
		int i = 0;
	
		while(r == 0 && i < s1.length() && i < s2.length()){
			r = s1.charAt(i) - s2.charAt(i);
			i++;
		}
			
		if(r == 0)
			r = s1.length() - s2.length();
		return r;
	}
}
