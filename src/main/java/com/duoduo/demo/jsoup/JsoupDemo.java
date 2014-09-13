package com.duoduo.demo.jsoup;

import java.io.IOException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 * 中国足彩网解析
 * @author chengesheng@gmail.com
 * @date 2014-9-13 下午11:48:21
 * @version 1.0.0
 */
public class JsoupDemo {

	public static void main(String[] args) {
		try {
			Document doc = Jsoup
					.connect("http://cp.zgzcw.com/lottery/jchtplayvsForJsp.action?lotteryId=47&type=jcmini").get();
			String title = doc.title();
			System.out.println(title);
			Elements elements = doc.select(".tz-body .tz-t");
			for (int i = 0, len = elements.size(); i < len; i++) {
				Element element = elements.get(i);
				Elements dateTime = element.select(".ps strong");
				if (!dateTime.isEmpty()) {
					String dateTitle = dateTime.get(0).text();
					Elements count = element.select(".ps em");
					if (!count.isEmpty()) {
						dateTitle += "(" + count.get(0).text() + ")";
					}
					System.out.println(dateTitle);
				}

				System.out.println("-----------------------------------------------------------");

				Elements lists = doc.select(".tz-body #hide_box_" + (i + 1));
				if (!lists.isEmpty()) {
					Elements rows = lists.get(0).select("tr");
					if (!rows.isEmpty()) {
						for (int r = 0, rlen = rows.size(); r < rlen; r++) {
							Elements cells = rows.get(r).select("td");
							if (cells.isEmpty()) {
								continue;
							}

							for (int c = 0, clen = cells.size(); c < clen; c++) {
								System.out.print(cells.get(c).text() + "\t");
								if (cells.get(c).hasClass("wh-10")) {
									getLszj(cells.get(c).attr("newplayid"));
								}
							}
							System.out.println();
						}
					}
					System.out.println("rows: " + rows.size());
				}
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private static void getLszj(String newplayid) {
		try {
			Document doc = Jsoup.connect("http://fenxi.zgzcw.com/" + newplayid + "/bsls").get();
			Elements matchHome = doc.select("#match_Home");
			System.out.print("\t历史战绩:");
			if (!matchHome.isEmpty()) {
				Elements rows = matchHome.select("tr[f]");
				if (!rows.isEmpty()) {
					int ss = 0, pp = 0, ff = 0;
					int hs = 0, hp = 0, hf = 0;
					for (int r = 0, rlen = rows.size(); r < rlen; r++) {
						String f = rows.get(r).attr("f");
						String h = rows.get(r).attr("h");
						if ("胜".equals(f)) {
							ss++;
							if ("0".equals(h)) {
								hs++;
							}
						} else if ("平".equals(f)) {
							pp++;
							if ("0".equals(h)) {
								hp++;
							}
						} else if ("负".equals(f)) {
							ff++;
							if ("0".equals(h)) {
								hf++;
							}
						}
					}
					System.out.print(ss + "胜" + pp + "平" + ff + "负,其中主场" + hs + "胜" + hp + "平" + hf + "负\t");
				}
			}
		} catch (Exception e) {
			// TODO: handle exception
		}

	}

}
