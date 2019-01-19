package snippet;

import com.kyee.monitor.base.common.util.CommonUtils.JsonUtil;

public class Snippet {
	public static void main(String[] args) {
		String object2Json = JsonUtil.object2Json(null);
		System.out.println(object2Json);
	}
}

