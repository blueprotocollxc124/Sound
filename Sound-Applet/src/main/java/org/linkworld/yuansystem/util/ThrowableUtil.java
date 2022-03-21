package org.linkworld.yuansystem.util;

/*打印堆栈信息
 *@Author  LiuXiangCheng
 *@Since   2021/12/5  19:41
 */


import java.io.PrintWriter;
import java.io.StringWriter;

public class ThrowableUtil {

 public static String getStackTrance(Throwable throwable) {
  StringWriter sw = new StringWriter();
  try(PrintWriter printWriter = new PrintWriter(sw)) {
   throwable.printStackTrace(printWriter);
   return sw.toString();
  }
 }
}
