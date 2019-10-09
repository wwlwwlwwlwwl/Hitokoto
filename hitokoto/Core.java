package hitokoto;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

import org.json.JSONObject;

public class Core {
	SimpleDateFormat formatter = new SimpleDateFormat("yyyy.MM.dd-HH.mm.ss");
	String time = formatter.format(new Date());
	private static Core Core;
	public static boolean enable = false;
	private File file = new File(getFileName());
	private String list = "#Hitokoto \r\n";
	private String newList;
	
	public static Thread thread;
	public Core() {
		Core = this;
		enable = true;
	}
	
	public String getFileName() {
		//int file = 0;
		//while (new File("D:/Hitokoto" + file + ".txt").exists()) {
		//	file++;
		//}
		return "D:/Hitokoto-" + time + ".txt";
	}
	
	public void GetMSG() {
		//System.out.println("Core Launched! Mode : MSG");
		new Thread(new Runnable() {
			@Override
			public void run() {
				thread.currentThread().setName("Hitokoto Thread");
				EasyGUI.addMSG("Thread Launched! Mode : MSG");
				while(enable) {
					thread = Thread.currentThread();
						String s = sendGet("https://v1.hitokoto.cn/?c=a&encode=text&charset=gbk");
						//WebGet.sendGet("https://api.lwl12.com/hitokoto/v1/get?charset=utf-8");
						if (!s.equals("null")) {
							if (newList == null) {
								newList = list;
							}
							if (!newList.contains(s)) {
							newList = newList + s;
							EasyGUI.addMSG("抓取内容 : " + s);
							WriteFile(file, newList);
							//System.out.println("Save File!");
							//System.out.println(newList);
							} else {
								EasyGUI.addMSG("发现重复内容! 已自动忽略!");
								EasyGUI.addMSG(s);
							}
						} else {
							EasyGUI.addMSG("获取失败!检查网络连接?");
						}
						//System.out.println("Do Sleep...");
						try {
							Thread.currentThread().sleep(500 + randomNumber(500, 100));
						} catch (InterruptedException e) {}
					}
				}
		}).start();
	}
	
	public String getMode(String type) {
		switch (type) {
		case "a":
			return "动画";
		case "b":
			return "漫画";
		case "c":
			return "游戏";
		case "d":
			return "小说";
		case "e":
			return "原创";
		case "f":
			return "来自网络";
		case "g":
			return "其他";
		default:
			break;
		}
		return "未知";
	}
	
	public void GetJSON() {
		//System.out.println("Core Launched! Mode : JSON");
		new Thread(new Runnable() {
			@Override
			public void run() {
				thread = Thread.currentThread();
				thread.currentThread().setName("Hitokoto Thread");
				EasyGUI.addMSG("Thread Launched! Mode : JSON");
				int allGet = 0;
				int delay = 0;
				while(enable) {
						String s = sendGet("https://v1.hitokoto.cn/?encode=json&charset=utf-8");
							s.replace("{", "[");
							s.replace("}", "]");
						//WebGet.sendGet("https://api.lwl12.com/hitokoto/v1/get?charset=utf-8");
						if (!s.equals("null")) {
							JSONObject json = new JSONObject(s);
							if (newList == null) {
								newList = list;
							}
							if (!newList.contains((String)(json.getString("hitokoto")))) {
							String showMSG = "抓取内容 : " + json.getString("hitokoto") + " | 来自 : " + json.getString("from") + " | 类型 : " + getMode(json.getString("type")) + " | ID : " + json.getInt("id");
							//String str = json.getString("hitokoto") + "   - " + json.getString("from") + "   | Type : " + getMode(json.getString("type")) + "  | ID : " + json.getInt("id") + "\r\n";
							//String str = json.getString("hitokoto") + "          -" + json.getString("from") + " | 类型 : " + getMode(json.getString("type")) + "\r\n";
							String str = json.getString("hitokoto") + "     -" + json.getString("from") + "\r\n";
							newList = newList + str;
							WriteFile(file, newList);
							allGet++;
							delay++;
							EasyGUI.addMSG(showMSG);
							EasyGUI.setTitle("Hitokoto | 已抓取 " + allGet + " 次");
							//System.out.println(showMSG);
							} else {
								delay++;
								//System.out.println("Already Have This! " + s + "AllGet : " + allGet);
								EasyGUI.addMSG("发现重复内容! 已自动忽略!");
								EasyGUI.addMSG(s);
							}
						} else {
							//System.out.println("Get Fail!");
							EasyGUI.addMSG("获取失败!检查网络连接?");
						}
						try {
							Thread.currentThread().sleep(500 + randomNumber(500, 100));
						} catch (InterruptedException e) {}
					}
			}
		}).start();
	}
	
	public static void WriteFile(File file,String string) {
		try {
			PrintWriter pw = new PrintWriter(file);
			pw.print(string);
			pw.close();
		} catch (Exception var2) {
			System.out.println("WriteFile: Write Failed! " + var2.getMessage());
		}
	}
	
	
	@SuppressWarnings("resource")
	public static String ReadFile(File file) {
		try {
			File f = file;
			if (!f.exists()) {
				f.createNewFile();
			} else {
				BufferedReader br = new BufferedReader(new FileReader(f));
				String line;
				while ((line = br.readLine()) != null) {
					try {
						String text = String.valueOf(line);
						return text;
					} catch (Exception var4) {
						System.out.println("ReadFile: Read Failed! " + var4.getMessage());
					}
				}
			}
		} catch (IOException e) {
			System.out.println("ReadFile: IO Failed! " + e.getMessage());
		}
		return null;
	}
	
	@SuppressWarnings("resource")
	public static boolean IsEmpty(File file) {
		try {
			File f = file;
			if (!f.exists()) {
				return true;
			} else {
				BufferedReader br = new BufferedReader(new FileReader(f));
				String line;
				while ((line = br.readLine()) != null) {
					try {
						return false;
					} catch (Exception var4) {
						System.out.println("ReadFile: Read Failed! " + var4.getMessage());
						return false;
					}
				}
			}
		} catch (IOException e) {
			System.out.println("ReadFile: IO Failed! " + e.getMessage());
			return false;
		}
		return true;
	}
	
	public static Core getCore() {
		if (Core != null) {
			return Core;
		}
		return null;
	}
	
	public static int randomNumber(int max, int min) {
		return (int)(Math.random() * (double)(max - min)) + min;
	}
	
	public static String GetRandomString(int Value) {
		String str = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
		Random random = new Random();
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < Value;i++) {
			int number = random.nextInt(62);
			sb.append(str.charAt(number));
		}
		return sb.toString();
	}
	
    public static String sendGet(final String url) {
    	//System.out.println("SendGet : " + url);
	    String result = "";
       try {
    	    final String urlNameString = url;
    	    final URL realurl = new URL(urlNameString);
	        HttpURLConnection httpUrlConn = (HttpURLConnection) realurl.openConnection();
	        httpUrlConn.setDoInput(true);  
            httpUrlConn.setRequestMethod("GET");  
            httpUrlConn.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 5.0; Windows NT; DigExt)");
            InputStream input = httpUrlConn.getInputStream();
            InputStreamReader read = new InputStreamReader(input, "utf-8");
            BufferedReader br = new BufferedReader(read);
            String data = br.readLine();
            while(data!=null) {
                result = String.valueOf(result) + data + "\n";
                data=br.readLine();
                }
       br.close();  
       read.close();  
       input.close();  
       httpUrlConn.disconnect();  
   } catch (MalformedURLException e) {
       return "null";
   } catch (IOException e) {
       return "null";
   }
	    return result;
	}
}
